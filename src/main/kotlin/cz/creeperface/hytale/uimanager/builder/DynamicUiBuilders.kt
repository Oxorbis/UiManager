package cz.creeperface.hytale.uimanager.builder

import cz.creeperface.hytale.uimanager.ChildNodeBuilder

inline fun <T : ChildNodeBuilder> T.conditionalBlock(
    condition: Boolean,
    builder: T.() -> Unit,
) {
    conditionalBlock(condition, builder, builder)
}

inline fun <T : ChildNodeBuilder> T.conditionalBlock(
    condition: Boolean,
    trueBuilder: T.() -> Unit,
    falseBuilder: T.() -> Unit
) {
    val previousListener = this.nodeListener

    this.nodeListener = { node ->
        previousListener?.invoke(node)
        node.visible = condition
    }

    trueBuilder()

    this.nodeListener = { node ->
        previousListener?.invoke(node)
        node.visible = !condition
    }

    falseBuilder()

    this.nodeListener = previousListener
}

inline fun <T : ChildNodeBuilder, D> T.listBlock(
    data: List<D?>,
    maxItems: Int,
    builder: T.(D?, Int) -> Unit,
) {
    val previousListener = this.nodeListener

    for (i in 0 until maxItems) {
        val item = data.getOrNull(i)

        this.nodeListener = { node ->
            previousListener?.invoke(node)
            node.visible = item != null
        }

        builder(item, i)
    }

    this.nodeListener = previousListener
}