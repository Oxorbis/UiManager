package cz.creeperface.hytale.uimanager.serializer

import com.google.gson.Gson
import com.hypixel.hytale.common.util.ArrayUtil
import com.hypixel.hytale.logger.HytaleLogger
import com.hypixel.hytale.protocol.ToClientPacket
import com.hypixel.hytale.protocol.packets.interface_.CustomUICommand
import com.hypixel.hytale.protocol.packets.interface_.CustomUICommandType
import com.hypixel.hytale.protocol.packets.setup.AssetFinalize
import com.hypixel.hytale.protocol.packets.setup.AssetInitialize
import com.hypixel.hytale.protocol.packets.setup.AssetPart
import com.hypixel.hytale.server.core.Message
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder
import com.hypixel.hytale.server.core.universe.Universe
import cz.creeperface.hytale.uimanager.Color
import cz.creeperface.hytale.uimanager.UiManager.ASSET_PATH
import cz.creeperface.hytale.uimanager.UiManager.PAGE_PATH
import cz.creeperface.hytale.uimanager.UiPage
import cz.creeperface.hytale.uimanager.asset.DynamicAsset
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

object UiDiffProcessor {

    private val gson = Gson()

    /**
     * Node types with editable values that must always be sent to the client,
     * because the client may have modified them without the server knowing.
     * Maps node type name to the PascalCase property name holding the editable value.
     */
    private val EDITABLE_VALUE_PROPERTIES = mapOf(
        "TextField" to "Value",
        "CompactTextField" to "Value",
        "MultilineTextField" to "Value",
        "NumberField" to "Value",
        "CheckBox" to "Value",
        "LabeledCheckBox" to "Value",
        "ToggleButton" to "IsChecked",
        "ColorPicker" to "Value",
        "ColorPickerDropdownBox" to "Color",
        "DropdownBox" to "Value",
        "Slider" to "Value",
        "FloatSlider" to "Value",
        "SliderNumberField" to "Value",
        "FloatSliderNumberField" to "Value",
    )

    private var dynamicPageAssetCounter = 0
    private val dynamicPageAssets = mutableMapOf<String, String>()

    interface CommandBuilder {
        fun set(path: String, value: Boolean)
        fun set(path: String, value: Int)
        fun set(path: String, value: Float)
        fun set(path: String, value: Double)
        fun set(path: String, value: String)
        fun set(path: String, value: Message)
        fun set(path: String, value: List<*>)
        fun setNull(path: String)
        fun setRaw(path: String, value: Any) {
            set(path, value.toString())
        }
        fun appendInline(path: String, serializedNode: String)
        fun append(path: String, documentPath: String)
        fun insertBeforeInline(selector: String, serializedNode: String)
        fun insertBefore(selector: String, documentPath: String)
        fun remove(selector: String)
        fun clear(selector: String)

        fun createNodeAsset(node: GenericNode, listParent: Boolean): String {
            return getDynamicAssetForNode(node, listParent)
        }
    }

