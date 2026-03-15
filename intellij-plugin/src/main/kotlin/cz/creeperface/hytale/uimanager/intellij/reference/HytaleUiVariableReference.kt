package cz.creeperface.hytale.uimanager.intellij.reference

import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import cz.creeperface.hytale.uimanager.intellij.psi.HytaleUiElementTypes
import cz.creeperface.hytale.uimanager.intellij.psi.HytaleUiFile
import cz.creeperface.hytale.uimanager.intellij.psi.HytaleUiPsiUtil
import cz.creeperface.hytale.uimanager.intellij.psi.HytaleUiTokenTypes

/**
 * Reference for @variable. Works on composite elements (VARIABLE_REF, TEMPLATE_INST)
 * that contain an AT_IDENTIFIER token.
 */
class HytaleUiVariableReference(element: PsiElement) : PsiReferenceBase<PsiElement>(element, calcRange(element)) {

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

        // 1. Walk up from current position to find variable declarations in enclosing scopes
        findVariableInScope(element)?.let { return it }

        // 2. Search file-level variables
        findInFileLevel(file)?.let { return it }

        // 3. Search all imported files
        findInImports(file)?.let { return it }

        return null
    }

    override fun getVariants(): Array<Any> = emptyArray()

    private fun findVariableInScope(from: PsiElement): PsiElement? {
        var current = from.parent
        while (current != null && current !is HytaleUiFile) {
            for (child in current.children) {
                if (child.node.elementType == HytaleUiElementTypes.VARIABLE_DECL) {
                    val name = HytaleUiPsiUtil.getVariableName(child)
                    if (name == variableName) {
                        return child
                    }
                }
            }
            current = current.parent
        }
        return null
    }

    private fun findInFileLevel(file: HytaleUiFile): PsiElement? {
        for (varDecl in HytaleUiPsiUtil.findVariableDeclarations(file)) {
            if (HytaleUiPsiUtil.getVariableName(varDecl) == variableName) {
                return varDecl
            }
        }
        return null
    }

    private fun findInImports(file: HytaleUiFile): PsiElement? {
        for (importElement in HytaleUiPsiUtil.findFileImports(file)) {
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

    companion object {
        private fun calcRange(element: PsiElement): TextRange {
            // Point the reference range at the @IDENTIFIER token within the composite
            val atIdent = element.node.findChildByType(HytaleUiTokenTypes.AT_IDENTIFIER)
            if (atIdent != null) {
                val start = atIdent.startOffset - element.textRange.startOffset
                return TextRange(start, start + atIdent.textLength)
            }
            return TextRange(0, element.textLength)
        }
    }
}
