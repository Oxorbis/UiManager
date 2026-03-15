package cz.creeperface.hytale.uimanager.type

import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiDsl
import cz.creeperface.hytale.uimanager.UiType
import kotlin.Unit
import kotlin.collections.MutableList

@UiDsl
public data class UiButtonStyleState(
  public var background: UiPatchStyle? = null,
) : UiType {
  @ExcludeProperty
  override val templates: MutableList<UiType> = mutableListOf()
}

@UiDsl
public fun buttonStyleState(`init`: UiButtonStyleState.() -> Unit): UiButtonStyleState {
  val result = UiButtonStyleState()
  result.init()
  return result
}
