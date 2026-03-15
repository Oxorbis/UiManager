package cz.creeperface.hytale.uimanager

import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType
import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.`property`.HasDelegates
import cz.creeperface.hytale.uimanager.`property`.Rebindable
import cz.creeperface.hytale.uimanager.event.EventBinding
import cz.creeperface.hytale.uimanager.event.EventContext
import cz.creeperface.hytale.uimanager.event.eventBinding
import kotlin.Any
import kotlin.Boolean
import kotlin.Double
import kotlin.DslMarker
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

  public fun markDirty()

  public fun resetDirty()

  public fun clone(): UiNode

  public fun getEventBindings(): List<EventBinding>
}

public interface UiNodeWithChildren {
  @get:ExcludeProperty
  public val children: MutableList<UiNode>

  public fun addNodeToChildren(node: UiNode) {
    children.add(node)
  }
}

public abstract class BaseUiNode : UiNode, HasDelegates {
  @ExcludeProperty
  override val templates: MutableList<UiNode> = mutableListOf()

  @ExcludeProperty
  override val delegates: MutableMap<String, Rebindable<*>> = mutableMapOf()

  @ExcludeProperty
  open override val isDirty: Boolean
    get() = delegates.values.any { it.isDirty } || templates.any { it.isDirty }

  private val eventBindings: MutableList<EventBinding> = mutableListOf()

  open override fun markDirty() {
    // This is mainly for manual triggers or when a child becomes dirty
  }

  open override fun resetDirty() {
    delegates.values.forEach { it.resetDirty() }
    templates.forEach { it.resetDirty() }
  }

  override fun getEventBindings(): List<EventBinding> = eventBindings

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
