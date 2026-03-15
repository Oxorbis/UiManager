package cz.creeperface.hytale.uimanager.type

import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiDsl
import cz.creeperface.hytale.uimanager.UiType
import kotlin.Boolean
import kotlin.Double
import kotlin.String
import kotlin.Unit
import kotlin.collections.MutableList

@UiDsl
public data class UiSoundStyle(
  public var maxPitch: Double? = null,
  public var minPitch: Double? = null,
  public var soundPath: String? = null,
  public var stopExistingPlayback: Boolean? = null,
  public var volume: Double? = null,
) : UiType {
  @ExcludeProperty
  override val templates: MutableList<UiType> = mutableListOf()
}

@UiDsl
public fun soundStyle(`init`: UiSoundStyle.() -> Unit): UiSoundStyle {
  val result = UiSoundStyle()
  result.init()
  return result
}
