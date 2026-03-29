package cz.creeperface.hytale.uimanager.node

import cz.creeperface.hytale.uimanager.BaseUiNode
import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiNode
import cz.creeperface.hytale.uimanager.`property`.rebindable
import cz.creeperface.hytale.uimanager.type.UiSliderStyle
import kotlin.Boolean
import kotlin.Int
import kotlin.String

public open class UiSlider(
  @ExcludeProperty
  override var id: String? = null,
  omitName: Boolean = false,
  isReadOnly: Boolean? = null,
  max: Int? = null,
  min: Int? = null,
  step: Int? = null,
  style: UiSliderStyle? = null,
  `value`: Int? = null,
) : BaseUiNode() {
  @ExcludeProperty
  override var omitName: Boolean by rebindable(omitName)

  public var isReadOnly: Boolean? by rebindable(isReadOnly)

  public var max: Int? by rebindable(max)

  public var min: Int? by rebindable(min)

  public var step: Int? by rebindable(step)

  public var style: UiSliderStyle? by rebindable(style)

  public var `value`: Int? by rebindable(value)

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
    val clone = UiSlider()
    cloneBaseProperties(clone)
    clone.isReadOnly = this.isReadOnly
    clone.max = this.max
    clone.min = this.min
    clone.step = this.step
    clone.style = this.style
    clone.value = this.value
    return clone
  }

  public companion object {
    public const val NODE_NAME: String = "Slider"
  }
}
