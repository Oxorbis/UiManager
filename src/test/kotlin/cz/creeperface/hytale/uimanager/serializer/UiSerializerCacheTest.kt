package cz.creeperface.hytale.uimanager.serializer

import com.hypixel.hytale.server.core.ui.DropdownEntryInfo
import com.hypixel.hytale.server.core.ui.LocalizableString
import cz.creeperface.hytale.uimanager.Color
import cz.creeperface.hytale.uimanager.UiPage
import cz.creeperface.hytale.uimanager.builder.customUi
import cz.creeperface.hytale.uimanager.builder.dropdownBox
import cz.creeperface.hytale.uimanager.builder.label
import cz.creeperface.hytale.uimanager.node.UiDropdownBox
import cz.creeperface.hytale.uimanager.special.listGroup
import cz.creeperface.hytale.uimanager.type.anchor
import cz.creeperface.hytale.uimanager.type.labelStyle
import cz.creeperface.hytale.uimanager.type.patchStyle
import cz.creeperface.hytale.uimanager.util.toMessage
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class UiSerializerCacheTest {

    private fun dropdownEntry(value: String, label: String): DropdownEntryInfo =
        DropdownEntryInfo(LocalizableString.fromString(label), value)

    // Representative tree: UiListGroup (listNode), @DynamicProperty (DropdownBox.entries),
    // @ExcludeProperty (id), nested UiType (anchor/background/style), editable value.
    // NOTE: label is at top level (not inside listGroup) so its #id selector is emitted.
    private fun buildTree(): UiPage = customUi {
        listGroup {
            id = "list"
            dropdownBox {
                id = "dd"
                entries = listOf(dropdownEntry("o1", "Option 1"))
                value = "o1"
            }
        }
        label {
            id = "lbl"
            text = "Hello".toMessage()
            anchor = anchor { top = 10; width = 100 }
            background = patchStyle {
                color = Color("#FF0000")
                texturePath = "tex/path"
            }
            style = labelStyle { fontSize = 12.0 }
        }
    }

    @Test
    fun testSerializationIsDeterministic() {
        val first = UiSerializer.serialize(buildTree())
        val second = UiSerializer.serialize(buildTree())
        assertEquals(first, second, "Serialization must be deterministic (stable property ordering)")
    }

    @Test
    fun testExcludeAndDynamicPropertiesPreserved() {
        val page = buildTree()
        val serialized = UiSerializer.serialize(page)

        // @ExcludeProperty: ids appear as #selectors, never as an "Id:" property line.
        assertFalse(
            serialized.contains("Id:"),
            "id is @ExcludeProperty and must not serialize as a property. Got: $serialized"
        )
        assertTrue(serialized.contains("#lbl"), "label id should appear as a selector")

        // @DynamicProperty: DropdownBox.entries is excluded from the .ui text...
        assertFalse(
            serialized.contains("Entries"),
            "entries (@DynamicProperty) must not appear in .ui text. Got: $serialized"
        )

        // ...but is present in toGenericNode(includeDynamic = true).
        val dd = (page.nodes[0] as cz.creeperface.hytale.uimanager.special.UiListGroup)
            .children.first { it is UiDropdownBox }
        val generic = UiSerializer.toGenericNode(dd, includeDynamic = true)
        assertTrue(generic.properties.containsKey("Entries"), "Entries should be present when includeDynamic = true")
    }

    @Test
    fun testNestedUiTypesPreserved() {
        val serialized = UiSerializer.serialize(buildTree())
        assertTrue(serialized.contains("Anchor"), "nested Anchor UiType should serialize. Got: $serialized")
        assertTrue(serialized.contains("Background"), "nested Background UiType should serialize. Got: $serialized")
        assertTrue(serialized.contains("Style"), "nested Style UiType should serialize. Got: $serialized")
    }

    @Test
    fun testConcurrentSerializationProducesIdenticalResults() {
        val page = buildTree()
        val expected = UiSerializer.serialize(page)

        val threads = 8
        val iterations = 100
        val pool = Executors.newFixedThreadPool(threads)
        val results = ConcurrentLinkedQueue<String>()
        val errors = ConcurrentLinkedQueue<Throwable>()
        val latch = CountDownLatch(threads)
        repeat(threads) {
            pool.submit {
                try {
                    repeat(iterations) { results.add(UiSerializer.serialize(page)) }
                } catch (e: Throwable) {
                    errors.add(e)
                } finally {
                    latch.countDown()
                }
            }
        }
        latch.await(30, TimeUnit.SECONDS)
        pool.shutdownNow()

        assertTrue(errors.isEmpty(), "No exceptions expected, got: ${errors.firstOrNull()}")
        assertEquals(threads * iterations, results.size, "Every task should have produced a result")
        assertTrue(results.all { it == expected }, "All concurrent serializations must be identical")
    }
}
