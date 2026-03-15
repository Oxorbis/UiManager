package cz.creeperface.hytale.uimanager.intellij.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiManager
import cz.creeperface.hytale.uimanager.intellij.psi.HytaleUiElementTypes
import cz.creeperface.hytale.uimanager.intellij.psi.HytaleUiFile
import cz.creeperface.hytale.uimanager.intellij.psi.HytaleUiPsiUtil
import cz.creeperface.hytale.uimanager.intellij.psi.HytaleUiTokenTypes
import cz.creeperface.hytale.uimanager.intellij.reference.HytaleUiFileResolver

class HytaleUiUnresolvedVariableInspection : LocalInspectionTool() {

    override fun getDisplayName(): String = "Unresolved variable reference"

    override fun getGroupDisplayName(): String = "Hytale UI"

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PsiElementVisitor() {
            override fun visitElement(element: PsiElement) {
                if (element.node.elementType == HytaleUiTokenTypes.AT_IDENTIFIER) {
                    checkAtVariable(element, holder)
                }
            }
        }
    }

    private fun checkAtVariable(element: PsiElement, holder: ProblemsHolder) {
        // Determine if this @IDENTIFIER is a declaration or a reference
        if (isDeclaration(element)) return

        // Skip cross-file references ($C.@var) - the @var is preceded by DOT
        if (isCrossFileRef(element)) return

        val varName = element.text.removePrefix("@")
        if (varName.isEmpty()) return

        val file = element.containingFile as? HytaleUiFile ?: return

        // Search local scopes (walk up through enclosing node bodies)
        if (findInLocalScopes(element, varName)) return

        // Search file-level declarations
        if (findInFileLevel(file, varName)) return

        // Search imported files (variables accessible without $prefix)
        if (findInImports(file, varName)) return

        holder.registerProblem(element, "Unresolved variable '@$varName'")
    }

    /**
     * Returns true if this @IDENTIFIER is the defining name of a variable declaration.
     * In `@name = value;`, the `@name` is the first AT_IDENTIFIER child of VARIABLE_DECL.
     */
    private fun isDeclaration(element: PsiElement): Boolean {
        val parent = element.parent ?: return false
        if (parent.node.elementType != HytaleUiElementTypes.VARIABLE_DECL) return false

        // It's a declaration if it appears before the '=' sign
        val equalsNode = parent.node.findChildByType(HytaleUiTokenTypes.EQUALS) ?: return false
        return element.textOffset < equalsNode.textRange.startOffset
    }

    /**
     * Returns true if this @IDENTIFIER is part of a $C.@var cross-file reference.
     * In that case, the @var refers to a variable in the imported file, not the current file.
     */
    private fun isCrossFileRef(element: PsiElement): Boolean {
        val prevSibling = skipWhitespaceBackward(element) ?: return false
        if (prevSibling.node.elementType == HytaleUiTokenTypes.DOT) return true
        return false
    }

    private fun findInLocalScopes(from: PsiElement, varName: String): Boolean {
        var current = from.parent
        while (current != null && current !is HytaleUiFile) {
            for (child in current.children) {
                if (child.node.elementType == HytaleUiElementTypes.VARIABLE_DECL) {
                    val name = HytaleUiPsiUtil.getVariableName(child)
                    if (name == varName) return true
                }
            }
            current = current.parent
        }
        return false
    }

    private fun findInFileLevel(file: HytaleUiFile, varName: String): Boolean {
        val variables = HytaleUiPsiUtil.findVariableDeclarations(file)
        return variables.any { HytaleUiPsiUtil.getVariableName(it) == varName }
    }

    private fun findInImports(file: HytaleUiFile, varName: String): Boolean {
        val imports = HytaleUiPsiUtil.findFileImports(file)
        for (importElement in imports) {
            val path = HytaleUiPsiUtil.getImportPath(importElement) ?: continue
            val targetFile = HytaleUiFileResolver.resolveImportPath(file, path) ?: continue
            val psiFile = PsiManager.getInstance(file.project).findFile(targetFile) as? HytaleUiFile ?: continue

            val vars = HytaleUiPsiUtil.findVariableDeclarations(psiFile)
            if (vars.any { HytaleUiPsiUtil.getVariableName(it) == varName }) return true
        }
        return false
    }

    private fun skipWhitespaceBackward(element: PsiElement): PsiElement? {
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
}
