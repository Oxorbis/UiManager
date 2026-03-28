package cz.creeperface.hytale.uimanager

import aster.amo.kytale.dsl.command
import aster.amo.kytale.dsl.event
import com.buuz135.mhud.MultipleHUD
import com.hypixel.hytale.protocol.packets.interface_.CustomHud
import com.hypixel.hytale.protocol.packets.interface_.CustomPage
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime
import com.hypixel.hytale.server.core.HytaleServer
import com.hypixel.hytale.server.core.command.system.CommandContext
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase
import com.hypixel.hytale.server.core.entity.entities.Player
import com.hypixel.hytale.server.core.entity.entities.player.hud.HudManager
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent
import com.hypixel.hytale.server.core.io.PacketHandler
import com.hypixel.hytale.server.core.plugin.JavaPlugin
import com.hypixel.hytale.server.core.plugin.JavaPluginInit
import com.hypixel.hytale.server.core.ui.Anchor
import com.hypixel.hytale.server.core.ui.Value
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder
import com.hypixel.hytale.server.core.universe.PlayerRef
import com.hypixel.hytale.server.core.universe.Universe
import cz.creeperface.hytale.*
import cz.creeperface.hytale.uimanager.builder.*
import cz.creeperface.hytale.uimanager.enum.LabelAlignment
import cz.creeperface.hytale.uimanager.enum.LayoutMode
import cz.creeperface.hytale.uimanager.templates.CommonTemplate
import cz.creeperface.hytale.uimanager.templates.CommonTemplate.defaultTextButton
import cz.creeperface.hytale.uimanager.templates.CommonTemplate.defaultTitle
import cz.creeperface.hytale.uimanager.templates.CommonTemplate.pageOverlay
import cz.creeperface.hytale.uimanager.templates.decoratedContainer
import cz.creeperface.hytale.uimanager.type.*
import cz.creeperface.hytale.uimanager.util.DelegatedChannel
import cz.creeperface.hytale.uimanager.util.ref
import io.netty.channel.Channel
import org.bson.BsonDocument
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.milliseconds

class UiManagerPlugin(init: JavaPluginInit) : JavaPlugin(init) {

