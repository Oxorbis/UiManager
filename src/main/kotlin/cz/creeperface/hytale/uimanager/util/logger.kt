package cz.creeperface.hytale.uimanager.util

import com.google.common.flogger.AbstractLogger
import com.google.common.flogger.LoggingApi
import java.util.logging.Level

inline fun <API : LoggingApi<API>> AbstractLogger<API>.debug(message: () -> String) {
    val at = at(Level.FINE)

    if (at is LoggingApi.NoOp<*>) {
        return
    }

    at.log(message())
}

inline fun <API : LoggingApi<API>> AbstractLogger<API>.inDebug(message: (LoggingApi<API>) -> Unit) {
    val at = at(Level.FINE)

    if (at is LoggingApi.NoOp<*>) {
        return
    }

    message(at)
}