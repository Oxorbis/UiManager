package cz.creeperface.hytale.uimanager.special

import cz.creeperface.hytale.uimanager.BaseUiNode
import cz.creeperface.hytale.uimanager.ChildNodeBuilder
import cz.creeperface.hytale.uimanager.IdGenerator
import cz.creeperface.hytale.uimanager.UiNode
import cz.creeperface.hytale.uimanager.UiNodeWithChildren
import cz.creeperface.hytale.uimanager.enum.LayoutMode
import cz.creeperface.hytale.uimanager.property.rebindable
import cz.creeperface.hytale.uimanager.type.UiAnchor
import cz.creeperface.hytale.uimanager.type.UiPadding
import cz.creeperface.hytale.uimanager.type.UiPatchStyle
import cz.creeperface.hytale.uimanager.type.UiScrollbarStyle
import cz.creeperface.hytale.uimanager.type.UiTextTooltipStyle

class UiListGroup(
    override var id: String? = null,
    omitName: Boolean = false,
    anchor: UiAnchor? = null,
    background: UiPatchStyle? = null,
    flexWeight: Int? = null,
    hitTestVisible: Boolean? = null,
    layoutMode: LayoutMode? = null,
    padding: UiPadding? = null,
    scrollbarStyle: UiScrollbarStyle? = null,
    textTooltipShowDelay: Double? = null,
    textTooltipStyle: UiTextTooltipStyle? = null,
    tooltipText: String? = null,
    visible: Boolean? = null,
) : BaseUiNode(),
    UiNodeWithChildren,
    ChildNodeBuilder {

    init {
        this.anchor = anchor
        this.background = background
        this.flexWeight = flexWeight
        this.hitTestVisible = hitTestVisible
        this.padding = padding
        this.textTooltipShowDelay = textTooltipShowDelay
        this.textTooltipStyle = textTooltipStyle
        this.tooltipText = tooltipText
        this.visible = visible
    }

    override var omitName: Boolean by rebindable(omitName)

    public var layoutMode: LayoutMode? by rebindable(layoutMode)

    public var scrollbarStyle: UiScrollbarStyle? by rebindable(scrollbarStyle)

    override var children: MutableList<UiNode> = mutableListOf()

    override fun addNode(node: UiNode) {
        addNodeToChildren(node)
    }

    override val isDirty: Boolean
        get() {
            var dirty = super.isDirty
            if (!dirty) dirty = children.any { it.isDirty }
            return dirty
        }

    override fun resetDirty() {
        super.resetDirty()
        children.forEach { it.resetDirty() }
    }

    override fun markDirty() {
        super.markDirty()
    }

    override fun clone(): UiNode {
        val clone = UiListGroup()
        cloneBaseProperties(clone)
        clone.layoutMode = this.layoutMode
        clone.scrollbarStyle = this.scrollbarStyle
        this.children.forEach { child ->
            clone.children.add(child.clone())
        }
        return clone
    }

    public companion object {
        public const val NODE_NAME: String = "Group"
    }
}

public fun ChildNodeBuilder.listGroup(`init`: UiListGroup.() -> Unit): UiListGroup {
    val node = UiListGroup()
    val prefix = if (this is UiNode) (this.id ?: "Node") else "Node"
    node.id = IdGenerator.getNext(prefix + "ListGroup")
    node.init()
    this.addNode(node)
    return node
}

public fun listGroup(`init`: UiListGroup.() -> Unit): UiListGroup {
    val node = UiListGroup()
    node.id = IdGenerator.getNext("ListGroup")
    node.init()
    return node
}
