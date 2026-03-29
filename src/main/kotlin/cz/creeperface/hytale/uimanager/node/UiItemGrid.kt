package cz.creeperface.hytale.uimanager.node

import cz.creeperface.hytale.uimanager.BaseUiNode
import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiNode
import cz.creeperface.hytale.uimanager.`enum`.ItemGridInfoDisplayMode
import cz.creeperface.hytale.uimanager.`property`.rebindable
import cz.creeperface.hytale.uimanager.type.UiClientItemStack
import cz.creeperface.hytale.uimanager.type.UiItemGridSlot
import cz.creeperface.hytale.uimanager.type.UiItemGridStyle
import cz.creeperface.hytale.uimanager.type.UiScrollbarStyle
import kotlin.Boolean
import kotlin.Int
import kotlin.String

public open class UiItemGrid(
  @ExcludeProperty
  override var id: String? = null,
  omitName: Boolean = false,
  adjacentInfoPaneGridWidth: Int? = null,
  allowMaxStackDraggableItems: Boolean? = null,
  areItemsDraggable: Boolean? = null,
  displayItemQuantity: Boolean? = null,
  infoDisplay: ItemGridInfoDisplayMode? = null,
  inventorySectionId: Int? = null,
  itemStacks: UiClientItemStack? = null,
  renderItemQualityBackground: Boolean? = null,
  scrollbarStyle: UiScrollbarStyle? = null,
  showScrollbar: Boolean? = null,
  slots: UiItemGridSlot? = null,
  slotsPerRow: Int? = null,
  style: UiItemGridStyle? = null,
) : BaseUiNode() {
  @ExcludeProperty
  override var omitName: Boolean by rebindable(omitName)

  public var adjacentInfoPaneGridWidth: Int? by rebindable(adjacentInfoPaneGridWidth)

  public var allowMaxStackDraggableItems: Boolean? by rebindable(allowMaxStackDraggableItems)

  public var areItemsDraggable: Boolean? by rebindable(areItemsDraggable)

  public var displayItemQuantity: Boolean? by rebindable(displayItemQuantity)

  public var infoDisplay: ItemGridInfoDisplayMode? by rebindable(infoDisplay)

  public var inventorySectionId: Int? by rebindable(inventorySectionId)

  public var itemStacks: UiClientItemStack? by rebindable(itemStacks)

  public var renderItemQualityBackground: Boolean? by rebindable(renderItemQualityBackground)

  public var scrollbarStyle: UiScrollbarStyle? by rebindable(scrollbarStyle)

  public var showScrollbar: Boolean? by rebindable(showScrollbar)

  public var slots: UiItemGridSlot? by rebindable(slots)

  public var slotsPerRow: Int? by rebindable(slotsPerRow)

  public var style: UiItemGridStyle? by rebindable(style)

  @ExcludeProperty
  override val isDirty: Boolean
    get() {
      var dirty = super.isDirty
      return dirty
    }

  override fun resetDirty() {
    super.resetDirty()
  }

  override fun clone(): UiNode {
    val clone = UiItemGrid()
    cloneBaseProperties(clone)
    clone.adjacentInfoPaneGridWidth = this.adjacentInfoPaneGridWidth
    clone.allowMaxStackDraggableItems = this.allowMaxStackDraggableItems
    clone.areItemsDraggable = this.areItemsDraggable
    clone.displayItemQuantity = this.displayItemQuantity
    clone.infoDisplay = this.infoDisplay
    clone.inventorySectionId = this.inventorySectionId
    clone.itemStacks = this.itemStacks
    clone.renderItemQualityBackground = this.renderItemQualityBackground
    clone.scrollbarStyle = this.scrollbarStyle
    clone.showScrollbar = this.showScrollbar
    clone.slots = this.slots
    clone.slotsPerRow = this.slotsPerRow
    clone.style = this.style
    return clone
  }

  public companion object {
    public const val NODE_NAME: String = "ItemGrid"
  }
}
