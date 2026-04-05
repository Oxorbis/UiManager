package cz.creeperface.hytale.uimanager.event

import com.google.gson.internal.LazilyParsedNumber
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
    val shiftHeld get() = response.shiftHeld

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

        val value = eventData["@$propertyPath"]

        if (value is LazilyParsedNumber) {
            val returnType = property.returnType.classifier
            val converted = when (returnType) {
                Int::class -> value.toInt()
                Double::class -> value.toDouble()
                Long::class -> value.toLong()
                Float::class -> value.toFloat()
                Short::class -> value.toShort()
                Byte::class -> value.toByte()
                else -> value
            }
            @Suppress("UNCHECKED_CAST")
            return converted as T
        }

        @Suppress("UNCHECKED_CAST")
        return value as T
    }
}