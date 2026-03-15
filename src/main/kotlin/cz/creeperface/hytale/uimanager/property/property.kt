package cz.creeperface.hytale.uimanager.property

import cz.creeperface.hytale.uimanager.model.Observable
import kotlin.jvm.internal.CallableReference
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty

interface HasDelegates {
    val delegates: MutableMap<String, Rebindable<*>>

    fun markDirty()

    fun <T> KMutableProperty0<T?>.bind(sourceProperty: KMutableProperty0<T>) {
        val propertyName = if (this is CallableReference) this.name else {
            // fallback if it's not a CallableReference for some reason, 
            // though property references usually are
            throw IllegalStateException("Property must be a CallableReference to be bound")
        }
        val delegate = delegates[propertyName]!!

        require(delegate.clazz == this.returnType.classifier) {
            "Target property type mismatch for ${sourceProperty.name} and ${this.name}"
        }

        val rebindableDelegate = delegate as Rebindable<T>

        rebindableDelegate.bindTo(sourceProperty)
    }
}

class Rebindable<T>(
    val clazz: KClass<*>,
    initial: T
) {

    private var local: T = initial
    private var target: KMutableProperty0<T>? = null
    var isDirty: Boolean = false
        private set

    fun resetDirty() {
        isDirty = false
    }

    fun bindTo(t: KMutableProperty0<T>) {
        target = t
        if (t is CallableReference) {
            val receiver = t.boundReceiver
            if (receiver is Observable) {
                receiver.addChangeListener(t.name) { _, _ ->
                    isDirty = true
                    owner?.markDirty()
                }
            }
        }
    }

    fun unbind() { target = null }

    private var owner: HasDelegates? = null

    operator fun provideDelegate(thisRef: Any?, prop: KProperty<*>): Rebindable<T> {
        if (thisRef is HasDelegates) {
            thisRef.delegates[prop.name] = this
            owner = thisRef
        }
        return this
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T =
        target?.get() ?: local

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        val currentValue = getValue(thisRef, property)
        if (currentValue != value) {
            val t = target
            if (t != null) t.set(value) else local = value
            isDirty = true
            owner?.markDirty()
        }
    }
}

inline fun <reified T> rebindable(initial: T): Rebindable<T> = Rebindable(T::class, initial)
