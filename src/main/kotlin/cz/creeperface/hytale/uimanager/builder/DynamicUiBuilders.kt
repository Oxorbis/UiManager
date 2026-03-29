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
    this.nodeListener = { node ->
        node.visible = condition
//        if (!condition) {
//            node.visible = false
//        }
    }

    trueBuilder()

    this.nodeListener = { node ->
        node.visible = !condition
//        if (condition) {
//            node.visible = false
//        }
    }

    falseBuilder()

    this.nodeListener = null
}

inline fun <T : ChildNodeBuilder, D> T.listBlock(
    data: List<D?>,
    maxItems: Int,
    builder: T.(D?) -> Unit,
) {
    for (i in 0 until maxItems) {
        val item = data.getOrNull(i)

        this.nodeListener = { node ->
            node.visible = item != null
//            if (item == null) {
//                node.visible = false
//            }
        }

        builder(item)

        this.nodeListener = null
    }
}