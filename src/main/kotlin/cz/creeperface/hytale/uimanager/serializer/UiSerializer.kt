package cz.creeperface.hytale.uimanager.serializer

import com.hypixel.hytale.server.core.Message
import cz.creeperface.hytale.uimanager.*
import cz.creeperface.hytale.uimanager.special.UiListGroup
import cz.creeperface.hytale.uimanager.type.UiPatchStyle
import kotlin.reflect.KClass
import kotlin.reflect.KVisibility
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

object UiSerializer {

    fun serialize(page: UiPage): String {
        val sb = StringBuilder()
        page.variables.forEach { (name, value) ->
            sb.append("@").append(name).append(" = ")
            sb.append(serializeVariableValue(value))
            sb.append(";\n")
        }
        if (page.variables.isNotEmpty()) {
            sb.append("\n")
        }
        sb.append(page.nodes.joinToString("\n") { serialize(toGenericNode(it)) })
        return sb.toString()
    }

    private fun serializeVariableValue(value: Any, indent: Int = 0): String {
        return when (value) {
            is UiNode -> {
                val genericNode = toGenericNode(value)
                serialize(genericNode, indent).trim()
            }
            is UiType -> {
                val typeName = value::class.simpleName?.removePrefix("Ui") ?: "Unknown"
                val props = serializeObject(value)
                val formattedProps = formatValue(typeName, props, indent)
                "$typeName$formattedProps"
            }
            else -> formatValue("", serializeValue(value), indent)
        }
    }

    private fun getHierarchy(instance: Any, externalTemplates: List<Any> = emptyList()): List<Any> {
        val result = mutableListOf<Any>()
        result.add(instance)

        val ownTemplates = when (instance) {
            is UiNode -> instance.templates
            is UiType -> instance.templates
            else -> emptyList<Any>()
        }

        // Priority: instance > ownTemplates (reversed) > externalTemplates (reversed)
        for (i in ownTemplates.indices.reversed()) {
            result.addAll(getHierarchy(ownTemplates[i]))
        }

        for (i in externalTemplates.indices.reversed()) {
            result.addAll(getHierarchy(externalTemplates[i]))
        }

        return result.distinct()
    }

    private fun getAllProperties(clazz: KClass<*>): List<String> {
        return clazz.memberProperties
            .filter { it.visibility == KVisibility.PUBLIC }
            .filter { prop ->
                val hasAnnotation = prop.annotations.any { it.annotationClass == ExcludeProperty::class } ||
                        prop.getter.annotations.any { it.annotationClass == ExcludeProperty::class }

                if (hasAnnotation) return@filter false

                // Check hierarchy for the annotation
                val name = prop.name
                val hasAnnotationInHierarchy = clazz.supertypes
                    .mapNotNull { it.classifier as? KClass<*> }
                    .any { superClazz ->
                        superClazz.memberProperties.find { it.name == name }?.let { superProp ->
                            superProp.annotations.any { it.annotationClass == ExcludeProperty::class } ||
                                    superProp.getter.annotations.any { it.annotationClass == ExcludeProperty::class }
                        } ?: false
                    }

                !hasAnnotationInHierarchy
            }
            .map { it.name }
            .distinct()
    }

    fun toGenericNode(uiNode: Any, templates: List<Any> = emptyList(), flattenPatchStyle: Boolean = true): GenericNode {
        val clazz = uiNode::class
        val nodeName = clazz.companionObjectInstance?.let { companion ->
            val nodeNameProp = companion::class.memberProperties.find { it.name == "NODE_NAME" }
            nodeNameProp?.apply { isAccessible = true }?.call(companion) as? String
        } ?: (uiNode as? UiNode)?.let { "Group" } ?: clazz.simpleName?.removePrefix("Ui") ?: "Unknown"

        val idProp = clazz.memberProperties.find { it.name == "id" }
        val id = idProp?.apply { isAccessible = true }?.call(uiNode) as? String

        val omitNameProp = clazz.memberProperties.find { it.name == "omitName" }
        val omitName = omitNameProp?.apply { isAccessible = true }?.call(uiNode) as? Boolean ?: false

        val genericNode = GenericNode(
            if (omitName) "" else nodeName,
            id,
            uiNode is UiListGroup,
            uiNode
        )

        val propsMap = serializeObject(uiNode, templates, flattenPatchStyle)
        propsMap.forEach { (name, value) ->
            if (value != null) genericNode.properties[name] = value
        }

        // Children merging logic
        val hierarchy = getHierarchy(uiNode, templates)

        val allChildIds = mutableSetOf<String>()
        val idToChildren = mutableMapOf<String, MutableList<UiNode>>()

        hierarchy.forEach { h ->
            val children = (h as? UiNodeWithChildren)?.children ?: emptyList()
            children.forEach { child ->
                if (child.id != null) {
                    allChildIds.add(child.id!!)
                    idToChildren.getOrPut(child.id!!) { mutableListOf() }.add(child)
                }
            }
        }

        // Ordered IDs (keep first appearance in hierarchy)
        val orderedIds = mutableListOf<String>()
        hierarchy.forEach { h ->
            val children = (h as? UiNodeWithChildren)?.children ?: emptyList()
            children.forEach { child ->
                if (child.id != null && child.id!! !in orderedIds) {
                    orderedIds.add(child.id!!)
                }
            }
        }

        orderedIds.forEach { childId ->
            val childrenForId = idToChildren[childId]!!
            val primary = childrenForId.first()
            val remaining = childrenForId.drop(1)
            genericNode.children.add(toGenericNode(primary, remaining, flattenPatchStyle))
        }

        // Handle children without IDs (only from primary node)
        val children = (uiNode as? UiNodeWithChildren)?.children ?: emptyList()
        children.filter { it.id == null }.forEach { child ->
            genericNode.children.add(toGenericNode(child, flattenPatchStyle = flattenPatchStyle))
        }

        return genericNode
    }

