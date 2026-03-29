package cz.creeperface.hytale.uimanager.node

import cz.creeperface.hytale.uimanager.BaseUiNode
import cz.creeperface.hytale.uimanager.ChildNodeBuilder
import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiNode
import cz.creeperface.hytale.uimanager.UiNodeWithChildren
import cz.creeperface.hytale.uimanager.`property`.rebindable
import cz.creeperface.hytale.uimanager.type.UiToggleButtonStyle
import kotlin.Boolean
import kotlin.String
import kotlin.collections.MutableList

public open class UiToggleButton(
  @ExcludeProperty
  override var id: String? = null,
  omitName: Boolean = false,
  checkedStyle: UiToggleButtonStyle? = null,
  disabled: Boolean? = null,
  isChecked: Boolean? = null,
  style: UiToggleButtonStyle? = null,
) : BaseUiNode(),
    UiNodeWithChildren,
    ChildNodeBuilder {
  @ExcludeProperty
  override var omitName: Boolean by rebindable(omitName)

  public var checkedStyle: UiToggleButtonStyle? by rebindable(checkedStyle)

  public var disabled: Boolean? by rebindable(disabled)

  public var isChecked: Boolean? by rebindable(isChecked)

  public var style: UiToggleButtonStyle? by rebindable(style)

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
    val clone = UiToggleButton()
    cloneBaseProperties(clone)
    clone.checkedStyle = this.checkedStyle
    clone.disabled = this.disabled
    clone.isChecked = this.isChecked
    clone.style = this.style
    this.children.forEach { child ->
      clone.children.add(child.clone())
    }
    return clone
  }

  public companion object {
    public const val NODE_NAME: String = "ToggleButton"
  }
}
