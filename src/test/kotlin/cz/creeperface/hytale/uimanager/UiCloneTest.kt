package cz.creeperface.hytale.uimanager

import com.hypixel.hytale.server.core.Message
import cz.creeperface.hytale.uimanager.node.UiTextButton
import cz.creeperface.hytale.uimanager.util.toMessage
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class UiCloneTest {

    private fun assertMessageEquals(expected: String, actual: Message?) {
        assertNotNull(actual)
        assertEquals(expected, actual!!.rawText)
    }

    @Test
    fun testNodeClone() {
        val original = UiTextButton(text = "Original".toMessage())
        original.id = "btn1"

        val clone = original.clone() as UiTextButton

        assertEquals(original.id, clone.id)
        assertNotSame(original, clone)

        // Change original, clone should not change
        original.text = "Changed".toMessage()
        assertMessageEquals("Original", clone.text)
    }

    @Test
    fun testCloneWithDelegates() {
        val original = UiTextButton(text = "Original".toMessage())

        // Simulating binding effect manually since the bind extension is tricky to use in test
        original.text = "Bound Value".toMessage()

        val clone = original.clone() as UiTextButton
        assertMessageEquals("Bound Value", clone.text)

        // Change original, clone should NOT change
        original.text = "New Value".toMessage()
        assertMessageEquals("New Value", original.text)
        assertMessageEquals("Bound Value", clone.text)

        // Change clone, original should NOT change
        clone.text = "Clone Change".toMessage()
        assertMessageEquals("New Value", original.text)
        assertMessageEquals("Clone Change", clone.text)
    }

    @Test
    fun testPageClone() {
        val page = UiPage()
        val btn = UiTextButton(text = "Button".toMessage())
        page.nodes.add(btn)
        page.variable("v1", "val1")

        val clonedPage = page.clone()

        assertEquals(1, clonedPage.nodes.size)
        val clonedBtn = clonedPage.nodes[0] as UiTextButton
        assertMessageEquals("Button", clonedBtn.text)
        assertEquals("val1", clonedPage.variables["v1"])

        assertNotSame(page, clonedPage)
        assertNotSame(btn, clonedBtn)

        btn.text = "Changed".toMessage()
        assertMessageEquals("Button", clonedBtn.text)
    }
}
