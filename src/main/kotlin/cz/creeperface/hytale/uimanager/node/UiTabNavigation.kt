package cz.creeperface.hytale.uimanager.node

import cz.creeperface.hytale.uimanager.BaseUiNode
import cz.creeperface.hytale.uimanager.ChildNodeBuilder
import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiNode
import cz.creeperface.hytale.uimanager.UiNodeWithChildren
import cz.creeperface.hytale.uimanager.`property`.rebindable
import cz.creeperface.hytale.uimanager.type.UiTab
import cz.creeperface.hytale.uimanager.type.UiTabNavigationStyle
import kotlin.Boolean
import kotlin.String
import kotlin.collections.MutableList

public open class UiTabNavigation(
  @ExcludeProperty
  override var id: String? = null,
  omitName: Boolean = false,
  allowUnselection: Boolean? = null,
  selectedTab: String? = null,
  style: UiTabNavigationStyle? = null,
  tabs: UiTab? = null,
) : BaseUiNode(),
    UiNodeWithChildren,
    ChildNodeBuilder {
  @ExcludeProperty
  override var omitName: Boolean by rebindable(omitName)

  public var allowUnselection: Boolean? by rebindable(allowUnselection)

  public var selectedTab: String? by rebindable(selectedTab)

  public var style: UiTabNavigationStyle? by rebindable(style)

  public var tabs: UiTab? by rebindable(tabs)

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
    val clone = UiTabNavigation()
    cloneBaseProperties(clone)
    clone.allowUnselection = this.allowUnselection
    clone.selectedTab = this.selectedTab
    clone.style = this.style
    clone.tabs = this.tabs
    this.children.forEach { child ->
      clone.children.add(child.clone())
    }
    return clone
  }

  public companion object {
    public const val NODE_NAME: String = "TabNavigation"
  }
}
