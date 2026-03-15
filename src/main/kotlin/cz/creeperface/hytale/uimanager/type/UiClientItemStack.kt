package cz.creeperface.hytale.uimanager.type

import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiDsl
import cz.creeperface.hytale.uimanager.UiType
import kotlin.Boolean
import kotlin.Double
import kotlin.Int
import kotlin.Unit
import kotlin.collections.MutableList

@UiDsl
public data class UiClientItemStack(
  public var durability: Double? = null,
  public var maxDurability: Double? = null,
  public var overrideDroppedItemAnimation: Boolean? = null,
  public var quantity: Int? = null,
) : UiType {
  @ExcludeProperty
  override val templates: MutableList<UiType> = mutableListOf()
}

@UiDsl
public fun clientItemStack(`init`: UiClientItemStack.() -> Unit): UiClientItemStack {
  val result = UiClientItemStack()
  result.init()
  return result
}
