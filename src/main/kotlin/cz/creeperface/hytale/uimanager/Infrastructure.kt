package cz.creeperface.hytale.uimanager

import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType
import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.`enum`.MouseWheelScrollBehaviourType
import cz.creeperface.hytale.uimanager.`property`.HasDelegates
import cz.creeperface.hytale.uimanager.`property`.Rebindable
import cz.creeperface.hytale.uimanager.`property`.rebindable
import cz.creeperface.hytale.uimanager.event.EventBinding
import cz.creeperface.hytale.uimanager.event.EventContext
import cz.creeperface.hytale.uimanager.event.eventBinding
import cz.creeperface.hytale.uimanager.type.UiAnchor
import cz.creeperface.hytale.uimanager.type.UiPadding
import cz.creeperface.hytale.uimanager.type.UiPatchStyle
import cz.creeperface.hytale.uimanager.type.UiTextTooltipStyle
import kotlin.Any
import kotlin.Boolean
import kotlin.Double
import kotlin.DslMarker
import kotlin.Int
import kotlin.String
import kotlin.Unit
import kotlin.`annotation`.AnnotationRetention
import kotlin.`annotation`.AnnotationTarget
import kotlin.`annotation`.Retention
import kotlin.`annotation`.Target
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.jvm.JvmStatic
import kotlin.reflect.KProperty0

@DslMarker
public annotation class UiDsl

@Target(
  AnnotationTarget.PROPERTY,
  AnnotationTarget.FIELD,
  AnnotationTarget.FUNCTION,
  AnnotationTarget.PROPERTY_GETTER,
)
@Retention(AnnotationRetention.RUNTIME)
public annotation class ExcludeProperty

public interface ChildNodeBuilder : UiNodeWithChildren {
  public fun addNode(node: UiNode)
}

@UiDsl
public interface UiNode {
  @get:ExcludeProperty
  public var id: String?

  @get:ExcludeProperty
  public var omitName: Boolean

  @get:ExcludeProperty
  public val templates: MutableList<UiNode>

  @get:ExcludeProperty
  public val isDirty: Boolean

  @get:ExcludeProperty
  public var nodeListener: ((node: UiNode) -> Unit)?

  public var anchor: UiAnchor?

  public var autoScrollDown: Boolean?

  public var background: UiPatchStyle?

  public var contentHeight: Int?

  public var contentWidth: Int?

  public var flexWeight: Int?

  public var hitTestVisible: Boolean?

  public var keepScrollPosition: Boolean?

  public var maskTexturePath: String?

  public var mouseWheelScrollBehaviour: MouseWheelScrollBehaviourType?

  public var outlineColor: Color?

  public var outlineSize: Double?

  public var overscroll: Boolean?

  public var padding: UiPadding?

  public var textTooltipShowDelay: Double?

  public var textTooltipStyle: UiTextTooltipStyle?

  public var tooltipText: String?

  public var tooltipTextSpans: String?

  public var visible: Boolean?

  public fun markDirty()

  public fun resetDirty()

  public fun clone(): UiNode

  public fun getEventBindings(): List<EventBinding>
}

public interface UiNodeWithChildren {
  @get:ExcludeProperty
  public val children: MutableList<UiNode>

  @get:ExcludeProperty
  public var nodeListener: ((node: UiNode) -> Unit)?

  public fun addNodeToChildren(node: UiNode) {
    children.add(node)
    nodeListener?.invoke(node)
  }
}

public abstract class BaseUiNode : UiNode, HasDelegates {
  @ExcludeProperty
  override val templates: MutableList<UiNode> = mutableListOf()

  @ExcludeProperty
  override val delegates: MutableMap<String, Rebindable<*>> = mutableMapOf()

  @ExcludeProperty
  override var nodeListener: ((node: UiNode) -> Unit)? = null

  @ExcludeProperty
  open override val isDirty: Boolean
    get() = delegates.values.any { it.isDirty } || templates.any { it.isDirty }

  private val eventBindings: MutableList<EventBinding> = mutableListOf()

  override var anchor: UiAnchor? by rebindable(null)

  override var autoScrollDown: Boolean? by rebindable(null)

  override var background: UiPatchStyle? by rebindable(null)

  override var contentHeight: Int? by rebindable(null)

  override var contentWidth: Int? by rebindable(null)

  override var flexWeight: Int? by rebindable(null)

  override var hitTestVisible: Boolean? by rebindable(null)

  override var keepScrollPosition: Boolean? by rebindable(null)

  override var maskTexturePath: String? by rebindable(null)

  override var mouseWheelScrollBehaviour: MouseWheelScrollBehaviourType? by rebindable(null)

  override var outlineColor: Color? by rebindable(null)

  override var outlineSize: Double? by rebindable(null)

  override var overscroll: Boolean? by rebindable(null)

  override var padding: UiPadding? by rebindable(null)

  override var textTooltipShowDelay: Double? by rebindable(null)

  override var textTooltipStyle: UiTextTooltipStyle? by rebindable(null)

  override var tooltipText: String? by rebindable(null)

  override var tooltipTextSpans: String? by rebindable(null)

  override var visible: Boolean? by rebindable(null)

  open override fun markDirty() {
    // This is mainly for manual triggers or when a child becomes dirty
  }

  open override fun resetDirty() {
    delegates.values.forEach { it.resetDirty() }
    templates.forEach { it.resetDirty() }
  }

  override fun getEventBindings(): List<EventBinding> = eventBindings

  protected fun cloneBaseProperties(target: BaseUiNode) {
    target.id = this.id
    target.omitName = this.omitName
    target.anchor = this.anchor
    target.autoScrollDown = this.autoScrollDown
    target.background = this.background
    target.contentHeight = this.contentHeight
    target.contentWidth = this.contentWidth
    target.flexWeight = this.flexWeight
    target.hitTestVisible = this.hitTestVisible
    target.keepScrollPosition = this.keepScrollPosition
    target.maskTexturePath = this.maskTexturePath
    target.mouseWheelScrollBehaviour = this.mouseWheelScrollBehaviour
    target.outlineColor = this.outlineColor
    target.outlineSize = this.outlineSize
    target.overscroll = this.overscroll
    target.padding = this.padding
    target.textTooltipShowDelay = this.textTooltipShowDelay
    target.textTooltipStyle = this.textTooltipStyle
    target.tooltipText = this.tooltipText
    target.tooltipTextSpans = this.tooltipTextSpans
    target.visible = this.visible
  }

  public fun addEventBinding(
    type: CustomUIEventBindingType,
    nodeId: String,
    vararg properties: KProperty0<Any?>,
    action: (context: EventContext) -> Unit,
  ) {
    eventBindings.add(eventBinding(type, nodeId, *properties, action = action))
  }

  public fun addEventBinding(eventBinding: EventBinding) {
    eventBindings.add(eventBinding)
  }
}

@UiDsl
public interface UiType {
  @get:ExcludeProperty
  public val templates: MutableList<UiType>
}

public data class Color(
  public val hex: String,
  public val alpha: Double? = null,
) {
  override fun toString(): String = if (alpha != null) "$hex($alpha)" else hex

  public companion object {
    public operator fun invoke(`value`: String): Color = if (value.contains('(') && value.endsWith(')')) {
      val hex = value.substringBefore('(')
      val alpha = value.substringAfter('(').removeSuffix(")").toDoubleOrNull()
      Color(hex, alpha)
    } else {
      Color(value, null)
    }
  }
}
