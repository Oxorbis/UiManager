package cz.creeperface.hytale.uimanager.intellij.parser

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import com.intellij.util.IncorrectOperationException
import cz.creeperface.hytale.uimanager.intellij.HytaleUiLanguage
import cz.creeperface.hytale.uimanager.intellij.lexer.HytaleUiLexer
import cz.creeperface.hytale.uimanager.intellij.psi.HytaleUiElementTypes
import cz.creeperface.hytale.uimanager.intellij.psi.HytaleUiFile
import cz.creeperface.hytale.uimanager.intellij.psi.HytaleUiPsiUtil
import cz.creeperface.hytale.uimanager.intellij.psi.HytaleUiTokenTypes
import cz.creeperface.hytale.uimanager.intellij.reference.*

class HytaleUiParserDefinition : ParserDefinition {

    companion object {
        val FILE = IFileElementType(HytaleUiLanguage.INSTANCE)

        val COMMENTS = TokenSet.create(HytaleUiTokenTypes.LINE_COMMENT)
        val STRING_LITERALS = TokenSet.create(HytaleUiTokenTypes.STRING_LITERAL)
        val WHITE_SPACES = TokenSet.create(HytaleUiTokenTypes.WHITE_SPACE)
    }

    override fun createLexer(project: Project?): Lexer = HytaleUiLexer()

    override fun createParser(project: Project?): PsiParser = HytaleUiParser()

    override fun getFileNodeType(): IFileElementType = FILE

    override fun getCommentTokens(): TokenSet = COMMENTS

    override fun getStringLiteralElements(): TokenSet = STRING_LITERALS

    override fun getWhitespaceTokens(): TokenSet = WHITE_SPACES

    override fun createElement(node: ASTNode): PsiElement {
        return when (node.elementType) {
            HytaleUiElementTypes.NODE_DECL -> HytaleUiNodeDeclElement(node)
            HytaleUiElementTypes.VARIABLE_DECL -> HytaleUiVariableDeclElement(node)
            HytaleUiElementTypes.VARIABLE_REF -> HytaleUiVariableRefElement(node)
            HytaleUiElementTypes.FILE_VAR_REF -> HytaleUiFileVarRefElement(node)
            HytaleUiElementTypes.TEMPLATE_INST -> HytaleUiTemplateInstElement(node)
            HytaleUiElementTypes.FILE_IMPORT -> HytaleUiFileImportElement(node)
            HytaleUiElementTypes.SPREAD_EXPR -> HytaleUiSpreadExprElement(node)
            HytaleUiElementTypes.STRING_VALUE -> HytaleUiStringValueElement(node)
            HytaleUiElementTypes.TYPE_CONSTRUCTOR -> HytaleUiTypeConstructorElement(node)
            HytaleUiElementTypes.PROPERTY_KEY -> HytaleUiPropertyKeyElement(node)
            HytaleUiElementTypes.PROPERTY_LIST_ENTRY -> HytaleUiPropertyListEntryElement(node)
            else -> ASTWrapperPsiElement(node)
        }
    }

    override fun createFile(viewProvider: FileViewProvider): PsiFile = HytaleUiFile(viewProvider)
}

/**
 * NODE_DECL element: NodeType #id { ... }
 * Provides a self-reference on the node type name for Ctrl+hover underline and documentation.
 */
class HytaleUiNodeDeclElement(node: ASTNode) : ASTWrapperPsiElement(node) {
    override fun getReference(): PsiReference? {
        val ident = node.findChildByType(HytaleUiTokenTypes.IDENTIFIER) ?: return null
        // Only if it's the first child (the node type name)
        if (ident !== node.firstChildNode) return null
        return HytaleUiSelfReference(this, ident.psi)
    }
}

/**
 * TYPE_CONSTRUCTOR element: TypeName(prop: val, ...)
 * Provides a self-reference on the type name for Ctrl+hover underline and documentation.
 */
class HytaleUiTypeConstructorElement(node: ASTNode) : ASTWrapperPsiElement(node) {
    override fun getReference(): PsiReference? {
        val ident = node.findChildByType(HytaleUiTokenTypes.IDENTIFIER) ?: return null
        if (ident !== node.firstChildNode) return null
        return HytaleUiSelfReference(this, ident.psi)
    }
}

/**
 * PROPERTY_KEY element: wraps the property name identifier in node body properties.
 * Provides a self-reference for Ctrl+hover underline and documentation.
 */
class HytaleUiPropertyKeyElement(node: ASTNode) : ASTWrapperPsiElement(node) {
    override fun getReference(): PsiReference? {
        val ident = node.findChildByType(HytaleUiTokenTypes.IDENTIFIER) ?: return null
        return HytaleUiSelfReference(this, ident.psi)
    }
}

/**
 * PROPERTY_LIST_ENTRY element: Key: value inside TypeName(...) or (...)
 * Provides a self-reference on the key identifier for Ctrl+hover underline and documentation.
 */
