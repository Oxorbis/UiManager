package cz.creeperface.hytale.uimanager.special

import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType
import com.hypixel.hytale.server.core.universe.PlayerRef
import cz.creeperface.hytale.uimanager.BaseUiNode
import cz.creeperface.hytale.uimanager.ChildNodeBuilder
import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiDsl
import cz.creeperface.hytale.uimanager.UiNode
import cz.creeperface.hytale.uimanager.UiNodeWithChildren
import cz.creeperface.hytale.uimanager.builder.textButton
import cz.creeperface.hytale.uimanager.builder.textField
import cz.creeperface.hytale.uimanager.node.UiTextButton
import cz.creeperface.hytale.uimanager.node.UiTextField
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KMutableProperty1

class UiForm<T: Any>(
    internal val boundClass: KClass<T>,
    private val parent: ChildNodeBuilder
) : BaseUiNode(), UiFormContext<T>, ChildNodeBuilder {

    @ExcludeProperty
    override var id: String? = null
    @ExcludeProperty
    override var omitName: Boolean = false

    @ExcludeProperty
    override val form: UiForm<*>
        get() = this

    @ExcludeProperty
    override var children = mutableListOf<UiNode>()

    internal var boundProperties = mutableListOf<BoundPropertyEntry>()
    internal var submitters: MutableList<FormSubmitter> = mutableListOf()

    @ExcludeProperty
    var submitHandler: ((PlayerRef, T) -> Unit)? = null

    override fun addNode(node: UiNode) {
        addNodeToChildren(node)
    }

    override fun clone(): UiForm<T> {
        val clone = UiForm(boundClass, parent)

        clone.id = id
        clone.children = children.map { it.clone() }.toMutableList()
        clone.boundProperties = boundProperties
        clone.submitters = submitters
        clone.submitHandler = submitHandler

        return clone
    }

    fun <Value> bind(
        value: UiNode,
        property: KMutableProperty0<Value>,
        boundProperty: KMutableProperty1<T, Value>,
    ) {
        @Suppress("UNCHECKED_CAST")
        boundProperties.add(BoundPropertyEntry(
            nodeInstance = value,
            nodeProperty = property as KMutableProperty0<Any?>,
            boundProperty = boundProperty as KMutableProperty1<T, Any?>,
        ))
    }

    fun <Value, PropertyValue> bind(
        value: UiNode,
        property: KMutableProperty0<Value>,
        boundProperty: KMutableProperty1<T, PropertyValue>,
        valueMapper: ((PropertyValue) -> Value)? = null,
    ) {
        @Suppress("UNCHECKED_CAST")
        boundProperties.add(BoundPropertyEntry(
            nodeInstance = value,
            nodeProperty = property as KMutableProperty0<Any?>,
            boundProperty = boundProperty as KMutableProperty1<T, Any?>,
            valueMapper = valueMapper as ((Any?) -> Any?)
        ))
    }

    fun <Value> bindToForm(
        node: UiNode,
        property: KMutableProperty0<Value>,
        boundProperty: KMutableProperty1<T, Value>
    ) {
        this@UiForm.bind(node, property, boundProperty)
    }

    fun <Value, PropertyValue> bindToForm(
        node: UiNode,
        property: KMutableProperty0<Value>,
        boundProperty: KMutableProperty1<T, PropertyValue>,
        valueMapper: (PropertyValue) -> Value
    ) {
        this@UiForm.bind(node, property, boundProperty, valueMapper)
    }

    fun bindSubmit(node: UiNode, eventType: CustomUIEventBindingType = CustomUIEventBindingType.Activating) {
        submitters.add(FormSubmitter(node, eventType))
    }

    internal inner class BoundPropertyEntry(
        val nodeInstance: UiNode,
        val nodeProperty: KMutableProperty0<Any?>,
        val boundProperty: KMutableProperty1<T, Any?>,
        val valueMapper: ((Any?) -> Any?)? = null
    )

    internal class FormSubmitter(
        val node: UiNode,
        val eventType: CustomUIEventBindingType
    )
}