    val bwData = BwData(
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("mm:ss"))
    )

    override fun setup() {
        MultipleHUD.getInstance()

        val anchor2 = Anchor()
        anchor2.setHeight(Value.of(100))
        anchor2.setWidth(Value.of(200))

        val bson = Anchor.CODEC.encode(anchor2)

        val valueWrapper = BsonDocument()
        valueWrapper.put("0", bson)

        // Bson: {"0": {"Height": 100, "Width": 200}}
        logger.atInfo().log("BSON DEBUG")
        logger.atInfo().log("Bson: " + valueWrapper.toJson())

        val test = Class.forName("com.buuz135.mhud.MultipleHUD")

        UiManager.registerDynamicHudWithAutoUpdate(
            "bwtest",
            { _ ->
                createBedWarsHud(bwData)
            },
            500.milliseconds
        )

        UiManager.registerPage("formtest", Unit) { _, _ ->
            createSampleForm()
        }

        UiManager.registerPage("barter", Unit) { _, _ ->
            createBlockPageInstanceConfigurationPage()
        }

        UiManager.registerPage("dynamicPage", Unit) { playerRef, _ ->
            pageOverlay {
                layoutMode = LayoutMode.Middle

                decoratedContainer {
                    anchor = UiAnchor(width = 532)

                    title {
                        defaultTitle {
                            text = "Page title"
                        }
                    }

                    content {
//                        group {
//                            id = "MainTabs"
//                            anchor = anchor { height = 66; bottom = 8; }
//
//                            if (playerRef != null) {
////                                for (i in 1..5) {
//                                    textButton {
////                                        id = "tab$i"
//                                        id = ""
//                                        anchor = anchor { width = 100; height = 30; }
//                                        style = textButtonStyle {
//                                            default = textButtonStyleState {
//                                                background = patchStyle {
//                                                    texturePath = CommonTemplate.UI_ROOT + "Common/RecipesIcon.png"
//                                                }
//                                            }
//                                        }
////                                        background = patchStyle {
////                                            texturePath = CommonTemplate.UI_ROOT + "Common/RecipesIcon.png"
////                                        }
//                                        tooltipText = "Tab"
//                                        text = "Test button"
//                                    }
////                                }
//                            }
//                        }
                        if (playerRef != null) {
                            tabNavigation {
                                id = "MainTabs"
                                anchor = anchor { height = 66; bottom = 8; }
                                style = CommonTemplate.topTabsStyle
                                selectedTab = "Tab1"

                                tabButton {
                                    id = "tab0"
                                    icon = patchStyle {
                                        texturePath = CommonTemplate.UI_ROOT + "Common/RecipesIcon.png"
                                    }
                                    tooltipText = "Tab0"
                                }

                                tabButton {
                                    id = "tab1"
                                    icon = patchStyle {
                                        texturePath = CommonTemplate.UI_ROOT + "Common/RecipesIcon.png"
                                    }
                                    tooltipText = "Tab1"
                                }

                                tabButton {
                                    id = "tab2"
                                    icon = patchStyle {
                                        texturePath = CommonTemplate.UI_ROOT + "Common/RecipesIcon.png"
                                    }
                                    tooltipText = "Tab2"
                                }

//                            for (i in 1..5) {
//                                tabButton {
//                                    id = "tab$i"
//                                    icon = patchStyle {
//                                        texturePath = CommonTemplate.UI_ROOT + "Common/RecipesIcon.png"
//                                    }
//                                    tooltipText = "Tab$i"
//                                }
//                            }

//                                for (i in 1..1) {
//                                    tabButton {
//                                        id = "tab$i"
////                                        icon = patchStyle {
////                                            texturePath = CommonTemplate.UI_ROOT + "Common/RecipesIcon.png"
////                                        }
//                                        tooltipText = "Tab$i"
//                                    }
//                                }
                            }
                        }

                        defaultTitle {
//                            text = playerRef?.username ?: ""
                            text = ""
                        }

                        if (playerRef != null) {
                            defaultTitle {
                                text = "Logged in"
                            }
                        }

                        group {
                            if (playerRef != null) {
                                defaultTextButton {
                                    text = "Logout"
                                }
                            } else {
                                defaultTextButton {
                                    text = "Login"
                                }
                            }
                        }
                    }
                }
            }
        }

        val testBarterData = BarterData(
            listOf(
                BarterTrade(
                    input = BarterTradeItem(
                        "Ingredient_Bar_Thorium",
                        5
                    ),
                    output = BarterTradeItem(
                        "Tool_Shovel_Thorium",
                        1
                    ),
                    10,
                    6
                ),
                BarterTrade(
                    input = BarterTradeItem(
                        "Ingredient_Bar_Cobalt",
                        3
                    ),
                    output = BarterTradeItem(
                        "Tool_Hatchet_Cobalt",
                        1
                    ),
                    10,
                    13
                ),
                BarterTrade(
                    input = BarterTradeItem(
                        "Ingredient_Bar_Adamantite",
                        10
                    ),
                    output = BarterTradeItem(
                        "Tool_Hatchet_Adamantite",
                        1
                    ),
                    10,
                    5
                )
            )
        )

        UiManager.registerPage("barter2", testBarterData.copy()) { _, data ->
            createBarterPage(data)
        }

//        UiManager.registerDyna("formtest", )

        HytaleServer.SCHEDULED_EXECUTOR.scheduleAtFixedRate(
            {
                bwData.time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("mm:ss"))
//                try {
//                    Universe.get().players.forEach { playerRef ->
//                        UiManager.update(playerRef, "bwtest", Unit)
//                    }
//                } catch (e: Exception) {
//                    logger.atSevere().withCause(e).log("Updating failed")
//                }
            },
            1000,
            1000,
            TimeUnit.MILLISECONDS
        )

