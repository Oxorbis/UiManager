package cz.creeperface.hytale.uimanager.type

import com.hypixel.hytale.server.core.Message
import cz.creeperface.hytale.uimanager.Color
import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiDsl
import cz.creeperface.hytale.uimanager.UiType

@UiDsl
public data class UiLabelSpan(
  public var color: Color? = null,
  public var isBold: Boolean? = null,
  public var isItalics: Boolean? = null,
  public var isMonospace: Boolean? = null,
  public var isUnderlined: Boolean? = null,
  public var isUppercase: Boolean? = null,
  public var link: String? = null,
  public var outlineColor: Color? = null,
  public var text: Message? = null,
) : UiType {
  @ExcludeProperty
  override val templates: MutableList<UiType> = mutableListOf()

  @UiDsl
  public fun color(`value`: String) {
    this.color = Color(value)
  }

  @UiDsl
  public fun outlineColor(`value`: String) {
    this.outlineColor = Color(value)
  }
}

@UiDsl
public fun labelSpan(`init`: UiLabelSpan.() -> Unit): UiLabelSpan {
  val result = UiLabelSpan()
  result.init()
  return result
}
