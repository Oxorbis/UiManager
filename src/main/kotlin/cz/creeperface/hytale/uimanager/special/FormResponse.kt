package cz.creeperface.hytale.uimanager.special

import com.google.gson.*
import java.lang.reflect.Type

class FormResponse(
    val formIndex: Int,
    val formSubmitterIndex: Int,
    val shiftHeld: Boolean,
    val values: Map<String, String>
) {
    companion object {
        val GSON: Gson = GsonBuilder()
            .registerTypeAdapter(FormResponse::class.java, FormResponseDeserializer())
            .create()

        fun fromJson(json: String): FormResponse {
            return GSON.fromJson(json, FormResponse::class.java)
        }

        fun fromJson(json: JsonElement): FormResponse {
            return GSON.fromJson(json, FormResponse::class.java)
        }
    }

    private class FormResponseDeserializer : JsonDeserializer<FormResponse> {
        override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): FormResponse {
            val obj = json.asJsonObject
            val values = mutableMapOf<String, String>()

            obj.entrySet().forEach { (key, value) ->
                if (key.startsWith("@Value")) {
                    values[key] = value.asString
                }
            }

            return FormResponse(
                formIndex = obj.get("FormIndex")?.asInt ?: 0,
                formSubmitterIndex = obj.get("FormSubmitterIndex")?.asInt ?: 0,
                shiftHeld = obj.get("ShiftHeld")?.asBoolean ?: false,
                values = values
            )
        }
    }
}