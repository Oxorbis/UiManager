package cz.creeperface.hytale.uimanager.node

import com.hypixel.hytale.server.core.Message
import cz.creeperface.hytale.uimanager.BaseUiNode
import cz.creeperface.hytale.uimanager.Color
import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiNode
import cz.creeperface.hytale.uimanager.`enum`.CodeEditorLanguage
import cz.creeperface.hytale.uimanager.`enum`.MouseWheelScrollBehaviourType
import cz.creeperface.hytale.uimanager.`property`.rebindable
import cz.creeperface.hytale.uimanager.type.UiAnchor
import cz.creeperface.hytale.uimanager.type.UiInputFieldDecorationStyle
import cz.creeperface.hytale.uimanager.type.UiInputFieldStyle
import cz.creeperface.hytale.uimanager.type.UiPadding
import cz.creeperface.hytale.uimanager.type.UiPatchStyle
import cz.creeperface.hytale.uimanager.type.UiScrollbarStyle
import cz.creeperface.hytale.uimanager.type.UiTextTooltipStyle
import kotlin.Boolean
import kotlin.Double
import kotlin.Int
import kotlin.String

public open class UiCodeEditor(
  @ExcludeProperty
  override var id: String? = null,
  omitName: Boolean = false,
  anchor: UiAnchor? = null,
  autoFocus: Boolean? = null,
  autoGrow: Boolean? = null,
  autoScrollDown: Boolean? = null,
  autoSelectAll: Boolean? = null,
  background: UiPatchStyle? = null,
  contentHeight: Int? = null,
  contentPadding: UiPadding? = null,
  contentWidth: Int? = null,
  decoration: UiInputFieldDecorationStyle? = null,
  flexWeight: Int? = null,
  hitTestVisible: Boolean? = null,
  isReadOnly: Boolean? = null,
  keepScrollPosition: Boolean? = null,
  language: CodeEditorLanguage? = null,
  lineNumberBackground: UiPatchStyle? = null,
  lineNumberPadding: Int? = null,
  lineNumberTextColor: Color? = null,
  lineNumberWidth: Int? = null,
  maskTexturePath: String? = null,
  maxLength: Int? = null,
  maxLines: Int? = null,
  maxVisibleLines: Int? = null,
  mouseWheelScrollBehaviour: MouseWheelScrollBehaviourType? = null,
  outlineColor: Color? = null,
  outlineSize: Double? = null,
  overscroll: Boolean? = null,
  padding: UiPadding? = null,
  placeholderStyle: UiInputFieldStyle? = null,
  placeholderText: String? = null,
  scrollbarStyle: UiScrollbarStyle? = null,
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

  public var autoGrow: Boolean? by rebindable(autoGrow)

  public var autoScrollDown: Boolean? by rebindable(autoScrollDown)

  public var autoSelectAll: Boolean? by rebindable(autoSelectAll)

  public var background: UiPatchStyle? by rebindable(background)

  public var contentHeight: Int? by rebindable(contentHeight)

  public var contentPadding: UiPadding? by rebindable(contentPadding)

  public var contentWidth: Int? by rebindable(contentWidth)

  public var decoration: UiInputFieldDecorationStyle? by rebindable(decoration)

  public var flexWeight: Int? by rebindable(flexWeight)

  public var hitTestVisible: Boolean? by rebindable(hitTestVisible)

  public var isReadOnly: Boolean? by rebindable(isReadOnly)

  public var keepScrollPosition: Boolean? by rebindable(keepScrollPosition)

  public var language: CodeEditorLanguage? by rebindable(language)

  public var lineNumberBackground: UiPatchStyle? by rebindable(lineNumberBackground)

  public var lineNumberPadding: Int? by rebindable(lineNumberPadding)

  public var lineNumberTextColor: Color? by rebindable(lineNumberTextColor)

  public var lineNumberWidth: Int? by rebindable(lineNumberWidth)

  public var maskTexturePath: String? by rebindable(maskTexturePath)

  public var maxLength: Int? by rebindable(maxLength)

  public var maxLines: Int? by rebindable(maxLines)

  public var maxVisibleLines: Int? by rebindable(maxVisibleLines)

  public var mouseWheelScrollBehaviour: MouseWheelScrollBehaviourType? by
      rebindable(mouseWheelScrollBehaviour)

  public var outlineColor: Color? by rebindable(outlineColor)

  public var outlineSize: Double? by rebindable(outlineSize)

  public var overscroll: Boolean? by rebindable(overscroll)

  public var padding: UiPadding? by rebindable(padding)

  public var placeholderStyle: UiInputFieldStyle? by rebindable(placeholderStyle)

  public var placeholderText: String? by rebindable(placeholderText)

  public var scrollbarStyle: UiScrollbarStyle? by rebindable(scrollbarStyle)

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
    val clone = UiCodeEditor()
    clone.id = this.id
    clone.omitName = this.omitName
    clone.anchor = this.anchor
    clone.autoFocus = this.autoFocus
    clone.autoGrow = this.autoGrow
    clone.autoScrollDown = this.autoScrollDown
    clone.autoSelectAll = this.autoSelectAll
    clone.background = this.background
    clone.contentHeight = this.contentHeight
    clone.contentPadding = this.contentPadding
    clone.contentWidth = this.contentWidth
    clone.decoration = this.decoration
    clone.flexWeight = this.flexWeight
    clone.hitTestVisible = this.hitTestVisible
    clone.isReadOnly = this.isReadOnly
    clone.keepScrollPosition = this.keepScrollPosition
    clone.language = this.language
    clone.lineNumberBackground = this.lineNumberBackground
    clone.lineNumberPadding = this.lineNumberPadding
    clone.lineNumberTextColor = this.lineNumberTextColor
    clone.lineNumberWidth = this.lineNumberWidth
    clone.maskTexturePath = this.maskTexturePath
    clone.maxLength = this.maxLength
    clone.maxLines = this.maxLines
    clone.maxVisibleLines = this.maxVisibleLines
    clone.mouseWheelScrollBehaviour = this.mouseWheelScrollBehaviour
    clone.outlineColor = this.outlineColor
    clone.outlineSize = this.outlineSize
    clone.overscroll = this.overscroll
    clone.padding = this.padding
    clone.placeholderStyle = this.placeholderStyle
    clone.placeholderText = this.placeholderText
    clone.scrollbarStyle = this.scrollbarStyle
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
    public const val NODE_NAME: String = "CodeEditor"
  }
}
