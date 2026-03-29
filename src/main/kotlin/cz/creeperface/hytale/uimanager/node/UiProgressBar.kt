package cz.creeperface.hytale.uimanager.node

import cz.creeperface.hytale.uimanager.BaseUiNode
import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiNode
import cz.creeperface.hytale.uimanager.`enum`.ProgressBarAlignment
import cz.creeperface.hytale.uimanager.`enum`.ProgressBarDirection
import cz.creeperface.hytale.uimanager.`property`.rebindable
import cz.creeperface.hytale.uimanager.type.UiPatchStyle
import kotlin.Boolean
import kotlin.Double
import kotlin.Int
import kotlin.String

public open class UiProgressBar(
  @ExcludeProperty
  override var id: String? = null,
  omitName: Boolean = false,
  alignment: ProgressBarAlignment? = null,
  bar: UiPatchStyle? = null,
  barTexturePath: String? = null,
  direction: ProgressBarDirection? = null,
  effectHeight: Int? = null,
  effectOffset: Int? = null,
  effectTexturePath: String? = null,
  effectWidth: Int? = null,
  `value`: Double? = null,
) : BaseUiNode() {
  @ExcludeProperty
  override var omitName: Boolean by rebindable(omitName)

  public var alignment: ProgressBarAlignment? by rebindable(alignment)

  public var bar: UiPatchStyle? by rebindable(bar)

  public var barTexturePath: String? by rebindable(barTexturePath)

  public var direction: ProgressBarDirection? by rebindable(direction)

  public var effectHeight: Int? by rebindable(effectHeight)

  public var effectOffset: Int? by rebindable(effectOffset)

  public var effectTexturePath: String? by rebindable(effectTexturePath)

  public var effectWidth: Int? by rebindable(effectWidth)

  public var `value`: Double? by rebindable(value)

  @ExcludeProperty
  override val isDirty: Boolean
    get() {
      var dirty = super.isDirty
      return dirty
    }

  override fun resetDirty() {
    super.resetDirty()
  }

  override fun clone(): UiNode {
    val clone = UiProgressBar()
    cloneBaseProperties(clone)
    clone.alignment = this.alignment
    clone.bar = this.bar
    clone.barTexturePath = this.barTexturePath
    clone.direction = this.direction
    clone.effectHeight = this.effectHeight
    clone.effectOffset = this.effectOffset
    clone.effectTexturePath = this.effectTexturePath
    clone.effectWidth = this.effectWidth
    clone.value = this.value
    return clone
  }

  public companion object {
    public const val NODE_NAME: String = "ProgressBar"
  }
}
