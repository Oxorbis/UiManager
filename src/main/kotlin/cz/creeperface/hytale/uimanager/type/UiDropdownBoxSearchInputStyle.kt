package cz.creeperface.hytale.uimanager.type

import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiDsl
import cz.creeperface.hytale.uimanager.UiType
import kotlin.String
import kotlin.Unit
import kotlin.collections.MutableList

@UiDsl
public data class UiDropdownBoxSearchInputStyle(
  public var anchor: UiAnchor? = null,
  public var background: UiPatchStyle? = null,
  public var clearButtonStyle: UiInputFieldButtonStyle? = null,
  public var icon: UiInputFieldIcon? = null,
  public var padding: UiPadding? = null,
  public var placeholderStyle: UiInputFieldStyle? = null,
  public var placeholderText: String? = null,
  public var style: UiInputFieldStyle? = null,
) : UiType {
  @ExcludeProperty
  override val templates: MutableList<UiType> = mutableListOf()
}

@UiDsl
public fun dropdownBoxSearchInputStyle(`init`: UiDropdownBoxSearchInputStyle.() -> Unit): UiDropdownBoxSearchInputStyle {
  val result = UiDropdownBoxSearchInputStyle()
  result.init()
  return result
}
