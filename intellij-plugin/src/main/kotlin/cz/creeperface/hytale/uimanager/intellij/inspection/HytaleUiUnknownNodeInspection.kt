package cz.creeperface.hytale.uimanager.intellij.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import cz.creeperface.hytale.uimanager.intellij.HytaleUiSchemaService
import cz.creeperface.hytale.uimanager.intellij.psi.HytaleUiElementTypes
import cz.creeperface.hytale.uimanager.intellij.psi.HytaleUiTokenTypes

class HytaleUiUnknownNodeInspection : LocalInspectionTool() {

    override fun getDisplayName(): String = "Unknown node type"

    override fun getGroupDisplayName(): String = "Hytale UI"

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PsiElementVisitor() {
            override fun visitElement(element: PsiElement) {
                if (element.node.elementType != HytaleUiTokenTypes.IDENTIFIER) return

                val parent = element.parent ?: return
                if (parent.node.elementType != HytaleUiElementTypes.NODE_DECL) return

                // Check if this is the first meaningful child (the node type name)
                val prevSibling = findPrevMeaningfulSibling(element)
                if (prevSibling != null) return

                val schema = HytaleUiSchemaService.getInstance()
                val name = element.text

                if (!schema.isNodeName(name) && !schema.isTypeName(name)) {
                    val similar = findSimilarNames(name, schema.nodeNames)
                    val message = if (similar.isNotEmpty()) {
                        "Unknown node type '$name'. Did you mean: ${similar.joinToString(", ")}?"
                    } else {
                        "Unknown node type '$name'"
                    }
                    holder.registerProblem(element, message)
                }
            }
        }
    }

    private fun findPrevMeaningfulSibling(element: PsiElement): PsiElement? {
        var sibling = element.prevSibling
        while (sibling != null) {
            val type = sibling.node.elementType
            if (type != HytaleUiTokenTypes.WHITE_SPACE && type != HytaleUiTokenTypes.LINE_COMMENT) {
                return sibling
            }
            sibling = sibling.prevSibling
        }
        return null
    }

    private fun findSimilarNames(name: String, candidates: Set<String>): List<String> {
        return candidates.filter { levenshtein(name.lowercase(), it.lowercase()) <= 3 }
            .sortedBy { levenshtein(name.lowercase(), it.lowercase()) }
            .take(3)
    }

    private fun levenshtein(a: String, b: String): Int {
        val dp = Array(a.length + 1) { IntArray(b.length + 1) }
        for (i in 0..a.length) dp[i][0] = i
        for (j in 0..b.length) dp[0][j] = j
        for (i in 1..a.length) {
            for (j in 1..b.length) {
                val cost = if (a[i - 1] == b[j - 1]) 0 else 1
                dp[i][j] = minOf(dp[i - 1][j] + 1, dp[i][j - 1] + 1, dp[i - 1][j - 1] + cost)
            }
        }
        return dp[a.length][b.length]
    }
}
