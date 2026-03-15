package cz.creeperface.hytale.uimanager.intellij.documentation

import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.lang.documentation.DocumentationMarkup
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.richcopy.HtmlSyntaxInfoUtil
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import cz.creeperface.hytale.uimanager.intellij.HytaleUiSchemaService
import cz.creeperface.hytale.uimanager.intellij.parser.HytaleUiFileImportElement
import cz.creeperface.hytale.uimanager.intellij.parser.HytaleUiVariableDeclElement
import cz.creeperface.hytale.uimanager.intellij.psi.HytaleUiElementTypes
import cz.creeperface.hytale.uimanager.intellij.psi.HytaleUiPsiUtil
import cz.creeperface.hytale.uimanager.intellij.psi.HytaleUiTokenTypes

class HytaleUiDocumentationProvider : AbstractDocumentationProvider() {

    override fun getCustomDocumentationElement(
        editor: Editor,
        file: PsiFile,
        contextElement: PsiElement?,
        targetOffset: Int
    ): PsiElement? {
        val elem = contextElement ?: return null
        val tokenType = elem.node.elementType
        val parentType = elem.parent?.node?.elementType

        if (tokenType == HytaleUiTokenTypes.IDENTIFIER && parentType == HytaleUiElementTypes.NODE_DECL && isFirstIdentifier(elem)) return elem
        if (tokenType == HytaleUiTokenTypes.IDENTIFIER && parentType == HytaleUiElementTypes.TYPE_CONSTRUCTOR && isFirstIdentifier(elem)) return elem
        if (tokenType == HytaleUiTokenTypes.IDENTIFIER && parentType == HytaleUiElementTypes.PROPERTY_KEY) return elem
        if (tokenType == HytaleUiTokenTypes.IDENTIFIER && parentType == HytaleUiElementTypes.PROPERTY_LIST_ENTRY && isFirstIdentifier(elem)) return elem

        if (tokenType == HytaleUiTokenTypes.IDENTIFIER && isInValueContext(elem)) {
            val schema = try { HytaleUiSchemaService.getInstance() } catch (_: Exception) { return null }
            if (schema.getEnumForValue(elem.text) != null) return elem
        }

        return null
    }

    override fun generateDoc(element: PsiElement, originalElement: PsiElement?): String? {
        val schema = try { HytaleUiSchemaService.getInstance() } catch (_: Exception) { return null }
        val orig = originalElement ?: element
        return generateDocForElement(orig, schema)
            ?: if (originalElement != null && element !== originalElement) generateDocForElement(element, schema) else null
    }

    override fun getQuickNavigateInfo(element: PsiElement, originalElement: PsiElement?): String? {
        val schema = try { HytaleUiSchemaService.getInstance() } catch (_: Exception) { return null }
        val orig = originalElement ?: element
        return getQuickInfoForElement(orig, schema)
            ?: if (originalElement != null && element !== originalElement) getQuickInfoForElement(element, schema) else null
    }

    // ── Main dispatch ──

    private fun generateDocForElement(elem: PsiElement, schema: HytaleUiSchemaService): String? {
        val tokenType = elem.node.elementType
        val parentType = elem.parent?.node?.elementType

        if (elem is HytaleUiVariableDeclElement) return generateVariableDeclDoc(elem)
        if (elem is HytaleUiFileImportElement) return generateFileImportDeclDoc(elem)
        if (tokenType == HytaleUiTokenTypes.IDENTIFIER && parentType == HytaleUiElementTypes.NODE_DECL && isFirstIdentifier(elem)) return generateNodeDoc(elem.text, schema)
        if (tokenType == HytaleUiTokenTypes.IDENTIFIER && parentType == HytaleUiElementTypes.TYPE_CONSTRUCTOR && isFirstIdentifier(elem)) return generateTypeDoc(elem.text, schema)
        if (tokenType == HytaleUiTokenTypes.IDENTIFIER && parentType == HytaleUiElementTypes.PROPERTY_KEY) return generatePropertyDoc(elem.text, elem, schema)
        if (parentType == HytaleUiElementTypes.PROPERTY_KEY) return generatePropertyDoc(elem.text, elem, schema)
        if (tokenType == HytaleUiTokenTypes.IDENTIFIER && parentType == HytaleUiElementTypes.PROPERTY_LIST_ENTRY && isFirstIdentifier(elem)) return generatePropertyListEntryDoc(elem.text, elem.parent, schema)
        if (tokenType == HytaleUiTokenTypes.IDENTIFIER && isInValueContext(elem)) {
            val enumName = schema.getEnumForValue(elem.text)
            if (enumName != null) return generateEnumValueDoc(elem.text, enumName, schema)
        }
        if (tokenType == HytaleUiTokenTypes.AT_IDENTIFIER) return generateVariableDoc(elem)
        if (tokenType == HytaleUiTokenTypes.DOLLAR_IDENTIFIER) return generateFileImportDoc(elem)

        return null
    }

