package cz.creeperface.hytale.uimanager.type

import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiDsl
import cz.creeperface.hytale.uimanager.UiType
import kotlin.Int
import kotlin.Unit
import kotlin.collections.MutableList

@UiDsl
public data class UiColorPickerDropdownBoxStyle(
  public var arrowAnchor: UiAnchor? = null,
  public var arrowBackground: UiColorPickerDropdownBoxStateBackground? = null,
  public var background: UiColorPickerDropdownBoxStateBackground? = null,
  public var colorPickerStyle: UiColorPickerStyle? = null,
  public var overlay: UiColorPickerDropdownBoxStateBackground? = null,
  public var panelBackground: UiPatchStyle? = null,
  public var panelHeight: Int? = null,
  public var panelOffset: Int? = null,
  public var panelPadding: UiPadding? = null,
  public var panelWidth: Int? = null,
  public var sounds: UiButtonSounds? = null,
) : UiType {
  @ExcludeProperty
  override val templates: MutableList<UiType> = mutableListOf()
}

@UiDsl
public fun colorPickerDropdownBoxStyle(`init`: UiColorPickerDropdownBoxStyle.() -> Unit): UiColorPickerDropdownBoxStyle {
  val result = UiColorPickerDropdownBoxStyle()
  result.init()
  return result
}
