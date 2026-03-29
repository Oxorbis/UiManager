package cz.creeperface.hytale.uimanager.node

import cz.creeperface.hytale.uimanager.BaseUiNode
import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiNode
import cz.creeperface.hytale.uimanager.`property`.rebindable
import kotlin.Boolean
import kotlin.Int
import kotlin.String

public open class UiItemSlot(
  @ExcludeProperty
  override var id: String? = null,
  omitName: Boolean = false,
  itemId: String? = null,
  quantity: Int? = null,
  showDurabilityBar: Boolean? = null,
  showQualityBackground: Boolean? = null,
  showQuantity: Boolean? = null,
) : BaseUiNode() {
  @ExcludeProperty
  override var omitName: Boolean by rebindable(omitName)

  public var itemId: String? by rebindable(itemId)

  public var quantity: Int? by rebindable(quantity)

  public var showDurabilityBar: Boolean? by rebindable(showDurabilityBar)

  public var showQualityBackground: Boolean? by rebindable(showQualityBackground)

  public var showQuantity: Boolean? by rebindable(showQuantity)

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
    val clone = UiItemSlot()
    cloneBaseProperties(clone)
    clone.itemId = this.itemId
    clone.quantity = this.quantity
    clone.showDurabilityBar = this.showDurabilityBar
    clone.showQualityBackground = this.showQualityBackground
    clone.showQuantity = this.showQuantity
    return clone
  }

  public companion object {
    public const val NODE_NAME: String = "ItemSlot"
  }
}