    private fun getQuickInfoForElement(elem: PsiElement, schema: HytaleUiSchemaService): String? {
        val tokenType = elem.node.elementType
        val parentType = elem.parent?.node?.elementType

        if (elem is HytaleUiVariableDeclElement) {
            val name = elem.name ?: return null
            return highlightCode(elem.project, "@$name = ${extractFullVariableValue(elem) ?: "..."}")
        }
        if (elem is HytaleUiFileImportElement) {
            val alias = elem.name ?: return null
            val path = HytaleUiPsiUtil.getImportPath(elem)
            return highlightCode(elem.project, "\$$alias = \"$path\"")
        }
        if (tokenType == HytaleUiTokenTypes.IDENTIFIER && parentType == HytaleUiElementTypes.NODE_DECL && isFirstIdentifier(elem)) {
            val info = schema.getNodeInfo(elem.text) ?: return null
            val children = if (info.hasChildren) ", container" else ""
            return "<b>node</b> ${elem.text} — ${info.properties.size} properties$children"
        }
        if (tokenType == HytaleUiTokenTypes.IDENTIFIER && parentType == HytaleUiElementTypes.TYPE_CONSTRUCTOR && isFirstIdentifier(elem)) {
            val info = schema.getTypeInfo(elem.text) ?: return null
            return "<b>type</b> ${elem.text} — ${info.properties.size} properties"
        }
        if (tokenType == HytaleUiTokenTypes.IDENTIFIER && parentType == HytaleUiElementTypes.PROPERTY_KEY) {
            val nodeType = HytaleUiPsiUtil.findEnclosingNodeType(elem)
            if (nodeType != null) {
                val expectedType = schema.getPropertyExpectedType(nodeType, elem.text)
                if (expectedType != null) return "<b>${elem.text}</b>: ${simplifyType(expectedType)} <i>($nodeType)</i>"
            }
        }
        if (parentType == HytaleUiElementTypes.PROPERTY_KEY) {
            val nodeType = HytaleUiPsiUtil.findEnclosingNodeType(elem)
            if (nodeType != null) {
                val expectedType = schema.getPropertyExpectedType(nodeType, elem.text)
                if (expectedType != null) return "<b>${elem.text}</b>: ${simplifyType(expectedType)} <i>($nodeType)</i>"
            }
        }
        if (tokenType == HytaleUiTokenTypes.IDENTIFIER && parentType == HytaleUiElementTypes.PROPERTY_LIST_ENTRY && isFirstIdentifier(elem)) {
            val contextType = HytaleUiPsiUtil.inferPropertyContextType(elem.parent)
            if (contextType != null) {
                val expectedType = schema.getPropertyExpectedType(contextType, elem.text)
                if (expectedType != null) return "<b>${elem.text}</b>: ${simplifyType(expectedType)} <i>($contextType)</i>"
            }
        }
        if (tokenType == HytaleUiTokenTypes.IDENTIFIER && isInValueContext(elem)) {
            val enumName = schema.getEnumForValue(elem.text)
            if (enumName != null) return "<b>${elem.text}</b> <i>($enumName)</i>"
        }
        if (tokenType == HytaleUiTokenTypes.AT_IDENTIFIER) {
            val varName = elem.text.removePrefix("@")
            val varDecl = resolveVariableDecl(elem)
            if (varDecl != null) {
                return highlightCode(elem.project, "@$varName = ${extractFullVariableValue(varDecl) ?: "..."}")
            }
            return "@$varName"
        }
        if (tokenType == HytaleUiTokenTypes.DOLLAR_IDENTIFIER) {
            val alias = elem.text.removePrefix("$")
            val parent = elem.parent
            if (parent?.node?.elementType == HytaleUiElementTypes.FILE_IMPORT) {
                val path = HytaleUiPsiUtil.getImportPath(parent)
                return highlightCode(elem.project, "\$$alias = \"$path\"")
            }
            return "\$$alias"
        }
        return null
    }

