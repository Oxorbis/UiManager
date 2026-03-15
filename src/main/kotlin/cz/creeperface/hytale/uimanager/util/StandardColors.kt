package cz.creeperface.hytale.uimanager.util

import cz.creeperface.hytale.uimanager.Color
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.pow

class DynamicColor(val name: String, private val baseHex500: String) {
    private val cache = ConcurrentHashMap<Int, Color>()

    private val whiteAnchors = listOf(
        0 to 1.0,      // Pure white at step 0
        50 to 0.92,
        100 to 0.85,
        200 to 0.72,
        300 to 0.6,
        400 to 0.45,
        500 to 0.0     // Base color at 500
    )

    private val blackAnchors = listOf(
        500 to 0.0,    // Base color at 500
        600 to 0.12,
        700 to 0.24,
        800 to 0.36,
        900 to 0.5,
        950 to 0.7,
        1000 to 1.0    // Pure black at step 1000
    )

    fun step(step: Int): Color {
        val clampedStep = step.coerceIn(0, 1000)
        if (clampedStep == 500) return cache.getOrPut(500) { Color(baseHex500) }

        return cache.getOrPut(clampedStep) {
            val base = hexToRgb(baseHex500)
            val target = if (clampedStep < 500) intArrayOf(255, 255, 255) else intArrayOf(0, 0, 0)
            
            val factor = if (clampedStep < 500) {
                interpolate(clampedStep, whiteAnchors)
            } else {
                interpolate(clampedStep, blackAnchors)
            }

            val mixed = mixLinearSrgb(base, target, factor)
            val hex = rgbToHex(mixed)
            Color(hex)
        }
    }

    private fun interpolate(step: Int, anchors: List<Pair<Int, Double>>): Double {
        for (i in 0 until anchors.size - 1) {
            val (s1, f1) = anchors[i]
            val (s2, f2) = anchors[i + 1]
            if (step in s1..s2) {
                val t = (step - s1).toDouble() / (s2 - s1)
                return f1 + t * (f2 - f1)
            }
        }
        return anchors.last().second
    }

    operator fun invoke(step: Int) = step(step)

    private fun hexToRgb(hex: String): IntArray {
        val clean = hex.removePrefix("#")
        val r = clean.substring(0, 2).toInt(16)
        val g = clean.substring(2, 4).toInt(16)
        val b = clean.substring(4, 6).toInt(16)
        return intArrayOf(r, g, b)
    }

    private fun rgbToHex(rgb: IntArray): String {
        val (r, g, b) = rgb
        return "#" + listOf(r, g, b).joinToString("") { it.coerceIn(0, 255).toString(16).padStart(2, '0') }
    }

    // Mix in linear sRGB for better perceptual results
    private fun toLinear(c: Int): Double {
        val cs = c / 255.0
        return if (cs <= 0.04045) cs / 12.92 else ((cs + 0.055) / 1.055).pow(2.4)
    }

    private fun toSrgb(c: Double): Int {
        val v = if (c <= 0.0031308) c * 12.92 else 1.055 * c.pow(1.0 / 2.4) - 0.055
        return (v * 255.0 + 0.5).toInt().coerceIn(0, 255)
    }

    private fun mixLinearSrgb(a: IntArray, b: IntArray, t: Double): IntArray {
        val r = toSrgb((1 - t) * toLinear(a[0]) + t * toLinear(b[0]))
        val g = toSrgb((1 - t) * toLinear(a[1]) + t * toLinear(b[1]))
        val bl = toSrgb((1 - t) * toLinear(a[2]) + t * toLinear(b[2]))
        return intArrayOf(r, g, bl)
    }
}

object StandardColors {
    val slate = DynamicColor("slate", "#64748b")
    val gray = DynamicColor("gray", "#6b7280")
    val zinc = DynamicColor("zinc", "#71717a")
    val neutral = DynamicColor("neutral", "#737373")
    val stone = DynamicColor("stone", "#78716c")

    val red = DynamicColor("red", "#ef4444")
    val orange = DynamicColor("orange", "#f97316")
    val amber = DynamicColor("amber", "#f59e0b")
    val yellow = DynamicColor("yellow", "#eab308")
    val lime = DynamicColor("lime", "#84cc16")

    val green = DynamicColor("green", "#22c55e")
    val emerald = DynamicColor("emerald", "#10b981")
    val teal = DynamicColor("teal", "#14b8a6")
    val cyan = DynamicColor("cyan", "#06b6d4")
    val sky = DynamicColor("sky", "#0ea5e9")

    val blue = DynamicColor("blue", "#3b82f6")
    val indigo = DynamicColor("indigo", "#6366f1")
    val violet = DynamicColor("violet", "#8b5cf6")
    val purple = DynamicColor("purple", "#a855f7")
    val fuchsia = DynamicColor("fuchsia", "#d946ef")

    val pink = DynamicColor("pink", "#ec4899")
    val rose = DynamicColor("rose", "#f43f5e")
}