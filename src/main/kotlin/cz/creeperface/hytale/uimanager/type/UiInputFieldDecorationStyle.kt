package cz.creeperface.hytale.uimanager.type

import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiDsl
import cz.creeperface.hytale.uimanager.UiType
import kotlin.Unit
import kotlin.collections.MutableList

@UiDsl
public data class UiInputFieldDecorationStyle(
  public var default: UiInputFieldDecorationStyleState? = null,
  public var focused: UiInputFieldDecorationStyleState? = null,
) : UiType {
  @ExcludeProperty
  override val templates: MutableList<UiType> = mutableListOf()
}

@UiDsl
public fun inputFieldDecorationStyle(`init`: UiInputFieldDecorationStyle.() -> Unit): UiInputFieldDecorationStyle {
  val result = UiInputFieldDecorationStyle()
  result.init()
  return result
}
