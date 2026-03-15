package cz.creeperface.hytale.uimanager.type

import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiDsl
import cz.creeperface.hytale.uimanager.UiType
import kotlin.Unit
import kotlin.collections.MutableList

@UiDsl
public data class UiToggleButtonStyle(
  public var default: UiToggleButtonStyleState? = null,
  public var disabled: UiToggleButtonStyleState? = null,
  public var hovered: UiToggleButtonStyleState? = null,
  public var pressed: UiToggleButtonStyleState? = null,
  public var sounds: UiButtonSounds? = null,
) : UiType {
  @ExcludeProperty
  override val templates: MutableList<UiType> = mutableListOf()
}

@UiDsl
public fun toggleButtonStyle(`init`: UiToggleButtonStyle.() -> Unit): UiToggleButtonStyle {
  val result = UiToggleButtonStyle()
  result.init()
  return result
}
