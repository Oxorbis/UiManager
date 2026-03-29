package cz.creeperface.hytale.uimanager

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import java.io.File

class UiGenerator(private val reportFile: File, private val outputDir: File) {

    private val gson = Gson()
    private val rootPackage = "cz.creeperface.hytale.uimanager"
    private val nodePackage = "$rootPackage.node"
    private val typePackage = "$rootPackage.type"
    private val enumPackage = "$rootPackage.enum"
    private val builderPackage = "$rootPackage.builder"

    private var nodeOverrides: Map<String, Map<String, String>> = emptyMap()
    private var typeOverrides: Map<String, Map<String, String>> = emptyMap()

    private var enumsJson: JsonObject? = null
    private var typesJson: JsonObject? = null

    // Properties present in every single node type - promoted to UiNode/BaseUiNode
    private var universalProperties: Map<String, TypeName> = emptyMap()

    fun generate() {
        val json = gson.fromJson(reportFile.readText(), JsonObject::class.java)
        loadOverrides()

        val enums = json.getAsJsonObject("enums")
        val types = json.getAsJsonObject("types")
        val nodes = json.getAsJsonObject("nodes")

        this.enumsJson = enums
        this.typesJson = types
        this.universalProperties = computeUniversalProperties(nodes)

        generateInfrastructure()
        generateEnums(enums)
        generateTypes(types)
        generateNodes(nodes)
        generateIdGenerator()
        generateBuilders(nodes)
    }

    private fun computeUniversalProperties(nodes: JsonObject): Map<String, TypeName> {
        val allNodeProperties = nodes.entrySet().map { (_, data) ->
            val props = data.asJsonObject.getAsJsonObject("properties") ?: return@map emptySet<String>()
            props.entrySet().map { it.key }.toSet()
        }
        if (allNodeProperties.isEmpty()) return emptyMap()

        val universal = allNodeProperties.reduce { acc, set -> acc.intersect(set) }

        // Resolve types from the first node that has each property
        val firstNode = nodes.entrySet().first().value.asJsonObject.getAsJsonObject("properties")
        return universal.associateWith { propName ->
            val propTypes = firstNode.getAsJsonArray(propName).map { it.asString }
            mapToKotlinType(propTypes)
        }
    }

    private fun loadOverrides() {
        val overridesFile = File(reportFile.parentFile, "ui_type_overrides.json")
        if (!overridesFile.exists()) return

        val overridesJson = gson.fromJson(overridesFile.readText(), JsonObject::class.java)
        
        nodeOverrides = overridesJson.getAsJsonObject("nodes")?.entrySet()?.associate { (nodeName, overrides) ->
            nodeName to overrides.asJsonObject.entrySet().associate { it.key to it.value.asString }
        } ?: emptyMap()

        typeOverrides = overridesJson.getAsJsonObject("types")?.entrySet()?.associate { (typeName, overrides) ->
            typeName to overrides.asJsonObject.entrySet().associate { it.key to it.value.asString }
        } ?: emptyMap()
    }

