package cz.creeperface.hytale.uimanager.node

import cz.creeperface.hytale.uimanager.BaseUiNode
import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiNode
import cz.creeperface.hytale.uimanager.`property`.rebindable
import kotlin.Boolean
import kotlin.String

public open class UiHotkeyLabel(
  @ExcludeProperty
  override var id: String? = null,
  omitName: Boolean = false,
  inputBindingKey: String? = null,
  inputBindingKeyPrefix: String? = null,
  inputBindingKeyPrefixBinding: String? = null,
) : BaseUiNode() {
  @ExcludeProperty
  override var omitName: Boolean by rebindable(omitName)

  public var inputBindingKey: String? by rebindable(inputBindingKey)

  public var inputBindingKeyPrefix: String? by rebindable(inputBindingKeyPrefix)

  public var inputBindingKeyPrefixBinding: String? by rebindable(inputBindingKeyPrefixBinding)

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
    val clone = UiHotkeyLabel()
    cloneBaseProperties(clone)
    clone.inputBindingKey = this.inputBindingKey
    clone.inputBindingKeyPrefix = this.inputBindingKeyPrefix
    clone.inputBindingKeyPrefixBinding = this.inputBindingKeyPrefixBinding
    return clone
  }

  public companion object {
    public const val NODE_NAME: String = "HotkeyLabel"
  }
}
