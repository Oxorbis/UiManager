package cz.creeperface.hytale.uimanager.intellij.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiFile
import cz.creeperface.hytale.uimanager.intellij.HytaleUiSchemaService
import cz.creeperface.hytale.uimanager.intellij.psi.HytaleUiElementTypes
import cz.creeperface.hytale.uimanager.intellij.psi.HytaleUiPsiUtil
import cz.creeperface.hytale.uimanager.intellij.psi.HytaleUiTokenTypes

/**
 * Validates that property values match their expected types.
 * E.g., assigning a string to an Integer property, or an invalid enum value.
 */
class HytaleUiTypeMismatchInspection : LocalInspectionTool() {

    override fun getDisplayName(): String = "Property type mismatch"

    override fun getGroupDisplayName(): String = "Hytale UI"

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PsiElementVisitor() {
            override fun visitElement(element: PsiElement) {
                when (element.node.elementType) {
                    HytaleUiElementTypes.PROPERTY -> checkProperty(element, holder)
                    HytaleUiElementTypes.PROPERTY_LIST_ENTRY -> checkPropertyListEntry(element, holder)
                }
            }
        }
    }

    /**
     * Check a node-level property: `PropertyName: value;`
     */
    private fun checkProperty(property: PsiElement, holder: ProblemsHolder) {
        val schema = try { HytaleUiSchemaService.getInstance() } catch (_: Exception) { return }

        val keyNode = property.node.findChildByType(HytaleUiElementTypes.PROPERTY_KEY) ?: return
        val propertyName = keyNode.text

        // Find the expected type from the enclosing node
        val nodeType = HytaleUiPsiUtil.findEnclosingNodeType(property)
        if (nodeType == null || !schema.isNodeName(nodeType)) return

        val expectedType = schema.getPropertyExpectedType(nodeType, propertyName) ?: return

        // Find the value element (everything after the colon)
        val valueKind = extractValueKind(property) ?: return

        val compatible = schema.isValueCompatible(expectedType, valueKind) ?: return
        if (!compatible) {
            val valueElement = findValueElement(property) ?: return
            val simplifiedExpected = expectedType.substringAfterLast('.')
            holder.registerProblem(
                valueElement,
                "Type mismatch: expected '$simplifiedExpected', got ${describeValueKind(valueKind)}"
            )
        }
    }

    /**
     * Check a property inside a parenthesized list: `Key: value` inside TypeName(...) or (...)
     */
    private fun checkPropertyListEntry(entry: PsiElement, holder: ProblemsHolder) {
        val schema = try { HytaleUiSchemaService.getInstance() } catch (_: Exception) { return }

        val keyElement = entry.firstChild ?: return
        if (keyElement.node.elementType != HytaleUiTokenTypes.IDENTIFIER) return
        val propertyName = keyElement.text

        val contextType = HytaleUiPsiUtil.inferPropertyContextType(entry)
        if (contextType == null || (!schema.isTypeName(contextType) && !schema.isNodeName(contextType))) return

        val expectedType = schema.getPropertyExpectedType(contextType, propertyName) ?: return

        val valueKind = extractValueKindFromEntry(entry) ?: return

        val compatible = schema.isValueCompatible(expectedType, valueKind) ?: return
        if (!compatible) {
            val valueElement = findValueElementInEntry(entry) ?: return
            val simplifiedExpected = expectedType.substringAfterLast('.')
            holder.registerProblem(
                valueElement,
                "Type mismatch: expected '$simplifiedExpected', got ${describeValueKind(valueKind)}"
            )
        }
    }

    /**
     * Extract the value kind from a PROPERTY element (the value after ':')
     */
    private fun extractValueKind(property: PsiElement): String? {
        val colon = property.node.findChildByType(HytaleUiTokenTypes.COLON) ?: return null
        // Find the first meaningful element after the colon
        var child = colon.psi.nextSibling
        while (child != null) {
            val kind = classifyElement(child)
            if (kind != null) return kind
            child = child.nextSibling
        }
        return null
    }

    /**
     * Extract the value kind from a PROPERTY_LIST_ENTRY (the value after ':')
     */
    private fun extractValueKindFromEntry(entry: PsiElement): String? {
        val colon = entry.node.findChildByType(HytaleUiTokenTypes.COLON) ?: return null
        var child = colon.psi.nextSibling
        while (child != null) {
            val kind = classifyElement(child)
            if (kind != null) return kind
            child = child.nextSibling
        }
        return null
    }

    /**
     * Find the value PsiElement for error highlighting (after ':')
     */
    private fun findValueElement(property: PsiElement): PsiElement? {
        val colon = property.node.findChildByType(HytaleUiTokenTypes.COLON) ?: return null
        var child = colon.psi.nextSibling
        while (child != null) {
            val type = child.node.elementType
            if (type != HytaleUiTokenTypes.WHITE_SPACE && type != HytaleUiTokenTypes.LINE_COMMENT) {
                return child
            }
            child = child.nextSibling
        }
        return null
    }

    private fun findValueElementInEntry(entry: PsiElement): PsiElement? {
        val colon = entry.node.findChildByType(HytaleUiTokenTypes.COLON) ?: return null
        var child = colon.psi.nextSibling
        while (child != null) {
            val type = child.node.elementType
            if (type != HytaleUiTokenTypes.WHITE_SPACE && type != HytaleUiTokenTypes.LINE_COMMENT) {
                return child
            }
            child = child.nextSibling
        }
        return null
    }

    /**
     * Classify a PSI element into a value kind string for type checking.
     */
    private fun classifyElement(element: PsiElement): String? {
        return when (element.node.elementType) {
            HytaleUiTokenTypes.STRING_LITERAL -> "String"
            HytaleUiElementTypes.STRING_VALUE -> "String"
            HytaleUiTokenTypes.INTEGER_LITERAL -> "Integer"
            HytaleUiTokenTypes.FLOAT_LITERAL -> "Float"
            HytaleUiTokenTypes.BOOLEAN_LITERAL -> "Boolean"
            HytaleUiTokenTypes.SERVER_STRING -> "ServerString"
            HytaleUiTokenTypes.AT_IDENTIFIER -> "Variable"
            HytaleUiTokenTypes.DOLLAR_IDENTIFIER -> "Variable"
            HytaleUiTokenTypes.WHITE_SPACE, HytaleUiTokenTypes.LINE_COMMENT -> null

            HytaleUiElementTypes.COLOR_VALUE -> "Color"
            HytaleUiTokenTypes.COLOR_LITERAL -> "Color"

            HytaleUiElementTypes.VARIABLE_REF -> "Variable"
            HytaleUiElementTypes.FILE_VAR_REF -> "Variable"
            HytaleUiElementTypes.SPREAD_EXPR -> "Variable"

            HytaleUiElementTypes.OBJECT_LITERAL -> "Object"

            HytaleUiElementTypes.TYPE_CONSTRUCTOR -> {
                val typeName = element.firstChild?.text ?: return "Object"
                "TypeConstructor:$typeName"
            }

            HytaleUiTokenTypes.IDENTIFIER -> {
                // Bare identifier = enum value
                "Identifier:${element.text}"
            }

            HytaleUiTokenTypes.MINUS -> null // part of negative number, skip

            else -> null
        }
    }

    private fun describeValueKind(kind: String): String {
        return when {
            kind == "String" -> "String"
            kind == "Integer" -> "Integer"
            kind == "Float" -> "Float"
            kind == "Boolean" -> "Boolean"
            kind == "Color" -> "Color"
            kind == "ServerString" -> "server string"
            kind == "Object" -> "object"
            kind.startsWith("TypeConstructor:") -> kind.removePrefix("TypeConstructor:")
            kind.startsWith("Identifier:") -> "'${kind.removePrefix("Identifier:")}'"
            else -> kind
        }
    }
}
