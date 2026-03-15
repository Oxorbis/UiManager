package cz.creeperface.hytale.uimanager.event

import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType
import cz.creeperface.hytale.uimanager.UiNode
import cz.creeperface.hytale.uimanager.util.boundReceiverOrNull
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty0
import kotlin.reflect.KProperty1

class BoundPropertyEntry(
    val nodeInstance: UiNode,
    val nodeProperty: KProperty0<Any?>,
)

class EventBinding(
    val type: CustomUIEventBindingType,
    val nodeId: String,
    val boundProperties: List<BoundPropertyEntry>,
    val action: (EventContext) -> Unit,
)

fun eventBinding(
    type: CustomUIEventBindingType,
    nodeId: String,
    vararg properties: KProperty0<Any?>,
    action: (EventContext) -> Unit,
): EventBinding {
    val boundProperties = properties.map { property ->
        val receiver = requireNotNull(property.boundReceiverOrNull()) {
            "Property receiver cannot be null for property: ${property.name}"
        }

        require(receiver is UiNode) {
            "Property receiver must be UiNode"
        }

        BoundPropertyEntry(receiver, property)
    }

    return EventBinding(type, nodeId,boundProperties, action)
}

interface EventBindable {
    fun addEventBinding(eventBinding: EventBinding)
}