    private fun generateInfrastructure() {
        val dslMarker = TypeSpec.annotationBuilder("UiDsl")
            .addAnnotation(AnnotationSpec.builder(DslMarker::class).build())
            .build()

        val uiNodeInterface = ClassName(rootPackage, "UiNode")
        val uiNodeWithChildrenInterface = ClassName(rootPackage, "UiNodeWithChildren")
        val childNodeBuilderInterface = ClassName(rootPackage, "ChildNodeBuilder")
        val uiTypeInterface = ClassName(rootPackage, "UiType")
        val hasDelegatesInterface = ClassName("$rootPackage.property", "HasDelegates")
        val rebindableClass = ClassName("$rootPackage.property", "Rebindable")
        val rebindableFunc = MemberName("$rootPackage.property", "rebindable")
        val eventBindingClass = ClassName("$rootPackage.event", "EventBinding")
        val excludePropertyAnnotation = ClassName(rootPackage, "ExcludeProperty")
        val excludePropertyAnnotationOnGet = AnnotationSpec.builder(excludePropertyAnnotation)
            .useSiteTarget(AnnotationSpec.UseSiteTarget.GET)
            .build()

        val nodeListenerType = LambdaTypeName.get(
            parameters = listOf(ParameterSpec.builder("node", uiNodeInterface).build()),
            returnType = UNIT
        ).copy(nullable = true)

        val uiNodeWithChildren = TypeSpec.interfaceBuilder("UiNodeWithChildren")
            .addProperty(PropertySpec.builder("children", MUTABLE_LIST.parameterizedBy(uiNodeInterface))
                .addAnnotation(excludePropertyAnnotationOnGet)
                .build())
            .addProperty(PropertySpec.builder("nodeListener", nodeListenerType)
                .addAnnotation(excludePropertyAnnotationOnGet)
                .mutable()
                .build())
            .addFunction(FunSpec.builder("addNodeToChildren")
                .addParameter("node", uiNodeInterface)
                .addStatement("children.add(node)")
                .addStatement("nodeListener?.invoke(node)")
                .build())
            .build()

        val childNodeBuilder = TypeSpec.interfaceBuilder("ChildNodeBuilder")
            .addSuperinterface(uiNodeWithChildrenInterface)
            .addFunction(FunSpec.builder("addNode")
                .addModifiers(KModifier.ABSTRACT)
                .addParameter("node", uiNodeInterface)
                .build())
            .build()

        val excludePropertyType = TypeSpec.annotationBuilder("ExcludeProperty")
            .addModifiers(KModifier.PUBLIC)
            .addAnnotation(AnnotationSpec.builder(Target::class)
                .addMember("%T.PROPERTY", AnnotationTarget::class)
                .addMember("%T.FIELD", AnnotationTarget::class)
                .addMember("%T.FUNCTION", AnnotationTarget::class)
                .addMember("%T.PROPERTY_GETTER", AnnotationTarget::class)
                .build())
            .addAnnotation(AnnotationSpec.builder(Retention::class)
                .addMember("%T.RUNTIME", AnnotationRetention::class)
                .build())
            .build()

        val uiNodeBuilder = TypeSpec.interfaceBuilder("UiNode")
            .addAnnotation(ClassName(rootPackage, "UiDsl"))
            .addProperty(PropertySpec.builder("id", String::class.asTypeName().copy(nullable = true))
                .addAnnotation(excludePropertyAnnotationOnGet)
                .mutable().build())
            .addProperty(PropertySpec.builder("omitName", BOOLEAN)
                .addAnnotation(excludePropertyAnnotationOnGet)
                .mutable().build())
            .addProperty(PropertySpec.builder("templates", MUTABLE_LIST.parameterizedBy(uiNodeInterface))
                .addAnnotation(excludePropertyAnnotationOnGet)
                .build())
            .addProperty(PropertySpec.builder("isDirty", BOOLEAN)
                .addAnnotation(excludePropertyAnnotationOnGet)
                .build())
            .addProperty(PropertySpec.builder("nodeListener", nodeListenerType)
                .addAnnotation(excludePropertyAnnotationOnGet)
                .mutable().build())

        // Add universal properties to the interface
        universalProperties.entries.sortedBy { it.key }.forEach { (propName, kotlinType) ->
            val propertyName = propName.replaceFirstChar { it.lowercase() }
            uiNodeBuilder.addProperty(PropertySpec.builder(propertyName, kotlinType)
                .mutable().build())
        }

        val uiNode = uiNodeBuilder
            .addFunction(FunSpec.builder("markDirty").addModifiers(KModifier.ABSTRACT).build())
            .addFunction(FunSpec.builder("resetDirty").addModifiers(KModifier.ABSTRACT).build())
            .addFunction(FunSpec.builder("clone").addModifiers(KModifier.ABSTRACT).returns(uiNodeInterface).build())
            .addFunction(FunSpec.builder("getEventBindings").addModifiers(KModifier.ABSTRACT).returns(LIST.parameterizedBy(eventBindingClass)).build())
            .build()

        val baseUiNodeBuilder = TypeSpec.classBuilder("BaseUiNode")
            .addModifiers(KModifier.ABSTRACT)
            .addSuperinterface(uiNodeInterface)
            .addSuperinterface(hasDelegatesInterface)
            .addProperty(PropertySpec.builder("templates", MUTABLE_LIST.parameterizedBy(uiNodeInterface))
                .addModifiers(KModifier.OVERRIDE)
                .addAnnotation(excludePropertyAnnotation)
                .initializer("mutableListOf()")
                .build())
            .addProperty(PropertySpec.builder("delegates", MUTABLE_MAP.parameterizedBy(STRING, rebindableClass.parameterizedBy(STAR)))
                .addModifiers(KModifier.OVERRIDE)
                .addAnnotation(excludePropertyAnnotation)
                .initializer("mutableMapOf()")
                .build())
            .addProperty(PropertySpec.builder("nodeListener", nodeListenerType)
                .addModifiers(KModifier.OVERRIDE)
                .addAnnotation(excludePropertyAnnotation)
                .mutable()
                .initializer("null")
                .build())
            .addProperty(PropertySpec.builder("isDirty", BOOLEAN)
                .addModifiers(KModifier.OPEN, KModifier.OVERRIDE)
                .addAnnotation(excludePropertyAnnotation)
                .getter(FunSpec.getterBuilder()
                    .addStatement("return delegates.values.any { it.isDirty } || templates.any { it.isDirty }")
                    .build())
                .build())
            .addProperty(PropertySpec.builder("eventBindings", MUTABLE_LIST.parameterizedBy(eventBindingClass))
                .addModifiers(KModifier.PRIVATE)
                .initializer("mutableListOf()")
                .build())

        // Add universal properties with rebindable delegates
        universalProperties.entries.sortedBy { it.key }.forEach { (propName, kotlinType) ->
            val propertyName = propName.replaceFirstChar { it.lowercase() }
            baseUiNodeBuilder.addProperty(PropertySpec.builder(propertyName, kotlinType)
                .addModifiers(KModifier.OVERRIDE)
                .mutable(true)
                .delegate("%M(null)", rebindableFunc)
                .build())
        }

        // Add cloneBaseProperties helper for use in generated clone() methods
        val cloneBasePropsBuilder = FunSpec.builder("cloneBaseProperties")
            .addModifiers(KModifier.PROTECTED)
            .addParameter("target", ClassName(rootPackage, "BaseUiNode"))
            .addStatement("target.id = this.id")
            .addStatement("target.omitName = this.omitName")
        universalProperties.entries.sortedBy { it.key }.forEach { (propName, _) ->
            val propertyName = propName.replaceFirstChar { it.lowercase() }
            cloneBasePropsBuilder.addStatement("target.%L = this.%L", propertyName, propertyName)
        }

        val baseUiNode = baseUiNodeBuilder
            .addFunction(FunSpec.builder("markDirty")
                .addModifiers(KModifier.OPEN, KModifier.OVERRIDE)
                .addComment("This is mainly for manual triggers or when a child becomes dirty")
                .build())
            .addFunction(FunSpec.builder("resetDirty")
                .addModifiers(KModifier.OPEN, KModifier.OVERRIDE)
                .addStatement("delegates.values.forEach { it.resetDirty() }")
                .addStatement("templates.forEach { it.resetDirty() }")
                .build())
            .addFunction(FunSpec.builder("getEventBindings")
                .addModifiers(KModifier.OVERRIDE)
                .returns(LIST.parameterizedBy(eventBindingClass))
                .addStatement("return eventBindings")
                .build())
            .addFunction(cloneBasePropsBuilder.build())
            .addFunction(FunSpec.builder("addEventBinding")
                .addParameter("type", ClassName("com.hypixel.hytale.protocol.packets.interface_", "CustomUIEventBindingType"))
                .addParameter("nodeId", STRING)
                .addParameter(ParameterSpec.builder("properties", ClassName("kotlin.reflect", "KProperty0").parameterizedBy(ANY.copy(nullable = true)))
                    .addModifiers(KModifier.VARARG)
                    .build())
                .addParameter("action", LambdaTypeName.get(parameters = listOf(ParameterSpec.builder("context", ClassName("$rootPackage.event", "EventContext")).build()), returnType = UNIT))
                .addStatement("eventBindings.add(%M(type, nodeId, *properties, action = action))", MemberName("$rootPackage.event", "eventBinding"))
                .build())
            .addFunction(FunSpec.builder("addEventBinding")
                .addParameter("eventBinding", eventBindingClass)
                .addStatement("eventBindings.add(eventBinding)")
                .build())
            .build()

        val baseType = TypeSpec.interfaceBuilder("UiType")
            .addAnnotation(ClassName(rootPackage, "UiDsl"))
            .addProperty(PropertySpec.builder("templates", MUTABLE_LIST.parameterizedBy(uiTypeInterface))
                .addAnnotation(excludePropertyAnnotationOnGet)
                .build())
            .build()

        val colorClass = TypeSpec.classBuilder("Color")
            .addModifiers(KModifier.DATA)
            .primaryConstructor(FunSpec.constructorBuilder()
                .addParameter("hex", STRING)
                .addParameter(ParameterSpec.builder("alpha", DOUBLE.copy(nullable = true))
                    .defaultValue("null")
                    .build())
                .build())
            .addProperty(PropertySpec.builder("hex", STRING).initializer("hex").build())
            .addProperty(PropertySpec.builder("alpha", DOUBLE.copy(nullable = true)).initializer("alpha").build())
            .addFunction(FunSpec.builder("toString")
                .addModifiers(KModifier.OVERRIDE)
                .returns(STRING)
                .addStatement("return if (alpha != null) \"${'$'}hex(${'$'}alpha)\" else hex")
                .build())
            .addType(TypeSpec.companionObjectBuilder()
                .addFunction(FunSpec.builder("invoke")
                    .addModifiers(KModifier.OPERATOR)
                    .addParameter("value", STRING)
                    .returns(ClassName(rootPackage, "Color"))
                    .beginControlFlow("return if (value.contains('(') && value.endsWith(')'))")
                    .addStatement("val hex = value.substringBefore('(')")
                    .addStatement("val alpha = value.substringAfter('(').removeSuffix(\")\").toDoubleOrNull()")
                    .addStatement("%T(hex, alpha)", ClassName(rootPackage, "Color"))
                    .nextControlFlow("else")
                    .addStatement("%T(value, null)", ClassName(rootPackage, "Color"))
                    .endControlFlow()
                    .build())
                .build())
            .build()

        val colorExtensions = FileSpec.builder(rootPackage, "ColorExtensions")
            .addProperty(PropertySpec.builder("color", ClassName(rootPackage, "Color"))
                .receiver(STRING)
                .getter(FunSpec.getterBuilder()
                    .addStatement("return %T(this)", ClassName(rootPackage, "Color"))
                    .build())
                .build())
            .build()

        FileSpec.builder(rootPackage, "Infrastructure")
            .addType(dslMarker)
            .addType(excludePropertyType)
            .addType(childNodeBuilder)
            .addType(uiNode)
            .addType(uiNodeWithChildren)
            .addType(baseUiNode)
            .addType(baseType)
            .addType(colorClass)
            .addImport("kotlin.jvm", "JvmStatic")
            .addImport("kotlin.annotation", "AnnotationTarget", "AnnotationRetention", "Target", "Retention")
            .addImport("cz.creeperface.hytale.uimanager", "ExcludeProperty")
            .build()
            .writeTo(outputDir)

        colorExtensions.writeTo(outputDir)
    }

