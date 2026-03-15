package cz.creeperface.hytale.uimanager.type

import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiDsl
import cz.creeperface.hytale.uimanager.UiType
import cz.creeperface.hytale.uimanager.`enum`.InputFieldButtonSide
import kotlin.Int
import kotlin.Unit
import kotlin.collections.MutableList

@UiDsl
public data class UiInputFieldButtonStyle(
  public var height: Int? = null,
  public var hoveredTexture: UiPatchStyle? = null,
  public var offset: Int? = null,
  public var pressedTexture: UiPatchStyle? = null,
  public var side: InputFieldButtonSide? = null,
  public var texture: UiPatchStyle? = null,
  public var width: Int? = null,
) : UiType {
  @ExcludeProperty
  override val templates: MutableList<UiType> = mutableListOf()
}

@UiDsl
public fun inputFieldButtonStyle(`init`: UiInputFieldButtonStyle.() -> Unit): UiInputFieldButtonStyle {
  val result = UiInputFieldButtonStyle()
  result.init()
  return result
}
