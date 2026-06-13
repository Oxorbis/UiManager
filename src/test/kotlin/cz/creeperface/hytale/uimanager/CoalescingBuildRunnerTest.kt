package cz.creeperface.hytale.uimanager

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

class CoalescingBuildRunnerTest {

    @Test
    fun testFutureCompletesOnSuccess() {
        val runner = CoalescingBuildRunner()
        val pool = Executors.newSingleThreadExecutor()
        try {
            val ran = AtomicBoolean(false)
            val future = runner.submit(pool) { ran.set(true) }
            future.get(5, TimeUnit.SECONDS)
            assertTrue(ran.get())
        } finally {
            pool.shutdownNow()
        }
    }

    @Test
    fun testFailureCompletesFutureExceptionally() {
        val runner = CoalescingBuildRunner()
        val pool = Executors.newSingleThreadExecutor()
        try {
            val future = runner.submit(pool) { throw IllegalStateException("boom") }
            var failed = false
            try {
                future.get(5, TimeUnit.SECONDS)
            } catch (e: java.util.concurrent.ExecutionException) {
                failed = e.cause is IllegalStateException
            }
            assertTrue(failed, "future should complete exceptionally")
        } finally {
            pool.shutdownNow()
        }
    }

    @Test
    fun testExecutionIsSerialized() {
        val runner = CoalescingBuildRunner()
        val pool = Executors.newFixedThreadPool(4)
        try {
            val concurrent = AtomicInteger(0)
            val maxObserved = AtomicInteger(0)
            val futures = (1..50).map {
                runner.submit(pool) {
                    val now = concurrent.incrementAndGet()
                    maxObserved.accumulateAndGet(now) { a, b -> maxOf(a, b) }
                    Thread.sleep(1)
                    concurrent.decrementAndGet()
                }
            }
            // Drain all terminal futures (coalesced ones complete when their successor does).
            futures.forEach { runCatching { it.get(10, TimeUnit.SECONDS) } }
            assertEquals(1, maxObserved.get(), "runner must never execute two builds at once")
        } finally {
            pool.shutdownNow()
        }
    }

    @Test
    fun testLatestWinsCoalescing() {
        val runner = CoalescingBuildRunner()
        val pool = Executors.newFixedThreadPool(2)
        try {
            val executed = ConcurrentLinkedQueue<Int>()
            val release = CountDownLatch(1)
            val firstStarted = CountDownLatch(1)

            // Task 0 blocks until released, occupying the single in-flight slot.
            val f0 = runner.submit(pool) {
                firstStarted.countDown()
                release.await(5, TimeUnit.SECONDS)
                executed.add(0)
            }
            assertTrue(firstStarted.await(5, TimeUnit.SECONDS))

            // While 0 runs, enqueue 1, 2, 3 — only the last should run (latest-wins).
            val f1 = runner.submit(pool) { executed.add(1) }
            val f2 = runner.submit(pool) { executed.add(2) }
            val f3 = runner.submit(pool) { executed.add(3) }

            release.countDown()
            listOf(f0, f1, f2, f3).forEach { it.get(10, TimeUnit.SECONDS) }

            assertTrue(executed.contains(0), "first build runs")
            assertTrue(executed.contains(3), "newest pending build runs")
            assertFalse(executed.contains(1), "superseded build is dropped")
            assertFalse(executed.contains(2), "superseded build is dropped")
        } finally {
            pool.shutdownNow()
        }
    }
}