    fun generateUpdateCommands(initial: UiPage, current: UiPage, commandBuilder: UICommandBuilder): List<GenericNode> {
        return generateUpdateCommands(initial, current, object : CommandBuilder {
            override fun set(path: String, value: Boolean) { commandBuilder.set(path, value) }
            override fun set(path: String, value: Int) { commandBuilder.set(path, value) }
            override fun set(path: String, value: Float) { commandBuilder.set(path, value) }
            override fun set(path: String, value: Double) { commandBuilder.set(path, value) }
            override fun set(path: String, value: String) { commandBuilder.set(path, value) }
            override fun set(path: String, value: Message) {
                commandBuilder.set(path, value)
            }
            override fun set(path: String, value: List<*>) {
                commandBuilder.set(path, value)
            }
            override fun setNull(path: String) {
                commandBuilder.setNull(path)
            }
            override fun appendInline(path: String, serializedNode: String) {
                commandBuilder.appendInline(path, serializedNode)
            }

            override fun append(path: String, documentPath: String) {
                commandBuilder.append(path, documentPath)
            }
            override fun setRaw(path: String, value: Any) {
                val commandsProperty = UICommandBuilder::class.declaredMemberProperties.first {
                    it.name == "commands"
                }
                commandsProperty.isAccessible = true
                val commandsMap = commandsProperty.get(commandBuilder) as MutableList<CustomUICommand>

                val jsonWrapper = mapOf("0" to value)

                val jsonValue = gson.toJson(jsonWrapper)
//                HytaleLogger.getLogger().atInfo().log(value::class.simpleName)
//                HytaleLogger.getLogger().atInfo().log("Raw set - $path - $jsonValue")
                commandsMap.add(CustomUICommand(CustomUICommandType.Set, path, jsonValue, null))
            }
            override fun insertBeforeInline(selector: String, serializedNode: String) { commandBuilder.insertBeforeInline(selector, serializedNode) }
            override fun insertBefore(selector: String, documentPath: String) {
                commandBuilder.insertBefore(selector, documentPath)
            }
            override fun remove(selector: String) { commandBuilder.remove(selector) }
            override fun clear(selector: String) { commandBuilder.clear(selector) }
        })
    }

    fun generateUpdateCommands(initial: UiPage, current: UiPage, commandBuilder: CommandBuilder): List<GenericNode> {
        val initialNodes = initial.nodes.map { UiSerializer.toGenericNode(it, flattenPatchStyle = false) }
        val currentNodes = current.nodes.map { UiSerializer.toGenericNode(it, flattenPatchStyle = false) }

        // Root nodes comparison
        // Assuming the order of root nodes might change or they might be added/removed, 
        // but for now let's try to match them by index if they are at the root level and don't have IDs,
        // or match them by ID.
        
        val addedNodes = mutableListOf<GenericNode>()
        val matchedCurrentIndices = mutableSetOf<Int>()
        
        initialNodes.forEachIndexed { index, initialNode ->
            val currentNode = findMatchingNode(initialNode, index, currentNodes, matchedCurrentIndices)
            if (currentNode != null) {
                val selector = getNodeSelector(currentNode, index, null)
                addedNodes.addAll(compareNodes(initialNode, currentNode, selector, commandBuilder))
            }
        }

        return addedNodes
    }

    private fun findMatchingNode(initial: GenericNode, index: Int, currentNodes: List<GenericNode>, matchedIndices: MutableSet<Int>): GenericNode? {
        if (initial.id != null) {
            val found = currentNodes.find { it.id == initial.id }
            if (found != null) {
                matchedIndices.add(currentNodes.indexOf(found))
                return found
            }
        } else if (index < currentNodes.size && !matchedIndices.contains(index)) {
            val candidate = currentNodes[index]
            if (candidate.id == null && candidate.name == initial.name) {
                matchedIndices.add(index)
                return candidate
            }
        }
        return null
    }

    private fun getNodeSelector(node: GenericNode, index: Int, parentSelector: String?, parentIsList: Boolean = false): String {
        if (!parentIsList && node.id != null) return "#${node.id}"
        
        return if (parentSelector == null) {
            "[$index]"
        } else {
            "$parentSelector[$index]"
        }
    }

