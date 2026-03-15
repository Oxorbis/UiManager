package cz.creeperface.hytale.uimanager.type

import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiDsl
import cz.creeperface.hytale.uimanager.UiType
import kotlin.String
import kotlin.Unit
import kotlin.collections.MutableList

@UiDsl
public data class UiLabeledCheckBoxStyleState(
  public var changedSound: UiSoundStyle? = null,
  public var defaultBackground: UiPatchStyle? = null,
  public var defaultLabelStyle: UiLabelStyle? = null,
  public var disabledBackground: UiPatchStyle? = null,
  public var disabledLabelStyle: UiLabelStyle? = null,
  public var hoveredBackground: UiPatchStyle? = null,
  public var hoveredLabelStyle: UiLabelStyle? = null,
  public var hoveredSound: UiSoundStyle? = null,
  public var pressedBackground: UiPatchStyle? = null,
  public var pressedLabelStyle: UiLabelStyle? = null,
  public var text: String? = null,
) : UiType {
  @ExcludeProperty
  override val templates: MutableList<UiType> = mutableListOf()
}

@UiDsl
public fun labeledCheckBoxStyleState(`init`: UiLabeledCheckBoxStyleState.() -> Unit): UiLabeledCheckBoxStyleState {
  val result = UiLabeledCheckBoxStyleState()
  result.init()
  return result
}
