package cz.creeperface.hytale.uimanager.type

import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiDsl
import cz.creeperface.hytale.uimanager.UiType
import kotlin.Unit
import kotlin.collections.MutableList

@UiDsl
public data class UiColorPickerDropdownBoxStateBackground(
  public var default: UiPatchStyle? = null,
  public var hovered: UiPatchStyle? = null,
  public var pressed: UiPatchStyle? = null,
) : UiType {
  @ExcludeProperty
  override val templates: MutableList<UiType> = mutableListOf()
}

@UiDsl
public fun colorPickerDropdownBoxStateBackground(`init`: UiColorPickerDropdownBoxStateBackground.() -> Unit): UiColorPickerDropdownBoxStateBackground {
  val result = UiColorPickerDropdownBoxStateBackground()
  result.init()
  return result
}
