package cz.creeperface.hytale.uimanager.util

import com.hypixel.hytale.server.core.Message

fun String.toMessage() = Message.raw(this)

fun String.translated() = Message.translation(this)