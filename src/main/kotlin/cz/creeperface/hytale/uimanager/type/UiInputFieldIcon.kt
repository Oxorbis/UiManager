package cz.creeperface.hytale.uimanager.type

import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiDsl
import cz.creeperface.hytale.uimanager.UiType
import cz.creeperface.hytale.uimanager.`enum`.InputFieldIconSide
import kotlin.Int
import kotlin.Unit
import kotlin.collections.MutableList

@UiDsl
public data class UiInputFieldIcon(
  public var height: Int? = null,
  public var offset: Int? = null,
  public var side: InputFieldIconSide? = null,
  public var texture: UiPatchStyle? = null,
  public var width: Int? = null,
) : UiType {
  @ExcludeProperty
  override val templates: MutableList<UiType> = mutableListOf()
}

@UiDsl
public fun inputFieldIcon(`init`: UiInputFieldIcon.() -> Unit): UiInputFieldIcon {
  val result = UiInputFieldIcon()
  result.init()
  return result
}
