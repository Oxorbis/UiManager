package cz.creeperface.hytale.uimanager.node

import cz.creeperface.hytale.uimanager.BaseUiNode
import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiNode
import cz.creeperface.hytale.uimanager.`property`.rebindable
import kotlin.Boolean
import kotlin.String

public open class UiSceneBlur(
  @ExcludeProperty
  override var id: String? = null,
  omitName: Boolean = false,
) : BaseUiNode() {
  @ExcludeProperty
  override var omitName: Boolean by rebindable(omitName)

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
    val clone = UiSceneBlur()
    cloneBaseProperties(clone)
    return clone
  }

  public companion object {
    public const val NODE_NAME: String = "SceneBlur"
  }
}
