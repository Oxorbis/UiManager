package cz.creeperface.hytale.uimanager.type

import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiDsl
import cz.creeperface.hytale.uimanager.UiType
import kotlin.Unit
import kotlin.collections.MutableList

@UiDsl
public data class UiButtonSounds(
  public var activate: UiSoundStyle? = null,
  public var context: UiSoundStyle? = null,
  public var mouseHover: UiSoundStyle? = null,
) : UiType {
  @ExcludeProperty
  override val templates: MutableList<UiType> = mutableListOf()
}

@UiDsl
public fun buttonSounds(`init`: UiButtonSounds.() -> Unit): UiButtonSounds {
  val result = UiButtonSounds()
  result.init()
  return result
}
