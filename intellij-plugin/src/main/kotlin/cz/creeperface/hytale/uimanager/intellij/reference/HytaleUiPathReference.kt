package cz.creeperface.hytale.uimanager.intellij.reference

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiReferenceBase

/**
 * Reference for file path string values (texture paths, sound paths, asset paths).
 * Resolves to the target file for navigation (Ctrl+click).
 * The reference range covers the string content (excluding quotes).
 *
 * @param element the STRING_VALUE composite element
 * @param stringElement the STRING_LITERAL leaf inside it
 */
class HytaleUiPathReference(element: PsiElement, private val stringElement: PsiElement) :
    PsiReferenceBase<PsiElement>(element, calcRange(element, stringElement)) {

    private val path: String = stringElement.text.removeSurrounding("\"")

    override fun resolve(): PsiElement? {
        if (path.isBlank()) return null
        val targetFile = HytaleUiFileResolver.resolveResourcePath(element.containingFile, path) ?: return null
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
