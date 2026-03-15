package cz.creeperface.hytale.uimanager.type

import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiDsl
import cz.creeperface.hytale.uimanager.UiType
import cz.creeperface.hytale.uimanager.`enum`.TooltipAlignment
import kotlin.Int
import kotlin.Unit
import kotlin.collections.MutableList

@UiDsl
public data class UiTextTooltipStyle(
  public var alignment: TooltipAlignment? = null,
  public var background: UiPatchStyle? = null,
  public var labelStyle: UiLabelStyle? = null,
  public var maxWidth: Int? = null,
  public var padding: UiPadding? = null,
) : UiType {
  @ExcludeProperty
  override val templates: MutableList<UiType> = mutableListOf()
}

@UiDsl
public fun textTooltipStyle(`init`: UiTextTooltipStyle.() -> Unit): UiTextTooltipStyle {
  val result = UiTextTooltipStyle()
  result.init()
  return result
}
