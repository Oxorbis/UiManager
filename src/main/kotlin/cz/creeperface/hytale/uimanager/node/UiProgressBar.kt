package cz.creeperface.hytale.uimanager.node

import com.hypixel.hytale.server.core.Message
import cz.creeperface.hytale.uimanager.BaseUiNode
import cz.creeperface.hytale.uimanager.Color
import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiNode
import cz.creeperface.hytale.uimanager.`enum`.MouseWheelScrollBehaviourType
import cz.creeperface.hytale.uimanager.`enum`.ProgressBarAlignment
import cz.creeperface.hytale.uimanager.`enum`.ProgressBarDirection
import cz.creeperface.hytale.uimanager.`property`.rebindable
import cz.creeperface.hytale.uimanager.type.UiAnchor
import cz.creeperface.hytale.uimanager.type.UiPadding
import cz.creeperface.hytale.uimanager.type.UiPatchStyle
import cz.creeperface.hytale.uimanager.type.UiTextTooltipStyle
import kotlin.Boolean
import kotlin.Double
import kotlin.Int
import kotlin.String

public open class UiProgressBar(
  @ExcludeProperty
  override var id: String? = null,
  omitName: Boolean = false,
  alignment: ProgressBarAlignment? = null,
  anchor: UiAnchor? = null,
  autoScrollDown: Boolean? = null,
  background: UiPatchStyle? = null,
  bar: UiPatchStyle? = null,
  barTexturePath: String? = null,
  contentHeight: Int? = null,
  contentWidth: Int? = null,
  direction: ProgressBarDirection? = null,
  effectHeight: Int? = null,
  effectOffset: Int? = null,
  effectTexturePath: String? = null,
  effectWidth: Int? = null,
  flexWeight: Int? = null,
  hitTestVisible: Boolean? = null,
  keepScrollPosition: Boolean? = null,
  maskTexturePath: String? = null,
  mouseWheelScrollBehaviour: MouseWheelScrollBehaviourType? = null,
  outlineColor: Color? = null,
  outlineSize: Double? = null,
  overscroll: Boolean? = null,
  padding: UiPadding? = null,
  textTooltipShowDelay: Double? = null,
  textTooltipStyle: UiTextTooltipStyle? = null,
  tooltipText: String? = null,
  tooltipTextSpans: Message? = null,
  `value`: Double? = null,
  visible: Boolean? = null,
) : BaseUiNode() {
  @ExcludeProperty
  override var omitName: Boolean by rebindable(omitName)

  public var alignment: ProgressBarAlignment? by rebindable(alignment)

  public var anchor: UiAnchor? by rebindable(anchor)

  public var autoScrollDown: Boolean? by rebindable(autoScrollDown)

  public var background: UiPatchStyle? by rebindable(background)

  public var bar: UiPatchStyle? by rebindable(bar)

  public var barTexturePath: String? by rebindable(barTexturePath)

  public var contentHeight: Int? by rebindable(contentHeight)

  public var contentWidth: Int? by rebindable(contentWidth)

  public var direction: ProgressBarDirection? by rebindable(direction)

  public var effectHeight: Int? by rebindable(effectHeight)

  public var effectOffset: Int? by rebindable(effectOffset)

  public var effectTexturePath: String? by rebindable(effectTexturePath)

  public var effectWidth: Int? by rebindable(effectWidth)

  public var flexWeight: Int? by rebindable(flexWeight)

  public var hitTestVisible: Boolean? by rebindable(hitTestVisible)

  public var keepScrollPosition: Boolean? by rebindable(keepScrollPosition)

  public var maskTexturePath: String? by rebindable(maskTexturePath)

  public var mouseWheelScrollBehaviour: MouseWheelScrollBehaviourType? by
      rebindable(mouseWheelScrollBehaviour)

  public var outlineColor: Color? by rebindable(outlineColor)

  public var outlineSize: Double? by rebindable(outlineSize)

  public var overscroll: Boolean? by rebindable(overscroll)

  public var padding: UiPadding? by rebindable(padding)

  public var textTooltipShowDelay: Double? by rebindable(textTooltipShowDelay)

  public var textTooltipStyle: UiTextTooltipStyle? by rebindable(textTooltipStyle)

  public var tooltipText: String? by rebindable(tooltipText)

  public var tooltipTextSpans: Message? by rebindable(tooltipTextSpans)

  public var `value`: Double? by rebindable(value)

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
    val clone = UiProgressBar()
    clone.id = this.id
    clone.omitName = this.omitName
    clone.alignment = this.alignment
    clone.anchor = this.anchor
    clone.autoScrollDown = this.autoScrollDown
    clone.background = this.background
    clone.bar = this.bar
    clone.barTexturePath = this.barTexturePath
    clone.contentHeight = this.contentHeight
    clone.contentWidth = this.contentWidth
    clone.direction = this.direction
    clone.effectHeight = this.effectHeight
    clone.effectOffset = this.effectOffset
    clone.effectTexturePath = this.effectTexturePath
    clone.effectWidth = this.effectWidth
    clone.flexWeight = this.flexWeight
    clone.hitTestVisible = this.hitTestVisible
    clone.keepScrollPosition = this.keepScrollPosition
    clone.maskTexturePath = this.maskTexturePath
    clone.mouseWheelScrollBehaviour = this.mouseWheelScrollBehaviour
    clone.outlineColor = this.outlineColor
    clone.outlineSize = this.outlineSize
    clone.overscroll = this.overscroll
    clone.padding = this.padding
    clone.textTooltipShowDelay = this.textTooltipShowDelay
    clone.textTooltipStyle = this.textTooltipStyle
    clone.tooltipText = this.tooltipText
    clone.tooltipTextSpans = this.tooltipTextSpans
    clone.value = this.value
    clone.visible = this.visible
    return clone
  }

  public companion object {
    public const val NODE_NAME: String = "ProgressBar"
  }
}
