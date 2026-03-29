package cz.creeperface.hytale.uimanager.node

import cz.creeperface.hytale.uimanager.BaseUiNode
import cz.creeperface.hytale.uimanager.ChildNodeBuilder
import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiNode
import cz.creeperface.hytale.uimanager.UiNodeWithChildren
import cz.creeperface.hytale.uimanager.`enum`.LayoutMode
import cz.creeperface.hytale.uimanager.`enum`.ResizeType
import cz.creeperface.hytale.uimanager.`property`.rebindable
import cz.creeperface.hytale.uimanager.type.UiPatchStyle
import cz.creeperface.hytale.uimanager.type.UiScrollbarStyle
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.collections.MutableList

public open class UiDynamicPane(
  @ExcludeProperty
  override var id: String? = null,
  omitName: Boolean = false,
  layoutMode: LayoutMode? = null,
  minSize: Int? = null,
  resizeAt: ResizeType? = null,
  resizerBackground: UiPatchStyle? = null,
  resizerSize: Int? = null,
  scrollbarStyle: UiScrollbarStyle? = null,
) : BaseUiNode(),
    UiNodeWithChildren,
    ChildNodeBuilder {
  @ExcludeProperty
  override var omitName: Boolean by rebindable(omitName)

  public var layoutMode: LayoutMode? by rebindable(layoutMode)

  public var minSize: Int? by rebindable(minSize)

  public var resizeAt: ResizeType? by rebindable(resizeAt)

  public var resizerBackground: UiPatchStyle? by rebindable(resizerBackground)

  public var resizerSize: Int? by rebindable(resizerSize)

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
    val clone = UiDynamicPane()
    cloneBaseProperties(clone)
    clone.layoutMode = this.layoutMode
    clone.minSize = this.minSize
    clone.resizeAt = this.resizeAt
    clone.resizerBackground = this.resizerBackground
    clone.resizerSize = this.resizerSize
    clone.scrollbarStyle = this.scrollbarStyle
    this.children.forEach { child ->
      clone.children.add(child.clone())
    }
    return clone
  }

  public companion object {
    public const val NODE_NAME: String = "DynamicPane"
  }
}
