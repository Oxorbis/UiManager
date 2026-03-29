package cz.creeperface.hytale.uimanager.node

import cz.creeperface.hytale.uimanager.BaseUiNode
import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiNode
import cz.creeperface.hytale.uimanager.`property`.rebindable
import cz.creeperface.hytale.uimanager.type.UiAnchor
import cz.creeperface.hytale.uimanager.type.UiInputFieldStyle
import cz.creeperface.hytale.uimanager.type.UiSliderStyle
import kotlin.Boolean
import kotlin.Double
import kotlin.Int
import kotlin.String

public open class UiSliderNumberField(
  @ExcludeProperty
  override var id: String? = null,
  omitName: Boolean = false,
  max: Int? = null,
  min: Int? = null,
  numberFieldContainerAnchor: UiAnchor? = null,
  numberFieldDefaultValue: Double? = null,
  numberFieldMaxDecimalPlaces: Int? = null,
  numberFieldStyle: UiInputFieldStyle? = null,
  numberFieldSuffix: String? = null,
  sliderStyle: UiSliderStyle? = null,
  step: Int? = null,
  `value`: Int? = null,
) : BaseUiNode() {
  @ExcludeProperty
  override var omitName: Boolean by rebindable(omitName)

  public var max: Int? by rebindable(max)

  public var min: Int? by rebindable(min)

  public var numberFieldContainerAnchor: UiAnchor? by rebindable(numberFieldContainerAnchor)

  public var numberFieldDefaultValue: Double? by rebindable(numberFieldDefaultValue)

  public var numberFieldMaxDecimalPlaces: Int? by rebindable(numberFieldMaxDecimalPlaces)

  public var numberFieldStyle: UiInputFieldStyle? by rebindable(numberFieldStyle)

  public var numberFieldSuffix: String? by rebindable(numberFieldSuffix)

  public var sliderStyle: UiSliderStyle? by rebindable(sliderStyle)

  public var step: Int? by rebindable(step)

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
    val clone = UiSliderNumberField()
    cloneBaseProperties(clone)
    clone.max = this.max
    clone.min = this.min
    clone.numberFieldContainerAnchor = this.numberFieldContainerAnchor
    clone.numberFieldDefaultValue = this.numberFieldDefaultValue
    clone.numberFieldMaxDecimalPlaces = this.numberFieldMaxDecimalPlaces
    clone.numberFieldStyle = this.numberFieldStyle
    clone.numberFieldSuffix = this.numberFieldSuffix
    clone.sliderStyle = this.sliderStyle
    clone.step = this.step
    clone.value = this.value
    return clone
  }

  public companion object {
    public const val NODE_NAME: String = "SliderNumberField"
  }
}
