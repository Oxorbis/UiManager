package cz.creeperface.hytale.uimanager.node

import cz.creeperface.hytale.uimanager.BaseUiNode
import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiNode
import cz.creeperface.hytale.uimanager.`property`.rebindable
import cz.creeperface.hytale.uimanager.type.UiCheckBoxStyle
import kotlin.Boolean
import kotlin.String

public open class UiCheckBox(
  @ExcludeProperty
  override var id: String? = null,
  omitName: Boolean = false,
  disabled: Boolean? = null,
  style: UiCheckBoxStyle? = null,
  `value`: Boolean? = null,
) : BaseUiNode() {
  @ExcludeProperty
  override var omitName: Boolean by rebindable(omitName)

  public var disabled: Boolean? by rebindable(disabled)

  public var style: UiCheckBoxStyle? by rebindable(style)

  public var `value`: Boolean? by rebindable(value)

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
    val clone = UiCheckBox()
    cloneBaseProperties(clone)
    clone.disabled = this.disabled
    clone.style = this.style
    clone.value = this.value
    return clone
  }

  public companion object {
    public const val NODE_NAME: String = "CheckBox"
  }
}
