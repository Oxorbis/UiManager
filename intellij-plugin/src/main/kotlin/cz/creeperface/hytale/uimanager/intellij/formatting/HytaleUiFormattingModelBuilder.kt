package cz.creeperface.hytale.uimanager.intellij.formatting

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiFile
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.formatter.common.AbstractBlock
import com.intellij.psi.tree.TokenSet
import cz.creeperface.hytale.uimanager.intellij.HytaleUiLanguage
import cz.creeperface.hytale.uimanager.intellij.psi.HytaleUiElementTypes
import cz.creeperface.hytale.uimanager.intellij.psi.HytaleUiTokenTypes

class HytaleUiFormattingModelBuilder : FormattingModelBuilder {

    override fun createModel(formattingContext: FormattingContext): FormattingModel {
        val settings = formattingContext.codeStyleSettings
        val customSettings = settings.getCustomSettings(HytaleUiCodeStyleSettings::class.java)
        val spacingBuilder = createSpacingBuilder(settings, customSettings)

        val block = HytaleUiBlock(
            formattingContext.node,
            null,
            Indent.getNoneIndent(),
            spacingBuilder,
            customSettings
        )
        return FormattingModelProvider.createFormattingModelForPsiFile(
            formattingContext.containingFile,
            block,
            settings
        )
    }

    private fun createSpacingBuilder(
        settings: CodeStyleSettings,
        custom: HytaleUiCodeStyleSettings
    ): SpacingBuilder {
        return SpacingBuilder(settings, HytaleUiLanguage.INSTANCE)
            // Space after colon
            .after(HytaleUiTokenTypes.COLON)
            .spacing(if (custom.SPACE_AFTER_COLON) 1 else 0, if (custom.SPACE_AFTER_COLON) 1 else 0, 0, false, 0)
            // Space before colon
            .before(HytaleUiTokenTypes.COLON)
            .spacing(if (custom.SPACE_BEFORE_COLON) 1 else 0, if (custom.SPACE_BEFORE_COLON) 1 else 0, 0, false, 0)
            // Space after comma
            .after(HytaleUiTokenTypes.COMMA)
            .spacing(if (custom.SPACE_AFTER_COMMA) 1 else 0, if (custom.SPACE_AFTER_COMMA) 1 else 0, 0, true, 0)
            // Space before opening brace
            .before(HytaleUiTokenTypes.LBRACE)
            .spacing(if (custom.SPACE_BEFORE_BRACE) 1 else 0, if (custom.SPACE_BEFORE_BRACE) 1 else 0, 0, false, 0)
            // Space after equals
            .after(HytaleUiTokenTypes.EQUALS)
            .spacing(1, 1, 0, false, 0)
            // Space before equals
            .before(HytaleUiTokenTypes.EQUALS)
            .spacing(1, 1, 0, false, 0)
            // No space before semicolon
            .before(HytaleUiTokenTypes.SEMICOLON)
            .spacing(0, 0, 0, false, 0)
            // No space before comma
            .before(HytaleUiTokenTypes.COMMA)
            .spacing(0, 0, 0, false, 0)
    }
}

private val BLOCKS_WITH_CHILDREN = TokenSet.create(
    HytaleUiElementTypes.NODE_BODY,
    HytaleUiElementTypes.OBJECT_LITERAL,
    HytaleUiElementTypes.TYPE_CONSTRUCTOR,
)

private val INDENTED_CHILDREN = TokenSet.create(
    HytaleUiElementTypes.PROPERTY,
    HytaleUiElementTypes.NODE_DECL,
    HytaleUiElementTypes.TEMPLATE_INST,
    HytaleUiElementTypes.ID_OVERRIDE,
    HytaleUiElementTypes.VARIABLE_DECL,
    HytaleUiElementTypes.PROPERTY_LIST_ENTRY,
    HytaleUiElementTypes.SPREAD_EXPR,
)

class HytaleUiBlock(
    node: ASTNode,
    private val myAlignment: Alignment?,
    private val myIndent: Indent,
    private val spacingBuilder: SpacingBuilder,
    private val customSettings: HytaleUiCodeStyleSettings,
) : AbstractBlock(node, null, myAlignment) {

    override fun getIndent(): Indent = myIndent

    override fun buildChildren(): List<Block> {
        val blocks = mutableListOf<Block>()
        var child = myNode.firstChildNode
        while (child != null) {
            if (child.elementType != HytaleUiTokenTypes.WHITE_SPACE) {
                val childIndent = computeChildIndent(child)
                blocks.add(
                    HytaleUiBlock(child, null, childIndent, spacingBuilder, customSettings)
                )
            }
            child = child.treeNext
        }
        return blocks
    }

    private fun computeChildIndent(child: ASTNode): Indent {
        val parentType = myNode.elementType

        // Children inside NODE_BODY get indented (but not the braces themselves)
        if (parentType == HytaleUiElementTypes.NODE_BODY) {
            val childType = child.elementType
            if (childType == HytaleUiTokenTypes.LBRACE || childType == HytaleUiTokenTypes.RBRACE) {
                return Indent.getNoneIndent()
            }
            if (childType in INDENTED_CHILDREN || childType == HytaleUiTokenTypes.LINE_COMMENT) {
                return Indent.getNormalIndent()
            }
        }

        // Properties inside TYPE_CONSTRUCTOR and OBJECT_LITERAL get indented
        if (parentType == HytaleUiElementTypes.TYPE_CONSTRUCTOR || parentType == HytaleUiElementTypes.OBJECT_LITERAL) {
            val childType = child.elementType
            if (childType == HytaleUiTokenTypes.LPAREN || childType == HytaleUiTokenTypes.RPAREN ||
                childType == HytaleUiTokenTypes.IDENTIFIER) {
                return Indent.getNoneIndent()
            }
            if (childType in INDENTED_CHILDREN) {
                return Indent.getNormalIndent()
            }
        }

        return Indent.getNoneIndent()
    }

    override fun getSpacing(child1: Block?, child2: Block): Spacing? {
        return spacingBuilder.getSpacing(this, child1, child2)
    }

    override fun isLeaf(): Boolean = myNode.firstChildNode == null

    override fun getChildAttributes(newChildIndex: Int): ChildAttributes {
        val type = myNode.elementType
        // When pressing Enter inside a node body, indent the new line
        if (type == HytaleUiElementTypes.NODE_BODY ||
            type == HytaleUiElementTypes.TYPE_CONSTRUCTOR ||
            type == HytaleUiElementTypes.OBJECT_LITERAL) {
            return ChildAttributes(Indent.getNormalIndent(), null)
        }
        return ChildAttributes(Indent.getNoneIndent(), null)
    }
}
