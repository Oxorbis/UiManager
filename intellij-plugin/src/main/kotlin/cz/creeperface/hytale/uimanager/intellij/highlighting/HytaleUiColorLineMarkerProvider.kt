package cz.creeperface.hytale.uimanager.intellij.highlighting

import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.psi.PsiElement
import com.intellij.util.ui.ColorIcon
import cz.creeperface.hytale.uimanager.intellij.psi.HytaleUiTokenTypes
import java.awt.Color
import javax.swing.Icon

class HytaleUiColorLineMarkerProvider : LineMarkerProvider {

    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? {
        if (element.node.elementType != HytaleUiTokenTypes.COLOR_LITERAL) return null

        val color = parseColor(element.text) ?: return null
        val icon: Icon = ColorIcon(12, color)

        return LineMarkerInfo(
            element,
            element.textRange,
            icon,
            { "Color: ${element.text}" },
            null,
            GutterIconRenderer.Alignment.LEFT,
            { "Color preview" }
        )
    }

    companion object {
        fun parseColor(text: String): Color? {
            val hex = text.removePrefix("#")
            return when (hex.length) {
                6 -> {
                    val r = hex.substring(0, 2).toIntOrNull(16) ?: return null
                    val g = hex.substring(2, 4).toIntOrNull(16) ?: return null
                    val b = hex.substring(4, 6).toIntOrNull(16) ?: return null
                    Color(r, g, b)
                }
                8 -> {
                    val r = hex.substring(0, 2).toIntOrNull(16) ?: return null
                    val g = hex.substring(2, 4).toIntOrNull(16) ?: return null
                    val b = hex.substring(4, 6).toIntOrNull(16) ?: return null
                    val a = hex.substring(6, 8).toIntOrNull(16) ?: return null
                    Color(r, g, b, a)
                }
                else -> null
            }
        }
    }
}
