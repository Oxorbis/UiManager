package cz.creeperface.hytale.uimanager.type

import com.hypixel.hytale.server.core.Message
import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiDsl
import cz.creeperface.hytale.uimanager.UiType

@UiDsl
public data class UiTab(
  public var icon: UiPatchStyle? = null,
  public var iconAnchor: UiAnchor? = null,
  public var iconSelected: UiPatchStyle? = null,
  public var text: Message? = null,
  public var tooltipText: String? = null,
) : UiType {
  @ExcludeProperty
  override val templates: MutableList<UiType> = mutableListOf()
}

@UiDsl
public fun tab(`init`: UiTab.() -> Unit): UiTab {
  val result = UiTab()
  result.init()
  return result
}
