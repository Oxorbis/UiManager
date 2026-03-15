package cz.creeperface.hytale.uimanager.type

import cz.creeperface.hytale.uimanager.Color
import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiDsl
import cz.creeperface.hytale.uimanager.UiType
import cz.creeperface.hytale.uimanager.`enum`.LabelAlignment
import kotlin.Boolean
import kotlin.Double
import kotlin.String
import kotlin.Unit
import kotlin.collections.MutableList

@UiDsl
public data class UiLabelStyle(
  public var alignment: LabelAlignment? = null,
  public var fontName: String? = null,
  public var fontSize: Double? = null,
  public var horizontalAlignment: LabelAlignment? = null,
  public var letterSpacing: Double? = null,
  public var outlineColor: Color? = null,
  public var renderBold: Boolean? = null,
  public var renderItalics: Boolean? = null,
  public var renderUnderlined: Boolean? = null,
  public var renderUppercase: Boolean? = null,
  public var textColor: Color? = null,
  public var verticalAlignment: LabelAlignment? = null,
  public var wrap: Boolean? = null,
) : UiType {
  @ExcludeProperty
  override val templates: MutableList<UiType> = mutableListOf()

  @UiDsl
  public fun outlineColor(`value`: String) {
    this.outlineColor = Color(value)
  }

  @UiDsl
  public fun textColor(`value`: String) {
    this.textColor = Color(value)
  }
}

@UiDsl
public fun labelStyle(`init`: UiLabelStyle.() -> Unit): UiLabelStyle {
  val result = UiLabelStyle()
  result.init()
  return result
}
