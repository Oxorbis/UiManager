package cz.creeperface.hytale.uimanager.node

import cz.creeperface.hytale.uimanager.BaseUiNode
import cz.creeperface.hytale.uimanager.Color
import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiNode
import cz.creeperface.hytale.uimanager.`property`.rebindable
import kotlin.Boolean
import kotlin.Double
import kotlin.String

public open class UiCircularProgressBar(
  @ExcludeProperty
  override var id: String? = null,
  omitName: Boolean = false,
  color: Color? = null,
  `value`: Double? = null,
) : BaseUiNode() {
  @ExcludeProperty
  override var omitName: Boolean by rebindable(omitName)

  public var color: Color? by rebindable(color)

  public var `value`: Double? by rebindable(value)

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
    val clone = UiCircularProgressBar()
    cloneBaseProperties(clone)
    clone.color = this.color
    clone.value = this.value
    return clone
  }

  public companion object {
    public const val NODE_NAME: String = "CircularProgressBar"
  }
}
