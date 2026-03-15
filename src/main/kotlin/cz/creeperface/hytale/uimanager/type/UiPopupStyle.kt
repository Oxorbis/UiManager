package cz.creeperface.hytale.uimanager.type

import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiDsl
import cz.creeperface.hytale.uimanager.UiType
import kotlin.Int
import kotlin.Unit
import kotlin.collections.MutableList

@UiDsl
public data class UiPopupStyle(
  public var background: UiPatchStyle? = null,
  public var buttonPadding: UiPadding? = null,
  public var buttonStyle: UiSubMenuItemStyle? = null,
  public var padding: UiPadding? = null,
  public var selectedButtonStyle: UiSubMenuItemStyle? = null,
  public var tooltipStyle: UiTextTooltipStyle? = null,
  public var width: Int? = null,
) : UiType {
  @ExcludeProperty
  override val templates: MutableList<UiType> = mutableListOf()
}

@UiDsl
public fun popupStyle(`init`: UiPopupStyle.() -> Unit): UiPopupStyle {
  val result = UiPopupStyle()
  result.init()
  return result
}
