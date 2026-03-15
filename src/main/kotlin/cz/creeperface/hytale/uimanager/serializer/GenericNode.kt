package cz.creeperface.hytale.uimanager.serializer

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
}
