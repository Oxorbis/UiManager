package cz.creeperface.hytale.uimanager.property

import cz.creeperface.hytale.uimanager.model.BaseObservable
import cz.creeperface.hytale.uimanager.node.UiLabel
import org.junit.jupiter.api.Test

class PropertyBindTest {

    class Data(time: String) : BaseObservable() {
        var time: String by observable(time)
    }

    @Test
    fun testPropertyBindValue() {
        val data = Data("10:00")
        val label = UiLabel()
        
        label.apply {
            ::text.bind(data::time)
        }
        
        assert(label.text == "10:00")
        
        data.time = "09:59"
        assert(label.text == "09:59")
        assert(label.isDirty)
    }

    @Test
    fun testPropertyBindSetter() {
        val data = Data("10:00")
        val label = UiLabel()
        
        label.apply {
            ::text.bind(data::time)
        }
        
        label.text = "08:00"
        assert(data.time == "08:00")
    }
}
