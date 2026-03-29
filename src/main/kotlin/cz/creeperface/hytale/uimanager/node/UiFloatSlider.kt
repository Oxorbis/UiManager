package cz.creeperface.hytale.uimanager.node

import cz.creeperface.hytale.uimanager.BaseUiNode
import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiNode
import cz.creeperface.hytale.uimanager.`property`.rebindable
import cz.creeperface.hytale.uimanager.type.UiSliderStyle
import kotlin.Boolean
import kotlin.Double
import kotlin.String

public open class UiFloatSlider(
  @ExcludeProperty
  override var id: String? = null,
  omitName: Boolean = false,
  max: Double? = null,
  min: Double? = null,
  step: Double? = null,
  style: UiSliderStyle? = null,
  `value`: Double? = null,
) : BaseUiNode() {
  @ExcludeProperty
  override var omitName: Boolean by rebindable(omitName)

  public var max: Double? by rebindable(max)

  public var min: Double? by rebindable(min)

  public var step: Double? by rebindable(step)

  public var style: UiSliderStyle? by rebindable(style)

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
    val clone = UiFloatSlider()
    cloneBaseProperties(clone)
    clone.max = this.max
    clone.min = this.min
    clone.step = this.step
    clone.style = this.style
    clone.value = this.value
    return clone
  }

  public companion object {
    public const val NODE_NAME: String = "FloatSlider"
  }
}
