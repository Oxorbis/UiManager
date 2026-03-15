package cz.creeperface.hytale.uimanager.type

import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiDsl
import cz.creeperface.hytale.uimanager.UiType
import kotlin.Unit
import kotlin.collections.MutableList

@UiDsl
public data class UiLabeledCheckBoxStyle(
  public var checked: UiLabeledCheckBoxStyleState? = null,
  public var unchecked: UiLabeledCheckBoxStyleState? = null,
) : UiType {
  @ExcludeProperty
  override val templates: MutableList<UiType> = mutableListOf()
}

@UiDsl
public fun labeledCheckBoxStyle(`init`: UiLabeledCheckBoxStyle.() -> Unit): UiLabeledCheckBoxStyle {
  val result = UiLabeledCheckBoxStyle()
  result.init()
  return result
}
