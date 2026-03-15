package cz.creeperface.hytale.uimanager.intellij

import com.intellij.lang.Language

class HytaleUiLanguage private constructor() : Language("HytaleUi") {
    companion object {
        @JvmField
        val INSTANCE = HytaleUiLanguage()
    }

    override fun getDisplayName(): String = "Hytale UI"
}
