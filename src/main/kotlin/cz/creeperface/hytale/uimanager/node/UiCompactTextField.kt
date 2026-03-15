package cz.creeperface.hytale.uimanager.node

import com.hypixel.hytale.server.core.Message
import cz.creeperface.hytale.uimanager.BaseUiNode
import cz.creeperface.hytale.uimanager.Color
import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiNode
import cz.creeperface.hytale.uimanager.`enum`.MouseWheelScrollBehaviourType
import cz.creeperface.hytale.uimanager.`property`.rebindable
import cz.creeperface.hytale.uimanager.type.UiAnchor
import cz.creeperface.hytale.uimanager.type.UiInputFieldDecorationStyle
import cz.creeperface.hytale.uimanager.type.UiInputFieldStyle
import cz.creeperface.hytale.uimanager.type.UiPadding
import cz.creeperface.hytale.uimanager.type.UiPatchStyle
import cz.creeperface.hytale.uimanager.type.UiSoundStyle
import cz.creeperface.hytale.uimanager.type.UiTextTooltipStyle
import kotlin.Boolean
import kotlin.Char
import kotlin.Double
import kotlin.Int
import kotlin.String

public open class UiCompactTextField(
  @ExcludeProperty
  override var id: String? = null,
  omitName: Boolean = false,
  anchor: UiAnchor? = null,
  autoFocus: Boolean? = null,
  autoScrollDown: Boolean? = null,
  autoSelectAll: Boolean? = null,
  background: UiPatchStyle? = null,
  collapseSound: UiSoundStyle? = null,
  collapsedWidth: Int? = null,
  contentHeight: Int? = null,
  contentWidth: Int? = null,
  decoration: UiInputFieldDecorationStyle? = null,
  expandSound: UiSoundStyle? = null,
  expandedWidth: Int? = null,
  flexWeight: Int? = null,
  hitTestVisible: Boolean? = null,
  isReadOnly: Boolean? = null,
  keepScrollPosition: Boolean? = null,
  maskTexturePath: String? = null,
  maxLength: Int? = null,
  mouseWheelScrollBehaviour: MouseWheelScrollBehaviourType? = null,
  outlineColor: Color? = null,
  outlineSize: Double? = null,
  overscroll: Boolean? = null,
  padding: UiPadding? = null,
  passwordChar: Char? = null,
  placeholderStyle: UiInputFieldStyle? = null,
  placeholderText: String? = null,
  style: UiInputFieldStyle? = null,
  textTooltipShowDelay: Double? = null,
  textTooltipStyle: UiTextTooltipStyle? = null,
  tooltipText: String? = null,
  tooltipTextSpans: Message? = null,
  `value`: String? = null,
  visible: Boolean? = null,
) : BaseUiNode() {
  @ExcludeProperty
  override var omitName: Boolean by rebindable(omitName)

  public var anchor: UiAnchor? by rebindable(anchor)

  public var autoFocus: Boolean? by rebindable(autoFocus)

  public var autoScrollDown: Boolean? by rebindable(autoScrollDown)

  public var autoSelectAll: Boolean? by rebindable(autoSelectAll)

  public var background: UiPatchStyle? by rebindable(background)

  public var collapseSound: UiSoundStyle? by rebindable(collapseSound)

  public var collapsedWidth: Int? by rebindable(collapsedWidth)

  public var contentHeight: Int? by rebindable(contentHeight)

  public var contentWidth: Int? by rebindable(contentWidth)

  public var decoration: UiInputFieldDecorationStyle? by rebindable(decoration)

  public var expandSound: UiSoundStyle? by rebindable(expandSound)

  public var expandedWidth: Int? by rebindable(expandedWidth)

  public var flexWeight: Int? by rebindable(flexWeight)

  public var hitTestVisible: Boolean? by rebindable(hitTestVisible)

  public var isReadOnly: Boolean? by rebindable(isReadOnly)

  public var keepScrollPosition: Boolean? by rebindable(keepScrollPosition)

  public var maskTexturePath: String? by rebindable(maskTexturePath)

  public var maxLength: Int? by rebindable(maxLength)

  public var mouseWheelScrollBehaviour: MouseWheelScrollBehaviourType? by
      rebindable(mouseWheelScrollBehaviour)

  public var outlineColor: Color? by rebindable(outlineColor)

  public var outlineSize: Double? by rebindable(outlineSize)

  public var overscroll: Boolean? by rebindable(overscroll)

  public var padding: UiPadding? by rebindable(padding)

  public var passwordChar: Char? by rebindable(passwordChar)

  public var placeholderStyle: UiInputFieldStyle? by rebindable(placeholderStyle)

  public var placeholderText: String? by rebindable(placeholderText)

  public var style: UiInputFieldStyle? by rebindable(style)

  public var textTooltipShowDelay: Double? by rebindable(textTooltipShowDelay)

  public var textTooltipStyle: UiTextTooltipStyle? by rebindable(textTooltipStyle)

  public var tooltipText: String? by rebindable(tooltipText)

  public var tooltipTextSpans: Message? by rebindable(tooltipTextSpans)

  public var `value`: String? by rebindable(value)

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
    val clone = UiCompactTextField()
    clone.id = this.id
    clone.omitName = this.omitName
    clone.anchor = this.anchor
    clone.autoFocus = this.autoFocus
    clone.autoScrollDown = this.autoScrollDown
    clone.autoSelectAll = this.autoSelectAll
    clone.background = this.background
    clone.collapseSound = this.collapseSound
    clone.collapsedWidth = this.collapsedWidth
    clone.contentHeight = this.contentHeight
    clone.contentWidth = this.contentWidth
    clone.decoration = this.decoration
    clone.expandSound = this.expandSound
    clone.expandedWidth = this.expandedWidth
    clone.flexWeight = this.flexWeight
    clone.hitTestVisible = this.hitTestVisible
    clone.isReadOnly = this.isReadOnly
    clone.keepScrollPosition = this.keepScrollPosition
    clone.maskTexturePath = this.maskTexturePath
    clone.maxLength = this.maxLength
    clone.mouseWheelScrollBehaviour = this.mouseWheelScrollBehaviour
    clone.outlineColor = this.outlineColor
    clone.outlineSize = this.outlineSize
    clone.overscroll = this.overscroll
    clone.padding = this.padding
    clone.passwordChar = this.passwordChar
    clone.placeholderStyle = this.placeholderStyle
    clone.placeholderText = this.placeholderText
    clone.style = this.style
    clone.textTooltipShowDelay = this.textTooltipShowDelay
    clone.textTooltipStyle = this.textTooltipStyle
    clone.tooltipText = this.tooltipText
    clone.tooltipTextSpans = this.tooltipTextSpans
    clone.value = this.value
    clone.visible = this.visible
    return clone
  }

  public companion object {
    public const val NODE_NAME: String = "CompactTextField"
  }
}
