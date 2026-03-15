package cz.creeperface.hytale.uimanager.intellij.reference

import com.intellij.lang.cacheBuilder.DefaultWordsScanner
import com.intellij.lang.cacheBuilder.WordsScanner
import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNameIdentifierOwner
import com.intellij.psi.tree.TokenSet
import cz.creeperface.hytale.uimanager.intellij.lexer.HytaleUiLexer
import cz.creeperface.hytale.uimanager.intellij.parser.HytaleUiFileImportElement
import cz.creeperface.hytale.uimanager.intellij.parser.HytaleUiVariableDeclElement
import cz.creeperface.hytale.uimanager.intellij.psi.HytaleUiTokenTypes

class HytaleUiFindUsagesProvider : FindUsagesProvider {

    override fun getWordsScanner(): WordsScanner {
        return DefaultWordsScanner(
            HytaleUiLexer(),
            TokenSet.create(HytaleUiTokenTypes.IDENTIFIER, HytaleUiTokenTypes.AT_IDENTIFIER, HytaleUiTokenTypes.DOLLAR_IDENTIFIER),
            TokenSet.create(HytaleUiTokenTypes.LINE_COMMENT),
            TokenSet.create(HytaleUiTokenTypes.STRING_LITERAL)
        )
    }

    override fun canFindUsagesFor(psiElement: PsiElement): Boolean {
        return psiElement is PsiNameIdentifierOwner
    }

    override fun getHelpId(psiElement: PsiElement): String? = null

    override fun getType(element: PsiElement): String {
        return when (element) {
            is HytaleUiVariableDeclElement -> "variable"
            is HytaleUiFileImportElement -> "file import"
            else -> "element"
        }
    }

    override fun getDescriptiveName(element: PsiElement): String {
        if (element is PsiNameIdentifierOwner) {
            return element.name ?: ""
        }
        return ""
    }

    override fun getNodeText(element: PsiElement, useFullName: Boolean): String {
        if (element is PsiNameIdentifierOwner) {
            val name = element.name ?: return element.text
            return when (element) {
                is HytaleUiVariableDeclElement -> "@$name"
                is HytaleUiFileImportElement -> "\$$name"
                else -> name
            }
        }
        return element.text
    }
}
