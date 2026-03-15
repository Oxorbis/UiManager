package cz.creeperface.hytale.uimanager.intellij.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import cz.creeperface.hytale.uimanager.intellij.HytaleUiSchemaService
import cz.creeperface.hytale.uimanager.intellij.psi.HytaleUiElementTypes
import cz.creeperface.hytale.uimanager.intellij.psi.HytaleUiPsiUtil
import cz.creeperface.hytale.uimanager.intellij.psi.HytaleUiTokenTypes

class HytaleUiUnknownPropertyInspection : LocalInspectionTool() {

    override fun getDisplayName(): String = "Unknown property"

    override fun getGroupDisplayName(): String = "Hytale UI"

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PsiElementVisitor() {
            override fun visitElement(element: PsiElement) {
                val schema = HytaleUiSchemaService.getInstance()

                when (element.node.elementType) {
                    // Property in node body: `PropertyName: value;`
                    HytaleUiElementTypes.PROPERTY_KEY -> {
                        checkNodeProperty(element, schema, holder)
                    }

                    // Property in parenthesized list: `Key: value` inside TypeName(...) or (...)
                    HytaleUiElementTypes.PROPERTY_LIST_ENTRY -> {
                        checkPropertyListEntry(element, schema, holder)
                    }
                }
            }
        }
    }

    /**
     * Check a PROPERTY_KEY inside a node body against the enclosing node's known properties.
     */
    private fun checkNodeProperty(propertyKey: PsiElement, schema: HytaleUiSchemaService, holder: ProblemsHolder) {
        val propertyName = propertyKey.text
        val nodeType = HytaleUiPsiUtil.findEnclosingNodeType(propertyKey)

        if (nodeType != null && schema.isNodeName(nodeType)) {
            val validProperties = schema.getNodeProperties(nodeType) ?: return
            if (propertyName !in validProperties) {
                val similar = findSimilarNames(propertyName, validProperties.keys)
                val message = if (similar.isNotEmpty()) {
                    "Unknown property '$propertyName' for node '$nodeType'. Did you mean: ${similar.joinToString(", ")}?"
                } else {
                    "Unknown property '$propertyName' for node '$nodeType'"
                }
                holder.registerProblem(propertyKey, message)
            }
        }
    }

    /**
     * Check a property inside a parenthesized list. The key is the first IDENTIFIER child.
     * Uses context inference to determine the expected type:
     * - Inside `LabelStyle(FontSize: ...)` → check against LabelStyle
     * - Inside `Style: (FontSize: ...)` where parent node is Label → infer LabelStyle
     */
    private fun checkPropertyListEntry(entry: PsiElement, schema: HytaleUiSchemaService, holder: ProblemsHolder) {
        val keyElement = entry.firstChild ?: return
        if (keyElement.node.elementType != HytaleUiTokenTypes.IDENTIFIER) return
        val propertyName = keyElement.text

        val contextType = HytaleUiPsiUtil.inferPropertyContextType(entry)
        if (contextType != null) {
            val validProperties = schema.getTypeProperties(contextType) ?: return
            if (propertyName !in validProperties) {
                val similar = findSimilarNames(propertyName, validProperties.keys)
                val message = if (similar.isNotEmpty()) {
                    "Unknown property '$propertyName' for type '$contextType'. Did you mean: ${similar.joinToString(", ")}?"
                } else {
                    "Unknown property '$propertyName' for type '$contextType'"
                }
                holder.registerProblem(keyElement, message)
            }
        }
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
