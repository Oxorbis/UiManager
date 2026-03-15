package cz.creeperface.hytale.uimanager.node

import com.hypixel.hytale.server.core.Message
import com.hypixel.hytale.server.core.ui.DropdownEntryInfo
import cz.creeperface.hytale.uimanager.BaseUiNode
import cz.creeperface.hytale.uimanager.ChildNodeBuilder
import cz.creeperface.hytale.uimanager.Color
import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiNode
import cz.creeperface.hytale.uimanager.UiNodeWithChildren
import cz.creeperface.hytale.uimanager.`enum`.MouseWheelScrollBehaviourType
import cz.creeperface.hytale.uimanager.`property`.rebindable
import cz.creeperface.hytale.uimanager.type.UiAnchor
import cz.creeperface.hytale.uimanager.type.UiDropdownBoxStyle
import cz.creeperface.hytale.uimanager.type.UiPadding
import cz.creeperface.hytale.uimanager.type.UiPatchStyle
import cz.creeperface.hytale.uimanager.type.UiTextTooltipStyle
import kotlin.Boolean
import kotlin.Double
import kotlin.Int
import kotlin.String
import kotlin.collections.List
import kotlin.collections.MutableList

public open class UiDropdownBox(
  @ExcludeProperty
  override var id: String? = null,
  omitName: Boolean = false,
  anchor: UiAnchor? = null,
  autoScrollDown: Boolean? = null,
  background: UiPatchStyle? = null,
  contentHeight: Int? = null,
  contentWidth: Int? = null,
  disabled: Boolean? = null,
  displayNonExistingValue: Boolean? = null,
  entries: List<DropdownEntryInfo?>? = null,
  flexWeight: Int? = null,
  forcedLabel: String? = null,
  hitTestVisible: Boolean? = null,
  isReadOnly: Boolean? = null,
  keepScrollPosition: Boolean? = null,
  maskTexturePath: String? = null,
  maxSelection: Int? = null,
  mouseWheelScrollBehaviour: MouseWheelScrollBehaviourType? = null,
  noItemsText: String? = null,
  outlineColor: Color? = null,
  outlineSize: Double? = null,
  overscroll: Boolean? = null,
  padding: UiPadding? = null,
  panelTitleText: String? = null,
  showLabel: Boolean? = null,
  showSearchInput: Boolean? = null,
  style: UiDropdownBoxStyle? = null,
  textTooltipShowDelay: Double? = null,
  textTooltipStyle: UiTextTooltipStyle? = null,
  tooltipText: String? = null,
  tooltipTextSpans: Message? = null,
  `value`: String? = null,
  visible: Boolean? = null,
) : BaseUiNode(),
    UiNodeWithChildren,
    ChildNodeBuilder {
  @ExcludeProperty
  override var omitName: Boolean by rebindable(omitName)

  public var anchor: UiAnchor? by rebindable(anchor)

  public var autoScrollDown: Boolean? by rebindable(autoScrollDown)

  public var background: UiPatchStyle? by rebindable(background)

  public var contentHeight: Int? by rebindable(contentHeight)

  public var contentWidth: Int? by rebindable(contentWidth)

  public var disabled: Boolean? by rebindable(disabled)

  public var displayNonExistingValue: Boolean? by rebindable(displayNonExistingValue)

  public var entries: List<DropdownEntryInfo?>? by rebindable(entries)

  public var flexWeight: Int? by rebindable(flexWeight)

  public var forcedLabel: String? by rebindable(forcedLabel)

  public var hitTestVisible: Boolean? by rebindable(hitTestVisible)

  public var isReadOnly: Boolean? by rebindable(isReadOnly)

  public var keepScrollPosition: Boolean? by rebindable(keepScrollPosition)

  public var maskTexturePath: String? by rebindable(maskTexturePath)

  public var maxSelection: Int? by rebindable(maxSelection)

  public var mouseWheelScrollBehaviour: MouseWheelScrollBehaviourType? by
      rebindable(mouseWheelScrollBehaviour)

  public var noItemsText: String? by rebindable(noItemsText)

  public var outlineColor: Color? by rebindable(outlineColor)

  public var outlineSize: Double? by rebindable(outlineSize)

  public var overscroll: Boolean? by rebindable(overscroll)

  public var padding: UiPadding? by rebindable(padding)

  public var panelTitleText: String? by rebindable(panelTitleText)

  public var showLabel: Boolean? by rebindable(showLabel)

  public var showSearchInput: Boolean? by rebindable(showSearchInput)

  public var style: UiDropdownBoxStyle? by rebindable(style)

  public var textTooltipShowDelay: Double? by rebindable(textTooltipShowDelay)

  public var textTooltipStyle: UiTextTooltipStyle? by rebindable(textTooltipStyle)

  public var tooltipText: String? by rebindable(tooltipText)

  public var tooltipTextSpans: Message? by rebindable(tooltipTextSpans)

  public var `value`: String? by rebindable(value)

  public var visible: Boolean? by rebindable(visible)

  @ExcludeProperty
  override val children: MutableList<UiNode> = mutableListOf()

  @ExcludeProperty
  override val isDirty: Boolean
    get() {
      var dirty = super.isDirty
      if (!dirty) dirty = children.any { it.isDirty }
      return dirty
    }

  override fun addNode(node: UiNode) {
    addNodeToChildren(node)
  }

  override fun resetDirty() {
    super.resetDirty()
    children.forEach { it.resetDirty() }
  }

  override fun clone(): UiNode {
    val clone = UiDropdownBox()
    clone.id = this.id
    clone.omitName = this.omitName
    clone.anchor = this.anchor
    clone.autoScrollDown = this.autoScrollDown
    clone.background = this.background
    clone.contentHeight = this.contentHeight
    clone.contentWidth = this.contentWidth
    clone.disabled = this.disabled
    clone.displayNonExistingValue = this.displayNonExistingValue
    clone.entries = this.entries
    clone.flexWeight = this.flexWeight
    clone.forcedLabel = this.forcedLabel
    clone.hitTestVisible = this.hitTestVisible
    clone.isReadOnly = this.isReadOnly
    clone.keepScrollPosition = this.keepScrollPosition
    clone.maskTexturePath = this.maskTexturePath
    clone.maxSelection = this.maxSelection
    clone.mouseWheelScrollBehaviour = this.mouseWheelScrollBehaviour
    clone.noItemsText = this.noItemsText
    clone.outlineColor = this.outlineColor
    clone.outlineSize = this.outlineSize
    clone.overscroll = this.overscroll
    clone.padding = this.padding
    clone.panelTitleText = this.panelTitleText
    clone.showLabel = this.showLabel
    clone.showSearchInput = this.showSearchInput
    clone.style = this.style
    clone.textTooltipShowDelay = this.textTooltipShowDelay
    clone.textTooltipStyle = this.textTooltipStyle
    clone.tooltipText = this.tooltipText
    clone.tooltipTextSpans = this.tooltipTextSpans
    clone.value = this.value
    clone.visible = this.visible
    this.children.forEach { child ->
      clone.children.add(child.clone())
    }
    return clone
  }

  public companion object {
    public const val NODE_NAME: String = "DropdownBox"
  }
}
