package cz.creeperface.hytale.uimanager.intellij

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.intellij.openapi.application.ApplicationManager

class HytaleUiSchemaService {

    companion object {
        fun getInstance(): HytaleUiSchemaService =
            ApplicationManager.getApplication().getService(HytaleUiSchemaService::class.java)
    }

    data class NodeInfo(
        val name: String,
        val hasChildren: Boolean,
        val properties: Map<String, List<String>>,
        val events: List<String>
    )

    data class TypeInfo(
        val name: String,
        val properties: Map<String, List<String>>
    )

    val nodeNames: Set<String>
    val typeNames: Set<String>
    val enumNames: Set<String>

    private val nodes: Map<String, NodeInfo>
    private val types: Map<String, TypeInfo>
    private val enums: Map<String, List<String>>

    // Reverse lookup: enum value -> enum name
    private val enumValueToEnum: Map<String, String>

    init {
        val json = loadSchemaJson()
        nodes = parseNodes(json)
        types = parseTypes(json)
        enums = parseEnums(json)

        nodeNames = nodes.keys
        typeNames = types.keys
        enumNames = enums.keys

        val reverseMap = mutableMapOf<String, String>()
        for ((enumName, values) in enums) {
            for (value in values) {
                reverseMap[value] = enumName
            }
        }
        enumValueToEnum = reverseMap
    }

    fun getNodeInfo(name: String): NodeInfo? = nodes[name]
    fun getTypeInfo(name: String): TypeInfo? = types[name]
    fun getEnumValues(name: String): List<String>? = enums[name]

    fun isNodeName(name: String): Boolean = name in nodeNames
    fun isTypeName(name: String): Boolean = name in typeNames
    fun isEnumValue(value: String): Boolean = value in enumValueToEnum

    fun getEnumForValue(value: String): String? = enumValueToEnum[value]

    fun getNodeProperties(nodeName: String): Map<String, List<String>>? = nodes[nodeName]?.properties
    fun getTypeProperties(typeName: String): Map<String, List<String>>? = types[typeName]?.properties

    /**
     * Given a context type and property name, returns the expected property type.
     */
    fun getPropertyExpectedType(contextType: String, propertyName: String): String? {
        val nodeInfo = nodes[contextType]
        if (nodeInfo != null) {
            return nodeInfo.properties[propertyName]?.firstOrNull()
        }
        val typeInfo = types[contextType]
        if (typeInfo != null) {
            return typeInfo.properties[propertyName]?.firstOrNull()
        }
        return null
    }

    /**
     * Primitive type names used in the schema.
     */
    private val primitiveTypes = setOf(
        "String", "Integer", "Float", "Decimal", "Boolean", "Char", "Color"
    )

    fun isPrimitiveType(type: String): Boolean = type in primitiveTypes

    /**
     * Property names whose String values represent file paths (textures, sounds, assets).
     * These get file navigation and existence checks.
     */
    private val pathPropertyNames = setOf(
        "TexturePath", "MaskTexturePath", "BarTexturePath", "EffectTexturePath",
        "AssetPath", "LabelMaskTexturePath", "ContentMaskTexturePath",
        "SoundPath",
        "IconTexturePath",
        "DefaultArrowTexturePath", "HoveredArrowTexturePath",
        "PressedArrowTexturePath", "DisabledArrowTexturePath",
    )

    /**
     * Returns true if a property with the given name holds a file path value.
     */
    fun isPathProperty(propertyName: String): Boolean = propertyName in pathPropertyNames

    /**
     * Types that additionally accept a bare string as a shorthand.
     * PatchStyle: string = texture path
     * SoundStyle: string = sound path
     */
    private val stringShorthandTypes = setOf("PatchStyle", "SoundStyle")

    /**
     * Types that additionally accept a color literal as a shorthand.
     * PatchStyle: color = solid color background
     */
    private val colorShorthandTypes = setOf("PatchStyle")