    private fun generateIdGenerator() {
        val typeSpec = TypeSpec.objectBuilder("IdGenerator")
            .addProperty(PropertySpec.builder("counters", MUTABLE_MAP.parameterizedBy(STRING, INT))
                .initializer("mutableMapOf()")
                .addModifiers(KModifier.PRIVATE)
                .build())
            .addFunction(FunSpec.builder("getNext")
                .addParameter("prefix", STRING)
                .returns(STRING)
                .addStatement("val count = counters.getOrDefault(prefix, 0) + 1")
                .addStatement("counters[prefix] = count")
                .addStatement("return \"${'$'}prefix${'$'}count\"")
                .build())
            .addFunction(FunSpec.builder("reset")
                .addStatement("counters.clear()")
                .build())
            .build()

        FileSpec.builder(rootPackage, "IdGenerator")
            .addType(typeSpec)
            .build()
            .writeTo(outputDir)
    }

    private fun generateEnums(enums: JsonObject) {
        enums.entrySet().forEach { (name, values) ->
            val enumSpec = TypeSpec.enumBuilder(name)
                .apply {
                    values.asJsonArray.forEach { value ->
                        addEnumConstant(value.asString)
                    }
                }
                .build()

            FileSpec.builder(enumPackage, name)
                .addType(enumSpec)
                .build()
                .writeTo(outputDir)
        }
    }

