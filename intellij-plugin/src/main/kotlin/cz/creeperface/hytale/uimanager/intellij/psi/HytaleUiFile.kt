package cz.creeperface.hytale.uimanager.intellij.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import cz.creeperface.hytale.uimanager.intellij.HytaleUiFileType
import cz.creeperface.hytale.uimanager.intellij.HytaleUiLanguage

class HytaleUiFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, HytaleUiLanguage.INSTANCE) {
    override fun getFileType(): FileType = HytaleUiFileType.INSTANCE
    override fun toString(): String = "Hytale UI File"
}
