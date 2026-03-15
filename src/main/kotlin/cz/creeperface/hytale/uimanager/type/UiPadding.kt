package cz.creeperface.hytale.uimanager.type

import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiDsl
import cz.creeperface.hytale.uimanager.UiType
import kotlin.Int
import kotlin.Unit
import kotlin.collections.MutableList

@UiDsl
public data class UiPadding(
  public var bottom: Int? = null,
  public var full: Int? = null,
  public var horizontal: Int? = null,
  public var left: Int? = null,
  public var right: Int? = null,
  public var top: Int? = null,
  public var vertical: Int? = null,
) : UiType {
  @ExcludeProperty
  override val templates: MutableList<UiType> = mutableListOf()
}

@UiDsl
public fun padding(`init`: UiPadding.() -> Unit): UiPadding {
  val result = UiPadding()
  result.init()
  return result
}
