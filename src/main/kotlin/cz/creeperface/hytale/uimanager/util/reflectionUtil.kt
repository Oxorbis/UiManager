package cz.creeperface.hytale.uimanager.util

import cz.creeperface.hytale.uimanager.UiType
import kotlin.reflect.KProperty0
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.javaGetter
import kotlin.jvm.internal.CallableReference
import kotlin.reflect.full.primaryConstructor

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

inline fun <reified T : UiType> T?.getOrDefault(): T {
    if (this != null) {
        return this
    }

    val constructor = requireNotNull(T::class.primaryConstructor) {
        "Type ${T::class.simpleName} must have primary constructor"
    }

    return constructor.callBy(emptyMap())
}