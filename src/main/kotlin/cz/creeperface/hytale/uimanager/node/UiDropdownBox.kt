package cz.creeperface.hytale.uimanager.node

import com.hypixel.hytale.server.core.ui.DropdownEntryInfo
import cz.creeperface.hytale.uimanager.BaseUiNode
import cz.creeperface.hytale.uimanager.ChildNodeBuilder
import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiNode
import cz.creeperface.hytale.uimanager.UiNodeWithChildren
import cz.creeperface.hytale.uimanager.`property`.rebindable
import cz.creeperface.hytale.uimanager.type.UiDropdownBoxStyle
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.collections.List
import kotlin.collections.MutableList

public open class UiDropdownBox(
  @ExcludeProperty
  override var id: String? = null,
  omitName: Boolean = false,
  disabled: Boolean? = null,
  displayNonExistingValue: Boolean? = null,
  entries: List<DropdownEntryInfo?>? = null,
  forcedLabel: String? = null,
  isReadOnly: Boolean? = null,
  maxSelection: Int? = null,
  noItemsText: String? = null,
  panelTitleText: String? = null,
  showLabel: Boolean? = null,
  showSearchInput: Boolean? = null,
  style: UiDropdownBoxStyle? = null,
  `value`: String? = null,
) : BaseUiNode(),
    UiNodeWithChildren,
    ChildNodeBuilder {
  @ExcludeProperty
  override var omitName: Boolean by rebindable(omitName)

  public var disabled: Boolean? by rebindable(disabled)

  public var displayNonExistingValue: Boolean? by rebindable(displayNonExistingValue)

  public var entries: List<DropdownEntryInfo?>? by rebindable(entries)

  public var forcedLabel: String? by rebindable(forcedLabel)

  public var isReadOnly: Boolean? by rebindable(isReadOnly)

  public var maxSelection: Int? by rebindable(maxSelection)

  public var noItemsText: String? by rebindable(noItemsText)

  public var panelTitleText: String? by rebindable(panelTitleText)

  public var showLabel: Boolean? by rebindable(showLabel)

  public var showSearchInput: Boolean? by rebindable(showSearchInput)

  public var style: UiDropdownBoxStyle? by rebindable(style)

  public var `value`: String? by rebindable(value)

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
    val clone = UiDropdownBox()
    cloneBaseProperties(clone)
    clone.disabled = this.disabled
    clone.displayNonExistingValue = this.displayNonExistingValue
    clone.entries = this.entries
    clone.forcedLabel = this.forcedLabel
    clone.isReadOnly = this.isReadOnly
    clone.maxSelection = this.maxSelection
    clone.noItemsText = this.noItemsText
    clone.panelTitleText = this.panelTitleText
    clone.showLabel = this.showLabel
    clone.showSearchInput = this.showSearchInput
    clone.style = this.style
    clone.value = this.value
    this.children.forEach { child ->
      clone.children.add(child.clone())
    }
    return clone
  }

  public companion object {
    public const val NODE_NAME: String = "DropdownBox"
  }
}
