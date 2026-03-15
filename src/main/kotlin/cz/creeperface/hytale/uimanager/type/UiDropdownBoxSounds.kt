package cz.creeperface.hytale.uimanager.type

import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiDsl
import cz.creeperface.hytale.uimanager.UiType
import kotlin.Unit
import kotlin.collections.MutableList

@UiDsl
public data class UiDropdownBoxSounds(
  public var activate: UiSoundStyle? = null,
  public var close: UiSoundStyle? = null,
  public var mouseHover: UiSoundStyle? = null,
) : UiType {
  @ExcludeProperty
  override val templates: MutableList<UiType> = mutableListOf()
}

@UiDsl
public fun dropdownBoxSounds(`init`: UiDropdownBoxSounds.() -> Unit): UiDropdownBoxSounds {
  val result = UiDropdownBoxSounds()
  result.init()
  return result
}
