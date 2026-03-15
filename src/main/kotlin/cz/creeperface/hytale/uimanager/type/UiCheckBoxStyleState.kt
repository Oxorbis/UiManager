package cz.creeperface.hytale.uimanager.type

import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiDsl
import cz.creeperface.hytale.uimanager.UiType
import kotlin.Unit
import kotlin.collections.MutableList

@UiDsl
public data class UiCheckBoxStyleState(
  public var changedSound: UiSoundStyle? = null,
  public var defaultBackground: UiPatchStyle? = null,
  public var disabledBackground: UiPatchStyle? = null,
  public var hoveredBackground: UiPatchStyle? = null,
  public var hoveredSound: UiSoundStyle? = null,
  public var pressedBackground: UiPatchStyle? = null,
) : UiType {
  @ExcludeProperty
  override val templates: MutableList<UiType> = mutableListOf()
}

@UiDsl
public fun checkBoxStyleState(`init`: UiCheckBoxStyleState.() -> Unit): UiCheckBoxStyleState {
  val result = UiCheckBoxStyleState()
  result.init()
  return result
}