class HytaleUiPropertyListEntryElement(node: ASTNode) : ASTWrapperPsiElement(node) {
    override fun getReference(): PsiReference? {
        val firstChild = node.firstChildNode ?: return null
        if (firstChild.elementType != HytaleUiTokenTypes.IDENTIFIER) return null
        return HytaleUiSelfReference(this, firstChild.psi)
    }
}

/**
 * A self-referencing reference that resolves to the element itself.
 * Used to enable Ctrl+hover underline and documentation popup on elements
 * that don't navigate anywhere (node names, type names, property keys).
 */
class HytaleUiSelfReference(element: PsiElement, private val targetChild: PsiElement) :
    PsiReferenceBase<PsiElement>(element, calcRange(element, targetChild)) {

    override fun resolve(): PsiElement = element

    override fun getVariants(): Array<Any> = emptyArray()

    // Prevent rename refactoring on self-references
    override fun handleElementRename(newElementName: String): PsiElement = element

    companion object {
        private fun calcRange(element: PsiElement, child: PsiElement): TextRange {
            val start = child.textRange.startOffset - element.textRange.startOffset
            return TextRange(start, start + child.textLength)
        }
    }
}

/**
 * VARIABLE_DECL element: @name = value;
 * Implements PsiNameIdentifierOwner so Find Usages and highlight usages work.
 */
class HytaleUiVariableDeclElement(node: ASTNode) : ASTWrapperPsiElement(node), PsiNameIdentifierOwner {
    override fun getName(): String? {
        val atIdent = node.findChildByType(HytaleUiTokenTypes.AT_IDENTIFIER)
        return atIdent?.text?.removePrefix("@")
    }

    override fun setName(name: String): PsiElement {
        val atIdent = node.findChildByType(HytaleUiTokenTypes.AT_IDENTIFIER) ?: throw IncorrectOperationException()
        val newNode = HytaleUiPsiUtil.createAtIdentifierNode(project, name)
        node.replaceChild(atIdent, newNode)
        return this
    }

    override fun getNameIdentifier(): PsiElement? {
        return node.findChildByType(HytaleUiTokenTypes.AT_IDENTIFIER)?.psi
    }
}

/**
 * VARIABLE_REF element: @variable in value position.
 * Provides a reference to the variable declaration.
 */
class HytaleUiVariableRefElement(node: ASTNode) : ASTWrapperPsiElement(node) {
    override fun getReference(): PsiReference = HytaleUiVariableReference(this)
}

/**
 * FILE_VAR_REF element: $C.@variable in value position.
 * Provides references to both the file import ($C) and the variable (@var).
 */
class HytaleUiFileVarRefElement(node: ASTNode) : ASTWrapperPsiElement(node) {
    override fun getReference(): PsiReference? {
        val dollarIdent = node.findChildByType(HytaleUiTokenTypes.DOLLAR_IDENTIFIER)
        val alias = dollarIdent?.text?.removePrefix("$") ?: return null
        return HytaleUiCrossFileVariableReference(this, alias)
    }

    override fun getReferences(): Array<PsiReference> {
        val refs = mutableListOf<PsiReference>()
        val dollarIdent = node.findChildByType(HytaleUiTokenTypes.DOLLAR_IDENTIFIER)
        val alias = dollarIdent?.text?.removePrefix("$")
        if (alias != null) {
            refs.add(HytaleUiFileImportAliasReference(this, dollarIdent.psi))
            refs.add(HytaleUiCrossFileVariableReference(this, alias))
        }
        return refs.toTypedArray()
    }
}

/**
 * TEMPLATE_INST element: @Template { } or $C.@Template { }
 * Provides references to the template variable, and the file import if cross-file.
 */
class HytaleUiTemplateInstElement(node: ASTNode) : ASTWrapperPsiElement(node) {
    override fun getReference(): PsiReference? {
        val dollarIdent = node.findChildByType(HytaleUiTokenTypes.DOLLAR_IDENTIFIER)
        val atIdent = node.findChildByType(HytaleUiTokenTypes.AT_IDENTIFIER) ?: return null

        if (dollarIdent != null) {
            val alias = dollarIdent.text.removePrefix("$")
            return HytaleUiCrossFileVariableReference(this, alias)
        }

        return HytaleUiVariableReference(this)
    }

    override fun getReferences(): Array<PsiReference> {
        val refs = mutableListOf<PsiReference>()
        val dollarIdent = node.findChildByType(HytaleUiTokenTypes.DOLLAR_IDENTIFIER)
        val atIdent = node.findChildByType(HytaleUiTokenTypes.AT_IDENTIFIER)

        if (dollarIdent != null && atIdent != null) {
            val alias = dollarIdent.text.removePrefix("$")
            refs.add(HytaleUiFileImportAliasReference(this, dollarIdent.psi))
            refs.add(HytaleUiCrossFileVariableReference(this, alias))
        } else if (atIdent != null) {
            refs.add(HytaleUiVariableReference(this))
        }

        return refs.toTypedArray()
    }
}

/**
 * FILE_IMPORT element: $C = "path.ui";
 * Provides a reference to the imported file.
 * Implements PsiNameIdentifierOwner so Find Usages and highlight usages work for $aliases.
 */