    private fun compareNodes(initial: GenericNode, current: GenericNode, selector: String, commandBuilder: CommandBuilder): List<GenericNode> {
        // Compare properties
        current.properties.forEach { (name, value) ->
            val initialValue = initial.properties[name]
            if (value != initialValue) {
//                HytaleLogger.getLogger().atInfo().log("Comparing property $name for node $selector, value = $value, initialValue = $initialValue")
                if (value is List<*> || initialValue is List<*>) {
                    // List properties (e.g. DropdownBox Entries) need original values from source
                    val sourceValue = getSourcePropertyValue(current.source, name)
                    if (sourceValue is List<*>) {
                        commandBuilder.set("$selector.$name", sourceValue)
                    } else {
                        commandBuilder.setNull("$selector.$name")
                    }
                } else if (value is Map<*, *> && initialValue is Map<*, *>) {
                    @Suppress("UNCHECKED_CAST")
                    if (name == "Anchor") {
                        commandBuilder.setRaw("$selector.$name", convertColorsInMap(value as Map<String, Any?>))
                    } else {
                        compareObjects(
                            initialValue as Map<String, Any?>,
                            value as Map<String, Any?>,
                            "$selector.$name",
                            commandBuilder
                        )
                    }
                } else if (value is Map<*, *> || initialValue is Map<*, *>) {
                    // One is object, another is not (e.g. PatchStyle flattened to color)
                    // Just set the whole property
                    setCommand(commandBuilder, "$selector.$name", name, value)
                } else {
                    setCommand(commandBuilder, "$selector.$name", name, value)
                }
            }
        }

        // Detect properties removed (present in initial, absent in current) -> set to null
        initial.properties.forEach { (name, _) ->
            if (name !in current.properties) {
                setCommand(commandBuilder, "$selector.$name", name, null)
            }
        }

        // Always send editable input values — the client may have modified them
        val editableProperty = EDITABLE_VALUE_PROPERTIES[current.name]
        if (editableProperty != null) {
            val value = current.properties[editableProperty]
            val initialValue = initial.properties[editableProperty]
            if (value != null && value == initialValue) {
                setCommand(commandBuilder, "$selector.$editableProperty", editableProperty, value)
            }
        }

        return compareChildren(initial, current, selector, commandBuilder)
    }

