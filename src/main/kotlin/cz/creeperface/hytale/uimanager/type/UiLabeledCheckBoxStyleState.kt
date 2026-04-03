package cz.creeperface.hytale.uimanager.type

import com.hypixel.hytale.server.core.Message
import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiDsl
import cz.creeperface.hytale.uimanager.UiType

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
  public var text: Message? = null,
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
