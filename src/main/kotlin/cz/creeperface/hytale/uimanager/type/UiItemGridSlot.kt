package cz.creeperface.hytale.uimanager.type

import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiDsl
import cz.creeperface.hytale.uimanager.UiType
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.Unit
import kotlin.collections.MutableList

@UiDsl
public data class UiItemGridSlot(
  public var background: UiPatchStyle? = null,
  public var description: String? = null,
  public var extraOverlays: UiPatchStyle? = null,
  public var icon: UiPatchStyle? = null,
  public var inventorySlotIndex: Int? = null,
  public var isActivatable: Boolean? = null,
  public var isItemIncompatible: Boolean? = null,
  public var isItemUncraftable: Boolean? = null,
  public var itemStack: UiClientItemStack? = null,
  public var name: String? = null,
  public var overlay: UiPatchStyle? = null,
  public var skipItemQualityBackground: Boolean? = null,
) : UiType {
  @ExcludeProperty
  override val templates: MutableList<UiType> = mutableListOf()
}

@UiDsl
public fun itemGridSlot(`init`: UiItemGridSlot.() -> Unit): UiItemGridSlot {
  val result = UiItemGridSlot()
  result.init()
  return result
}
