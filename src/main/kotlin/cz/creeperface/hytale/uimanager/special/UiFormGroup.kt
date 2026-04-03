package cz.creeperface.hytale.uimanager.special

import com.hypixel.hytale.server.core.Message
import cz.creeperface.hytale.uimanager.*
import cz.creeperface.hytale.uimanager.enum.LayoutMode
import cz.creeperface.hytale.uimanager.property.rebindable
import cz.creeperface.hytale.uimanager.type.*

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
    tooltipText: Message? = null,
    visible: Boolean? = null,
) : UiFormContext<T>, BaseUiNode(),
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

    @get:ExcludeProperty
    override var omitName: Boolean by rebindable(omitName)

    public var layoutMode: LayoutMode? by rebindable(layoutMode)

    public var scrollbarStyle: UiScrollbarStyle? by rebindable(scrollbarStyle)

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
