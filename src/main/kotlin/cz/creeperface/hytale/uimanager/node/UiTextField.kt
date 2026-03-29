package cz.creeperface.hytale.uimanager.node

import cz.creeperface.hytale.uimanager.BaseUiNode
import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiNode
import cz.creeperface.hytale.uimanager.`property`.rebindable
import cz.creeperface.hytale.uimanager.type.UiInputFieldDecorationStyle
import cz.creeperface.hytale.uimanager.type.UiInputFieldStyle
import kotlin.Boolean
import kotlin.Char
import kotlin.Int
import kotlin.String

public open class UiTextField(
  @ExcludeProperty
  override var id: String? = null,
  omitName: Boolean = false,
  autoFocus: Boolean? = null,
  autoSelectAll: Boolean? = null,
  decoration: UiInputFieldDecorationStyle? = null,
  isReadOnly: Boolean? = null,
  maxLength: Int? = null,
  passwordChar: Char? = null,
  placeholderStyle: UiInputFieldStyle? = null,
  placeholderText: String? = null,
  style: UiInputFieldStyle? = null,
  `value`: String? = null,
) : BaseUiNode() {
  @ExcludeProperty
  override var omitName: Boolean by rebindable(omitName)

  public var autoFocus: Boolean? by rebindable(autoFocus)

  public var autoSelectAll: Boolean? by rebindable(autoSelectAll)

  public var decoration: UiInputFieldDecorationStyle? by rebindable(decoration)

  public var isReadOnly: Boolean? by rebindable(isReadOnly)

  public var maxLength: Int? by rebindable(maxLength)

  public var passwordChar: Char? by rebindable(passwordChar)

  public var placeholderStyle: UiInputFieldStyle? by rebindable(placeholderStyle)

  public var placeholderText: String? by rebindable(placeholderText)

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
    val clone = UiTextField()
    cloneBaseProperties(clone)
    clone.autoFocus = this.autoFocus
    clone.autoSelectAll = this.autoSelectAll
    clone.decoration = this.decoration
    clone.isReadOnly = this.isReadOnly
    clone.maxLength = this.maxLength
    clone.passwordChar = this.passwordChar
    clone.placeholderStyle = this.placeholderStyle
    clone.placeholderText = this.placeholderText
    clone.style = this.style
    clone.value = this.value
    return clone
  }

  public companion object {
    public const val NODE_NAME: String = "TextField"
  }
}