    private fun compareChildren(initial: GenericNode, current: GenericNode, selector: String, commandBuilder: CommandBuilder): List<GenericNode> {
        val addedNodes = mutableListOf<GenericNode>()
        if (current.listNode) {
            val clientChildren = initial.children.toMutableList()
            val currentChildren = current.children

            if (currentChildren.isEmpty() && clientChildren.isNotEmpty()) {
                commandBuilder.clear(selector)
                return emptyList()
            }
            
            var i = 0
            while (i < currentChildren.size || i < clientChildren.size) {
                val currentChild = currentChildren.getOrNull(i)
                val clientChild = clientChildren.getOrNull(i)
                
                if (currentChild == null) {
                    // Extra nodes in client, remove them
                    commandBuilder.remove("$selector[$i]")
                    clientChildren.removeAt(i)
                    continue
                }
                
            if (clientChild == null) {
                    // Extra nodes in current, append them
                    appendNodeRecursively(commandBuilder, selector, currentChild, listParent = true)

                    addedNodes.add(currentChild)
                    clientChildren.add(currentChild)
                    i++
                    continue
                }
                
                if (nodesMatchIdentity(currentChild, clientChild)) {
                    // Perfect match! Compare properties (and potentially nested children)
                    addedNodes.addAll(compareNodes(clientChild, currentChild, "$selector[$i]", commandBuilder))
                    i++
                } else {
                    // Not a perfect match. 
                    // Let's see if currentChild exists later in clientChildren
                    val nextMatchInClient = clientChildren.drop(i + 1).indexOfFirst { nodesMatchIdentity(currentChild, it) }
                    
                    if (nextMatchInClient != -1) {
                        // Some nodes were removed before this match
                        repeat(nextMatchInClient + 1) {
                            commandBuilder.remove("$selector[$i]")
                            clientChildren.removeAt(i)
                        }
                    } else {
                        // Current child NOT found later.
                        // Is clientChild found later in currentChildren?
                        val nextMatchInCurrent = currentChildren.drop(i + 1).indexOfFirst { nodesMatchIdentity(clientChild, it) }
                        
                        if (nextMatchInCurrent != -1) {
                            // clientChild found later, so currentChild was inserted
                            appendNodeRecursively(commandBuilder, "$selector[$i]", currentChild, insertBefore = "$selector[$i]", listParent = true)

                            addedNodes.add(currentChild)
                            clientChildren.add(i, currentChild)
                            i++
                        } else {
                            // Neither is found later. Just a property change of the node at this position if names match.
                            if (currentChild.name == clientChild.name) {
                                addedNodes.addAll(compareNodes(clientChild, currentChild, "$selector[$i]", commandBuilder))
                                i++
                            } else {
                                // Replacement: remove and insert
                                commandBuilder.remove("$selector[$i]")
                                clientChildren.removeAt(i)
                                // Next iteration will handle currentChild as insertion
                            }
                        }
                    }
                }
            }
        } else {
            // Compare children
            val initialChildren = initial.children.toMutableList()
            val currentChildren = current.children

            // Simple heuristic for "too many changes"
            val totalChanges = (initialChildren.size + currentChildren.size).coerceAtLeast(1)
            var unchangedNodes = 0
            
            val matchedCurrentIndices = mutableSetOf<Int>()
            initialChildren.forEachIndexed { index, initialChild ->
                val currentChild = findMatchingNode(initialChild, index, currentChildren, matchedCurrentIndices)
                if (currentChild != null) {
                    unchangedNodes++
                }
            }

            // If more than 50% of nodes changed, just clear and re-append everything
            // This is a rough heuristic, could be tuned.
            if (initialChildren.isNotEmpty() && unchangedNodes.toDouble() / initialChildren.size < 0.3 && currentChildren.size > 5) {
                commandBuilder.clear(selector)
                currentChildren.forEach { child ->
                    appendNodeRecursively(commandBuilder, selector, child)

                    addedNodes.add(child)
                }
                return addedNodes
            }

            val matchedInInitial = mutableSetOf<Int>()
            matchedCurrentIndices.clear()

            // First, remove nodes that are no longer present
            var removedCount = 0
            initialChildren.forEachIndexed { index, initialChild ->
                val currentChild = findMatchingNode(initialChild, index, currentChildren, matchedCurrentIndices)
                if (currentChild == null) {
                    val childSelector = getNodeSelector(initialChild, index - removedCount, selector, false)
                    commandBuilder.remove(childSelector)
                    removedCount++
                } else {
                    matchedInInitial.add(index)
                    // findMatchingNode already added to matchedCurrentIndices
                }
            }

            // Update remaining initialChildren to reflect removals
            val remainingInitial = initialChildren.filterIndexed { index, _ -> matchedInInitial.contains(index) }

            // Now iterate through currentChildren to find additions and updates
            var initialIdx = 0
            currentChildren.forEachIndexed { currentIdx, currentChild ->
                val matchingInitial = remainingInitial.getOrNull(initialIdx)
                
                if (matchingInitial != null && nodesMatchIdentity(currentChild, matchingInitial)) {
                    // Match found, update it
                    val childSelector = getNodeSelector(currentChild, currentIdx, selector, false)
                    addedNodes.addAll(compareNodes(matchingInitial, currentChild, childSelector, commandBuilder))
                    initialIdx++
                } else {
                    // Check if this currentChild is actually later in remainingInitial
                    val nextMatchInInitial = remainingInitial.drop(initialIdx).indexOfFirst { nodesMatchIdentity(currentChild, it) }
                    
                    if (nextMatchInInitial != -1) {
                        // The nodes in between were removed (shouldn't happen here as we handled removals)
                        // Or they were reordered. For simplicity, if they were reordered, let's just treat as insertion/removal for now
                        // or just skip to the match.
                        // Actually, if we are here, it means matchingInitial doesn't match currentChild.
                        // If currentChild matches something later in remainingInitial, it means some nodes from remainingInitial are missing in currentChildren at this position.
                        
                        // Let's just treat it as an insertion if not matched.
                        val parentSelector = if (selector.startsWith("#")) selector else selector

                        appendNodeRecursively(commandBuilder, parentSelector, currentChild)

                        addedNodes.add(currentChild)
                    } else {
                        // Insertion
                        if (currentIdx < currentChildren.size - 1) {
                            // If there's a next node, try to insert before it
                            val nextCurrentChild = currentChildren[currentIdx + 1]
                            val nextMatchingInitial = remainingInitial.drop(initialIdx).find { nodesMatchIdentity(nextCurrentChild, it) }
                            
                            if (nextMatchingInitial != null) {
                                val nextSelector = getNodeSelector(nextMatchingInitial, initialIdx, selector, false)

                                appendNodeRecursively(commandBuilder, selector, currentChild, insertBefore = nextSelector)

                                addedNodes.add(currentChild)
                            } else {
                                appendNodeRecursively(commandBuilder, selector, currentChild)

                                addedNodes.add(currentChild)
                            }
                        } else {
                            appendNodeRecursively(commandBuilder, selector, currentChild)

                            addedNodes.add(currentChild)
                        }
                    }
                }
            }
        }
        return addedNodes
    }