class HytaleUiFileImportElement(node: ASTNode) : ASTWrapperPsiElement(node), PsiNameIdentifierOwner {
    override fun getReference(): PsiReference? {
        val stringLit = node.findChildByType(HytaleUiTokenTypes.STRING_LITERAL) ?: return null
        return HytaleUiFileImportReference(this, stringLit.psi)
    }

    override fun getName(): String? {
        val dollarIdent = node.findChildByType(HytaleUiTokenTypes.DOLLAR_IDENTIFIER)
        return dollarIdent?.text?.removePrefix("$")
    }

    override fun setName(name: String): PsiElement {
        val dollarIdent = node.findChildByType(HytaleUiTokenTypes.DOLLAR_IDENTIFIER) ?: throw IncorrectOperationException()
        val newNode = HytaleUiPsiUtil.createDollarIdentifierNode(project, name)
        node.replaceChild(dollarIdent, newNode)
        return this
    }

    override fun getNameIdentifier(): PsiElement? {
        return node.findChildByType(HytaleUiTokenTypes.DOLLAR_IDENTIFIER)?.psi
    }
}

/**
 * SPREAD_EXPR element: ...@var or ...$C.@var
 * Provides a reference to the variable being spread.
 */
/**
 * STRING_VALUE element: wraps a string literal in value position.
 * Provides file path references for TexturePath, SoundPath, etc.
 */
class HytaleUiStringValueElement(node: ASTNode) : ASTWrapperPsiElement(node) {
    override fun getReference(): PsiReference? {
        val stringLit = node.findChildByType(HytaleUiTokenTypes.STRING_LITERAL) ?: return null
        val propertyName = findEnclosingPropertyName() ?: return null
        val schema = try { cz.creeperface.hytale.uimanager.intellij.HytaleUiSchemaService.getInstance() } catch (_: Exception) { return null }

        if (schema.isPathProperty(propertyName)) {
            return HytaleUiPathReference(this, stringLit.psi)
        }

        // PatchStyle/SoundStyle string shorthand
        val expectedType = resolveExpectedType(propertyName, schema)
        if (expectedType == "PatchStyle" || expectedType == "SoundStyle") {
            return HytaleUiPathReference(this, stringLit.psi)
        }

        return null
    }

    private fun findEnclosingPropertyName(): String? {
        val parent = this.parent ?: return null
        val parentType = parent.node.elementType

        if (parentType == HytaleUiElementTypes.PROPERTY) {
            val key = parent.node.findChildByType(HytaleUiElementTypes.PROPERTY_KEY)
            return key?.text
        }

        if (parentType == HytaleUiElementTypes.PROPERTY_LIST_ENTRY) {
            val firstChild = parent.firstChild
            if (firstChild?.node?.elementType == HytaleUiTokenTypes.IDENTIFIER) {
                return firstChild.text
            }
        }

        return null
    }

    private fun resolveExpectedType(propertyName: String, schema: cz.creeperface.hytale.uimanager.intellij.HytaleUiSchemaService): String? {
        val parent = this.parent ?: return null
        val parentType = parent.node.elementType

        if (parentType == HytaleUiElementTypes.PROPERTY) {
            val nodeType = HytaleUiPsiUtil.findEnclosingNodeType(parent)
            if (nodeType != null) {
                return schema.getPropertyExpectedType(nodeType, propertyName)
            }
        }

        if (parentType == HytaleUiElementTypes.PROPERTY_LIST_ENTRY) {
            val contextType = HytaleUiPsiUtil.inferPropertyContextType(parent)
            if (contextType != null) {
                return schema.getPropertyExpectedType(contextType, propertyName)
            }
        }

        return null
    }
}

/**
 * SPREAD_EXPR element: ...@var or ...$C.@var
 * Provides a reference to the variable being spread.
 */
class HytaleUiSpreadExprElement(node: ASTNode) : ASTWrapperPsiElement(node) {
    override fun getReference(): PsiReference? {
        val dollarIdent = node.findChildByType(HytaleUiTokenTypes.DOLLAR_IDENTIFIER)
        val atIdent = node.findChildByType(HytaleUiTokenTypes.AT_IDENTIFIER) ?: return null

        if (dollarIdent != null) {
            val alias = dollarIdent.text.removePrefix("$")
            return HytaleUiCrossFileVariableReference(this, alias)
        }

        return HytaleUiVariableReference(this)
    }

    override fun getReferences(): Array<PsiReference> {
        val refs = mutableListOf<PsiReference>()
        val dollarIdent = node.findChildByType(HytaleUiTokenTypes.DOLLAR_IDENTIFIER)
        val atIdent = node.findChildByType(HytaleUiTokenTypes.AT_IDENTIFIER)

        if (dollarIdent != null && atIdent != null) {
            val alias = dollarIdent.text.removePrefix("$")
            refs.add(HytaleUiFileImportAliasReference(this, dollarIdent.psi))
            refs.add(HytaleUiCrossFileVariableReference(this, alias))
        } else if (atIdent != null) {
            refs.add(HytaleUiVariableReference(this))
        }

        return refs.toTypedArray()
    }
}
