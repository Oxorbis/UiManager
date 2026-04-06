package cz.creeperface.hytale.uimanager

import aster.amo.kytale.dsl.event
import com.buuz135.mhud.MultipleHUD
import com.hypixel.hytale.protocol.packets.interface_.CustomHud
import com.hypixel.hytale.server.core.HytaleServer
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent
import com.hypixel.hytale.server.core.io.PacketHandler
import com.hypixel.hytale.server.core.plugin.JavaPlugin
import com.hypixel.hytale.server.core.plugin.JavaPluginInit
import cz.creeperface.hytale.uimanager.util.DelegatedChannel
import io.netty.channel.Channel
import java.util.concurrent.TimeUnit

class UiManagerPlugin(init: JavaPluginInit) : JavaPlugin(init) {

    override fun setup() {
        MultipleHUD.getInstance()

        event<PlayerConnectEvent> { event ->
            try {
                logger.atInfo().log("Injecting player network channel wrapper...")

                val playerRef = event.playerRef
                val packetHandler = event.playerRef.packetHandler

                val channelsField = PacketHandler::class.java.getDeclaredField("channels")
                channelsField.isAccessible = true
                val channels = channelsField.get(packetHandler) as Array<Channel?>

                var injected = false

                channels.indices.forEach { i ->
                    val channel = channels[i]

                    if (channel == null) {
                        logger.atInfo().log("Skipping null channel at index: $i")
                        return@forEach
                    }

                    channels[i] = DelegatedChannel(channel) { msg ->
                        if (msg is CustomHud && msg.clear && !UiManager.firstSendPlayers.contains(playerRef)) {
                            throw RuntimeException("Another plugin tried to clear player ${playerRef.username} HUD")
                        }
                    }

                    injected = true
                    logger.atInfo().log("Injected channel wrapper at index: $i")
                }

                if (!injected) {
                    logger.atWarning().log("Could not injecting player network channel wrapper.")
                }

            } catch (e: Throwable) {
                logger.atSevere().withCause(e)
                    .log("Could not set packet handler delegate for player ${event.playerRef.username}")
            }
        }

        event<PlayerDisconnectEvent> { event ->
            UiManager.onPlayerDisconnect(event.playerRef)
        }
    }

    override fun start() {
        HytaleServer.SCHEDULED_EXECUTOR.scheduleAtFixedRate(
            {
                try {
                    UiManager.update()
                } catch (e: Exception) {
                    logger.atSevere().withCause(e).log("UI update failed")
                }
            },
            0,
            100,
            TimeUnit.MILLISECONDS,
        )
    }
}