    private fun stripSimpleProperties(node: GenericNode, simpleProperties: MutableMap<String, Any>) {
        val nodeSelector = if (node.id != null) "#${node.id}" else null

        val iterator = node.properties.entries.iterator()
        while (iterator.hasNext()) {
            val (name, value) = iterator.next()
            if (value !is Map<*, *>) {
                if (nodeSelector != null) {
                    simpleProperties["$nodeSelector.$name"] = value
                }
                iterator.remove()
            }
        }

        node.children.forEach { stripSimpleProperties(it, simpleProperties) }
    }

    private fun appendNodeRecursively(commandBuilder: CommandBuilder, selector: String, node: GenericNode, insertBefore: String? = null, listParent: Boolean = false) {
        val simpleProperties = mutableMapOf<String, Any>()
        val strippedNode = node.copyNodeRecursive()
        stripSimpleProperties(strippedNode, simpleProperties)

        val partialNodeAsset = commandBuilder.createNodeAsset(strippedNode, listParent)

        if (insertBefore != null) {
            commandBuilder.insertBefore(insertBefore, partialNodeAsset)
        } else {
            commandBuilder.append(selector, partialNodeAsset)
        }

        simpleProperties.forEach { (path, value) ->
            setCommand(commandBuilder, path, path.substringAfterLast('.'), value)
        }
    }

    private fun getSourcePropertyValue(source: Any?, pascalName: String): Any? {
        if (source == null) return null
        val camelName = pascalName.replaceFirstChar { it.lowercase() }
        val prop = source::class.memberProperties.find { it.name == camelName }
        prop?.isAccessible = true
        return prop?.call(source)
    }

    private fun nodesMatchIdentity(a: GenericNode, b: GenericNode): Boolean {
        if (a.name != b.name) return false
        if (a.id != b.id) return false
        
        return true
    }


    private fun setCommand(commandBuilder: CommandBuilder, path: String, name: String, value: Any?) {
//        HytaleLogger.getLogger().atInfo().log("Command $path - $value")
        when (value) {
            null -> commandBuilder.setNull(path)
            is Boolean -> commandBuilder.set(path, value)
            is Int -> commandBuilder.set(path, value)
            is Float -> commandBuilder.set(path, value)
            is Double -> commandBuilder.set(path, value)
            is GenericNode.MessageValue -> {
                val rawText = value.message.rawText
                if (rawText != null) {
                    commandBuilder.set(path, rawText)
                } else {
                    commandBuilder.set(path, value.message)
                }
            }
            is GenericNode.Identifier -> {
                val color = parseColor(value.value)
                if (color != null) {
                    commandBuilder.set(path, color.toRgbaHex())
                } else {
                    commandBuilder.set(path, value.value)
                }
            }
            else -> {
                if (value is Map<*, *>) {
                    @Suppress("UNCHECKED_CAST")
                    commandBuilder.setRaw(path, convertColorsInMap(value as Map<String, Any?>))
                } else {
                    commandBuilder.set(path, UiSerializer.formatValue(name, value, quoteStrings = false))
                }
            }
        }
    }

