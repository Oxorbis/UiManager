package cz.creeperface.hytale.uimanager.node

import cz.creeperface.hytale.uimanager.BaseUiNode
import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiNode
import cz.creeperface.hytale.uimanager.`property`.rebindable
import kotlin.Boolean
import kotlin.String

public open class UiItemIcon(
  @ExcludeProperty
  override var id: String? = null,
  omitName: Boolean = false,
  itemId: String? = null,
  showItemTooltip: Boolean? = null,
) : BaseUiNode() {
  @ExcludeProperty
  override var omitName: Boolean by rebindable(omitName)

  public var itemId: String? by rebindable(itemId)

  public var showItemTooltip: Boolean? by rebindable(showItemTooltip)

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
    val clone = UiItemIcon()
    cloneBaseProperties(clone)
    clone.itemId = this.itemId
    clone.showItemTooltip = this.showItemTooltip
    return clone
  }

  public companion object {
    public const val NODE_NAME: String = "ItemIcon"
  }
}
