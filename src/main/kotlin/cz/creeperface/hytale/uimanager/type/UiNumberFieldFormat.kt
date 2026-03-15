package cz.creeperface.hytale.uimanager.type

import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiDsl
import cz.creeperface.hytale.uimanager.UiType
import kotlin.Double
import kotlin.Int
import kotlin.String
import kotlin.Unit
import kotlin.collections.MutableList

@UiDsl
public data class UiNumberFieldFormat(
  public var defaultValue: Double? = null,
  public var maxDecimalPlaces: Int? = null,
  public var maxValue: Double? = null,
  public var minValue: Double? = null,
  public var step: Double? = null,
  public var suffix: String? = null,
) : UiType {
  @ExcludeProperty
  override val templates: MutableList<UiType> = mutableListOf()
}

@UiDsl
public fun numberFieldFormat(`init`: UiNumberFieldFormat.() -> Unit): UiNumberFieldFormat {
  val result = UiNumberFieldFormat()
  result.init()
  return result
}
