package cz.creeperface.hytale.uimanager.node

import com.hypixel.hytale.server.core.Message
import cz.creeperface.hytale.uimanager.BaseUiNode
import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiNode
import cz.creeperface.hytale.uimanager.property.rebindable
import cz.creeperface.hytale.uimanager.type.UiLabelStyle

public open class UiLabel(
  @ExcludeProperty
  override var id: String? = null,
  omitName: Boolean = false,
  style: UiLabelStyle? = null,
  text: Message? = null,
  textSpans: Message? = null,
) : BaseUiNode() {
  @ExcludeProperty
  override var omitName: Boolean by rebindable(omitName)

  public var style: UiLabelStyle? by rebindable(style)

  public var text: Message? by rebindable(text)

  public var textSpans: Message? by rebindable(textSpans)

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
    val clone = UiLabel()
    cloneBaseProperties(clone)
    clone.style = this.style
    clone.text = this.text
    clone.textSpans = this.textSpans
    return clone
  }

  public companion object {
    public const val NODE_NAME: String = "Label"
  }
}
