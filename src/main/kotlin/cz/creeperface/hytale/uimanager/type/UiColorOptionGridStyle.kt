package cz.creeperface.hytale.uimanager.type

import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiDsl
import cz.creeperface.hytale.uimanager.UiType
import kotlin.Int
import kotlin.String
import kotlin.Unit
import kotlin.collections.MutableList

@UiDsl
public data class UiColorOptionGridStyle(
  public var frameBackground: UiPatchStyle? = null,
  public var highlightBackground: UiPatchStyle? = null,
  public var highlightOffsetLeft: Int? = null,
  public var highlightOffsetTop: Int? = null,
  public var highlightSize: Int? = null,
  public var maskTexturePath: String? = null,
  public var optionSize: Int? = null,
  public var optionSpacingHorizontal: Int? = null,
  public var optionSpacingVertical: Int? = null,
  public var sounds: UiButtonSounds? = null,
) : UiType {
  @ExcludeProperty
  override val templates: MutableList<UiType> = mutableListOf()
}

@UiDsl
public fun colorOptionGridStyle(`init`: UiColorOptionGridStyle.() -> Unit): UiColorOptionGridStyle {
  val result = UiColorOptionGridStyle()
  result.init()
  return result
}
