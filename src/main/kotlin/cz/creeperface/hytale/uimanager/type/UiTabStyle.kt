package cz.creeperface.hytale.uimanager.type

import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiDsl
import cz.creeperface.hytale.uimanager.UiType
import kotlin.Unit
import kotlin.collections.MutableList

@UiDsl
public data class UiTabStyle(
  public var default: UiTabStyleState? = null,
  public var hovered: UiTabStyleState? = null,
  public var pressed: UiTabStyleState? = null,
) : UiType {
  @ExcludeProperty
  override val templates: MutableList<UiType> = mutableListOf()
}

@UiDsl
public fun tabStyle(`init`: UiTabStyle.() -> Unit): UiTabStyle {
  val result = UiTabStyle()
  result.init()
  return result
}