    private fun parseColor(value: String): Color? {
        if (!value.startsWith("#")) return null
        val hex = value.substringBefore('(')
        val cleanHex = hex.removePrefix("#")
        if (cleanHex.length != 6 || !cleanHex.all { it.isLetterOrDigit() }) return null
        return Color.invoke(value)
    }

    private fun convertColorsInMap(map: Map<String, Any?>): Map<String, Any?> {
        return map.mapValues { (_, value) ->
            when (value) {
                is GenericNode.Identifier -> {
                    val color = parseColor(value.value)
                    if (color != null) color.toRgbaHex() else value.value
                }

                is Map<*, *> -> {
                    @Suppress("UNCHECKED_CAST")
                    convertColorsInMap(value as Map<String, Any?>)
                }

                else -> value
            }
        }
    }

    private fun compareObjects(initial: Map<String, Any?>, current: Map<String, Any?>, path: String, commandBuilder: CommandBuilder) {
        // TexturePath cannot be updated at runtime — skip it and warn
        if (current["TexturePath"] != initial["TexturePath"]) {
            HytaleLogger.getLogger().atWarning().log(
                "TexturePath change detected at '$path' but cannot be updated at runtime. " +
                        "Old: ${initial["TexturePath"]}, New: ${current["TexturePath"]}"
            )
        }

        current.forEach { (name, value) ->
            if (name == "TexturePath") return@forEach
            val initialValue = initial[name]
            if (value != initialValue) {
                if (value is Map<*, *> && initialValue is Map<*, *>) {
                    @Suppress("UNCHECKED_CAST")
                    if (name == "Anchor") {
                        commandBuilder.setRaw("$path.$name", convertColorsInMap(value as Map<String, Any?>))
                    } else {
                        compareObjects(
                            initialValue as Map<String, Any?>,
                            value as Map<String, Any?>,
                            "$path.$name",
                            commandBuilder
                        )
                    }
                } else {
                    setCommand(commandBuilder, "$path.$name", name, value)
                }
            }
        }

        // Detect removed keys
        initial.forEach { (name, _) ->
            if (name == "TexturePath") return@forEach
            if (name !in current) {
                setCommand(commandBuilder, "$path.$name", name, null)
            }
        }
    }

    private fun getDynamicAssetForNode(node: GenericNode, listParent: Boolean): String {
        val serialized = UiSerializer.serialize(node, listParent = listParent).trim()

        dynamicPageAssets[serialized]?.let { return it }

        val fileName = "CustomPartialNode" + dynamicPageAssetCounter++ + ".ui"

        val assetName = ASSET_PATH + fileName
        HytaleLogger.getLogger().atInfo().log("Adding partial node $assetName - file name: $fileName")
        HytaleLogger.getLogger().atInfo().log("Content: $serialized")
        val asset = DynamicAsset(assetName, serialized.toByteArray(Charsets.UTF_8))
//        CommonAssetModule.get().addCommonAsset("UiManager", asset)

        val allBytes = asset.blob.get()

        val parts = ArrayUtil.split(allBytes, 2621440)
        val packets = arrayOfNulls<ToClientPacket>(2 + parts.size)

        packets[0] = AssetInitialize(asset.toPacket(), allBytes.size)

        for (i in parts.indices) {
            packets[1 + i] = AssetPart(parts[i])
        }

        packets[1 + parts.size] = AssetFinalize()

        packets.forEach { packet ->
            packet?.let {
                Universe.get().broadcastPacketNoCache(packet)
            }
        }

        dynamicPageAssets[serialized] = assetName
        return PAGE_PATH + fileName
    }

}
