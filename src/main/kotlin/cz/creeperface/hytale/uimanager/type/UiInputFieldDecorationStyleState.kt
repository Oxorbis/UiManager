package cz.creeperface.hytale.uimanager.type

import cz.creeperface.hytale.uimanager.Color
import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiDsl
import cz.creeperface.hytale.uimanager.UiType
import kotlin.Int
import kotlin.String
import kotlin.Unit
import kotlin.collections.MutableList

@UiDsl
public data class UiInputFieldDecorationStyleState(
  public var background: UiPatchStyle? = null,
  public var clearButtonStyle: UiInputFieldButtonStyle? = null,
  public var icon: UiInputFieldIcon? = null,
  public var outlineColor: Color? = null,
  public var outlineSize: Int? = null,
  public var toggleVisibilityButtonStyle: UiInputFieldButtonStyle? = null,
) : UiType {
  @ExcludeProperty
  override val templates: MutableList<UiType> = mutableListOf()

  @UiDsl
  public fun outlineColor(`value`: String) {
    this.outlineColor = Color(value)
  }
}

@UiDsl
public fun inputFieldDecorationStyleState(`init`: UiInputFieldDecorationStyleState.() -> Unit): UiInputFieldDecorationStyleState {
  val result = UiInputFieldDecorationStyleState()
  result.init()
  return result
}
