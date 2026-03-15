package cz.creeperface.hytale.uimanager

import kotlin.Int
import kotlin.String
import kotlin.collections.MutableMap

public object IdGenerator {
  private val counters: MutableMap<String, Int> = mutableMapOf()

  public fun getNext(prefix: String): String {
    val count = counters.getOrDefault(prefix, 0) + 1
    counters[prefix] = count
    return "$prefix$count"
  }

  public fun reset() {
    counters.clear()
  }
}
