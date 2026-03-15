package cz.creeperface.hytale.uimanager.special

import cz.creeperface.hytale.uimanager.BaseUiNode
import cz.creeperface.hytale.uimanager.ChildNodeBuilder
import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiNode
import cz.creeperface.hytale.uimanager.UiNodeWithChildren
import cz.creeperface.hytale.uimanager.enum.LayoutMode
import cz.creeperface.hytale.uimanager.node.UiGroup
import cz.creeperface.hytale.uimanager.property.rebindable
import cz.creeperface.hytale.uimanager.type.UiAnchor
import cz.creeperface.hytale.uimanager.type.UiPadding
import cz.creeperface.hytale.uimanager.type.UiPatchStyle
import cz.creeperface.hytale.uimanager.type.UiScrollbarStyle
import cz.creeperface.hytale.uimanager.type.UiTextTooltipStyle
import kotlin.collections.forEach

class UiFormGroup<T: Any>(
    @ExcludeProperty
    override val form: UiForm<T>,
    @ExcludeProperty
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
) : UiFormContext<T>, BaseUiNode(),
    UiNodeWithChildren,
    ChildNodeBuilder {
    @get:ExcludeProperty
    override var omitName: Boolean by rebindable(omitName)

    public var anchor: UiAnchor? by rebindable(anchor)

    public var background: UiPatchStyle? by rebindable(background)

    public var flexWeight: Int? by rebindable(flexWeight)

    public var hitTestVisible: Boolean? by rebindable(hitTestVisible)

    public var layoutMode: LayoutMode? by rebindable(layoutMode)

    public var padding: UiPadding? by rebindable(padding)

    public var scrollbarStyle: UiScrollbarStyle? by rebindable(scrollbarStyle)

    public var textTooltipShowDelay: Double? by rebindable(textTooltipShowDelay)

    public var textTooltipStyle: UiTextTooltipStyle? by rebindable(textTooltipStyle)

    public var tooltipText: String? by rebindable(tooltipText)

    public var visible: Boolean? by rebindable(visible)

    @ExcludeProperty
    override var children: MutableList<UiNode> = mutableListOf()

    override fun addNode(node: UiNode) {
        addNodeToChildren(node)
    }

    @ExcludeProperty
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
        val clone = UiFormGroup(form)
        clone.id = this.id
        clone.omitName = this.omitName
        clone.anchor = this.anchor
        clone.background = this.background
        clone.flexWeight = this.flexWeight
        clone.hitTestVisible = this.hitTestVisible
        clone.layoutMode = this.layoutMode
        clone.padding = this.padding
        clone.scrollbarStyle = this.scrollbarStyle
        clone.textTooltipShowDelay = this.textTooltipShowDelay
        clone.textTooltipStyle = this.textTooltipStyle
        clone.tooltipText = this.tooltipText
        clone.visible = this.visible
        this.children.forEach { child ->
            clone.children.add(child.clone())
        }
        return clone
    }

    public companion object {
        public const val NODE_NAME: String = "Group"
    }
}