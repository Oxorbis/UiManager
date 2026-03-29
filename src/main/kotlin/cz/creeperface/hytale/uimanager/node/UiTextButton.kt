package cz.creeperface.hytale.uimanager.node

import cz.creeperface.hytale.uimanager.BaseUiNode
import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiNode
import cz.creeperface.hytale.uimanager.`property`.rebindable
import cz.creeperface.hytale.uimanager.type.UiTextButtonStyle
import kotlin.Boolean
import kotlin.String

public open class UiTextButton(
  @ExcludeProperty
  override var id: String? = null,
  omitName: Boolean = false,
  disabled: Boolean? = null,
  style: UiTextButtonStyle? = null,
  text: String? = null,
  textSpans: String? = null,
) : BaseUiNode() {
  @ExcludeProperty
  override var omitName: Boolean by rebindable(omitName)

  public var disabled: Boolean? by rebindable(disabled)

  public var style: UiTextButtonStyle? by rebindable(style)

  public var text: String? by rebindable(text)

  public var textSpans: String? by rebindable(textSpans)

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
    val clone = UiTextButton()
    cloneBaseProperties(clone)
    clone.disabled = this.disabled
    clone.style = this.style
    clone.text = this.text
    clone.textSpans = this.textSpans
    return clone
  }

  public companion object {
    public const val NODE_NAME: String = "TextButton"
  }
}
