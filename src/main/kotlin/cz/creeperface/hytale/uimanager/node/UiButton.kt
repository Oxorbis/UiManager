package cz.creeperface.hytale.uimanager.node

import cz.creeperface.hytale.uimanager.BaseUiNode
import cz.creeperface.hytale.uimanager.ChildNodeBuilder
import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiNode
import cz.creeperface.hytale.uimanager.UiNodeWithChildren
import cz.creeperface.hytale.uimanager.`enum`.LayoutMode
import cz.creeperface.hytale.uimanager.`property`.rebindable
import cz.creeperface.hytale.uimanager.type.UiButtonStyle
import kotlin.Boolean
import kotlin.String
import kotlin.collections.MutableList

public open class UiButton(
  @ExcludeProperty
  override var id: String? = null,
  omitName: Boolean = false,
  disabled: Boolean? = null,
  layoutMode: LayoutMode? = null,
  style: UiButtonStyle? = null,
) : BaseUiNode(),
    UiNodeWithChildren,
    ChildNodeBuilder {
  @ExcludeProperty
  override var omitName: Boolean by rebindable(omitName)

  public var disabled: Boolean? by rebindable(disabled)

  public var layoutMode: LayoutMode? by rebindable(layoutMode)

  public var style: UiButtonStyle? by rebindable(style)

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
    val clone = UiButton()
    cloneBaseProperties(clone)
    clone.disabled = this.disabled
    clone.layoutMode = this.layoutMode
    clone.style = this.style
    this.children.forEach { child ->
      clone.children.add(child.clone())
    }
    return clone
  }

  public companion object {
    public const val NODE_NAME: String = "Button"
  }
}
