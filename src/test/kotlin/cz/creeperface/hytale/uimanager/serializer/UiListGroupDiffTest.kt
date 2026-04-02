package cz.creeperface.hytale.uimanager.serializer

import cz.creeperface.hytale.uimanager.builder.customUi
import cz.creeperface.hytale.uimanager.builder.textButton
import cz.creeperface.hytale.uimanager.special.listGroup
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class UiListGroupDiffTest {

    class MockCommandBuilder : UiDiffProcessor.CommandBuilder {
        val commands = mutableListOf<Command>()

        sealed class Command {
            data class Set(val path: String, val value: Any?) : Command()
            data class AppendInline(val path: String, val serializedNode: String) : Command()
            data class InsertBeforeInline(val selector: String, val serializedNode: String) : Command()
            data class Remove(val selector: String) : Command()
            data class Clear(val selector: String) : Command()
        }

        override fun set(path: String, value: Boolean) { commands.add(Command.Set(path, value)) }
        override fun set(path: String, value: Int) { commands.add(Command.Set(path, value)) }
        override fun set(path: String, value: Float) { commands.add(Command.Set(path, value)) }
        override fun set(path: String, value: Double) { commands.add(Command.Set(path, value)) }
        override fun set(path: String, value: String) { commands.add(Command.Set(path, value)) }
        override fun set(path: String, value: List<*>) {
            commands.add(Command.Set(path, value))
        }

        override fun setNull(path: String) {
            commands.add(Command.Set(path, null))
        }
        override fun setRaw(path: String, value: Any) { commands.add(Command.Set(path, value)) }

        override fun appendInline(path: String, serializedNode: String) { commands.add(Command.AppendInline(path, serializedNode)) }
        override fun insertBeforeInline(selector: String, serializedNode: String) { commands.add(Command.InsertBeforeInline(selector, serializedNode)) }
        override fun append(path: String, documentPath: String) { commands.add(Command.AppendInline(path, documentPath)) }
        override fun insertBefore(selector: String, documentPath: String) { commands.add(Command.InsertBeforeInline(selector, documentPath)) }
        override fun remove(selector: String) { commands.add(Command.Remove(selector)) }
        override fun clear(selector: String) { commands.add(Command.Clear(selector)) }
    }

    @Test
    fun testListGroupPropertyChange() {
        val initial = customUi {
            listGroup {
                id = "list"
                textButton {
                    text = "Initial"
                }
            }
        }

        val current = customUi {
            listGroup {
                id = "list"
                textButton {
                    text = "Changed"
                }
            }
        }

        val builder = MockCommandBuilder()
        UiDiffProcessor.generateUpdateCommands(initial, current, builder)

        val sets = builder.commands.filterIsInstance<MockCommandBuilder.Command.Set>()
        // We expect #list[0].Text
        assertTrue(sets.any { it.path == "#list[0].Text" && it.value == "Changed" }, "Should have command for #list[0].Text. Commands: ${builder.commands}")
    }

    @Test
    fun testListGroupAppend() {
        val initial = customUi {
            listGroup {
                id = "list"
                textButton { text = "A" }
            }
        }

        val current = customUi {
            listGroup {
                id = "list"
                textButton { text = "A" }
                textButton { text = "B" }
            }
        }

        val builder = MockCommandBuilder()
        UiDiffProcessor.generateUpdateCommands(initial, current, builder)

        val appends = builder.commands.filterIsInstance<MockCommandBuilder.Command.AppendInline>()
        assertEquals(1, appends.size)
        assertEquals("#list", appends[0].path)
        assertTrue(appends[0].serializedNode.contains("Text: \"B\""))
    }

    @Test
    fun testListGroupInsert() {
        val initial = customUi {
            listGroup {
                id = "list"
                textButton { text = "A" }
                textButton { text = "B" }
            }
        }

        val current = customUi {
            listGroup {
                id = "list"
                textButton { text = "A" }
                textButton { text = "C" }
                textButton { text = "B" }
            }
        }

        val builder = MockCommandBuilder()
        UiDiffProcessor.generateUpdateCommands(initial, current, builder)

        val inserts = builder.commands.filterIsInstance<MockCommandBuilder.Command.InsertBeforeInline>()
        assertEquals(1, inserts.size)
        assertEquals("#list[1]", inserts[0].selector)
        assertTrue(inserts[0].serializedNode.contains("Text: \"C\""))
    }

    @Test
    fun testListGroupRemove() {
        val initial = customUi {
            listGroup {
                id = "list"
                textButton { text = "A" }
                textButton { text = "B" }
                textButton { text = "C" }
            }
        }

        val current = customUi {
            listGroup {
                id = "list"
                textButton { text = "A" }
                textButton { text = "C" }
            }
        }

        val builder = MockCommandBuilder()
        UiDiffProcessor.generateUpdateCommands(initial, current, builder)

        val removes = builder.commands.filterIsInstance<MockCommandBuilder.Command.Remove>()
        assertEquals(1, removes.size)
        assertEquals("#list[1]", removes[0].selector)
    }

    @Test
    fun testListGroupClear() {
        val initial = customUi {
            listGroup {
                id = "list"
                textButton { text = "A" }
            }
        }

        val current = customUi {
            listGroup {
                id = "list"
            }
        }

        val builder = MockCommandBuilder()
        UiDiffProcessor.generateUpdateCommands(initial, current, builder)

        val clears = builder.commands.filterIsInstance<MockCommandBuilder.Command.Clear>()
        assertEquals(1, clears.size)
        assertEquals("#list", clears[0].selector)
    }
}
