package cz.creeperface.hytale.uimanager.property

import com.hypixel.hytale.server.core.Message
import cz.creeperface.hytale.uimanager.model.BaseObservable
import cz.creeperface.hytale.uimanager.node.UiLabel
import cz.creeperface.hytale.uimanager.util.toMessage
import org.junit.jupiter.api.Test

class PropertyBindTest {

    class Data(time: Message) : BaseObservable() {
        var time: Message by observable(time)
    }

    @Test
    fun testPropertyBindValue() {
        val data = Data("10:00".toMessage())
        val label = UiLabel()

        label.apply {
            ::text.bind(data::time)
        }

        assert(label.text?.rawText == "10:00")

        data.time = "09:59".toMessage()
        assert(label.text?.rawText == "09:59")
        assert(label.isDirty)
    }

    @Test
    fun testPropertyBindSetter() {
        val data = Data("10:00".toMessage())
        val label = UiLabel()

        label.apply {
            ::text.bind(data::time)
        }

        label.text = "08:00".toMessage()
        assert(data.time.rawText == "08:00")
    }
}
