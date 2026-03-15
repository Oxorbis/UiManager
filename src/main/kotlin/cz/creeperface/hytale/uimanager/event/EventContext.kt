package cz.creeperface.hytale.uimanager.event

import com.hypixel.hytale.logger.HytaleLogger
import com.hypixel.hytale.server.core.universe.PlayerRef
import cz.creeperface.hytale.uimanager.UiNode
import cz.creeperface.hytale.uimanager.util.boundReceiverOrNull
import kotlin.reflect.KProperty0

class EventContext(
    val playerRef: PlayerRef,
    private val response: EventResponse,
    private val eventData: Map<String, Any?>,
) {
    fun <T> getData(property: KProperty0<T>): T {
        val receiver = property.boundReceiverOrNull()

        requireNotNull(receiver) {
            "Unable to get receiver from property $property"
        }

        require(receiver is UiNode) {
            "Receiver for property $property is not UiNode. Got ${receiver.javaClass.simpleName}"
        }

        val propertyPath = receiver.id + "." + property.name.replaceFirstChar {
            it.uppercase()
        }
        HytaleLogger.getLogger().atInfo().log("EventData: $eventData")
        HytaleLogger.getLogger().atInfo().log("PropertyPath: $propertyPath")
        return eventData["@$propertyPath"] as T
    }
}