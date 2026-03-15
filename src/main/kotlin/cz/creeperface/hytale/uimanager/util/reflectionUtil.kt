package cz.creeperface.hytale.uimanager.util

import kotlin.reflect.KProperty0
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.javaGetter
import kotlin.jvm.internal.CallableReference

@Suppress("UNCHECKED_CAST")
fun <R> KProperty0<R>.toUnboundOrNull(): KProperty1<Any, R>? {
    val declaringClass: Class<*>? =
        this.javaGetter?.declaringClass ?: this.javaField?.declaringClass

    val kClass = declaringClass?.kotlin ?: return null

    val prop = kClass.memberProperties.firstOrNull { it.name == this.name } ?: return null
    return prop as? KProperty1<Any, R>
}

fun KProperty0<*>.boundReceiverOrNull(): Any? {
    val cr = this as? CallableReference ?: return null
    val r = cr.boundReceiver
    return if (r === CallableReference.NO_RECEIVER) null else r
}