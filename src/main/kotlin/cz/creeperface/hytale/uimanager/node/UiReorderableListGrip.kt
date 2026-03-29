package cz.creeperface.hytale.uimanager.node

import cz.creeperface.hytale.uimanager.BaseUiNode
import cz.creeperface.hytale.uimanager.ChildNodeBuilder
import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiNode
import cz.creeperface.hytale.uimanager.UiNodeWithChildren
import cz.creeperface.hytale.uimanager.`enum`.LayoutMode
import cz.creeperface.hytale.uimanager.`property`.rebindable
import cz.creeperface.hytale.uimanager.type.UiScrollbarStyle
import kotlin.Boolean
import kotlin.String
import kotlin.collections.MutableList

public open class UiReorderableListGrip(
  @ExcludeProperty
  override var id: String? = null,
  omitName: Boolean = false,
  isDragEnabled: Boolean? = null,
  layoutMode: LayoutMode? = null,
  scrollbarStyle: UiScrollbarStyle? = null,
) : BaseUiNode(),
    UiNodeWithChildren,
    ChildNodeBuilder {
  @ExcludeProperty
  override var omitName: Boolean by rebindable(omitName)

  public var isDragEnabled: Boolean? by rebindable(isDragEnabled)

  public var layoutMode: LayoutMode? by rebindable(layoutMode)

  public var scrollbarStyle: UiScrollbarStyle? by rebindable(scrollbarStyle)

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
    val clone = UiReorderableListGrip()
    cloneBaseProperties(clone)
    clone.isDragEnabled = this.isDragEnabled
    clone.layoutMode = this.layoutMode
    clone.scrollbarStyle = this.scrollbarStyle
    this.children.forEach { child ->
      clone.children.add(child.clone())
    }
    return clone
  }

  public companion object {
    public const val NODE_NAME: String = "ReorderableListGrip"
  }
}
