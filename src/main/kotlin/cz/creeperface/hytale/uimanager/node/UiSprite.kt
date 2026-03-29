package cz.creeperface.hytale.uimanager.node

import cz.creeperface.hytale.uimanager.BaseUiNode
import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiNode
import cz.creeperface.hytale.uimanager.`property`.rebindable
import cz.creeperface.hytale.uimanager.type.UiSpriteFrame
import kotlin.Boolean
import kotlin.Double
import kotlin.Int
import kotlin.String

public open class UiSprite(
  @ExcludeProperty
  override var id: String? = null,
  omitName: Boolean = false,
  angle: Double? = null,
  autoPlay: Boolean? = null,
  frame: UiSpriteFrame? = null,
  framesPerSecond: Int? = null,
  isPlaying: Boolean? = null,
  repeatCount: Int? = null,
  texturePath: String? = null,
) : BaseUiNode() {
  @ExcludeProperty
  override var omitName: Boolean by rebindable(omitName)

  public var angle: Double? by rebindable(angle)

  public var autoPlay: Boolean? by rebindable(autoPlay)

  public var frame: UiSpriteFrame? by rebindable(frame)

  public var framesPerSecond: Int? by rebindable(framesPerSecond)

  public var isPlaying: Boolean? by rebindable(isPlaying)

  public var repeatCount: Int? by rebindable(repeatCount)

  public var texturePath: String? by rebindable(texturePath)

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
    val clone = UiSprite()
    cloneBaseProperties(clone)
    clone.angle = this.angle
    clone.autoPlay = this.autoPlay
    clone.frame = this.frame
    clone.framesPerSecond = this.framesPerSecond
    clone.isPlaying = this.isPlaying
    clone.repeatCount = this.repeatCount
    clone.texturePath = this.texturePath
    return clone
  }

  public companion object {
    public const val NODE_NAME: String = "Sprite"
  }
}
