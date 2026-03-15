package cz.creeperface.hytale.uimanager.util

fun <K, V> MutableMap<K, V>.extract(
    predicate: (K, V) -> Boolean
): Map<K, V> {
    val removed = mutableMapOf<K, V>()
    val iterator = entries.iterator()

    while (iterator.hasNext()) {
        val entry = iterator.next()
        if (predicate(entry.key, entry.value)) {
            removed[entry.key] = entry.value
            iterator.remove()
        }
    }

    return removed
}