package cz.creeperface.hytale.uimanager.node

import cz.creeperface.hytale.uimanager.BaseUiNode
import cz.creeperface.hytale.uimanager.Color
import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiNode
import cz.creeperface.hytale.uimanager.`enum`.ColorFormat
import cz.creeperface.hytale.uimanager.`property`.rebindable
import cz.creeperface.hytale.uimanager.type.UiColorPickerStyle
import kotlin.Boolean
import kotlin.String

public open class UiColorPicker(
  @ExcludeProperty
  override var id: String? = null,
  omitName: Boolean = false,
  displayTextField: Boolean? = null,
  format: ColorFormat? = null,
  resetTransparencyWhenChangingColor: Boolean? = null,
  style: UiColorPickerStyle? = null,
  `value`: Color? = null,
) : BaseUiNode() {
  @ExcludeProperty
  override var omitName: Boolean by rebindable(omitName)

  public var displayTextField: Boolean? by rebindable(displayTextField)

  public var format: ColorFormat? by rebindable(format)

  public var resetTransparencyWhenChangingColor: Boolean? by
      rebindable(resetTransparencyWhenChangingColor)

  public var style: UiColorPickerStyle? by rebindable(style)

  public var `value`: Color? by rebindable(value)

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
    val clone = UiColorPicker()
    cloneBaseProperties(clone)
    clone.displayTextField = this.displayTextField
    clone.format = this.format
    clone.resetTransparencyWhenChangingColor = this.resetTransparencyWhenChangingColor
    clone.style = this.style
    clone.value = this.value
    return clone
  }

  public companion object {
    public const val NODE_NAME: String = "ColorPicker"
  }
}