    private fun generateTypes(types: JsonObject) {
        val uiTypeInterface = ClassName(rootPackage, "UiType")
        val excludePropertyAnnotation = ClassName(rootPackage, "ExcludeProperty")
        types.entrySet().forEach { (name, data) ->
            val typeData = data.asJsonObject
            val className = "Ui$name"
            val classBuilder = TypeSpec.classBuilder(className)
                .addModifiers(KModifier.DATA)
                .addSuperinterface(uiTypeInterface)
                .addAnnotation(ClassName(rootPackage, "UiDsl"))

            // Add templates property
            val templatesProperty = PropertySpec.builder("templates", MUTABLE_LIST.parameterizedBy(uiTypeInterface))
                .addAnnotation(excludePropertyAnnotation)
                .addModifiers(KModifier.OVERRIDE)
                .initializer("mutableListOf()")
                .build()
            classBuilder.addProperty(templatesProperty)

            val constructorBuilder = FunSpec.constructorBuilder()

            val overrides = typeOverrides[name] ?: emptyMap()
            val properties = typeData.getAsJsonObject("properties")
            val allProperties = properties?.entrySet()?.associate { it.key to it.value.asJsonArray }?.toMutableMap() ?: mutableMapOf()

            overrides.forEach { (propName, propType) ->
                val normalizedPropName = propName.replaceFirstChar { it.lowercase() }
                if (allProperties.keys.none { it.replaceFirstChar { it.lowercase() } == normalizedPropName }) {
                    val jsonArray = com.google.gson.JsonArray()
                    jsonArray.add(propType)
                    allProperties[propName] = jsonArray
                }
            }

            val colorClassName = ClassName(rootPackage, "Color")

            allProperties.entries.sortedBy { it.key }.forEach { (propName, propTypes) ->
                val normalizedPropName = propName.replaceFirstChar { it.lowercase() }
                val override = overrides.entries.find { it.key.replaceFirstChar { it.lowercase() } == normalizedPropName }?.value
                val kotlinType = if (override != null) {
                    mapToKotlinType(listOf(override))
                } else {
                    val propTypeNames = propTypes.asJsonArray.map { it.asString }
                    mapToKotlinType(propTypeNames)
                }
                val propertyName = propName.replaceFirstChar { it.lowercase() }

                val propertySpec = PropertySpec.builder(propertyName, kotlinType)
                    .mutable(true)
                    .initializer(propertyName)
                    .build()

                classBuilder.addProperty(propertySpec)
                constructorBuilder.addParameter(
                    ParameterSpec.builder(propertyName, kotlinType)
                        .defaultValue("null")
                        .build()
                )

                if (kotlinType == colorClassName || kotlinType == colorClassName.copy(nullable = true)) {
                    classBuilder.addFunction(FunSpec.builder(propertyName)
                        .addAnnotation(ClassName(rootPackage, "UiDsl"))
                        .addParameter("value", STRING)
                        .addStatement("this.$propertyName = %T(value)", colorClassName)
                        .build())
                }
            }

            classBuilder.primaryConstructor(constructorBuilder.build())

            val builderClassName = name.replaceFirstChar { it.lowercase() }
            val builderFun = FunSpec.builder(builderClassName)
                .addAnnotation(ClassName(rootPackage, "UiDsl"))
                .addParameter(
                    ParameterSpec.builder(
                        "init",
                        LambdaTypeName.get(ClassName(typePackage, className), emptyList(), UNIT)
                    ).build()
                )
                .returns(ClassName(typePackage, className))
                .addCode(
                    """
                    |val result = $className()
                    |result.init()
                    |return result
                    """.trimMargin()
                )
                .build()

            FileSpec.builder(typePackage, className)
                .addType(classBuilder.build())
                .addFunction(builderFun)
                .build()
                .writeTo(outputDir)
        }
    }