    // ── Node documentation ──

    private fun generateNodeDoc(name: String, schema: HytaleUiSchemaService): String? {
        val info = schema.getNodeInfo(name) ?: return null
        val sb = StringBuilder()
        sb.append(DocumentationMarkup.DEFINITION_START)
        sb.append("<b>node</b> $name")
        sb.append(DocumentationMarkup.DEFINITION_END)

        sb.append(DocumentationMarkup.CONTENT_START)
        if (info.hasChildren) {
            sb.append("<p><i>Can contain child nodes</i></p>")
        }

        if (info.properties.isNotEmpty()) {
            sb.append(buildPropertiesCodeBlock(name, info.properties))
        }

        if (info.events.isNotEmpty()) {
            sb.append("<p><b>Events:</b> <code>${info.events.joinToString("</code>, <code>")}</code></p>")
        }
        sb.append(DocumentationMarkup.CONTENT_END)
        return sb.toString()
    }

    // ── Type documentation ──

    private fun generateTypeDoc(name: String, schema: HytaleUiSchemaService): String? {
        val info = schema.getTypeInfo(name) ?: return null
        val sb = StringBuilder()
        sb.append(DocumentationMarkup.DEFINITION_START)
        sb.append("<b>type</b> $name")
        sb.append(DocumentationMarkup.DEFINITION_END)

        if (info.properties.isNotEmpty()) {
            sb.append(DocumentationMarkup.CONTENT_START)
            sb.append(buildPropertiesCodeBlock(name, info.properties))
            sb.append(DocumentationMarkup.CONTENT_END)
        }
        return sb.toString()
    }

    // ── Property documentation ──

    private fun generatePropertyDoc(propName: String, element: PsiElement, schema: HytaleUiSchemaService): String? {
        val nodeType = HytaleUiPsiUtil.findEnclosingNodeType(element) ?: return null
        val expectedTypes = schema.getNodeProperties(nodeType)?.get(propName) ?: return null
        return buildPropertyDoc(propName, expectedTypes, nodeType, schema, element)
    }

    private fun generatePropertyListEntryDoc(propName: String, entry: PsiElement, schema: HytaleUiSchemaService): String? {
        val contextType = HytaleUiPsiUtil.inferPropertyContextType(entry) ?: return null
        val props = schema.getTypeProperties(contextType) ?: schema.getNodeProperties(contextType)
        val expectedTypes = props?.get(propName) ?: return null
        return buildPropertyDoc(propName, expectedTypes, contextType, schema, entry)
    }

