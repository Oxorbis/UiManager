package cz.creeperface.hytale.uimanager.type

import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiDsl
import cz.creeperface.hytale.uimanager.UiType
import kotlin.Double
import kotlin.Int
import kotlin.String
import kotlin.Unit
import kotlin.collections.MutableList

@UiDsl
public data class UiTabStyleState(
  public var anchor: UiAnchor? = null,
  public var background: UiPatchStyle? = null,
  public var contentMaskTexturePath: String? = null,
  public var flexWeight: Int? = null,
  public var iconAnchor: UiAnchor? = null,
  public var iconOpacity: Double? = null,
  public var labelStyle: UiLabelStyle? = null,
  public var overlay: UiPatchStyle? = null,
  public var padding: UiPadding? = null,
  public var tooltipStyle: UiTextTooltipStyle? = null,
) : UiType {
  @ExcludeProperty
  override val templates: MutableList<UiType> = mutableListOf()
}

@UiDsl
public fun tabStyleState(`init`: UiTabStyleState.() -> Unit): UiTabStyleState {
  val result = UiTabStyleState()
  result.init()
  return result
}
