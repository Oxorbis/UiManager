package cz.creeperface.hytale.uimanager.type

import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiDsl
import cz.creeperface.hytale.uimanager.UiType
import kotlin.Int
import kotlin.Unit
import kotlin.collections.MutableList

@UiDsl
public data class UiSliderStyle(
  public var background: UiPatchStyle? = null,
  public var fill: UiPatchStyle? = null,
  public var handle: UiPatchStyle? = null,
  public var handleHeight: Int? = null,
  public var handleWidth: Int? = null,
  public var sounds: UiButtonSounds? = null,
) : UiType {
  @ExcludeProperty
  override val templates: MutableList<UiType> = mutableListOf()
}

@UiDsl
public fun sliderStyle(`init`: UiSliderStyle.() -> Unit): UiSliderStyle {
  val result = UiSliderStyle()
  result.init()
  return result
}
