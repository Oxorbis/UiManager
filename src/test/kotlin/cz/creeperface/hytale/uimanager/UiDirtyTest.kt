package cz.creeperface.hytale.uimanager

import com.hypixel.hytale.server.core.Message
import cz.creeperface.hytale.uimanager.model.Observable
import cz.creeperface.hytale.uimanager.node.UiButton
import cz.creeperface.hytale.uimanager.node.UiTextButton
import cz.creeperface.hytale.uimanager.property.HasDelegates
import cz.creeperface.hytale.uimanager.property.Rebindable
import cz.creeperface.hytale.uimanager.util.toMessage
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class UiDirtyTest : HasDelegates {

    override val delegates = mutableMapOf<String, Rebindable<*>>()
    override fun markDirty() {}

    open class TestModel(open var name: Message)

    // Manual implementation of what KSP would generate
    class ObservableTestModel(name: Message) : TestModel(name), Observable {
        private val _listeners = mutableMapOf<String?, MutableList<(String, Any?) -> Unit>>()

        override fun addChangeListener(property: String?, listener: (String, Any?) -> Unit) {
            _listeners.getOrPut(property) { mutableListOf() }.add(listener)
        }

        override fun notifyChange(property: String, value: Any?) {
            _listeners[null]?.forEach { it(property, value) }
            _listeners[property]?.forEach { it(property, value) }
        }

        override var name: Message = name
            set(value) {
                if (field != value) {
                    field = value
                    notifyChange("name", value)
                }
            }
    }

    @Test
    fun testManualDirty() {
        val node = UiTextButton(text = "Initial".toMessage())
        assertFalse(node.isDirty)

        node.text = "Changed".toMessage()
        assertTrue(node.isDirty)

        node.resetDirty()
        assertFalse(node.isDirty)
    }

    @Test
    fun testObservableDirty() {
        val model = ObservableTestModel("Initial".toMessage())
        val node = UiTextButton()

        // Manual bind (simulating the bind extension)
        val delegate = node.delegates["text"] as Rebindable<Message>
        delegate.bindTo(model::name)

        assertFalse(node.isDirty)

        model.name = Message.raw("Changed")
        assertTrue(node.isDirty)

        node.resetDirty()
        assertFalse(node.isDirty)
    }

    @Test
    fun testPageDirty() {
        val page = UiPage()
        val node = UiTextButton(text = "Initial".toMessage())
        page.nodes.add(node)

        assertFalse(page.isDirty)

        node.text = "Changed".toMessage()
        assertTrue(page.isDirty)

        page.resetDirty()
        assertFalse(page.isDirty)
    }

    @Test
    fun testRecursiveDirty() {
        val parent = UiButton()
        val child = UiTextButton(text = "Initial".toMessage())
        parent.children.add(child)

        assertFalse(parent.isDirty)

        child.text = "Changed".toMessage()
        assertTrue(child.isDirty)
        assertTrue(parent.isDirty)

        parent.resetDirty()
        assertFalse(child.isDirty)
        assertFalse(parent.isDirty)
    }
}
