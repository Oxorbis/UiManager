package cz.creeperface.hytale.uimanager.node

import com.hypixel.hytale.server.core.Message
import cz.creeperface.hytale.uimanager.BaseUiNode
import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiNode
import cz.creeperface.hytale.uimanager.enum.LayoutMode
import cz.creeperface.hytale.uimanager.property.rebindable
import cz.creeperface.hytale.uimanager.type.UiButtonStyle

public open class UiDropdownEntry(
  @ExcludeProperty
  override var id: String? = null,
  omitName: Boolean = false,
  disabled: Boolean? = null,
  layoutMode: LayoutMode? = null,
  selected: Boolean? = null,
  style: UiButtonStyle? = null,
  text: Message? = null,
  `value`: String? = null,
) : BaseUiNode() {
  @ExcludeProperty
  override var omitName: Boolean by rebindable(omitName)

  public var disabled: Boolean? by rebindable(disabled)

  public var layoutMode: LayoutMode? by rebindable(layoutMode)

  public var selected: Boolean? by rebindable(selected)

  public var style: UiButtonStyle? by rebindable(style)

    public var text: Message? by rebindable(text)

  public var `value`: String? by rebindable(value)

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
    val clone = UiDropdownEntry()
    cloneBaseProperties(clone)
    clone.disabled = this.disabled
    clone.layoutMode = this.layoutMode
    clone.selected = this.selected
    clone.style = this.style
    clone.text = this.text
    clone.value = this.value
    return clone
  }

  public companion object {
    public const val NODE_NAME: String = "DropdownEntry"
  }
}
