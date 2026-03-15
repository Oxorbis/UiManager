package cz.creeperface.hytale.uimanager.type

import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiDsl
import cz.creeperface.hytale.uimanager.UiType
import kotlin.Int
import kotlin.Unit
import kotlin.collections.MutableList

@UiDsl
public data class UiColorPickerStyle(
  public var buttonBackground: UiPatchStyle? = null,
  public var buttonFill: UiPatchStyle? = null,
  public var opacitySelectorBackground: UiPatchStyle? = null,
  public var textFieldDecoration: UiInputFieldDecorationStyle? = null,
  public var textFieldHeight: Int? = null,
  public var textFieldInputStyle: UiInputFieldStyle? = null,
  public var textFieldPadding: UiPadding? = null,
) : UiType {
  @ExcludeProperty
  override val templates: MutableList<UiType> = mutableListOf()
}

@UiDsl
public fun colorPickerStyle(`init`: UiColorPickerStyle.() -> Unit): UiColorPickerStyle {
  val result = UiColorPickerStyle()
  result.init()
  return result
}
