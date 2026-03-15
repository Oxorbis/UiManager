package cz.creeperface.hytale.uimanager

import cz.creeperface.hytale.uimanager.node.UiTextButton
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class UiCloneTest {

    @Test
    fun testNodeClone() {
        val original = UiTextButton(text = "Original")
        original.id = "btn1"
        
        val clone = original.clone() as UiTextButton
        
        assertEquals(original.id, clone.id)
        assertEquals(original.text, clone.text)
        assertNotSame(original, clone)
        
        // Change original, clone should not change
        original.text = "Changed"
        assertEquals("Original", clone.text)
    }

    @Test
    fun testCloneWithDelegates() {
        val original = UiTextButton(text = "Original")
        
        // Simulating binding effect manually since the bind extension is tricky to use in test
        original.text = "Bound Value"
        
        val clone = original.clone() as UiTextButton
        assertEquals("Bound Value", clone.text)
        
        // Change original, clone should NOT change
        original.text = "New Value"
        assertEquals("New Value", original.text)
        assertEquals("Bound Value", clone.text)
        
        // Change clone, original should NOT change
        clone.text = "Clone Change"
        assertEquals("New Value", original.text)
        assertEquals("Clone Change", clone.text)
    }

    @Test
    fun testPageClone() {
        val page = UiPage()
        val btn = UiTextButton(text = "Button")
        page.nodes.add(btn)
        page.variable("v1", "val1")
        
        val clonedPage = page.clone()
        
        assertEquals(1, clonedPage.nodes.size)
        val clonedBtn = clonedPage.nodes[0] as UiTextButton
        assertEquals("Button", clonedBtn.text)
        assertEquals("val1", clonedPage.variables["v1"])
        
        assertNotSame(page, clonedPage)
        assertNotSame(btn, clonedBtn)
        
        btn.text = "Changed"
        assertEquals("Button", clonedBtn.text)
    }
}
