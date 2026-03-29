package cz.creeperface.hytale.uimanager.node

import cz.creeperface.hytale.uimanager.BaseUiNode
import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiNode
import cz.creeperface.hytale.uimanager.`property`.rebindable
import cz.creeperface.hytale.uimanager.type.UiBlockSelectorStyle
import kotlin.Boolean
import kotlin.Int
import kotlin.String

public open class UiBlockSelector(
  @ExcludeProperty
  override var id: String? = null,
  omitName: Boolean = false,
  capacity: Int? = null,
  style: UiBlockSelectorStyle? = null,
  `value`: String? = null,
) : BaseUiNode() {
  @ExcludeProperty
  override var omitName: Boolean by rebindable(omitName)

  public var capacity: Int? by rebindable(capacity)

  public var style: UiBlockSelectorStyle? by rebindable(style)

  public var `value`: String? by rebindable(value)

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
    val clone = UiBlockSelector()
    cloneBaseProperties(clone)
    clone.capacity = this.capacity
    clone.style = this.style
    clone.value = this.value
    return clone
  }

  public companion object {
    public const val NODE_NAME: String = "BlockSelector"
  }
}