    private fun generateNodes(nodes: JsonObject) {
        val uiNodeInterface = ClassName(rootPackage, "UiNode")
        val uiNodeWithChildrenInterface = ClassName(rootPackage, "UiNodeWithChildren")
        val childNodeBuilderInterface = ClassName(rootPackage, "ChildNodeBuilder")
        val baseUiNode = ClassName(rootPackage, "BaseUiNode")
        val rebindableFunc = MemberName("$rootPackage.property", "rebindable")
        val excludePropertyAnnotation = ClassName(rootPackage, "ExcludeProperty")

        nodes.entrySet().forEach { (name, data) ->
            val nodeData = data.asJsonObject
            val className = "Ui$name"
            val classBuilder = TypeSpec.classBuilder(className)
                .addModifiers(KModifier.OPEN)
                .superclass(baseUiNode)

            val constructorBuilder = FunSpec.constructorBuilder()

            // Add id property
            val idProperty = PropertySpec.builder("id", String::class.asTypeName().copy(nullable = true))
                .addAnnotation(excludePropertyAnnotation)
                .addModifiers(KModifier.OVERRIDE)
                .mutable(true)
                .initializer("id")
                .build()
            classBuilder.addProperty(idProperty)

            constructorBuilder.addParameter(
                ParameterSpec.builder("id", String::class.asTypeName().copy(nullable = true))
                    .defaultValue("null")
                    .build()
            )

            // Add omitName property
            val omitNameProperty = PropertySpec.builder("omitName", BOOLEAN)
                .addAnnotation(excludePropertyAnnotation)
                .addModifiers(KModifier.OVERRIDE)
                .mutable(true)
                .delegate("%M(omitName)", rebindableFunc)
                .build()
            classBuilder.addProperty(omitNameProperty)

            constructorBuilder.addParameter(
                ParameterSpec.builder("omitName", BOOLEAN)
                    .defaultValue("false")
                    .build()
            )

            // Add templates property (already in BaseUiNode)

            val overrides = nodeOverrides[name] ?: emptyMap()

            val properties = nodeData.getAsJsonObject("properties")
            val allProperties = properties?.entrySet()?.associate { it.key to it.value.asJsonArray }?.toMutableMap() ?: mutableMapOf()

            overrides.forEach { (propName, propType) ->
                val normalizedPropName = propName.replaceFirstChar { it.lowercase() }
                if (allProperties.keys.none { it.replaceFirstChar { it.lowercase() } == normalizedPropName }) {
                    val jsonArray = com.google.gson.JsonArray()
                    jsonArray.add(propType)
                    allProperties[propName] = jsonArray
                }
            }

            // Filter out universal properties - they are inherited from BaseUiNode
            val nodeSpecificProperties = allProperties.filter { it.key !in universalProperties }

            nodeSpecificProperties.entries.sortedBy { it.key }.forEach { (propName, propTypes) ->
                val normalizedPropName = propName.replaceFirstChar { it.lowercase() }
                val override = overrides.entries.find { it.key.replaceFirstChar { it.lowercase() } == normalizedPropName }?.value
                val kotlinType = if (override != null) {
                    mapToKotlinType(listOf(override))
                } else {
                    val propTypeNames = propTypes.asJsonArray.map { it.asString }
                    mapToKotlinType(propTypeNames)
                }
                val propertyName = propName.replaceFirstChar { it.lowercase() }

                val propertySpec = PropertySpec.builder(propertyName, kotlinType)
                    .mutable(true)
                    .delegate("%M($propertyName)", rebindableFunc)
                    .build()

                classBuilder.addProperty(propertySpec)
                constructorBuilder.addParameter(
                    ParameterSpec.builder(propertyName, kotlinType)
                        .defaultValue("null")
                        .build()
                )
            }

            val hasChildren = nodeData.getAsJsonPrimitive("hasChildren")?.asBoolean ?: false

            if (hasChildren) {
                classBuilder.addSuperinterface(uiNodeWithChildrenInterface)
                classBuilder.addSuperinterface(childNodeBuilderInterface)
                val childrenType = MUTABLE_LIST.parameterizedBy(uiNodeInterface).copy(nullable = false)
                val propertySpec = PropertySpec.builder("children", childrenType)
                    .addAnnotation(excludePropertyAnnotation)
                    .addModifiers(KModifier.OVERRIDE)
                    .initializer("mutableListOf()")
                    .build()
                classBuilder.addProperty(propertySpec)

                classBuilder.addFunction(FunSpec.builder("addNode")
                    .addModifiers(KModifier.OVERRIDE)
                    .addParameter("node", uiNodeInterface)
                    .addStatement("addNodeToChildren(node)")
                    .build())
            }

            classBuilder.addProperty(
                PropertySpec.builder("isDirty", BOOLEAN)
                    .addAnnotation(excludePropertyAnnotation)
                    .addModifiers(KModifier.OVERRIDE)
                    .getter(
                        FunSpec.getterBuilder()
                            .addStatement("var dirty = super.isDirty")
                            .apply {
                                if (hasChildren) {
                                    addStatement("if (!dirty) dirty = children.any { it.isDirty }")
                                }
                            }
                            .addStatement("return dirty")
                            .build()
                    )
                    .build()
            )

            classBuilder.addFunction(
                FunSpec.builder("resetDirty")
                    .addModifiers(KModifier.OVERRIDE)
                    .addStatement("super.resetDirty()")
                    .apply {
                        if (hasChildren) {
                            addStatement("children.forEach { it.resetDirty() }")
                        }
                    }
                    .build()
            )

            // Clone method
            val cloneBuilder = FunSpec.builder("clone")
                .addModifiers(KModifier.OVERRIDE)
                .returns(uiNodeInterface)
                .addStatement("val clone = %L()", className)
                .addStatement("cloneBaseProperties(clone)")

            nodeSpecificProperties.entries.sortedBy { it.key }.forEach { (propName, _) ->
                val propertyName = propName.replaceFirstChar { it.lowercase() }
                cloneBuilder.addStatement("clone.%L = this.%L", propertyName, propertyName)
            }

            if (hasChildren) {
                cloneBuilder.beginControlFlow("this.children.forEach { child ->")
                    .addStatement("clone.children.add(child.clone())")
                    .endControlFlow()
            }

            cloneBuilder.addStatement("return clone")
            classBuilder.addFunction(cloneBuilder.build())

            classBuilder.primaryConstructor(constructorBuilder.build())

            classBuilder.addType(
                TypeSpec.companionObjectBuilder()
                    .addProperty(
                        PropertySpec.builder("NODE_NAME", String::class)
                            .addModifiers(KModifier.CONST)
                            .initializer("%S", name)
                            .build()
                    )
                    .build()
            )

            // Generate event extensions
            val eventPackage = "$nodePackage.event"
            val events = nodeData.getAsJsonArray("events")
            if (events != null && events.size() > 0) {
                val eventFileBuilder = FileSpec.builder(eventPackage, "${className}Events")
                val nodeType = ClassName(nodePackage, className)
                val eventContextClass = ClassName("$rootPackage.event", "EventContext")
                val customUIEventBindingType = ClassName("com.hypixel.hytale.protocol.packets.interface_", "CustomUIEventBindingType")
                val kProperty0 = ClassName("kotlin.reflect", "KProperty0")
                val uiFormContext = ClassName("$rootPackage.special", "UiFormContext")

                val valueNodeNames = listOf(
                    "CheckBox", "CodeEditor", "CompactTextField", "DropdownBox",
                    "FloatSlider", "FloatSliderNumberField", "LabeledCheckbox", "MultilineTextField",
                    "NumberField", "Slider", "SliderNumberField", "TextField"
                )

                val valueType = if (name in valueNodeNames) {
                    val valueProp = allProperties["Value"]
                    if (valueProp != null) {
                        mapToKotlinType(valueProp.map { it.asString })
                    } else {
                        null
                    }
                } else {
                    null
                }

                events.forEach { eventElement ->
                    val eventName = eventElement.asString
                    val unifiedName = unifyEventName(eventName)
                    val functionName = "on$unifiedName"
                    val submitFunctionName = "submitOn$unifiedName"

                    // onEvent function
                    val onEventFun = FunSpec.builder(functionName)
                        .receiver(nodeType)
                        .addParameter(
                            ParameterSpec.builder("boundProperties", kProperty0.parameterizedBy(ANY.copy(nullable = true)))
                                .addModifiers(KModifier.VARARG)
                                .build()
                        )
                        .addParameter(
                            "action",
                            LambdaTypeName.get(
                                parameters = listOf(ParameterSpec.builder("context", eventContextClass).build()),
                                returnType = UNIT
                            )
                        )
                        .addStatement(
                            "addEventBinding(%T.%L, requireNotNull(this.id), *boundProperties, action = action)",
                            customUIEventBindingType,
                            eventName
                        )
                        .build()

                    eventFileBuilder.addFunction(onEventFun)

                    // Simplified onEvent function with value
                    if (valueType != null) {
                        val simplifiedOnEventFun = FunSpec.builder(functionName)
                            .receiver(nodeType)
                            .addParameter(
                                "action",
                                LambdaTypeName.get(
                                    parameters = listOf(ParameterSpec.builder("value", valueType).build()),
                                    returnType = UNIT
                                )
                            )
                            .beginControlFlow(
                                "addEventBinding(%T.%L, requireNotNull(this.id), this::value)",
                                customUIEventBindingType,
                                eventName
                            )
                            .addStatement("ctx -> action(ctx.getData(this::value))")
                            .endControlFlow()
                            .build()
                        eventFileBuilder.addFunction(simplifiedOnEventFun)
                    }

                    // submitOnEvent function
                    val submitOnEventFun = FunSpec.builder(submitFunctionName)
                        .receiver(nodeType)
                        .addParameter("formContext", uiFormContext.parameterizedBy(STAR))
                        .addStatement("formContext.form.bindSubmit(this, %T.%L)", customUIEventBindingType, eventName)
                        .build()

                    eventFileBuilder.addFunction(submitOnEventFun)

                    // Generic overloads
                    for (i in 1..5) {
                        val typeParams = (1..i).map { TypeVariableName("T$it") }
                        val props = (1..i).map { ParameterSpec.builder("prop$it", kProperty0.parameterizedBy(typeParams[it - 1])).build() }
                        
                        val genericOnEventFun = FunSpec.builder(functionName)
                            .receiver(nodeType)
                            .addTypeVariables(typeParams)
                            .addParameters(props)
                            .addParameter(
                                "action",
                                LambdaTypeName.get(
                                    parameters = typeParams.map { ParameterSpec.builder("p$it", it).build() },
                                    returnType = UNIT
                                )
                            )
                            .beginControlFlow(
                                "addEventBinding(%T.%L, requireNotNull(this.id), ${props.joinToString(", ") { it.name }})",
                                customUIEventBindingType,
                                eventName
                            )
                            .addCode("ctx -> action(\n")
                            .addCode(props.joinToString(",\n") { "  ctx.getData(${it.name})" })
                            .addCode("\n)\n")
                            .endControlFlow()
                            .build()
                        
                        eventFileBuilder.addFunction(genericOnEventFun)
                    }

                    // Value-plus-generic overloads
                    if (valueType != null) {
                        for (i in 1..5) {
                            val typeParams = (1..i).map { TypeVariableName("T$it") }
                            val props = (1..i).map { ParameterSpec.builder("prop$it", kProperty0.parameterizedBy(typeParams[it - 1])).build() }
                            
                            val valueGenericOnEventFun = FunSpec.builder(functionName)
                                .receiver(nodeType)
                                .addTypeVariables(typeParams)
                                .addParameters(props)
                                .addParameter(
                                    "action",
                                    LambdaTypeName.get(
                                        parameters = listOf(ParameterSpec.builder("value", valueType).build()) + 
                                                     typeParams.mapIndexed { index, type -> ParameterSpec.builder("p${index + 1}", type).build() },
                                        returnType = UNIT
                                    )
                                )
                                .beginControlFlow(
                                    "addEventBinding(%T.%L, requireNotNull(this.id), this::value, ${props.joinToString(", ") { it.name }})",
                                    customUIEventBindingType,
                                    eventName
                                )
                                .addCode("ctx -> action(\n")
                                .addCode("  ctx.getData(this::value),\n")
                                .addCode(props.joinToString(",\n") { "  ctx.getData(${it.name})" })
                                .addCode("\n)\n")
                                .endControlFlow()
                                .build()
                            
                            eventFileBuilder.addFunction(valueGenericOnEventFun)
                        }
                    }
                }

                eventFileBuilder.build().writeTo(outputDir)
            }

            FileSpec.builder(nodePackage, className)
                .addType(classBuilder.build())
                .build()
                .writeTo(outputDir)
        }
    }

