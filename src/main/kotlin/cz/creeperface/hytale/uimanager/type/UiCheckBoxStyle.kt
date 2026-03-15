package cz.creeperface.hytale.uimanager.type

import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiDsl
import cz.creeperface.hytale.uimanager.UiType
import kotlin.Unit
import kotlin.collections.MutableList

@UiDsl
public data class UiCheckBoxStyle(
  public var checked: UiCheckBoxStyleState? = null,
  public var unchecked: UiCheckBoxStyleState? = null,
) : UiType {
  @ExcludeProperty
  override val templates: MutableList<UiType> = mutableListOf()
}

@UiDsl
public fun checkBoxStyle(`init`: UiCheckBoxStyle.() -> Unit): UiCheckBoxStyle {
  val result = UiCheckBoxStyle()
  result.init()
  return result
}
