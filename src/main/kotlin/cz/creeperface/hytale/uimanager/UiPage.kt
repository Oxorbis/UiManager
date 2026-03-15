package cz.creeperface.hytale.uimanager

import cz.creeperface.hytale.uimanager.special.UiForm

@UiDsl
class UiPage : ChildNodeBuilder {
    override val children = mutableListOf<UiNode>()
    val nodes get() = children
    val variables = mutableMapOf<String, Any>()

    override fun addNode(node: UiNode) {
        addNodeToChildren(node)
    }

    internal val eventVariableBindings = mutableMapOf<String, Any>()

    fun variable(name: String, value: Any) {
        variables[name] = value
    }

    val isDirty: Boolean
        get() = nodes.any { it.isDirty }

    fun resetDirty() {
        nodes.forEach { it.resetDirty() }
    }

    fun clone(): UiPage {
        val clone = UiPage()
        clone.variables.putAll(this.variables)
        this.nodes.forEach { node ->
            clone.nodes.add(node.clone())
        }
        return clone
    }

    class EventVariableBinding(

    )
}
