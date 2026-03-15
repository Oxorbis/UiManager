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
public data class UiPatchStyle(
  public var anchor: UiAnchor? = null,
  public var area: UiPadding? = null,
  public var border: Int? = null,
  public var color: Color? = null,
  public var horizontalBorder: Int? = null,
  public var texturePath: String? = null,
  public var verticalBorder: Int? = null,
) : UiType {
  @ExcludeProperty
  override val templates: MutableList<UiType> = mutableListOf()

  @UiDsl
  public fun color(`value`: String) {
    this.color = Color(value)
  }
}

@UiDsl
public fun patchStyle(`init`: UiPatchStyle.() -> Unit): UiPatchStyle {
  val result = UiPatchStyle()
  result.init()
  return result
}
