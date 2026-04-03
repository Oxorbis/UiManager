package cz.creeperface.hytale.uimanager.node

import com.hypixel.hytale.server.core.Message
import cz.creeperface.hytale.uimanager.BaseUiNode
import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiNode
import cz.creeperface.hytale.uimanager.enum.LayoutMode
import cz.creeperface.hytale.uimanager.property.rebindable
import cz.creeperface.hytale.uimanager.type.UiAnchor
import cz.creeperface.hytale.uimanager.type.UiButtonStyle
import cz.creeperface.hytale.uimanager.type.UiPatchStyle

public open class UiTabButton(
  @ExcludeProperty
  override var id: String? = null,
  omitName: Boolean = false,
  disabled: Boolean? = null,
  icon: UiPatchStyle? = null,
  iconAnchor: UiAnchor? = null,
  iconSelected: UiPatchStyle? = null,
  layoutMode: LayoutMode? = null,
  style: UiButtonStyle? = null,
  text: Message? = null,
) : BaseUiNode() {
  @ExcludeProperty
  override var omitName: Boolean by rebindable(omitName)

  public var disabled: Boolean? by rebindable(disabled)

  public var icon: UiPatchStyle? by rebindable(icon)

  public var iconAnchor: UiAnchor? by rebindable(iconAnchor)

  public var iconSelected: UiPatchStyle? by rebindable(iconSelected)

  public var layoutMode: LayoutMode? by rebindable(layoutMode)

  public var style: UiButtonStyle? by rebindable(style)

    public var text: Message? by rebindable(text)

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
    val clone = UiTabButton()
    cloneBaseProperties(clone)
    clone.disabled = this.disabled
    clone.icon = this.icon
    clone.iconAnchor = this.iconAnchor
    clone.iconSelected = this.iconSelected
    clone.layoutMode = this.layoutMode
    clone.style = this.style
    clone.text = this.text
    return clone
  }

  public companion object {
    public const val NODE_NAME: String = "TabButton"
  }
}
