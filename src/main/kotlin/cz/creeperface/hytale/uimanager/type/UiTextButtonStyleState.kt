package cz.creeperface.hytale.uimanager.type

import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiDsl
import cz.creeperface.hytale.uimanager.UiType
import kotlin.String
import kotlin.Unit
import kotlin.collections.MutableList

@UiDsl
public data class UiTextButtonStyleState(
  public var background: UiPatchStyle? = null,
  public var labelMaskTexturePath: String? = null,
  public var labelStyle: UiLabelStyle? = null,
) : UiType {
  @ExcludeProperty
  override val templates: MutableList<UiType> = mutableListOf()
}

@UiDsl
public fun textButtonStyleState(`init`: UiTextButtonStyleState.() -> Unit): UiTextButtonStyleState {
  val result = UiTextButtonStyleState()
  result.init()
  return result
}
