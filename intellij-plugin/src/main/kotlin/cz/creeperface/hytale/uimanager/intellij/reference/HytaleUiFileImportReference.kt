package cz.creeperface.hytale.uimanager.intellij.reference

import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import cz.creeperface.hytale.uimanager.intellij.psi.HytaleUiTokenTypes

/**
 * Reference for file import paths. Works on the FILE_IMPORT composite element,
 * with the reference range pointing at the string literal.
 */
class HytaleUiFileImportReference(element: PsiElement, private val stringElement: PsiElement)
    : PsiReferenceBase<PsiElement>(element, calcRange(element, stringElement)) {

    private val importPath: String = stringElement.text.removeSurrounding("\"")

    override fun resolve(): PsiElement? {
        val targetFile = HytaleUiFileResolver.resolveImportPath(element.containingFile, importPath) ?: return null
        return PsiManager.getInstance(element.project).findFile(targetFile)
    }

    override fun getVariants(): Array<Any> = emptyArray()

    companion object {
        private fun calcRange(element: PsiElement, stringElement: PsiElement): TextRange {
            val start = stringElement.textRange.startOffset - element.textRange.startOffset + 1
            val end = stringElement.textRange.endOffset - element.textRange.startOffset - 1
            return TextRange(start, end.coerceAtLeast(start))
        }
    }
}