//        UiManager.registerDynamicPageWithAutoUpdate(
//            "bwtest",
//            { playerRef ->
//                createBedWarsHud()
//            },
//            10.milliseconds
//        )

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

        event<PlayerReadyEvent> { event ->
            val field = event.player::class.java.getDeclaredField("hudManager")
            field.setAccessible(true)
            field.set(event.player, HudManager())

            val playerRef = event.playerRef.store.getComponent(
                event.playerRef,
                PlayerRef.getComponentType()
            )!!

//            HytaleServer.SCHEDULED_EXECUTOR.schedule({
//                UiManager.showDynamicHud("bwtest", playerRef)
//            }, 5, TimeUnit.SECONDS)
        }

        event<PlayerDisconnectEvent> { event ->
            UiManager.onPlayerDisconnect(event.playerRef)
        }

        command("dynamicPage", "") {
            executes { ctx ->
                (ctx.sender() as? Player)?.let { player ->
                    player.world?.execute {
                        UiManager.showPage(player.ref, "dynamicPage", Unit)
                    }
                }
            }
        }

        command("page", "Show page") {
            this.executes { context ->
                (context.sender() as? Player)?.let { player ->
                    player.world?.execute {
                        UiManager.showPage(
                            player.ref,
                            "barter2",
                            testBarterData.copy()
                        )
                    }
                }
            }
        }

        command("updatepage2", "Update page") {
            executes { ctx ->
                Universe.get().players.forEach { player ->
                    UiManager.updatePage(
                        player,
                        "barter2",
                        BarterData(
                            listOf(
                                BarterTrade(
                                    input = BarterTradeItem(
                                        "Ingredient_Bar_Thorium",
                                        5
                                    ),
                                    output = BarterTradeItem(
                                        "Tool_Shovel_Thorium",
                                        1
                                    ),
                                    10,
                                    6
                                ),
                                BarterTrade(
                                    input = BarterTradeItem(
                                        "Ingredient_Bar_Adamantite",
                                        10
                                    ),
                                    output = BarterTradeItem(
                                        "Tool_Hatchet_Adamantite",
                                        1
                                    ),
                                    10,
                                    5
                                )
                            )
                        )
                    )
                }
            }
        }

        commandRegistry.registerCommand(object : CommandBase("updatepage", "Update page") {

            val pageId = withRequiredArg("pageId", "Page ID", ArgTypes.STRING)
            val action = withRequiredArg("action", "Action", ArgTypes.STRING)
            val selector = withRequiredArg("selector", "Element selector", ArgTypes.STRING)
            val elementValue = withRequiredArg("value", "value", ArgTypes.STRING)

            override fun executeSync(ctx: CommandContext) {
                val pageId = pageId.get(ctx)
                val selector = selector.get(ctx).trim('\"')
                val elementValue = elementValue.get(ctx).trim('\"')

                val commandBuilder = UICommandBuilder()

                when (action.get(ctx)) {
                    "set" -> {
                        commandBuilder.set(selector, elementValue)
                    }

                    "append" -> {
                        commandBuilder.append(selector, elementValue)
                    }
                }

                val player = (ctx.sender() as? Player)?.ref ?: Universe.get().players.firstOrNull() ?: return

                player.packetHandler.writeNoCache(
                    CustomPage(
                        pageId,
                        false,
                        false,
                        CustomPageLifetime.CanDismissOrCloseThroughInteraction,
                        commandBuilder.commands,
                        emptyArray()
                    )
                )
            }

        })
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

    companion object {
        fun createCustomUi() = customUi {
            group {
                anchor = UiAnchor(
                    width = 280,
                    height = 320,
                    right = 1,
                    top = 400,
                )
                layoutMode = LayoutMode.Top
                padding = UiPadding(
                    left = 10,
                    right = 10,
                    top = 10,
                    bottom = 10,
                )
                background = UiPatchStyle(
                    texturePath = "../Common/ContainerPanelPatch.png",
                    border = 6
                )

                group {
                    id = "Header"

                    anchor = UiAnchor(
                        width = 260,
                        height = 90,
                    )
                    layoutMode = LayoutMode.Left
                    padding = UiPadding(left = 4, right = 4)

                    group {
                        id = "BoardLogo"
                        anchor = UiAnchor(
                            width = 252,
                            height = 64,
                            left = 0,
                            top = 0
                        )
                        background = UiPatchStyle(
                            texturePath = "../Textures/BetterScoreBoard/better_logo.png",
                        )
                        visible = true
                    }

                    label {
                        id = "BoardTitle"
                        text = "Custom HUD Test"

                        anchor = UiAnchor(
                            width = 252,
                            height = 22,
                        )

                        style = UiLabelStyle(
                            fontSize = 16.0,
                            renderBold = true,
                            textColor = Color("#f2f4f8"),
                            horizontalAlignment = LabelAlignment.Center,
                            verticalAlignment = LabelAlignment.Center,
                        )
                    }
                }

                label {
                    id = "Divider"
                    anchor = UiAnchor(
                        width = 296,
                        height = 4,
                        top = 6,
                    )
                    background = UiPatchStyle(
                        texturePath = "../Textures/BetterScoreBoard/better_logo.png",
                    )
                }

                group {
                    id = "Lines"

                    anchor = UiAnchor(width = 260, height = 180)
                    layoutMode = LayoutMode.Top
                    padding = UiPadding(top = 6, bottom = 4)

                    label {
                        text = "Time: " + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                        anchor = UiAnchor(width = 260, height = 18)
                        style = UiLabelStyle(fontSize = 14.0, textColor = Color("#f6f8ff"))
                    }

                    label {
                        text = "Time: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
                        anchor = UiAnchor(width = 260, height = 18)
                        style = UiLabelStyle(fontSize = 14.0, textColor = Color("#f6f8ff"))
                    }

                    for (i in 0..10) {
                        label {
                            text = "Line $i"
                            anchor = UiAnchor(width = 260, height = 18)
                            style = UiLabelStyle(fontSize = 14.0, textColor = Color("#f6f8ff"))
                        }
                    }
                }
            }
        }
    }
}