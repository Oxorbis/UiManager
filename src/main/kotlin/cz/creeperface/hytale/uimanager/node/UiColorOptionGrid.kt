package cz.creeperface.hytale.uimanager.node

import cz.creeperface.hytale.uimanager.BaseUiNode
import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiNode
import cz.creeperface.hytale.uimanager.`property`.rebindable
import cz.creeperface.hytale.uimanager.type.UiColorOptionGridStyle
import kotlin.Boolean
import kotlin.Int
import kotlin.String

public open class UiColorOptionGrid(
  @ExcludeProperty
  override var id: String? = null,
  omitName: Boolean = false,
  colorsPerRow: Int? = null,
  style: UiColorOptionGridStyle? = null,
) : BaseUiNode() {
  @ExcludeProperty
  override var omitName: Boolean by rebindable(omitName)

  public var colorsPerRow: Int? by rebindable(colorsPerRow)

  public var style: UiColorOptionGridStyle? by rebindable(style)

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
    val clone = UiColorOptionGrid()
    cloneBaseProperties(clone)
    clone.colorsPerRow = this.colorsPerRow
    clone.style = this.style
    return clone
  }

  public companion object {
    public const val NODE_NAME: String = "ColorOptionGrid"
  }
}
