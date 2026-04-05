package cz.creeperface.hytale.uimanager.serializer

import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType
import com.hypixel.hytale.server.core.Message
import cz.creeperface.hytale.uimanager.Color
import cz.creeperface.hytale.uimanager.UiNode
import cz.creeperface.hytale.uimanager.builder.customUi
import cz.creeperface.hytale.uimanager.builder.group
import cz.creeperface.hytale.uimanager.builder.textButton
import cz.creeperface.hytale.uimanager.type.anchor
import cz.creeperface.hytale.uimanager.type.patchStyle
import cz.creeperface.hytale.uimanager.util.toMessage
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class UiDiffProcessorTest {

    class MockCommandBuilder : UiDiffProcessor.CommandBuilder {
        val commands = mutableMapOf<String, Any?>()
        val removals = mutableListOf<String>()
        val appends = mutableListOf<Pair<String, String>>()
        val inserts = mutableListOf<Pair<String, String>>()
        val clears = mutableListOf<String>()

        override fun set(path: String, value: Boolean) { commands[path] = value }
        override fun set(path: String, value: Int) { commands[path] = value }
        override fun set(path: String, value: Float) { commands[path] = value }
        override fun set(path: String, value: Double) { commands[path] = value }
        override fun set(path: String, value: String) { commands[path] = value }
        override fun set(path: String, value: Message) {
            commands[path] = value
        }
        override fun set(path: String, value: List<*>) {
            commands[path] = value
        }

        override fun setNull(path: String) {
            commands[path] = null
        }
        override fun appendInline(path: String, serializedNode: String) {
            appends.add(path to serializedNode)
        }
        override fun insertBeforeInline(selector: String, serializedNode: String) {
            inserts.add(selector to serializedNode)
        }
        override fun append(path: String, documentPath: String) {
            appends.add(path to documentPath)
        }
        override fun insertBefore(selector: String, documentPath: String) {
            inserts.add(selector to documentPath)
        }
        override fun setRaw(path: String, value: Any) {
            commands[path] = value
        }
        override fun remove(selector: String) {
            removals.add(selector)
        }
        override fun clear(selector: String) {
            clears.add(selector)
        }
        override fun createNodeAsset(node: GenericNode, listParent: Boolean): String {
            return UiSerializer.serialize(node, listParent = listParent).trim()
        }
    }

    @Test
    fun testNoChanges() {
        val initial = customUi {
            textButton {
                text = "Same".toMessage()
            }
        }
        
        val current = initial.clone()
        
        val builder = MockCommandBuilder()
        UiDiffProcessor.generateUpdateCommands(initial, current, builder)
        
        assertTrue(builder.commands.isEmpty(), "Should have no commands for identical UI")
    }

    @Test
    fun testPropertyChange() {
        val initial = customUi {
            textButton {
                id = "btn"
                text = "Initial".toMessage()
            }
        }

        val current = initial.clone()
        (current.nodes[0] as cz.creeperface.hytale.uimanager.node.UiTextButton).text = "Changed".toMessage()

        val builder = MockCommandBuilder()
        UiDiffProcessor.generateUpdateCommands(initial, current, builder)

        val commands = builder.commands
        assertEquals(1, commands.size)
        assertEquals("Changed", commands["#btn.Text"], "Raw message should be passed as String")
    }

    @Test
    fun testNestedPropertyChange() {
        val initial = customUi {
            textButton {
                id = "btn"
                anchor = anchor {
                    top = 10
                }
            }
        }

        val current = customUi {
            textButton {
                id = "btn"
                anchor = anchor {
                    top = 20
                }
            }
        }

        val builder = MockCommandBuilder()
        UiDiffProcessor.generateUpdateCommands(initial, current, builder)

        val commands = builder.commands
        assertEquals(1, commands.size, "Should have exactly one command. Commands: $commands")
        assertTrue(
            commands.containsKey("#btn.Anchor"),
            "Should set whole #btn.Anchor object. Commands: ${commands.keys}"
        )
        val anchorMap = commands["#btn.Anchor"] as Map<*, *>
        assertEquals(20, (anchorMap["Top"] as? Number)?.toInt())
    }

    @Test
    fun testNestedPatchStyleColorChange() {
        val initial = customUi {
            group {
                id = "node"
                background = patchStyle {
                    color = Color("#FF0000")
                }
            }
        }

        val current = customUi {
            group {
                id = "node"
                background = patchStyle {
                    color = Color("#00FF00")
                }
            }
        }

        val builder = MockCommandBuilder()
        UiDiffProcessor.generateUpdateCommands(initial, current, builder)

        val commands = builder.commands
        assertEquals(1, commands.size, "Should have exactly one command. Commands: $commands")
        assertTrue(
            commands.containsKey("#node.Background.Color"),
            "Path should be #node.Background.Color. Commands: ${commands.keys}"
        )
        assertEquals("#00FF00FF", commands["#node.Background.Color"])
    }

    @Test
    fun testNestedPatchStyleColorChangeWithAlpha() {
        val initial = customUi {
            group {
                id = "node"
                background = patchStyle {
                    color = Color("#fa1528", 0.5)
                }
            }
        }

        val current = customUi {
            group {
                id = "node"
                background = patchStyle {
                    color = Color("#fa1528", 0.7)
                }
            }
        }

        val builder = MockCommandBuilder()
        UiDiffProcessor.generateUpdateCommands(initial, current, builder)

        val commands = builder.commands
        assertEquals(1, commands.size, "Should have exactly one command. Commands: $commands")
        assertTrue(
            commands.containsKey("#node.Background.Color"),
            "Path should be #node.Background.Color. Commands: ${commands.keys}"
        )
        assertEquals("#FA1528B3", commands["#node.Background.Color"])
    }

    @Test
    fun testColorOnlyChangeWithTexturePath() {
        // When only Color changes but TexturePath stays the same,
        // should set Background.Color individually
        val initial = customUi {
            group {
                id = "node"
                background = patchStyle {
                    color = Color("#FF0000")
                    texturePath = "path/to/texture"
                }
            }
        }

        val current = customUi {
            group {
                id = "node"
                background = patchStyle {
                    color = Color("#00FF00")
                    texturePath = "path/to/texture"
                }
            }
        }

        val builder = MockCommandBuilder()
        UiDiffProcessor.generateUpdateCommands(initial, current, builder)

        val commands = builder.commands
        assertEquals(1, commands.size, "Should have exactly one command. Commands: $commands")
        assertTrue(
            commands.containsKey("#node.Background.Color"),
            "Path should be #node.Background.Color. Commands: ${commands.keys}"
        )
        assertEquals("#00FF00FF", commands["#node.Background.Color"], "Color value should be RGBA hex")
    }

    @Test
    fun testColorOnlyChangeWithAlphaAndTexturePath() {
        // When only Color (with alpha) changes but TexturePath stays the same
        val initial = customUi {
            group {
                id = "node"
                background = patchStyle {
                    color = Color("#ff0000", 0.35)
                    texturePath = "path/to/texture"
                }
            }
        }

        val current = customUi {
            group {
                id = "node"
                background = patchStyle {
                    color = Color("#ff0000", 0.7)
                    texturePath = "path/to/texture"
                }
            }
        }

        val builder = MockCommandBuilder()
        UiDiffProcessor.generateUpdateCommands(initial, current, builder)

        val commands = builder.commands
        assertEquals(1, commands.size, "Should have exactly one command. Commands: $commands")
        assertTrue(
            commands.containsKey("#node.Background.Color"),
            "Path should be #node.Background.Color. Commands: ${commands.keys}"
        )
        assertEquals("#FF0000B3", commands["#node.Background.Color"], "Color value should be RGBA hex with alpha")
    }

    @Test
    fun testTexturePathOnlyChange() {
        // TexturePath cannot be updated at runtime — should produce no commands
        val initial = customUi {
            group {
                id = "node"
                background = patchStyle {
                    texturePath = "path/to/old"
                    color = Color("#FF0000")
                }
            }
        }

        val current = customUi {
            group {
                id = "node"
                background = patchStyle {
                    texturePath = "path/to/new"
                    color = Color("#FF0000")
                }
            }
        }

        val builder = MockCommandBuilder()
        UiDiffProcessor.generateUpdateCommands(initial, current, builder)

        assertTrue(
            builder.commands.isEmpty(),
            "TexturePath change should not produce any commands. Commands: ${builder.commands}"
        )
    }

    @Test
    fun testTexturePathAndColorBothChange() {
        // When both change, only the color update should be produced (TexturePath is skipped)
        val initial = customUi {
            group {
                id = "node"
                background = patchStyle {
                    texturePath = "path/to/old"
                    color = Color("#FF0000")
                }
            }
        }

        val current = customUi {
            group {
                id = "node"
                background = patchStyle {
                    texturePath = "path/to/new"
                    color = Color("#00FF00", 0.5)
                }
            }
        }

        val builder = MockCommandBuilder()
        UiDiffProcessor.generateUpdateCommands(initial, current, builder)

        val commands = builder.commands
        assertEquals(1, commands.size, "Should have only the color command. Commands: $commands")
        assertFalse(commands.containsKey("#node.Background.TexturePath"), "TexturePath should not be updated")
        assertEquals("#00FF0080", commands["#node.Background.Color"], "Color should be RGBA hex")
    }

    @Test
    fun testNodeRemoval() {
        val initial = customUi {
            group {
                id = "parent"
                textButton { id = "btn1" }
                textButton { id = "btn2" }
                textButton { id = "btn3" }
            }
        }

        val current = customUi {
            group {
                id = "parent"
                textButton { id = "btn1" }
                // btn2 removed
                textButton { id = "btn3" }
            }
        }

        val builder = MockCommandBuilder()
        UiDiffProcessor.generateUpdateCommands(initial, current, builder)

        assertEquals(1, builder.removals.size)
        assertEquals("#btn2", builder.removals[0])
    }

    @Test
    fun testNodeAddedLast() {
        val initial = customUi {
            group {
                id = "parent"
                textButton { id = "btn1" }
            }
        }

        val current = customUi {
            group {
                id = "parent"
                textButton { id = "btn1" }
                textButton { id = "btn2" }
            }
        }

        val builder = MockCommandBuilder()
        UiDiffProcessor.generateUpdateCommands(initial, current, builder)

        assertEquals(1, builder.appends.size)
        assertEquals("#parent", builder.appends[0].first)
        assertTrue(builder.appends[0].second.contains("TextButton") && builder.appends[0].second.contains("#btn2"))
    }

    @Test
    fun testNodeAddedBetween() {
        val initial = customUi {
            group {
                id = "parent"
                textButton { id = "btn1" }
                textButton { id = "btn3" }
            }
        }

        val current = customUi {
            group {
                id = "parent"
                textButton { id = "btn1" }
                textButton {
                    id = "btn2"
                    text = "New".toMessage()
                }
                textButton { id = "btn3" }
            }
        }

        val builder = MockCommandBuilder()
        UiDiffProcessor.generateUpdateCommands(initial, current, builder)

        assertEquals(1, builder.inserts.size)
        assertEquals("#btn3", builder.inserts[0].first)
        assertTrue(builder.inserts[0].second.contains("TextButton") && builder.inserts[0].second.contains("#btn2"))
    }

    @Test
    fun testNodeWithChildrenAdded() {
        val initial = customUi {
            group { id = "parent" }
        }

        val current = customUi {
            group {
                id = "parent"
                group {
                    id = "childGroup"
                    textButton { id = "btn1" }
                }
            }
        }

        val builder = MockCommandBuilder()
        UiDiffProcessor.generateUpdateCommands(initial, current, builder)

        assertEquals(1, builder.appends.size)

        // Append to #parent — the whole subtree including children is serialized together
        val (path1, serialized1) = builder.appends[0]
        assertEquals("#parent", path1)
        assertTrue(serialized1.contains("Group") && serialized1.contains("#childGroup"))
        assertTrue(
            serialized1.contains("TextButton") && serialized1.contains("#btn1"),
            "Children should be included in the serialized subtree"
        )
    }

    @Test
    fun testEventBindingsForAddedNodes() {
        val initial = customUi {
            group { id = "parent" }
        }

        val current = customUi {
            group {
                id = "parent"
                textButton {
                    id = "newBtn"
                    addEventBinding(CustomUIEventBindingType.Activating, "newBtn") { }
                }
            }
        }

        val builder = MockCommandBuilder()
        val addedNodes = UiDiffProcessor.generateUpdateCommands(initial, current, builder)

        assertEquals(1, addedNodes.size)
        val addedNode = addedNodes[0]
        assertEquals("newBtn", addedNode.id)
        assertTrue(addedNode.source is UiNode)
        val sourceNode = addedNode.source as UiNode
        assertEquals(1, sourceNode.getEventBindings().size)
        assertEquals(CustomUIEventBindingType.Activating, sourceNode.getEventBindings()[0].type)
    }

    @Test
    fun testSubnodeAddition() {
        val initial = customUi {
            group {
                id = "root"
                group {
                    id = "parent"
                    textButton { id = "existingBtn" }
                }
            }
        }

        val current = customUi {
            group {
                id = "root"
                group {
                    id = "parent"
                    textButton { id = "existingBtn" }
                    textButton { id = "newBtn" }
                }
            }
        }

        val builder = MockCommandBuilder()
        UiDiffProcessor.generateUpdateCommands(initial, current, builder)

        // It should append to #parent, not #root
        assertEquals(1, builder.appends.size)
        assertEquals("#parent", builder.appends[0].first)
        assertTrue(builder.appends[0].second.contains("#newBtn"))
    }

    @Test
    fun testDeepSubnodeAddition() {
        val initial = customUi {
            group {
                id = "root"
                group {
                    id = "level1"
                    group {
                        id = "level2"
                        textButton { id = "btn1" }
                    }
                }
            }
        }

        val current = customUi {
            group {
                id = "root"
                group {
                    id = "level1"
                    group {
                        id = "level2"
                        textButton { id = "btn1" }
                        textButton { id = "btn2"; text = "A".toMessage() }
                    }
                }
            }
        }

        val builder = MockCommandBuilder()
        UiDiffProcessor.generateUpdateCommands(initial, current, builder)

        assertEquals(1, builder.appends.size)
        assertEquals("#level2", builder.appends[0].first)
        val serialized = builder.appends[0].second
        assertTrue(serialized.contains("#btn2"))
        assertFalse(serialized.contains("Text: \"A\""))
        assertEquals("A", builder.commands["#btn2.Text"], "Raw message should be passed as String")
    }

    @Test
    fun testComplexPropertySplittingOnAddition() {
        val initial = customUi {
            group { id = "parent" }
        }

        val current = customUi {
            group {
                id = "parent"
                textButton {
                    id = "btnWithIcon"
                    background = patchStyle {
                        texturePath = "path/to/texture"
                    }
                }
            }
        }

        val builder = MockCommandBuilder()
        UiDiffProcessor.generateUpdateCommands(initial, current, builder)

        assertEquals(1, builder.appends.size)
        val serialized = builder.appends[0].second

        assertTrue(serialized.contains("#btnWithIcon"), "Serialized node should contain its ID")

        // Background is a Map (complex property) — it stays in the serialized node, not set via command
        assertTrue(serialized.contains("Background:"), "Background (Map) should remain in serialized node")
        assertTrue(serialized.contains("TexturePath"), "Background Map should contain TexturePath in serialized node")
        assertFalse(
            builder.commands.containsKey("#btnWithIcon.Background"),
            "Should NOT have a command to set Background because it's in the serialized node"
        )
    }

    @Test
    fun testDeepComplexPropertySplittingOnAddition() {
        val initial = customUi {
            group { id = "parent" }
        }

        val current = customUi {
            group {
                id = "parent"
                group {
                    id = "childGroup"
                    textButton {
                        id = "nestedBtn"
                        background = patchStyle {
                            texturePath = "nested/path"
                        }
                    }
                }
            }
        }

        val builder = MockCommandBuilder()
        UiDiffProcessor.generateUpdateCommands(initial, current, builder)

        assertEquals(1, builder.appends.size)
        val serializedParent = builder.appends[0].second
        
        // Complex properties (Maps) should remain in the serialized node
        assertTrue(serializedParent.contains("Background:"), "Nested Background should NOT be stripped from serialized node tree because it's a Map")
        assertTrue(serializedParent.contains("nested/path"), "Nested texture path should be in serialized tree")
        
        // Background should NOT be set via a separate command
        assertFalse(builder.commands.containsKey("#nestedBtn.Background"), "Should NOT have a command to set nested Background")
    }

    @Test
    fun testChildPropertySetAfterChildAppend() {
        val initial = customUi {
            group { id = "parent" }
        }

        val current = customUi {
            group {
                id = "parent"
                group {
                    id = "child"
                    background = patchStyle {
                        texturePath = "child/texture"
                    }
                }
            }
        }

        val builder = MockCommandBuilder()
        UiDiffProcessor.generateUpdateCommands(initial, current, builder)

        // Find positions of commands in chronological order (appends and commands)
        // We'll use a wrapper to track the order of operations in MockCommandBuilder
        class OrderedMockCommandBuilder : UiDiffProcessor.CommandBuilder {
            val operations = mutableListOf<String>()

            override fun set(path: String, value: Boolean) { operations.add("set:$path") }
            override fun set(path: String, value: Int) { operations.add("set:$path") }
            override fun set(path: String, value: Float) { operations.add("set:$path") }
            override fun set(path: String, value: Double) { operations.add("set:$path") }
            override fun set(path: String, value: String) { operations.add("set:$path") }
            override fun set(path: String, value: Message) {
                operations.add("set:$path")
            }
            override fun set(path: String, value: List<*>) {
                operations.add("set:$path")
            }

            override fun setNull(path: String) {
                operations.add("setNull:$path")
            }
            override fun setRaw(path: String, value: Any) { operations.add("set:$path") }
            override fun appendInline(path: String, serializedNode: String) { operations.add("append:$path") }
            override fun insertBeforeInline(selector: String, serializedNode: String) { operations.add("insert:$selector") }
            override fun append(path: String, documentPath: String) { operations.add("append:$path") }
            override fun insertBefore(selector: String, documentPath: String) { operations.add("insert:$selector") }
            override fun remove(selector: String) { operations.add("remove:$selector") }
            override fun clear(selector: String) { operations.add("clear:$selector") }
            override fun createNodeAsset(node: GenericNode, listParent: Boolean): String {
                return UiSerializer.serialize(node, listParent = listParent).trim()
            }
        }

        val orderedBuilder = OrderedMockCommandBuilder()
        UiDiffProcessor.generateUpdateCommands(initial, current, orderedBuilder)

        val ops = orderedBuilder.operations
        
        // Expected order:
        // 1. append to #parent (adds #child and its background because background is a Map)
        // No separate set command for background
        
        val appendChildIdx = ops.indexOf("append:#parent")
        val setBackgroundIdx = ops.indexOf("set:#child.Background")

        assertTrue(appendChildIdx != -1, "Should have appended to #parent. Ops: $ops")
        assertEquals(-1, setBackgroundIdx, "Should NOT have set #child.Background via command because it's a Map. Ops: $ops")
    }
}
