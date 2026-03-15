package cz.creeperface.hytale.uimanager.type

import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiDsl
import cz.creeperface.hytale.uimanager.UiType
import kotlin.Unit
import kotlin.collections.MutableList

@UiDsl
public data class UiTabNavigationStyle(
  public var selectedTabStyle: UiTabStyle? = null,
  public var separatorAnchor: UiAnchor? = null,
  public var separatorBackground: UiPatchStyle? = null,
  public var tabSounds: UiButtonSounds? = null,
  public var tabStyle: UiTabStyle? = null,
) : UiType {
  @ExcludeProperty
  override val templates: MutableList<UiType> = mutableListOf()
}

@UiDsl
public fun tabNavigationStyle(`init`: UiTabNavigationStyle.() -> Unit): UiTabNavigationStyle {
  val result = UiTabNavigationStyle()
  result.init()
  return result
}
