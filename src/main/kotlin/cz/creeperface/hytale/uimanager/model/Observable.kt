package cz.creeperface.hytale.uimanager.model

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

interface Observable {
    fun addChangeListener(property: String?, listener: (String, Any?) -> Unit)
    fun notifyChange(property: String, value: Any?)
}

abstract class BaseObservable : Observable {
    private val listeners = mutableMapOf<String?, MutableList<(String, Any?) -> Unit>>()

    override fun addChangeListener(property: String?, listener: (String, Any?) -> Unit) {
        listeners.getOrPut(property) { mutableListOf() }.add(listener)
    }

    override fun notifyChange(property: String, value: Any?) {
        listeners[null]?.forEach { it(property, value) }
        listeners[property]?.forEach { it(property, value) }
    }

    protected fun <T> observable(initialValue: T): ReadWriteProperty<BaseObservable, T> =
        ObservableProperty(initialValue)
}

class ObservableProperty<T>(private var value: T) : ReadWriteProperty<BaseObservable, T> {
    override fun getValue(thisRef: BaseObservable, property: KProperty<*>): T = value

    override fun setValue(thisRef: BaseObservable, property: KProperty<*>, value: T) {
        if (this.value != value) {
            this.value = value
            thisRef.notifyChange(property.name, value)
        }
    }
}
