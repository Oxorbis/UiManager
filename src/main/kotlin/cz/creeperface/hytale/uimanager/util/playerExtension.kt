package cz.creeperface.hytale.uimanager.util

import com.hypixel.hytale.component.Component
import com.hypixel.hytale.component.ComponentType
import com.hypixel.hytale.component.Ref
import com.hypixel.hytale.component.Store
import com.hypixel.hytale.server.core.entity.entities.Player
import com.hypixel.hytale.server.core.universe.PlayerRef
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore

val PlayerRef.player: Player
    get() = requireNotNull(this.component<Player>(Player.getComponentType())) {
        "Player not found."
    }

internal inline fun <reified T: Component<EntityStore>> PlayerRef.component(type: ComponentType<EntityStore, T>): T? {
    val ref = this.reference ?: return null

    return ref.store.getComponent<T>(ref, type)
}

val Player.ref: PlayerRef
    get() {
//        val ref = this.reference!!
//        return ref.store.getComponent(ref, PlayerRef.getComponentType())!!
        return this.playerRef
    }

fun <T : Component<E>, E> Ref<E>.getComponent(type: ComponentType<E, T>): T? {
    return this.store.getComponent(this, type)
}