    private fun serializeValue(value: Any): Any {
        if (value is Color) return GenericNode.Identifier(value.toString())
        if (value is Enum<*>) return GenericNode.Identifier(value.name)
        if (value is Number || value is Boolean) return value
        if (value is List<*>) return value.map { if (it != null) serializeValue(it) else null }
        if (value is Message) return serializeMessage(value)

        val s = value.toString()
        if (isIdentifierInternal(s)) {
            return GenericNode.Identifier(s)
        }

        return if (value is String) {
            s
        } else {
            GenericNode.Identifier(s)
        }
    }

    private fun serializeMessage(message: Message): Any {
        val messageId = message.messageId
        val serialized = if (messageId != null) {
            GenericNode.Identifier("%$messageId")
        } else {
            message.rawText ?: ""
        }
        return GenericNode.MessageValue(message, serialized)
    }

    private fun isIdentifierInternal(s: String): Boolean {
        if (s.isEmpty()) return false

        // Variable/Import
        if (s.startsWith("@") || s.startsWith("$")) return true

        return false
    }

    private fun serializeObject(
        value: Any,
        templates: List<Any> = emptyList(),
        flattenPatchStyle: Boolean = true
    ): Map<String, Any?> {
        val clazz = value::class
        val map = mutableMapOf<String, Any?>()

        val hierarchy = getHierarchy(value, templates)
        val propNames = getAllProperties(clazz).sortedBy { it.replaceFirstChar { it.uppercase() } }

        propNames.forEach { propName ->
            val allInstances = mutableListOf<Any>()
            hierarchy.forEach { h ->
                val prop = h::class.memberProperties.find { it.name == propName }
                val v = prop?.apply { isAccessible = true }?.call(h)
                if (v != null) allInstances.add(v)
            }

            if (allInstances.isEmpty()) return@forEach

            val pascalName = propName.replaceFirstChar { it.uppercase() }
            val primary = allInstances.first()

            if (primary is UiType) {
                var serialized: Any? = serializeObject(primary, allInstances.drop(1), flattenPatchStyle)
                if (flattenPatchStyle && primary is UiPatchStyle) {
                    val m = serialized as Map<*, *>
                    if (m.size == 1 && m.containsKey("Color")) {
                        serialized = m["Color"]
                    }
                }
                if (serialized != null && (serialized !is Map<*, *> || (serialized is Map<*, *> && serialized.isNotEmpty()))) {
                    map[pascalName] = serialized
                }
            } else {
                map[pascalName] = serializeValue(primary)
            }
        }
        return map
    }

    fun serialize(node: GenericNode, indent: Int = 0, listParent: Boolean = false): String {
        val sb = StringBuilder()
        val indentStr = "  ".repeat(indent)

        val header = StringBuilder()
        header.append(indentStr)
        if (node.name.isNotEmpty()) {
            header.append(node.name)
        }
        if (!node.id.isNullOrEmpty() && !listParent) {
            if (node.name.isNotEmpty()) {
                header.append(" ")
            }

            header.append("#").append(node.id)
        }

        sb.append(header).append(" {\n")

        val nextIndent = indent + 1
        val nextIndentStr = "  ".repeat(nextIndent)

        node.properties.forEach { (name, value) ->
            val formatted = formatValue(name, value, nextIndent)
            if (formatted.isEmpty()) return@forEach
            sb.append(nextIndentStr).append(name).append(": ")
            sb.append(formatted)
            sb.append(";\n")
        }

        if (node.children.isNotEmpty()) {
            sb.append("\n")
            node.children.forEachIndexed { index, child ->
                sb.append(serialize(child, nextIndent, node.listNode))
                if (index < node.children.size - 1) {
                    sb.append("\n")
                }
            }
        }

        sb.append(indentStr).append("}\n")
        return sb.toString()
    }

    fun formatValue(name: String, value: Any?, indent: Int = 0, quoteStrings: Boolean = true): String {
        return when (value) {
            null -> "null"
            is GenericNode.MessageValue -> formatValue(name, value.serialized, indent, quoteStrings)
            is GenericNode.Identifier -> value.value
            is String -> if (quoteStrings) "\"${value.replace("\\", "\\\\").replace("\"", "\\\"")}\"" else value
            is Number -> value.toString()
            is Boolean -> value.toString()
            is Map<*, *> -> {
                if (value.isEmpty()) return "()"
                val nextIndent = indent + 1
                val indentStr = "  ".repeat(nextIndent)

                // Rule: Styles are multi-line, others are single-line
                val multiLine = name.endsWith("Style")

                val entries = value.entries.joinToString(if (multiLine) ",\n$indentStr" else ", ") { (k, v) ->
                    if (v == "___SPREAD___" && k.toString().startsWith("...")) {
                        k.toString()
                    } else {
                        "$k: ${formatValue(k.toString(), v, nextIndent, quoteStrings)}"
                    }
                }

                if (multiLine) {
                    "(\n$indentStr$entries,\n${"  ".repeat(indent)})"
                } else {
                    "($entries)"
                }
            }
            is List<*> -> {
                value.joinToString(", ") { formatValue("", it, indent, quoteStrings) }
            }
            else -> value.toString()
        }
    }

}