    private fun buildPropertyDoc(propName: String, expectedTypes: List<String>, contextType: String, schema: HytaleUiSchemaService, element: PsiElement): String {
        val sb = StringBuilder()
        val typeStr = expectedTypes.joinToString(" | ") { simplifyType(it) }

        sb.append(DocumentationMarkup.DEFINITION_START)
        sb.append("<b>$propName</b>: $typeStr")
        sb.append(DocumentationMarkup.DEFINITION_END)

        sb.append(DocumentationMarkup.CONTENT_START)
        sb.append("<p>Property of <code>$contextType</code></p>")

        for (typeName in expectedTypes) {
            val typeInfo = schema.getTypeInfo(typeName)
            if (typeInfo != null && typeInfo.properties.isNotEmpty()) {
                sb.append(buildPropertiesCodeBlock(typeName, typeInfo.properties))
            }
            val enumValues = schema.getEnumValues(typeName)
            if (enumValues != null) {
                sb.append("<p><b>$typeName values:</b></p>")
                sb.append("<pre><code>")
                sb.append(enumValues.joinToString(" | "))
                sb.append("</code></pre>")
            }
        }
        sb.append(DocumentationMarkup.CONTENT_END)
        return sb.toString()
    }

    // ── Enum value documentation ──

    private fun generateEnumValueDoc(value: String, enumName: String, schema: HytaleUiSchemaService): String {
        val allValues = schema.getEnumValues(enumName) ?: listOf(value)
        val sb = StringBuilder()
        sb.append(DocumentationMarkup.DEFINITION_START)
        sb.append("<b>$value</b> — enum <b>$enumName</b>")
        sb.append(DocumentationMarkup.DEFINITION_END)
        sb.append(DocumentationMarkup.CONTENT_START)
        sb.append("<pre><code>")
        sb.append(allValues.joinToString(" | ") { if (it == value) "<b>$it</b>" else it })
        sb.append("</code></pre>")
        sb.append(DocumentationMarkup.CONTENT_END)
        return sb.toString()
    }

    // ── Variable documentation ──

    private fun generateVariableDoc(element: PsiElement): String {
        val varName = element.text.removePrefix("@")
        val parent = element.parent ?: return "<code>@$varName</code>"
        val parentType = parent.node.elementType

        if (parentType == HytaleUiElementTypes.VARIABLE_DECL) {
            return buildVariableDocHtml(varName, parent, "declaration")
        }
        val varDecl = resolveVariableDecl(element)
        if (varDecl != null) {
            val file = varDecl.containingFile?.name ?: ""
            val location = if (file.isNotEmpty()) "variable (from $file)" else "variable"
            return buildVariableDocHtml(varName, varDecl, location)
        }
        return "${DocumentationMarkup.DEFINITION_START}<b>@$varName</b>${DocumentationMarkup.DEFINITION_END}"
    }

    private fun generateVariableDeclDoc(element: PsiElement): String {
        val varName = (element as? HytaleUiVariableDeclElement)?.name ?: "?"
        return buildVariableDocHtml(varName, element, "declaration")
    }

    private fun generateFileImportDoc(element: PsiElement): String {
        val alias = element.text.removePrefix("$")
        val parent = element.parent ?: return "<code>\$$alias</code>"
        if (parent.node.elementType == HytaleUiElementTypes.FILE_IMPORT) {
            val path = HytaleUiPsiUtil.getImportPath(parent)
            return "${DocumentationMarkup.DEFINITION_START}" +
                    highlightCode(element.project, "\$$alias = \"$path\"") +
                    "${DocumentationMarkup.DEFINITION_END}" +
                    "${DocumentationMarkup.CONTENT_START}<p><i>File import</i></p>${DocumentationMarkup.CONTENT_END}"
        }
        return "${DocumentationMarkup.DEFINITION_START}<b>\$$alias</b>${DocumentationMarkup.DEFINITION_END}"
    }

    private fun generateFileImportDeclDoc(element: PsiElement): String {
        val alias = (element as? HytaleUiFileImportElement)?.name ?: "?"
        val path = HytaleUiPsiUtil.getImportPath(element)
        return "${DocumentationMarkup.DEFINITION_START}" +
                highlightCode(element.project, "\$$alias = \"$path\"") +
                "${DocumentationMarkup.DEFINITION_END}" +
                "${DocumentationMarkup.CONTENT_START}<p><i>File import</i></p>${DocumentationMarkup.CONTENT_END}"
    }

