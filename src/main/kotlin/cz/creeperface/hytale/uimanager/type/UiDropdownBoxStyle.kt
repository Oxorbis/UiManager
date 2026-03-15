package cz.creeperface.hytale.uimanager.type

import cz.creeperface.hytale.uimanager.Color
import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiDsl
import cz.creeperface.hytale.uimanager.UiType
import cz.creeperface.hytale.uimanager.`enum`.DropdownBoxAlign
import kotlin.Int
import kotlin.String
import kotlin.Unit
import kotlin.collections.MutableList

@UiDsl
public data class UiDropdownBoxStyle(
  public var arrowHeight: Int? = null,
  public var arrowWidth: Int? = null,
  public var defaultArrowTexturePath: String? = null,
  public var defaultBackground: UiPatchStyle? = null,
  public var disabledArrowTexturePath: String? = null,
  public var disabledBackground: UiPatchStyle? = null,
  public var disabledLabelStyle: UiLabelStyle? = null,
  public var entriesInViewport: Int? = null,
  public var entryHeight: Int? = null,
  public var entryIconBackground: UiPatchStyle? = null,
  public var entryIconHeight: Int? = null,
  public var entryIconWidth: Int? = null,
  public var entryLabelStyle: UiLabelStyle? = null,
  public var entrySounds: UiButtonSounds? = null,
  public var focusOutlineColor: Color? = null,
  public var focusOutlineSize: Int? = null,
  public var horizontalEntryPadding: Int? = null,
  public var horizontalPadding: Int? = null,
  public var hoveredArrowTexturePath: String? = null,
  public var hoveredBackground: UiPatchStyle? = null,
  public var hoveredEntryBackground: UiPatchStyle? = null,
  public var iconHeight: Int? = null,
  public var iconTexturePath: String? = null,
  public var iconWidth: Int? = null,
  public var labelStyle: UiLabelStyle? = null,
  public var noItemsLabelStyle: UiLabelStyle? = null,
  public var panelAlign: DropdownBoxAlign? = null,
  public var panelBackground: UiPatchStyle? = null,
  public var panelOffset: Int? = null,
  public var panelPadding: Int? = null,
  public var panelScrollbarStyle: UiScrollbarStyle? = null,
  public var panelTitleLabelStyle: UiLabelStyle? = null,
  public var panelWidth: Int? = null,
  public var pressedArrowTexturePath: String? = null,
  public var pressedBackground: UiPatchStyle? = null,
  public var pressedEntryBackground: UiPatchStyle? = null,
  public var searchInputStyle: UiDropdownBoxSearchInputStyle? = null,
  public var selectedEntryIconBackground: UiPatchStyle? = null,
  public var selectedEntryLabelStyle: UiLabelStyle? = null,
  public var sounds: UiDropdownBoxSounds? = null,
) : UiType {
  @ExcludeProperty
  override val templates: MutableList<UiType> = mutableListOf()

  @UiDsl
  public fun focusOutlineColor(`value`: String) {
    this.focusOutlineColor = Color(value)
  }
}

@UiDsl
public fun dropdownBoxStyle(`init`: UiDropdownBoxStyle.() -> Unit): UiDropdownBoxStyle {
  val result = UiDropdownBoxStyle()
  result.init()
  return result
}
