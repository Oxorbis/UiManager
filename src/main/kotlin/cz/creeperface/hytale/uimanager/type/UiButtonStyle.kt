package cz.creeperface.hytale.uimanager.type

import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiDsl
import cz.creeperface.hytale.uimanager.UiType
import kotlin.Unit
import kotlin.collections.MutableList

@UiDsl
public data class UiButtonStyle(
  public var default: UiButtonStyleState? = null,
  public var disabled: UiButtonStyleState? = null,
  public var hovered: UiButtonStyleState? = null,
  public var pressed: UiButtonStyleState? = null,
  public var sounds: UiButtonSounds? = null,
) : UiType {
  @ExcludeProperty
  override val templates: MutableList<UiType> = mutableListOf()
}

@UiDsl
public fun buttonStyle(`init`: UiButtonStyle.() -> Unit): UiButtonStyle {
  val result = UiButtonStyle()
  result.init()
  return result
}
