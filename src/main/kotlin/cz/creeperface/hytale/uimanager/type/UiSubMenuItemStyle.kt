package cz.creeperface.hytale.uimanager.type

import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiDsl
import cz.creeperface.hytale.uimanager.UiType
import kotlin.Unit
import kotlin.collections.MutableList

@UiDsl
public data class UiSubMenuItemStyle(
  public var default: UiSubMenuItemStyleState? = null,
  public var disabled: UiSubMenuItemStyleState? = null,
  public var hovered: UiSubMenuItemStyleState? = null,
  public var pressed: UiSubMenuItemStyleState? = null,
  public var sounds: UiButtonSounds? = null,
) : UiType {
  @ExcludeProperty
  override val templates: MutableList<UiType> = mutableListOf()
}

@UiDsl
public fun subMenuItemStyle(`init`: UiSubMenuItemStyle.() -> Unit): UiSubMenuItemStyle {
  val result = UiSubMenuItemStyle()
  result.init()
  return result
}
