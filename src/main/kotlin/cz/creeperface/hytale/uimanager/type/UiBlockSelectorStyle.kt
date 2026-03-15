package cz.creeperface.hytale.uimanager.type

import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiDsl
import cz.creeperface.hytale.uimanager.UiType
import kotlin.Unit
import kotlin.collections.MutableList

@UiDsl
public data class UiBlockSelectorStyle(
  public var itemGridStyle: UiItemGridStyle? = null,
  public var slotDeleteIcon: UiPatchStyle? = null,
  public var slotDropIcon: UiPatchStyle? = null,
  public var slotHoverOverlay: UiPatchStyle? = null,
) : UiType {
  @ExcludeProperty
  override val templates: MutableList<UiType> = mutableListOf()
}

@UiDsl
public fun blockSelectorStyle(`init`: UiBlockSelectorStyle.() -> Unit): UiBlockSelectorStyle {
  val result = UiBlockSelectorStyle()
  result.init()
  return result
}
