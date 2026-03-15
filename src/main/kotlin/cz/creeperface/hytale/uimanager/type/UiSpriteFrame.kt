package cz.creeperface.hytale.uimanager.type

import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiDsl
import cz.creeperface.hytale.uimanager.UiType
import kotlin.Int
import kotlin.Unit
import kotlin.collections.MutableList

@UiDsl
public data class UiSpriteFrame(
  public var count: Int? = null,
  public var height: Int? = null,
  public var perRow: Int? = null,
  public var width: Int? = null,
) : UiType {
  @ExcludeProperty
  override val templates: MutableList<UiType> = mutableListOf()
}

@UiDsl
public fun spriteFrame(`init`: UiSpriteFrame.() -> Unit): UiSpriteFrame {
  val result = UiSpriteFrame()
  result.init()
  return result
}
