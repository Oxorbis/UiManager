package cz.creeperface.hytale.uimanager.event

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import cz.creeperface.hytale.uimanager.special.FormResponse
import cz.creeperface.hytale.uimanager.special.FormResponse.FormResponseDeserializer
import java.lang.reflect.Type
import kotlin.collections.component1
import kotlin.collections.component2

class EventResponse(
    val eventId: Int,
    val shiftHeld: Boolean,
    val values: Map<String, String>
) {

    companion object {
        val GSON: Gson = GsonBuilder()
            .registerTypeAdapter(EventResponse::class.java, EventResponseDeserializer())
            .create()

        fun fromJson(json: String): EventResponse {
            return GSON.fromJson(json, EventResponse::class.java)
        }

        fun fromJson(json: JsonElement): EventResponse {
            return GSON.fromJson(json, EventResponse::class.java)
        }
    }

    private class EventResponseDeserializer : JsonDeserializer<EventResponse> {
        override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): EventResponse {
            val obj = json.asJsonObject
            val values = mutableMapOf<String, String>()

            obj.entrySet().forEach { (key, value) ->
                if (key.startsWith("@")) {
                    values[key] = value.asString
                }
            }

            return EventResponse(
                eventId = obj.get("EventId")?.asInt ?: 0,
                shiftHeld = obj.get("ShiftHeld")?.asBoolean ?: false,
                values = values
            )
        }
    }
}