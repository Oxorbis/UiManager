package cz.creeperface.hytale.uimanager

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

class IdGeneratorTest {

    @Test
    fun testSequentialIdsAreDeterministic() {
        IdGenerator.reset()
        assertEquals("X1", IdGenerator.getNext("X"))
        assertEquals("X2", IdGenerator.getNext("X"))
        assertEquals("Y1", IdGenerator.getNext("Y"))
        assertEquals("X3", IdGenerator.getNext("X"))
    }

    @Test
    fun testConcurrentBuildsAreIsolatedPerThread() {
        // Each thread runs an independent reset()+getNext() sequence, exactly like a build.
        // With a shared (non-thread-local) counter map these interleave and produce wrong ids.
        val threads = 8
        val iterations = 500
        val pool = Executors.newFixedThreadPool(threads)
        val mismatches = AtomicInteger(0)
        val latch = CountDownLatch(threads)
        repeat(threads) {
            pool.submit {
                try {
                    repeat(iterations) {
                        IdGenerator.reset()
                        val a = IdGenerator.getNext("Node")
                        val b = IdGenerator.getNext("Node")
                        val c = IdGenerator.getNext("Node")
                        if (a != "Node1" || b != "Node2" || c != "Node3") {
                            mismatches.incrementAndGet()
                        }
                    }
                } finally {
                    latch.countDown()
                }
            }
        }
        latch.await(30, TimeUnit.SECONDS)
        pool.shutdownNow()
        assertEquals(0, mismatches.get(), "Concurrent builds must each see an isolated counter scope")
    }
}
