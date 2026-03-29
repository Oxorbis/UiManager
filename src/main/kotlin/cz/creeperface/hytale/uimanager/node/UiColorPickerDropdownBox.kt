package cz.creeperface.hytale.uimanager.node

import cz.creeperface.hytale.uimanager.BaseUiNode
import cz.creeperface.hytale.uimanager.Color
import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiNode
import cz.creeperface.hytale.uimanager.`enum`.ColorFormat
import cz.creeperface.hytale.uimanager.`property`.rebindable
import cz.creeperface.hytale.uimanager.type.UiColorPickerDropdownBoxStyle
import kotlin.Boolean
import kotlin.String

public open class UiColorPickerDropdownBox(
  @ExcludeProperty
  override var id: String? = null,
  omitName: Boolean = false,
  color: Color? = null,
  displayTextField: Boolean? = null,
  format: ColorFormat? = null,
  isReadOnly: Boolean? = null,
  resetTransparencyWhenChangingColor: Boolean? = null,
  style: UiColorPickerDropdownBoxStyle? = null,
) : BaseUiNode() {
  @ExcludeProperty
  override var omitName: Boolean by rebindable(omitName)

  public var color: Color? by rebindable(color)

  public var displayTextField: Boolean? by rebindable(displayTextField)

  public var format: ColorFormat? by rebindable(format)

  public var isReadOnly: Boolean? by rebindable(isReadOnly)

  public var resetTransparencyWhenChangingColor: Boolean? by
      rebindable(resetTransparencyWhenChangingColor)

  public var style: UiColorPickerDropdownBoxStyle? by rebindable(style)

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
    val clone = UiColorPickerDropdownBox()
    cloneBaseProperties(clone)
    clone.color = this.color
    clone.displayTextField = this.displayTextField
    clone.format = this.format
    clone.isReadOnly = this.isReadOnly
    clone.resetTransparencyWhenChangingColor = this.resetTransparencyWhenChangingColor
    clone.style = this.style
    return clone
  }

  public companion object {
    public const val NODE_NAME: String = "ColorPickerDropdownBox"
  }
}