    private fun buildVariableDocHtml(name: String, varDecl: PsiElement, kind: String): String {
        val sb = StringBuilder()
        val fullValue = extractFullVariableValue(varDecl)

        sb.append(DocumentationMarkup.DEFINITION_START)
        sb.append("<b>@$name</b>")
        sb.append(DocumentationMarkup.DEFINITION_END)

        sb.append(DocumentationMarkup.CONTENT_START)
        sb.append("<p><i>$kind</i></p>")
        if (fullValue != null) {
            val code = "@$name = $fullValue;"
            sb.append(highlightCode(varDecl.project, code))
        }
        sb.append(DocumentationMarkup.CONTENT_END)
        return sb.toString()
    }

    // ── Code rendering helpers ──

    /**
     * Builds a syntax-highlighted code block showing properties of a node/type
     * in .ui file syntax.
     */
    private fun buildPropertiesCodeBlock(typeName: String, properties: Map<String, List<String>>): String {
        val sb = StringBuilder()
        sb.appendLine("$typeName {")
        for ((prop, types) in properties.entries.sortedBy { it.key }) {
            val typeStr = types.joinToString(" | ") { simplifyType(it) }
            sb.appendLine("  $prop: $typeStr;")
        }
        sb.append("}")
        return "<pre><code>${escapeHtml(sb.toString())}</code></pre>"
    }

    /**
     * Renders code with syntax highlighting using the HytaleUi lexer.
     */
    private fun highlightCode(project: com.intellij.openapi.project.Project, code: String): String {
        val sb = StringBuilder()
        try {
            HtmlSyntaxInfoUtil.appendHighlightedByLexerAndEncodedAsHtmlCodeSnippet(
                sb,
                project,
                cz.creeperface.hytale.uimanager.intellij.HytaleUiLanguage.INSTANCE,
                code,
                1.0f
            )
        } catch (_: Exception) {
            sb.append("<pre><code>${escapeHtml(code)}</code></pre>")
        }
        return sb.toString()
    }

    private fun extractFullVariableValue(variableDecl: PsiElement): String? {
        val equals = variableDecl.node.findChildByType(HytaleUiTokenTypes.EQUALS) ?: return null
        var child = equals.psi.nextSibling
        while (child != null && child.node.elementType == HytaleUiTokenTypes.WHITE_SPACE) {
            child = child.nextSibling
        }
        if (child == null) return null

        // Collect all value text until semicolon or end
        val sb = StringBuilder()
        while (child != null) {
            val et = child.node.elementType
            if (et == HytaleUiTokenTypes.SEMICOLON) break
            sb.append(child.text)
            child = child.nextSibling
        }
        val text = sb.toString().trim()
        return text.ifEmpty { null }
    }

    private fun resolveVariableDecl(atIdentElement: PsiElement): PsiElement? {
        val parent = atIdentElement.parent ?: return null
        val ref = parent.reference
        val resolved = ref?.resolve()
        if (resolved != null && resolved.node.elementType == HytaleUiElementTypes.VARIABLE_DECL) {
            return resolved
        }
        return null
    }

    // ── Helpers ──

    private fun simplifyType(type: String): String = type.substringAfterLast('.')

    private fun escapeHtml(text: String): String = text
        .replace("&", "&amp;")
        .replace("<", "&lt;")
        .replace(">", "&gt;")

    private fun isFirstIdentifier(element: PsiElement): Boolean {
        var sibling = element.prevSibling
        while (sibling != null) {
            val type = sibling.node.elementType
            if (type != HytaleUiTokenTypes.WHITE_SPACE && type != HytaleUiTokenTypes.LINE_COMMENT) return false
            sibling = sibling.prevSibling
        }
        return true
    }

    private fun isInValueContext(element: PsiElement): Boolean {
        val parent = element.parent ?: return false
        val parentType = parent.node.elementType
        return parentType == HytaleUiElementTypes.PROPERTY || parentType == HytaleUiElementTypes.VARIABLE_DECL
    }
}