    private fun unifyEventName(name: String): String {
        return when (name) {
            "Activating" -> "Activate"
            "Validating" -> "Validate"
            "MouseEntered" -> "MouseEnter"
            "MouseExited" -> "MouseExit"
            "ValueChanged" -> "ValueChange"
            "DoubleClicking" -> "DoubleClick"
            "RightClicking" -> "RightClick"
            "Closing" -> "Close"
            "Opening" -> "Open"
            "Scrolled" -> "Scroll"
            "Selected" -> "Select"
            "Unselected" -> "Unselect"
            "DragStarted" -> "DragStart"
            "DragEnded" -> "DragEnd"
            "Dropped" -> "Drop"
            "Pressed" -> "Press"
            "Released" -> "Release"
            "MouseButtonReleased" -> "MouseButtonRelease"
            "Focused" -> "Focus"
            "Blurred" -> "Blur"
            "Changed" -> "Change"
            "Added" -> "Add"
            "Removed" -> "Remove"
            else -> {
                if (name.endsWith("ing")) {
                    name.removeSuffix("ing")
                } else if (name.endsWith("ed")) {
                    val base = name.removeSuffix("ed")
                    if (base.endsWith("sh") || base.endsWith("ch") || base.endsWith("s") || base.endsWith("x") || base.endsWith("z")) {
                        base
                    } else if (name.endsWith("eed")) {
                        name.removeSuffix("ed")
                    } else if (name.equals("Released", ignoreCase = true)) {
                        "Release"
                    } else {
                        base
                    }
                } else {
                    name
                }
            }
        }
    }

