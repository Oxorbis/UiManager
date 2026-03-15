package cz.creeperface.hytale.uimanager.util

import com.hypixel.hytale.protocol.packets.interface_.CustomUICommand
import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder
import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.jvm.isAccessible

object CustomHudHelper {

    val BUILD_METHOD: KFunction<*>

    init {
        val hudClass = CustomUIHud::class
        val method = hudClass.declaredFunctions.first { it.name == "build" }
        method.isAccessible = true

        BUILD_METHOD = method
    }

    fun build(customHud: CustomUIHud, idPrefix: String): List<CustomUICommand> {
        val commandBuilder = UICommandBuilder()
        BUILD_METHOD.call(customHud, commandBuilder)

        val commands = commandBuilder.commands.toList()

        commands.forEach { command ->
            if (command.selector == null) {
                command.selector = idPrefix
            } else {
                command.selector = idPrefix + " " + command.selector
            }
        }

        return commands
    }
}