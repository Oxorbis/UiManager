package cz.creeperface.hytale.uimanager.type

import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiDsl
import cz.creeperface.hytale.uimanager.UiType
import kotlin.Boolean
import kotlin.Int
import kotlin.Unit
import kotlin.collections.MutableList

@UiDsl
public data class UiScrollbarStyle(
  public var background: UiPatchStyle? = null,
  public var draggedHandle: UiPatchStyle? = null,
  public var handle: UiPatchStyle? = null,
  public var hoveredHandle: UiPatchStyle? = null,
  public var onlyVisibleWhenHovered: Boolean? = null,
  public var size: Int? = null,
  public var spacing: Int? = null,
) : UiType {
  @ExcludeProperty
  override val templates: MutableList<UiType> = mutableListOf()
}

@UiDsl
public fun scrollbarStyle(`init`: UiScrollbarStyle.() -> Unit): UiScrollbarStyle {
  val result = UiScrollbarStyle()
  result.init()
  return result
}