    private fun generateBuilders(nodes: JsonObject) {
        val fileBuilder = FileSpec.builder(builderPackage, "NodeBuilders")

        val childNodeBuilderInterface = ClassName(rootPackage, "ChildNodeBuilder")
        val idGenerator = ClassName(rootPackage, "IdGenerator")
        val uiNodeInterface = ClassName(rootPackage, "UiNode")

        nodes.entrySet().forEach { (name, _) ->
            val className = "Ui$name"
            val nodeType = ClassName(nodePackage, className)
            val builderMethodName = name.replaceFirstChar { it.lowercase() }

            // Extension builder for any ChildNodeBuilder
            val extensionFunSpec = FunSpec.builder(builderMethodName)
                .receiver(childNodeBuilderInterface)
                .addParameter("init", LambdaTypeName.get(receiver = nodeType, returnType = UNIT))
                .returns(nodeType)
                .addStatement("val node = %T()", nodeType)
                .addStatement("val prefix = if (this is %T) (this.id ?: %S) else %S", uiNodeInterface, "Node", "Node")
                .addStatement("node.id = %T.getNext(prefix + %S)", idGenerator, name)
                .addStatement("node.init()")
                .addStatement("this.addNode(node)")
                .addStatement("return node")
                .build()

            fileBuilder.addFunction(extensionFunSpec)

            // Top-level builder
            val topLevelFunSpec = FunSpec.builder(builderMethodName)
                .addParameter("init", LambdaTypeName.get(receiver = nodeType, returnType = UNIT))
                .returns(nodeType)
                .addStatement("val node = %T()", nodeType)
                .addStatement("node.id = %T.getNext(%S)", idGenerator, name)
                .addStatement("node.init()")
                .addStatement("return node")
                .build()

            fileBuilder.addFunction(topLevelFunSpec)
        }

        val file = fileBuilder.build()
        file.writeTo(outputDir)
    }

