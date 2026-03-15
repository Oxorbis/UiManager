package com.buuz135.mhud

import com.hypixel.hytale.server.core.entity.entities.Player
import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud
import com.hypixel.hytale.server.core.universe.PlayerRef
import cz.creeperface.hytale.uimanager.UiManager
import cz.creeperface.hytale.uimanager.util.ref

object MultipleHUD {

    fun setCustomHud(player: Player, playerRef: PlayerRef, hudIdentifier: String, customHud: CustomUIHud) {
        UiManager.addCustomUiHud(playerRef, hudIdentifier, customHud)
    }

    @JvmOverloads
    fun hideCustomHud(player: Player, playerRef: PlayerRef? = null, hudIdentifier: String) {
        UiManager.removeCustomUiHud(player.ref, hudIdentifier)
    }

    @JvmStatic
    fun getInstance(): MultipleHUD {
        return this
    }
}