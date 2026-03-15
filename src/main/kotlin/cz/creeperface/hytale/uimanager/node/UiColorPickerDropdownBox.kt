package cz.creeperface.hytale.uimanager.node

import com.hypixel.hytale.server.core.Message
import cz.creeperface.hytale.uimanager.BaseUiNode
import cz.creeperface.hytale.uimanager.Color
import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiNode
import cz.creeperface.hytale.uimanager.`enum`.ColorFormat
import cz.creeperface.hytale.uimanager.`enum`.MouseWheelScrollBehaviourType
import cz.creeperface.hytale.uimanager.`property`.rebindable
import cz.creeperface.hytale.uimanager.type.UiAnchor
import cz.creeperface.hytale.uimanager.type.UiColorPickerDropdownBoxStyle
import cz.creeperface.hytale.uimanager.type.UiPadding
import cz.creeperface.hytale.uimanager.type.UiPatchStyle
import cz.creeperface.hytale.uimanager.type.UiTextTooltipStyle
import kotlin.Boolean
import kotlin.Double
import kotlin.Int
import kotlin.String

public open class UiColorPickerDropdownBox(
  @ExcludeProperty
  override var id: String? = null,
  omitName: Boolean = false,
  anchor: UiAnchor? = null,
  autoScrollDown: Boolean? = null,
  background: UiPatchStyle? = null,
  color: Color? = null,
  contentHeight: Int? = null,
  contentWidth: Int? = null,
  displayTextField: Boolean? = null,
  flexWeight: Int? = null,
  format: ColorFormat? = null,
  hitTestVisible: Boolean? = null,
  isReadOnly: Boolean? = null,
  keepScrollPosition: Boolean? = null,
  maskTexturePath: String? = null,
  mouseWheelScrollBehaviour: MouseWheelScrollBehaviourType? = null,
  outlineColor: Color? = null,
  outlineSize: Double? = null,
  overscroll: Boolean? = null,
  padding: UiPadding? = null,
  resetTransparencyWhenChangingColor: Boolean? = null,
  style: UiColorPickerDropdownBoxStyle? = null,
  textTooltipShowDelay: Double? = null,
  textTooltipStyle: UiTextTooltipStyle? = null,
  tooltipText: String? = null,
  tooltipTextSpans: Message? = null,
  visible: Boolean? = null,
) : BaseUiNode() {
  @ExcludeProperty
  override var omitName: Boolean by rebindable(omitName)

  public var anchor: UiAnchor? by rebindable(anchor)

  public var autoScrollDown: Boolean? by rebindable(autoScrollDown)

  public var background: UiPatchStyle? by rebindable(background)

  public var color: Color? by rebindable(color)

  public var contentHeight: Int? by rebindable(contentHeight)

  public var contentWidth: Int? by rebindable(contentWidth)

  public var displayTextField: Boolean? by rebindable(displayTextField)

  public var flexWeight: Int? by rebindable(flexWeight)

  public var format: ColorFormat? by rebindable(format)

  public var hitTestVisible: Boolean? by rebindable(hitTestVisible)

  public var isReadOnly: Boolean? by rebindable(isReadOnly)

  public var keepScrollPosition: Boolean? by rebindable(keepScrollPosition)

  public var maskTexturePath: String? by rebindable(maskTexturePath)

  public var mouseWheelScrollBehaviour: MouseWheelScrollBehaviourType? by
      rebindable(mouseWheelScrollBehaviour)

  public var outlineColor: Color? by rebindable(outlineColor)

  public var outlineSize: Double? by rebindable(outlineSize)

  public var overscroll: Boolean? by rebindable(overscroll)

  public var padding: UiPadding? by rebindable(padding)

  public var resetTransparencyWhenChangingColor: Boolean? by
      rebindable(resetTransparencyWhenChangingColor)

  public var style: UiColorPickerDropdownBoxStyle? by rebindable(style)

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
    val clone = UiColorPickerDropdownBox()
    clone.id = this.id
    clone.omitName = this.omitName
    clone.anchor = this.anchor
    clone.autoScrollDown = this.autoScrollDown
    clone.background = this.background
    clone.color = this.color
    clone.contentHeight = this.contentHeight
    clone.contentWidth = this.contentWidth
    clone.displayTextField = this.displayTextField
    clone.flexWeight = this.flexWeight
    clone.format = this.format
    clone.hitTestVisible = this.hitTestVisible
    clone.isReadOnly = this.isReadOnly
    clone.keepScrollPosition = this.keepScrollPosition
    clone.maskTexturePath = this.maskTexturePath
    clone.mouseWheelScrollBehaviour = this.mouseWheelScrollBehaviour
    clone.outlineColor = this.outlineColor
    clone.outlineSize = this.outlineSize
    clone.overscroll = this.overscroll
    clone.padding = this.padding
    clone.resetTransparencyWhenChangingColor = this.resetTransparencyWhenChangingColor
    clone.style = this.style
    clone.textTooltipShowDelay = this.textTooltipShowDelay
    clone.textTooltipStyle = this.textTooltipStyle
    clone.tooltipText = this.tooltipText
    clone.tooltipTextSpans = this.tooltipTextSpans
    clone.visible = this.visible
    return clone
  }

  public companion object {
    public const val NODE_NAME: String = "ColorPickerDropdownBox"
  }
}
