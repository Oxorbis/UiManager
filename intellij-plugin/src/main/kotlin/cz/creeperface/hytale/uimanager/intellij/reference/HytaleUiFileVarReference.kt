package cz.creeperface.hytale.uimanager.intellij.reference

import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import cz.creeperface.hytale.uimanager.intellij.psi.HytaleUiFile
import cz.creeperface.hytale.uimanager.intellij.psi.HytaleUiPsiUtil
import cz.creeperface.hytale.uimanager.intellij.psi.HytaleUiTokenTypes

/**
 * Reference for $C — resolves to the FILE_IMPORT declaration.
 * Can be used on the DOLLAR_IDENTIFIER token itself, or on a composite element
 * with a dollarElement child for range calculation.
 */
class HytaleUiFileImportAliasReference : PsiReferenceBase<PsiElement> {

    private val alias: String

    /** For use directly on a DOLLAR_IDENTIFIER leaf element. */
    constructor(element: PsiElement) : super(element, TextRange(0, element.textLength)) {
        alias = element.text.removePrefix("$")
    }

    /** For use on a composite element, with range pointing at the dollarElement child. */
    constructor(element: PsiElement, dollarElement: PsiElement) : super(element, calcRange(element, dollarElement)) {
        alias = dollarElement.text.removePrefix("$")
    }

    override fun handleElementRename(newElementName: String): PsiElement {
        // Find the DOLLAR_IDENTIFIER in the element and replace it
        val dollarIdent = element.node.findChildByType(HytaleUiTokenTypes.DOLLAR_IDENTIFIER)
            ?: return element
        val newNode = HytaleUiPsiUtil.createDollarIdentifierNode(element.project, newElementName)
        element.node.replaceChild(dollarIdent, newNode)
        return element
    }

    override fun resolve(): PsiElement? {
        val file = element.containingFile as? HytaleUiFile ?: return null
        for (importElement in HytaleUiPsiUtil.findFileImports(file)) {
            if (HytaleUiPsiUtil.getImportAlias(importElement) == alias) {
                return importElement
            }
        }
        return null
    }

    override fun getVariants(): Array<Any> = emptyArray()

    companion object {
        private fun calcRange(element: PsiElement, dollarElement: PsiElement): TextRange {
            val start = dollarElement.textRange.startOffset - element.textRange.startOffset
            return TextRange(start, start + dollarElement.textLength)
        }
    }
}

/**
 * Reference for the @var part in $C.@var expressions.
 * Works on composite elements (FILE_VAR_REF, TEMPLATE_INST) that contain both
 * DOLLAR_IDENTIFIER and AT_IDENTIFIER tokens.
 */
class HytaleUiCrossFileVariableReference(element: PsiElement, private val dollarAlias: String)
    : PsiReferenceBase<PsiElement>(element, calcRange(element)) {

    private val variableName: String = run {
        val atIdent = element.node.findChildByType(HytaleUiTokenTypes.AT_IDENTIFIER)
        atIdent?.text?.removePrefix("@") ?: ""
    }

    override fun handleElementRename(newElementName: String): PsiElement {
        val atIdent = element.node.findChildByType(HytaleUiTokenTypes.AT_IDENTIFIER)
            ?: return element
        val newNode = HytaleUiPsiUtil.createAtIdentifierNode(element.project, newElementName)
        element.node.replaceChild(atIdent, newNode)
        return element
    }

    override fun resolve(): PsiElement? {
        if (variableName.isEmpty()) return null
        val file = element.containingFile as? HytaleUiFile ?: return null

        for (importElement in HytaleUiPsiUtil.findFileImports(file)) {
            if (HytaleUiPsiUtil.getImportAlias(importElement) != dollarAlias) continue

            val path = HytaleUiPsiUtil.getImportPath(importElement) ?: continue
            val targetVFile = HytaleUiFileResolver.resolveImportPath(file, path) ?: continue
            val targetPsi = PsiManager.getInstance(file.project).findFile(targetVFile) as? HytaleUiFile ?: continue

            for (varDecl in HytaleUiPsiUtil.findVariableDeclarations(targetPsi)) {
                if (HytaleUiPsiUtil.getVariableName(varDecl) == variableName) {
                    return varDecl
                }
            }
        }
        return null
    }

    override fun getVariants(): Array<Any> = emptyArray()

    companion object {
        private fun calcRange(element: PsiElement): TextRange {
            val atIdent = element.node.findChildByType(HytaleUiTokenTypes.AT_IDENTIFIER)
            if (atIdent != null) {
                val start = atIdent.startOffset - element.textRange.startOffset
                return TextRange(start, start + atIdent.textLength)
            }
            return TextRange(0, element.textLength)
        }
    }
}
