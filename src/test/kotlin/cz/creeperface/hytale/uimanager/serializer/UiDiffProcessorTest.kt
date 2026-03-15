package cz.creeperface.hytale.uimanager.serializer

import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType
import cz.creeperface.hytale.uimanager.builder.customUi
import cz.creeperface.hytale.uimanager.builder.group
import cz.creeperface.hytale.uimanager.builder.textButton
import cz.creeperface.hytale.uimanager.type.anchor
import cz.creeperface.hytale.uimanager.type.patchStyle
import cz.creeperface.hytale.uimanager.UiNode
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
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
    }

    @Test
    fun testNoChanges() {
        val initial = customUi {
            textButton {
                text = "Same"
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
                text = "Initial"
            }
        }
        
        val current = initial.clone()
        (current.nodes[0] as cz.creeperface.hytale.uimanager.node.UiTextButton).text = "Changed"
        
        val builder = MockCommandBuilder()
        UiDiffProcessor.generateUpdateCommands(initial, current, builder)
        
        val commands = builder.commands
        assertEquals(1, commands.size)
        assertEquals("Changed", commands["#btn.Text"])
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
        assertTrue(commands.containsKey("#btn.Anchor.Top") || commands.containsKey("#btn.Anchor"), "Should contain #btn.Anchor.Top or #btn.Anchor. Commands: ${commands.keys}")
        
        if (commands.containsKey("#btn.Anchor.Top")) {
            assertEquals(20, (commands["#btn.Anchor.Top"] as? Number)?.toInt())
        } else {
            val anchor = commands["#btn.Anchor"] as Map<String, Any?>
            assertEquals(20, (anchor["Top"] as? Number)?.toInt())
        }
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
                    text = "New"
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

        assertEquals(2, builder.appends.size)
        
        // First append to #parent
        val (path1, serialized1) = builder.appends[0]
        assertEquals("#parent", path1)
        assertTrue(serialized1.contains("Group") && serialized1.contains("#childGroup"))
        assertFalse(serialized1.contains("TextButton"), "Parent serialization should not contain children anymore")

        // Second append to #childGroup
        val (path2, serialized2) = builder.appends[1]
        assertEquals("#childGroup", path2)
        assertTrue(serialized2.contains("TextButton") && serialized2.contains("#btn1"))
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
                        textButton { id = "btn2"; text = "A" }
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
        assertEquals("A", builder.commands["#btn2.Text"])
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
        
        // Background should NOT be in the serialized node
        assertFalse(serialized.contains("Background:"), "Background should be stripped from serialized node. Serialized: $serialized")
        assertTrue(serialized.contains("#btnWithIcon"), "Serialized node should contain its ID")

        // Background should NOT be set via a separate command because it's a Map (complex property)
        assertFalse(builder.commands.containsKey("#btnWithIcon.Background"), "Should NOT have a command to set Background because it's a Map")
        assertTrue(serialized.contains("TexturePath"), "Background Map should still be in serialized node")
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
            override fun setRaw(path: String, value: Any) { operations.add("set:$path") }
            override fun appendInline(path: String, serializedNode: String) { operations.add("append:$path") }
            override fun insertBeforeInline(selector: String, serializedNode: String) { operations.add("insert:$selector") }
            override fun append(path: String, documentPath: String) { operations.add("append:$path") }
            override fun insertBefore(selector: String, documentPath: String) { operations.add("insert:$selector") }
            override fun remove(selector: String) { operations.add("remove:$selector") }
            override fun clear(selector: String) { operations.add("clear:$selector") }
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
