package cz.creeperface.hytale.uimanager.node

import cz.creeperface.hytale.uimanager.BaseUiNode
import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiNode
import cz.creeperface.hytale.uimanager.`property`.rebindable
import cz.creeperface.hytale.uimanager.type.UiInputFieldDecorationStyle
import cz.creeperface.hytale.uimanager.type.UiInputFieldStyle
import cz.creeperface.hytale.uimanager.type.UiNumberFieldFormat
import kotlin.Boolean
import kotlin.Char
import kotlin.Double
import kotlin.Int
import kotlin.String

public open class UiNumberField(
  @ExcludeProperty
  override var id: String? = null,
  omitName: Boolean = false,
  autoFocus: Boolean? = null,
  autoSelectAll: Boolean? = null,
  decoration: UiInputFieldDecorationStyle? = null,
  format: UiNumberFieldFormat? = null,
  isReadOnly: Boolean? = null,
  maxLength: Int? = null,
  passwordChar: Char? = null,
  placeholderStyle: UiInputFieldStyle? = null,
  style: UiInputFieldStyle? = null,
  `value`: Double? = null,
) : BaseUiNode() {
  @ExcludeProperty
  override var omitName: Boolean by rebindable(omitName)

  public var autoFocus: Boolean? by rebindable(autoFocus)

  public var autoSelectAll: Boolean? by rebindable(autoSelectAll)

  public var decoration: UiInputFieldDecorationStyle? by rebindable(decoration)

  public var format: UiNumberFieldFormat? by rebindable(format)

  public var isReadOnly: Boolean? by rebindable(isReadOnly)

  public var maxLength: Int? by rebindable(maxLength)

  public var passwordChar: Char? by rebindable(passwordChar)

  public var placeholderStyle: UiInputFieldStyle? by rebindable(placeholderStyle)

  public var style: UiInputFieldStyle? by rebindable(style)

  public var `value`: Double? by rebindable(value)

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
    val clone = UiNumberField()
    cloneBaseProperties(clone)
    clone.autoFocus = this.autoFocus
    clone.autoSelectAll = this.autoSelectAll
    clone.decoration = this.decoration
    clone.format = this.format
    clone.isReadOnly = this.isReadOnly
    clone.maxLength = this.maxLength
    clone.passwordChar = this.passwordChar
    clone.placeholderStyle = this.placeholderStyle
    clone.style = this.style
    clone.value = this.value
    return clone
  }

  public companion object {
    public const val NODE_NAME: String = "NumberField"
  }
}
