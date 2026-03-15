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
public data class UiItemGridStyle(
  public var brokenSlotBackgroundOverlay: UiPatchStyle? = null,
  public var brokenSlotIconOverlay: UiPatchStyle? = null,
  public var cursedIconAnchor: UiAnchor? = null,
  public var cursedIconPatch: UiPatchStyle? = null,
  public var defaultItemIcon: UiPatchStyle? = null,
  public var durabilityBar: String? = null,
  public var durabilityBarAnchor: UiAnchor? = null,
  public var durabilityBarBackground: UiPatchStyle? = null,
  public var durabilityBarColorEnd: Color? = null,
  public var durabilityBarColorStart: Color? = null,
  public var itemStackActivateSound: UiSoundStyle? = null,
  public var itemStackHoveredSound: UiSoundStyle? = null,
  public var quantityPopupSlotOverlay: UiPatchStyle? = null,
  public var slotBackground: UiPatchStyle? = null,
  public var slotIconSize: Int? = null,
  public var slotSize: Int? = null,
  public var slotSpacing: Int? = null,
) : UiType {
  @ExcludeProperty
  override val templates: MutableList<UiType> = mutableListOf()

  @UiDsl
  public fun durabilityBarColorEnd(`value`: String) {
    this.durabilityBarColorEnd = Color(value)
  }

  @UiDsl
  public fun durabilityBarColorStart(`value`: String) {
    this.durabilityBarColorStart = Color(value)
  }
}

@UiDsl
public fun itemGridStyle(`init`: UiItemGridStyle.() -> Unit): UiItemGridStyle {
  val result = UiItemGridStyle()
  result.init()
  return result
}
