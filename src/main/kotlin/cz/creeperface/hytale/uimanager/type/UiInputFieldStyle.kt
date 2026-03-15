package cz.creeperface.hytale.uimanager.type

import cz.creeperface.hytale.uimanager.Color
import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiDsl
import cz.creeperface.hytale.uimanager.UiType
import kotlin.Boolean
import kotlin.Double
import kotlin.String
import kotlin.Unit
import kotlin.collections.MutableList

@UiDsl
public data class UiInputFieldStyle(
  public var fontName: String? = null,
  public var fontSize: Double? = null,
  public var renderBold: Boolean? = null,
  public var renderItalics: Boolean? = null,
  public var renderUppercase: Boolean? = null,
  public var textColor: Color? = null,
) : UiType {
  @ExcludeProperty
  override val templates: MutableList<UiType> = mutableListOf()

  @UiDsl
  public fun textColor(`value`: String) {
    this.textColor = Color(value)
  }
}

@UiDsl
public fun inputFieldStyle(`init`: UiInputFieldStyle.() -> Unit): UiInputFieldStyle {
  val result = UiInputFieldStyle()
  result.init()
  return result
}
