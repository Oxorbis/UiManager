package cz.creeperface.hytale.uimanager.node

import cz.creeperface.hytale.uimanager.BaseUiNode
import cz.creeperface.hytale.uimanager.Color
import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiNode
import cz.creeperface.hytale.uimanager.`enum`.CodeEditorLanguage
import cz.creeperface.hytale.uimanager.`property`.rebindable
import cz.creeperface.hytale.uimanager.type.UiInputFieldDecorationStyle
import cz.creeperface.hytale.uimanager.type.UiInputFieldStyle
import cz.creeperface.hytale.uimanager.type.UiPadding
import cz.creeperface.hytale.uimanager.type.UiPatchStyle
import cz.creeperface.hytale.uimanager.type.UiScrollbarStyle
import kotlin.Boolean
import kotlin.Int
import kotlin.String

public open class UiCodeEditor(
  @ExcludeProperty
  override var id: String? = null,
  omitName: Boolean = false,
  autoFocus: Boolean? = null,
  autoGrow: Boolean? = null,
  autoSelectAll: Boolean? = null,
  contentPadding: UiPadding? = null,
  decoration: UiInputFieldDecorationStyle? = null,
  isReadOnly: Boolean? = null,
  language: CodeEditorLanguage? = null,
  lineNumberBackground: UiPatchStyle? = null,
  lineNumberPadding: Int? = null,
  lineNumberTextColor: Color? = null,
  lineNumberWidth: Int? = null,
  maxLength: Int? = null,
  maxLines: Int? = null,
  maxVisibleLines: Int? = null,
  placeholderStyle: UiInputFieldStyle? = null,
  placeholderText: String? = null,
  scrollbarStyle: UiScrollbarStyle? = null,
  style: UiInputFieldStyle? = null,
  `value`: String? = null,
) : BaseUiNode() {
  @ExcludeProperty
  override var omitName: Boolean by rebindable(omitName)

  public var autoFocus: Boolean? by rebindable(autoFocus)

  public var autoGrow: Boolean? by rebindable(autoGrow)

  public var autoSelectAll: Boolean? by rebindable(autoSelectAll)

  public var contentPadding: UiPadding? by rebindable(contentPadding)

  public var decoration: UiInputFieldDecorationStyle? by rebindable(decoration)

  public var isReadOnly: Boolean? by rebindable(isReadOnly)

  public var language: CodeEditorLanguage? by rebindable(language)

  public var lineNumberBackground: UiPatchStyle? by rebindable(lineNumberBackground)

  public var lineNumberPadding: Int? by rebindable(lineNumberPadding)

  public var lineNumberTextColor: Color? by rebindable(lineNumberTextColor)

  public var lineNumberWidth: Int? by rebindable(lineNumberWidth)

  public var maxLength: Int? by rebindable(maxLength)

  public var maxLines: Int? by rebindable(maxLines)

  public var maxVisibleLines: Int? by rebindable(maxVisibleLines)

  public var placeholderStyle: UiInputFieldStyle? by rebindable(placeholderStyle)

  public var placeholderText: String? by rebindable(placeholderText)

  public var scrollbarStyle: UiScrollbarStyle? by rebindable(scrollbarStyle)

  public var style: UiInputFieldStyle? by rebindable(style)

  public var `value`: String? by rebindable(value)

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
    cloneBaseProperties(clone)
    clone.autoFocus = this.autoFocus
    clone.autoGrow = this.autoGrow
    clone.autoSelectAll = this.autoSelectAll
    clone.contentPadding = this.contentPadding
    clone.decoration = this.decoration
    clone.isReadOnly = this.isReadOnly
    clone.language = this.language
    clone.lineNumberBackground = this.lineNumberBackground
    clone.lineNumberPadding = this.lineNumberPadding
    clone.lineNumberTextColor = this.lineNumberTextColor
    clone.lineNumberWidth = this.lineNumberWidth
    clone.maxLength = this.maxLength
    clone.maxLines = this.maxLines
    clone.maxVisibleLines = this.maxVisibleLines
    clone.placeholderStyle = this.placeholderStyle
    clone.placeholderText = this.placeholderText
    clone.scrollbarStyle = this.scrollbarStyle
    clone.style = this.style
    clone.value = this.value
    return clone
  }

  public companion object {
    public const val NODE_NAME: String = "CodeEditor"
  }
}