    private fun mapToKotlinType(types: List<String>): TypeName {
        if (types.size > 1) {
            return ANY.copy(nullable = true)
        }

        val type = types.first()
        return mapToKotlinType(type)
    }

    private fun mapToKotlinType(type: String): TypeName {
        if (type.startsWith("List<") && type.endsWith(">")) {
            val innerTypeStr = type.substring(5, type.length - 1)
            val innerType = mapToKotlinType(innerTypeStr)
            return LIST.parameterizedBy(innerType).copy(nullable = true)
        }

        return when {
            type.equals("integer", true) -> INT.copy(nullable = true)
            type.equals("long", true) -> LONG.copy(nullable = true)
            type.equals("float", true)
                    || type.equals("double", true)
                    || type.equals("decimal", true) -> DOUBLE.copy(nullable = true)
            type.equals("char", true) -> CHAR.copy(nullable = true)
            type.equals("number", true) -> DOUBLE.copy(nullable = true)
            type.equals("boolean", true) -> BOOLEAN.copy(nullable = true)
            type.equals("color", true) -> ClassName(rootPackage, "Color").copy(nullable = true)
            type.equals("string", true)
                    || type.equals("reference", true)
                    || type.equals("value", true)
                    || type.equals("localized_string", true) -> STRING.copy(nullable = true)
            type.startsWith("enum:") -> {
                ClassName(enumPackage, type.substringAfter("enum:"))
                    .copy(nullable = true)
            }
            enumsJson?.has(type) == true -> {
                ClassName(enumPackage, type).copy(nullable = true)
            }
            typesJson?.has(type) == true -> {
                ClassName(typePackage, "Ui$type").copy(nullable = true)
            }
            type.contains('.') -> {
                ClassName.bestGuess(type).copy(nullable = true)
            }
            else -> {
                // Check if it's an object type defined in the json
                ClassName(typePackage, "Ui$type")
                    .copy(nullable = true)
            }
        }
    }
}

fun main(args: Array<String>) {
    val reportFilePath = args.getOrNull(0) ?: "ui_structure_report.json"
    val outputDirPath = args.getOrNull(1) ?: "src/main/kotlin"
    val reportFile = File(reportFilePath)
    val outputDir = File(outputDirPath)

    if (!reportFile.exists()) {
        println("Report file not found: ${reportFile.absolutePath}")
        return
    }

    if (!outputDir.exists()) {
        println("Output directory not found: ${outputDir.absolutePath}")
        return
    }

    println("Generating UI classes from ${reportFile.name} to ${outputDir.path}...")
    try {
        UiGenerator(reportFile, outputDir).generate()
        println("Generation completed successfully.")
    } catch (e: Exception) {
        println("Generation failed: ${e.message}")
        e.printStackTrace()
    }
}
