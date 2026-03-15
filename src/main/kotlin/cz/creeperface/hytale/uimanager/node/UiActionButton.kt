package cz.creeperface.hytale.uimanager.node

import com.hypixel.hytale.server.core.Message
import cz.creeperface.hytale.uimanager.BaseUiNode
import cz.creeperface.hytale.uimanager.Color
import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiNode
import cz.creeperface.hytale.uimanager.`enum`.ActionButtonAlignment
import cz.creeperface.hytale.uimanager.`enum`.LayoutMode
import cz.creeperface.hytale.uimanager.`enum`.MouseWheelScrollBehaviourType
import cz.creeperface.hytale.uimanager.`property`.rebindable
import cz.creeperface.hytale.uimanager.type.UiAnchor
import cz.creeperface.hytale.uimanager.type.UiButtonStyle
import cz.creeperface.hytale.uimanager.type.UiPadding
import cz.creeperface.hytale.uimanager.type.UiPatchStyle
import cz.creeperface.hytale.uimanager.type.UiTextTooltipStyle
import kotlin.Boolean
import kotlin.Double
import kotlin.Int
import kotlin.String

public open class UiActionButton(
  @ExcludeProperty
  override var id: String? = null,
  omitName: Boolean = false,
  actionName: String? = null,
  alignment: ActionButtonAlignment? = null,
  anchor: UiAnchor? = null,
  autoScrollDown: Boolean? = null,
  background: UiPatchStyle? = null,
  bindingModifier1Label: String? = null,
  bindingModifier2Label: String? = null,
  contentHeight: Int? = null,
  contentWidth: Int? = null,
  disabled: Boolean? = null,
  flexWeight: Int? = null,
  hitTestVisible: Boolean? = null,
  isAvailable: Boolean? = null,
  isHoldBinding: Boolean? = null,
  keepScrollPosition: Boolean? = null,
  keyBindingLabel: String? = null,
  layoutMode: LayoutMode? = null,
  maskTexturePath: String? = null,
  mouseWheelScrollBehaviour: MouseWheelScrollBehaviourType? = null,
  outlineColor: Color? = null,
  outlineSize: Double? = null,
  overscroll: Boolean? = null,
  padding: UiPadding? = null,
  style: UiButtonStyle? = null,
  textTooltipShowDelay: Double? = null,
  textTooltipStyle: UiTextTooltipStyle? = null,
  tooltipText: String? = null,
  tooltipTextSpans: Message? = null,
  visible: Boolean? = null,
) : BaseUiNode() {
  @ExcludeProperty
  override var omitName: Boolean by rebindable(omitName)

  public var actionName: String? by rebindable(actionName)

  public var alignment: ActionButtonAlignment? by rebindable(alignment)

  public var anchor: UiAnchor? by rebindable(anchor)

  public var autoScrollDown: Boolean? by rebindable(autoScrollDown)

  public var background: UiPatchStyle? by rebindable(background)

  public var bindingModifier1Label: String? by rebindable(bindingModifier1Label)

  public var bindingModifier2Label: String? by rebindable(bindingModifier2Label)

  public var contentHeight: Int? by rebindable(contentHeight)

  public var contentWidth: Int? by rebindable(contentWidth)

  public var disabled: Boolean? by rebindable(disabled)

  public var flexWeight: Int? by rebindable(flexWeight)

  public var hitTestVisible: Boolean? by rebindable(hitTestVisible)

  public var isAvailable: Boolean? by rebindable(isAvailable)

  public var isHoldBinding: Boolean? by rebindable(isHoldBinding)

  public var keepScrollPosition: Boolean? by rebindable(keepScrollPosition)

  public var keyBindingLabel: String? by rebindable(keyBindingLabel)

  public var layoutMode: LayoutMode? by rebindable(layoutMode)

  public var maskTexturePath: String? by rebindable(maskTexturePath)

  public var mouseWheelScrollBehaviour: MouseWheelScrollBehaviourType? by
      rebindable(mouseWheelScrollBehaviour)

  public var outlineColor: Color? by rebindable(outlineColor)

  public var outlineSize: Double? by rebindable(outlineSize)

  public var overscroll: Boolean? by rebindable(overscroll)

  public var padding: UiPadding? by rebindable(padding)

  public var style: UiButtonStyle? by rebindable(style)

  public var textTooltipShowDelay: Double? by rebindable(textTooltipShowDelay)

  public var textTooltipStyle: UiTextTooltipStyle? by rebindable(textTooltipStyle)

  public var tooltipText: String? by rebindable(tooltipText)

  public var tooltipTextSpans: Message? by rebindable(tooltipTextSpans)

  public var visible: Boolean? by rebindable(visible)

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
    val clone = UiActionButton()
    clone.id = this.id
    clone.omitName = this.omitName
    clone.actionName = this.actionName
    clone.alignment = this.alignment
    clone.anchor = this.anchor
    clone.autoScrollDown = this.autoScrollDown
    clone.background = this.background
    clone.bindingModifier1Label = this.bindingModifier1Label
    clone.bindingModifier2Label = this.bindingModifier2Label
    clone.contentHeight = this.contentHeight
    clone.contentWidth = this.contentWidth
    clone.disabled = this.disabled
    clone.flexWeight = this.flexWeight
    clone.hitTestVisible = this.hitTestVisible
    clone.isAvailable = this.isAvailable
    clone.isHoldBinding = this.isHoldBinding
    clone.keepScrollPosition = this.keepScrollPosition
    clone.keyBindingLabel = this.keyBindingLabel
    clone.layoutMode = this.layoutMode
    clone.maskTexturePath = this.maskTexturePath
    clone.mouseWheelScrollBehaviour = this.mouseWheelScrollBehaviour
    clone.outlineColor = this.outlineColor
    clone.outlineSize = this.outlineSize
    clone.overscroll = this.overscroll
    clone.padding = this.padding
    clone.style = this.style
    clone.textTooltipShowDelay = this.textTooltipShowDelay
    clone.textTooltipStyle = this.textTooltipStyle
    clone.tooltipText = this.tooltipText
    clone.tooltipTextSpans = this.tooltipTextSpans
    clone.visible = this.visible
    return clone
  }

  public companion object {
    public const val NODE_NAME: String = "ActionButton"
  }
}
