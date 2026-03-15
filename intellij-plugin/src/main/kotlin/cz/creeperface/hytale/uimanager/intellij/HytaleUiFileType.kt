package cz.creeperface.hytale.uimanager.intellij

import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

class HytaleUiFileType private constructor() : LanguageFileType(HytaleUiLanguage.INSTANCE) {
    companion object {
        @JvmField
        val INSTANCE = HytaleUiFileType()
    }

    override fun getName(): String = "Hytale UI"
    override fun getDescription(): String = "Hytale UI definition file"
    override fun getDefaultExtension(): String = "ui"
    override fun getIcon(): Icon = HytaleUiIcons.FILE
}
