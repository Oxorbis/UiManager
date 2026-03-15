package cz.creeperface.hytale.uimanager.util

import cz.creeperface.hytale.uimanager.Color
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class StandardColorsTest {

    @Test
    fun testDynamicColor() {
        val red500 = StandardColors.red.step(500)
        assertEquals("#ef4444", red500.hex)

        // Test caching
        val red500Again = StandardColors.red(500)
        assert(red500 === red500Again) { "Color instance should be cached" }
    }

    @Test
    fun testArbitraryStep() {
        val blue123 = StandardColors.blue.step(123)
        // Should not throw anymore as arbitrary steps are supported
        assert(blue123.hex.startsWith("#"))
    }

    @Test
    fun testAllBaseColors() {
        // Just verify some colors exist
        assertEquals("#3b82f6", StandardColors.blue(500).hex)
        assertEquals("#22c55e", StandardColors.green(500).hex)
        assertEquals("#6366f1", StandardColors.indigo(500).hex)
    }
}
