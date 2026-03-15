package cz.creeperface.hytale.uimanager.intellij.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import cz.creeperface.hytale.uimanager.intellij.HytaleUiSchemaService
import cz.creeperface.hytale.uimanager.intellij.psi.HytaleUiElementTypes
import cz.creeperface.hytale.uimanager.intellij.psi.HytaleUiPsiUtil
import cz.creeperface.hytale.uimanager.intellij.psi.HytaleUiTokenTypes
import cz.creeperface.hytale.uimanager.intellij.reference.HytaleUiFileResolver

class HytaleUiUnresolvedFileInspection : LocalInspectionTool() {

    override fun getDisplayName(): String = "Unresolved file reference"

    override fun getGroupDisplayName(): String = "Hytale UI"

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PsiElementVisitor() {
            override fun visitElement(element: PsiElement) {
                when (element.node.elementType) {
                    // File import: $C = "path.ui";
                    HytaleUiElementTypes.FILE_IMPORT -> checkFileImport(element, holder)

                    // String value wrapping a string literal in value position
                    HytaleUiElementTypes.STRING_VALUE -> checkStringPath(element, holder)
                }
            }
        }
    }

    private fun checkFileImport(element: PsiElement, holder: ProblemsHolder) {
        val stringLit = element.node.findChildByType(HytaleUiTokenTypes.STRING_LITERAL) ?: return
        val path = stringLit.text.removeSurrounding("\"")
        if (path.isBlank()) return

        val resolved = HytaleUiFileResolver.resolveImportPath(element.containingFile, path)
        if (resolved == null) {
            holder.registerProblem(stringLit.psi, "Cannot resolve file '$path'")
        }
    }

    private fun checkStringPath(element: PsiElement, holder: ProblemsHolder) {
        val stringLit = element.node.findChildByType(HytaleUiTokenTypes.STRING_LITERAL) ?: return
        val path = stringLit.text.removeSurrounding("\"")
        if (path.isBlank()) return

        val propertyName = findEnclosingPropertyName(element) ?: return
        val schema = try { HytaleUiSchemaService.getInstance() } catch (_: Exception) { return }

        val isPathProp = schema.isPathProperty(propertyName)
        val isShorthandPath = if (!isPathProp) {
            val expectedType = resolveExpectedType(element, schema, propertyName)
            expectedType == "PatchStyle" || expectedType == "SoundStyle"
        } else false

        if (!isPathProp && !isShorthandPath) return

        val resolved = HytaleUiFileResolver.resolveResourcePath(element.containingFile, path)
        if (resolved == null) {
            holder.registerProblem(stringLit.psi, "Cannot resolve file '$path'")
        }
    }

    private fun findEnclosingPropertyName(element: PsiElement): String? {
        val parent = element.parent ?: return null
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

    private fun resolveExpectedType(element: PsiElement, schema: HytaleUiSchemaService, propertyName: String): String? {
        val parent = element.parent ?: return null
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
