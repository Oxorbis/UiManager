package cz.creeperface.hytale.uimanager.serializer

import com.hypixel.hytale.server.core.Message
import cz.creeperface.hytale.uimanager.builder.customUi
import cz.creeperface.hytale.uimanager.builder.label
import cz.creeperface.hytale.uimanager.builder.textButton
import cz.creeperface.hytale.uimanager.util.toMessage
import cz.creeperface.hytale.uimanager.util.translated
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class MessageSerializationTest {

    @Test
    fun testRawMessageSerialized() {
        val page = customUi {
            label {
                id = "lbl"
                text = "Hello World".toMessage()
            }
        }

        val serialized = UiSerializer.serialize(page)
        assertTrue(
            serialized.contains("Text: \"Hello World\""),
            "Raw message should be serialized as quoted string. Got: $serialized"
        )
    }

    @Test
    fun testTranslatedMessageSerialized() {
        val page = customUi {
            label {
                id = "lbl"
                text = "server.customUI.title".translated()
            }
        }

        val serialized = UiSerializer.serialize(page)
        assertTrue(
            serialized.contains("Text: %server.customUI.title"),
            "Translated message should be serialized with % prefix without quotes. Got: $serialized"
        )
        assertFalse(
            serialized.contains("Text: \"%server.customUI.title\""),
            "Translated message should NOT be quoted. Got: $serialized"
        )
    }

    @Test
    fun testEmptyRawMessageSerialized() {
        val page = customUi {
            label {
                id = "lbl"
                text = "".toMessage()
            }
        }

        val serialized = UiSerializer.serialize(page)
        assertTrue(
            serialized.contains("Text: \"\""),
            "Empty raw message should serialize as empty quoted string. Got: $serialized"
        )
    }

    @Test
    fun testRawMessageWithSpecialCharsSerialized() {
        val page = customUi {
            label {
                id = "lbl"
                text = "Hello \"World\"".toMessage()
            }
        }

        val serialized = UiSerializer.serialize(page)
        assertTrue(
            serialized.contains("Text: \"Hello \\\"World\\\"\""),
            "Quotes in raw message should be escaped. Got: $serialized"
        )
    }

    // --- Diff processor tests ---

    class MockCommandBuilder : UiDiffProcessor.CommandBuilder {
        val commands = mutableMapOf<String, Any?>()
        val removals = mutableListOf<String>()
        val appends = mutableListOf<Pair<String, String>>()
        val inserts = mutableListOf<Pair<String, String>>()
        val clears = mutableListOf<String>()

        override fun set(path: String, value: Boolean) {
            commands[path] = value
        }

        override fun set(path: String, value: Int) {
            commands[path] = value
        }

        override fun set(path: String, value: Float) {
            commands[path] = value
        }

        override fun set(path: String, value: Double) {
            commands[path] = value
        }

        override fun set(path: String, value: String) {
            commands[path] = value
        }

        override fun set(path: String, value: Message) {
            commands[path] = value
        }

        override fun set(path: String, value: List<*>) {
            commands[path] = value
        }

        override fun setNull(path: String) {
            commands[path] = null
        }

        override fun setRaw(path: String, value: Any) {
            commands[path] = value
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
    fun testDiffRawMessageChange() {
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

        assertEquals(1, builder.commands.size, "Should have exactly one command")
        assertEquals("Changed", builder.commands["#btn.Text"], "Raw message should be passed as String")
    }

    @Test
    fun testDiffTranslatedMessageChange() {
        val initial = customUi {
            textButton {
                id = "btn"
                text = "ui.button.save".translated()
            }
        }

        val current = initial.clone()
        (current.nodes[0] as cz.creeperface.hytale.uimanager.node.UiTextButton).text = "ui.button.cancel".translated()

        val builder = MockCommandBuilder()
        UiDiffProcessor.generateUpdateCommands(initial, current, builder)

        assertEquals(1, builder.commands.size, "Should have exactly one command")
        val textValue = builder.commands["#btn.Text"]
        assertTrue(textValue is Message, "Translated message should be passed as Message object")
        assertEquals("ui.button.cancel", (textValue as Message).messageId)
    }

    @Test
    fun testDiffRawToTranslatedChange() {
        val initial = customUi {
            textButton {
                id = "btn"
                text = "Save".toMessage()
            }
        }

        val current = initial.clone()
        (current.nodes[0] as cz.creeperface.hytale.uimanager.node.UiTextButton).text = "ui.button.save".translated()

        val builder = MockCommandBuilder()
        UiDiffProcessor.generateUpdateCommands(initial, current, builder)

        assertEquals(1, builder.commands.size, "Should have exactly one command")
        val textValue = builder.commands["#btn.Text"]
        assertTrue(textValue is Message, "Translated message should be passed as Message object")
        assertEquals("ui.button.save", (textValue as Message).messageId)
    }

    @Test
    fun testDiffTranslatedToRawChange() {
        val initial = customUi {
            textButton {
                id = "btn"
                text = "ui.button.save".translated()
            }
        }

        val current = initial.clone()
        (current.nodes[0] as cz.creeperface.hytale.uimanager.node.UiTextButton).text = "Save".toMessage()

        val builder = MockCommandBuilder()
        UiDiffProcessor.generateUpdateCommands(initial, current, builder)

        assertEquals(1, builder.commands.size, "Should have exactly one command")
        assertEquals("Save", builder.commands["#btn.Text"], "Raw message should be passed as String")
    }

    @Test
    fun testDiffNoChangeOnSameRawMessage() {
        val initial = customUi {
            textButton {
                id = "btn"
                text = "Same".toMessage()
            }
        }

        val current = initial.clone()

        val builder = MockCommandBuilder()
        UiDiffProcessor.generateUpdateCommands(initial, current, builder)

        assertTrue(
            builder.commands.isEmpty(),
            "Should have no commands for identical message. Commands: ${builder.commands}"
        )
    }

    @Test
    fun testDiffNoChangeOnSameTranslatedMessage() {
        val initial = customUi {
            textButton {
                id = "btn"
                text = "ui.button.save".translated()
            }
        }

        val current = initial.clone()

        val builder = MockCommandBuilder()
        UiDiffProcessor.generateUpdateCommands(initial, current, builder)

        assertTrue(
            builder.commands.isEmpty(),
            "Should have no commands for identical translated message. Commands: ${builder.commands}"
        )
    }

    // --- Translation with parameters diff tests ---

    @Test
    fun testDiffTranslationKeyToTranslationWithParams() {
        // Initial: translation key only, Update: same key with params
        val initial = customUi {
            textButton {
                id = "btn"
                text = "ui.score".translated()
            }
        }

        val current = initial.clone()
        (current.nodes[0] as cz.creeperface.hytale.uimanager.node.UiTextButton).text =
            "ui.score".translated().param("score", "10")

        val builder = MockCommandBuilder()
        UiDiffProcessor.generateUpdateCommands(initial, current, builder)

        assertEquals(1, builder.commands.size, "Should detect param addition. Commands: ${builder.commands}")
        val textValue = builder.commands["#btn.Text"]
        assertTrue(textValue is Message, "Translated message with params should be passed as Message object")
    }

    @Test
    fun testDiffTranslationWithParamsChange() {
        // Both have params but with different values
        val initial = customUi {
            textButton {
                id = "btn"
                text = "ui.score".translated().param("score", "10")
            }
        }

        val current = initial.clone()
        (current.nodes[0] as cz.creeperface.hytale.uimanager.node.UiTextButton).text =
            "ui.score".translated().param("score", "20")

        val builder = MockCommandBuilder()
        UiDiffProcessor.generateUpdateCommands(initial, current, builder)

        assertEquals(1, builder.commands.size, "Should detect param value change. Commands: ${builder.commands}")
        val textValue = builder.commands["#btn.Text"]
        assertTrue(textValue is Message, "Translated message with params should be passed as Message object")
    }

    @Test
    fun testDiffTranslationWithParamsNoChange() {
        // Same key, same params — no update expected
        val initial = customUi {
            textButton {
                id = "btn"
                text = "ui.score".translated().param("score", "10")
            }
        }

        val current = initial.clone()
        (current.nodes[0] as cz.creeperface.hytale.uimanager.node.UiTextButton).text =
            "ui.score".translated().param("score", "10")

        val builder = MockCommandBuilder()
        UiDiffProcessor.generateUpdateCommands(initial, current, builder)

        assertTrue(
            builder.commands.isEmpty(),
            "Should have no commands for identical translation with params. Commands: ${builder.commands}"
        )
    }

    @Test
    fun testSerializeGenericNodeWithRawMessage() {
        val node = GenericNode("Label", "lbl")
        node.properties["Text"] = GenericNode.MessageValue(Message.raw("Hello"), "Hello")

        val serialized = UiSerializer.serialize(node)
        assertTrue(
            serialized.contains("Text: \"Hello\""),
            "MessageValue with raw text should serialize as quoted string. Got: $serialized"
        )
    }

    @Test
    fun testSerializeGenericNodeWithTranslatedMessage() {
        val node = GenericNode("Label", "lbl")
        node.properties["Text"] = GenericNode.MessageValue(
            Message.translation("ui.title"),
            GenericNode.Identifier("%ui.title")
        )

        val serialized = UiSerializer.serialize(node)
        assertTrue(
            serialized.contains("Text: %ui.title"),
            "MessageValue with translation should serialize as identifier with %. Got: $serialized"
        )
    }
}