    /**
     * Checks if a given value kind is compatible with the expected property type.
     *
     * Value kinds (from PSI):
     * - "String" = string literal
     * - "Integer" = integer literal
     * - "Float" = float literal
     * - "Boolean" = boolean literal
     * - "Color" = color literal (#RRGGBB)
     * - "ServerString" = %server.path
     * - "Object" = anonymous object (...) — type inferred from property, valid for any complex type
     * - "TypeConstructor:TypeName" = named type constructor TypeName(...)
     * - "Identifier:Value" = bare identifier (enum value)
     * - "Variable" = @variable reference (cannot be checked statically)
     *
     * Returns null if we can't determine compatibility (e.g., variable refs), true/false otherwise.
     */
    fun isValueCompatible(expectedType: String, valueKind: String): Boolean? {
        // Variable references can hold anything - skip checking
        if (valueKind == "Variable") return null

        // Anonymous objects (...) are valid for any complex type — the type is inferred from
        // the property. This is the standard shorthand syntax.
        if (valueKind == "Object") {
            // Valid for any known complex type, not valid for primitives/enums
            return expectedType in typeNames ||
                    expectedType !in primitiveTypes && expectedType !in enumNames
        }

        // Server strings resolve to String at runtime
        if (valueKind == "ServerString") {
            return expectedType == "String" ||
                    expectedType == "com.hypixel.hytale.server.core.Message"
        }

        return when (expectedType) {
            // Primitive types
            "String" -> valueKind == "String" || valueKind == "ServerString"
            "Integer" -> valueKind == "Integer" || valueKind == "Float"
            "Float", "Decimal" -> valueKind == "Integer" || valueKind == "Float"
            "Boolean" -> valueKind == "Boolean"
            "Char" -> valueKind == "String"
            "Color" -> valueKind == "Color" || valueKind == "String"
            "com.hypixel.hytale.server.core.Message" -> true // accepts many things

            else -> {
                // Known enum type
                if (expectedType in enumNames) {
                    if (valueKind.startsWith("Identifier:")) {
                        val enumValue = valueKind.removePrefix("Identifier:")
                        val expectedValues = enums[expectedType]
                        return expectedValues?.contains(enumValue) ?: true
                    }
                    return false
                }

                // Known complex type (Anchor, LabelStyle, PatchStyle, etc.)
                if (expectedType in typeNames) {
                    // Named type constructor: TypeName(...)
                    if (valueKind.startsWith("TypeConstructor:")) return true

                    // Some types accept string shorthand (PatchStyle = texture path, SoundStyle = sound path)
                    if (valueKind == "String" && expectedType in stringShorthandTypes) return true

                    // Some types accept color shorthand (PatchStyle = solid color)
                    if (valueKind == "Color" && expectedType in colorShorthandTypes) return true

                    // Primitives and enum values are not valid for complex types
                    return false
                }

                // Unknown type (e.g., List<...>, server types) - don't flag
                null
            }
        }
    }

    private fun loadSchemaJson(): JsonObject {
        val stream = javaClass.getResourceAsStream("/schema/ui_structure_report.json")
            ?: return JsonObject()
        return stream.bufferedReader().use { reader ->
            Gson().fromJson(reader, JsonObject::class.java)
        }
    }

    private fun parseNodes(json: JsonObject): Map<String, NodeInfo> {
        val result = mutableMapOf<String, NodeInfo>()
        val nodesObj = json.getAsJsonObject("nodes") ?: return result
        for ((name, element) in nodesObj.entrySet()) {
            val obj = element.asJsonObject
            val properties = mutableMapOf<String, List<String>>()
            val propsObj = obj.getAsJsonObject("properties")
            if (propsObj != null) {
                for ((propName, propTypes) in propsObj.entrySet()) {
                    properties[propName] = propTypes.asJsonArray.map { it.asString }
                }
            }
            val events = obj.getAsJsonArray("events")?.map { it.asString } ?: emptyList()
            val hasChildren = obj.get("hasChildren")?.asBoolean ?: false
            result[name] = NodeInfo(name, hasChildren, properties, events)
        }
        return result
    }

    private fun parseTypes(json: JsonObject): Map<String, TypeInfo> {
        val result = mutableMapOf<String, TypeInfo>()
        val typesObj = json.getAsJsonObject("types") ?: return result
        for ((name, element) in typesObj.entrySet()) {
            val obj = element.asJsonObject
            val properties = mutableMapOf<String, List<String>>()
            val propsObj = obj.getAsJsonObject("properties")
            if (propsObj != null) {
                for ((propName, propTypes) in propsObj.entrySet()) {
                    properties[propName] = propTypes.asJsonArray.map { it.asString }
                }
            }
            result[name] = TypeInfo(name, properties)
        }
        return result
    }

    private fun parseEnums(json: JsonObject): Map<String, List<String>> {
        val result = mutableMapOf<String, List<String>>()
        val enumsObj = json.getAsJsonObject("enums") ?: return result
        for ((name, element) in enumsObj.entrySet()) {
            // Enums are stored as direct arrays: {"EnumName": ["Value1", "Value2"]}
            if (element.isJsonArray) {
                result[name] = element.asJsonArray.map { it.asString }
            } else if (element.isJsonObject) {
                val obj = element.asJsonObject
                val values = obj.getAsJsonArray("values")?.map { it.asString } ?: emptyList()
                result[name] = values
            }
        }
        return result
    }
}
