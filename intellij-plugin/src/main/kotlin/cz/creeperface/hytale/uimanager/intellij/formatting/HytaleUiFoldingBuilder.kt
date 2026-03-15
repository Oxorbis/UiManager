package cz.creeperface.hytale.uimanager.intellij.formatting

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import cz.creeperface.hytale.uimanager.intellij.psi.HytaleUiElementTypes

class HytaleUiFoldingBuilder : FoldingBuilderEx() {

    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> {
        val descriptors = mutableListOf<FoldingDescriptor>()
        collectFoldRegions(root, descriptors)
        return descriptors.toTypedArray()
    }

    private fun collectFoldRegions(element: PsiElement, descriptors: MutableList<FoldingDescriptor>) {
        val type = element.node.elementType
        if (type == HytaleUiElementTypes.NODE_BODY) {
            val range = element.textRange
            if (range.length > 2) {
                descriptors.add(FoldingDescriptor(element.node, range))
            }
        }

        for (child in element.children) {
            collectFoldRegions(child, descriptors)
        }
    }

    override fun getPlaceholderText(node: ASTNode): String = "{...}"

    override fun isCollapsedByDefault(node: ASTNode): Boolean = false
}
