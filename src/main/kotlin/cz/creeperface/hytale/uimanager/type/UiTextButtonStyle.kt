package cz.creeperface.hytale.uimanager.type

import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiDsl
import cz.creeperface.hytale.uimanager.UiType
import kotlin.Unit
import kotlin.collections.MutableList

@UiDsl
public data class UiTextButtonStyle(
  public var default: UiTextButtonStyleState? = null,
  public var disabled: UiTextButtonStyleState? = null,
  public var hovered: UiTextButtonStyleState? = null,
  public var pressed: UiTextButtonStyleState? = null,
  public var sounds: UiButtonSounds? = null,
) : UiType {
  @ExcludeProperty
  override val templates: MutableList<UiType> = mutableListOf()
}

@UiDsl
public fun textButtonStyle(`init`: UiTextButtonStyle.() -> Unit): UiTextButtonStyle {
  val result = UiTextButtonStyle()
  result.init()
  return result
}
