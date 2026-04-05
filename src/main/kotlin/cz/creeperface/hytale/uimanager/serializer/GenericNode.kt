package cz.creeperface.hytale.uimanager.serializer

import com.hypixel.hytale.server.core.Message

data class GenericNode(
    var name: String,
    val id: String? = null,
    val listNode: Boolean = false,
    val source: Any? = null
) {
    val properties = sortedMapOf<String, Any>()
    val children = mutableListOf<GenericNode>()

    fun copyNodeRecursive(): GenericNode {
        val copy = GenericNode(name, id, listNode, source)
        properties.forEach { (k, v) -> copy.properties[k] = v }
        children.forEach { copy.children.add(it.copyNodeRecursive()) }
        return copy
    }

    data class Identifier(val value: String) {
        override fun toString() = value
    }

    data class MessageValue(val message: Message, val serialized: Any) {
        override fun toString() = serialized.toString()

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is MessageValue) return false
            return message.formattedMessage == other.message.formattedMessage
        }

        override fun hashCode() = message.formattedMessage.hashCode()
    }
}
