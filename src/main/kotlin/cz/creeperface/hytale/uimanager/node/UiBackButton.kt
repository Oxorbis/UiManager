package cz.creeperface.hytale.uimanager.node

import cz.creeperface.hytale.uimanager.BaseUiNode
import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiNode
import cz.creeperface.hytale.uimanager.`enum`.ActionButtonAlignment
import cz.creeperface.hytale.uimanager.`enum`.LayoutMode
import cz.creeperface.hytale.uimanager.`property`.rebindable
import cz.creeperface.hytale.uimanager.type.UiButtonStyle
import kotlin.Boolean
import kotlin.String

public open class UiBackButton(
  @ExcludeProperty
  override var id: String? = null,
  omitName: Boolean = false,
  actionName: String? = null,
  alignment: ActionButtonAlignment? = null,
  bindingModifier1Label: String? = null,
  bindingModifier2Label: String? = null,
  disabled: Boolean? = null,
  isAvailable: Boolean? = null,
  isHoldBinding: Boolean? = null,
  keyBindingLabel: String? = null,
  layoutMode: LayoutMode? = null,
  style: UiButtonStyle? = null,
) : BaseUiNode() {
  @ExcludeProperty
  override var omitName: Boolean by rebindable(omitName)

  public var actionName: String? by rebindable(actionName)

  public var alignment: ActionButtonAlignment? by rebindable(alignment)

  public var bindingModifier1Label: String? by rebindable(bindingModifier1Label)

  public var bindingModifier2Label: String? by rebindable(bindingModifier2Label)

  public var disabled: Boolean? by rebindable(disabled)

  public var isAvailable: Boolean? by rebindable(isAvailable)

  public var isHoldBinding: Boolean? by rebindable(isHoldBinding)

  public var keyBindingLabel: String? by rebindable(keyBindingLabel)

  public var layoutMode: LayoutMode? by rebindable(layoutMode)

  public var style: UiButtonStyle? by rebindable(style)

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
    val clone = UiBackButton()
    cloneBaseProperties(clone)
    clone.actionName = this.actionName
    clone.alignment = this.alignment
    clone.bindingModifier1Label = this.bindingModifier1Label
    clone.bindingModifier2Label = this.bindingModifier2Label
    clone.disabled = this.disabled
    clone.isAvailable = this.isAvailable
    clone.isHoldBinding = this.isHoldBinding
    clone.keyBindingLabel = this.keyBindingLabel
    clone.layoutMode = this.layoutMode
    clone.style = this.style
    return clone
  }

  public companion object {
    public const val NODE_NAME: String = "BackButton"
  }
}
