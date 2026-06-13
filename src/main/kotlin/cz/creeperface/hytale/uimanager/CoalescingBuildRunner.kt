package cz.creeperface.hytale.uimanager

import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

/**
 * Serializes builds and coalesces them latest-wins: at most one build runs and at most one
 * is pending. If a new build is submitted while one is in flight, it replaces any earlier
 * pending build (intermediate builds are dropped — correct for a per-tick HUD). A superseded
 * build's future completes together with the build that replaces it.
 */
class CoalescingBuildRunner {
    private val lock = Any()
    private var inFlight = false
    private var pending: Pending? = null

    private class Pending(
        val executor: Executor,
        val build: () -> Unit,
        val future: CompletableFuture<Void>,
    )

    fun submit(executor: Executor, build: () -> Unit): CompletableFuture<Void> {
        val future = CompletableFuture<Void>()
        synchronized(lock) {
            if (inFlight) {
                val previous = pending
                pending = Pending(executor, build, future)
                // resolve the superseded future when this (newer) one resolves
                previous?.future?.let { prev ->
                    future.whenComplete { _, error ->
                        if (error != null) prev.completeExceptionally(error) else prev.complete(null)
                    }
                }
                return future
            }
            inFlight = true
        }
        run(executor, build, future)
        return future
    }

    private fun run(executor: Executor, build: () -> Unit, future: CompletableFuture<Void>) {
        executor.execute {
            try {
                build()
                future.complete(null)
            } catch (e: Throwable) {
                future.completeExceptionally(e)
            } finally {
                val next = synchronized(lock) {
                    val n = pending
                    pending = null
                    if (n == null) inFlight = false
                    n
                }
                if (next != null) run(next.executor, next.build, next.future)
            }
        }
    }
}
