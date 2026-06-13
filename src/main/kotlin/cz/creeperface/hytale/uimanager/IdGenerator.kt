package cz.creeperface.hytale.uimanager

public object IdGenerator {
  private val counters: ThreadLocal<MutableMap<String, Int>> =
    ThreadLocal.withInitial { mutableMapOf() }

  public fun getNext(prefix: String): String {
    val map = counters.get()
    val count = map.getOrDefault(prefix, 0) + 1
    map[prefix] = count
    return "$prefix$count"
  }

  public fun reset() {
    counters.get().clear()
  }
}
