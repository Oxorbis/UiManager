package cz.creeperface.hytale.uimanager.node

import cz.creeperface.hytale.uimanager.BaseUiNode
import cz.creeperface.hytale.uimanager.ChildNodeBuilder
import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiNode
import cz.creeperface.hytale.uimanager.UiNodeWithChildren
import cz.creeperface.hytale.uimanager.`property`.rebindable
import cz.creeperface.hytale.uimanager.type.UiAnchor
import cz.creeperface.hytale.uimanager.type.UiPatchStyle
import cz.creeperface.hytale.uimanager.type.UiPopupStyle
import cz.creeperface.hytale.uimanager.type.UiTextButtonStyle
import kotlin.Boolean
import kotlin.String
import kotlin.collections.MutableList

public open class UiMenuItem(
  @ExcludeProperty
  override var id: String? = null,
  omitName: Boolean = false,
  disabled: Boolean? = null,
  icon: UiPatchStyle? = null,
  iconAnchor: UiAnchor? = null,
  isSelected: Boolean? = null,
  popupStyle: UiPopupStyle? = null,
  selectedStyle: UiTextButtonStyle? = null,
  style: UiTextButtonStyle? = null,
  text: String? = null,
  textSpans: String? = null,
) : BaseUiNode(),
    UiNodeWithChildren,
    ChildNodeBuilder {
  @ExcludeProperty
  override var omitName: Boolean by rebindable(omitName)

  public var disabled: Boolean? by rebindable(disabled)

  public var icon: UiPatchStyle? by rebindable(icon)

  public var iconAnchor: UiAnchor? by rebindable(iconAnchor)

  public var isSelected: Boolean? by rebindable(isSelected)

  public var popupStyle: UiPopupStyle? by rebindable(popupStyle)

  public var selectedStyle: UiTextButtonStyle? by rebindable(selectedStyle)

  public var style: UiTextButtonStyle? by rebindable(style)

  public var text: String? by rebindable(text)

  public var textSpans: String? by rebindable(textSpans)

  @ExcludeProperty
  override val children: MutableList<UiNode> = mutableListOf()

  @ExcludeProperty
  override val isDirty: Boolean
    get() {
      var dirty = super.isDirty
      if (!dirty) dirty = children.any { it.isDirty }
      return dirty
    }

  override fun addNode(node: UiNode) {
    addNodeToChildren(node)
  }

  override fun resetDirty() {
    super.resetDirty()
    children.forEach { it.resetDirty() }
  }

  override fun clone(): UiNode {
    val clone = UiMenuItem()
    cloneBaseProperties(clone)
    clone.disabled = this.disabled
    clone.icon = this.icon
    clone.iconAnchor = this.iconAnchor
    clone.isSelected = this.isSelected
    clone.popupStyle = this.popupStyle
    clone.selectedStyle = this.selectedStyle
    clone.style = this.style
    clone.text = this.text
    clone.textSpans = this.textSpans
    this.children.forEach { child ->
      clone.children.add(child.clone())
    }
    return clone
  }

  public companion object {
    public const val NODE_NAME: String = "MenuItem"
  }
}
