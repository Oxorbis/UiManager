package cz.creeperface.hytale.uimanager.intellij.formatting

import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType
import cz.creeperface.hytale.uimanager.intellij.psi.HytaleUiTokenTypes

class HytaleUiBraceMatcher : PairedBraceMatcher {

    companion object {
        private val PAIRS = arrayOf(
            BracePair(HytaleUiTokenTypes.LBRACE, HytaleUiTokenTypes.RBRACE, true),
            BracePair(HytaleUiTokenTypes.LPAREN, HytaleUiTokenTypes.RPAREN, false),
        )
    }

    override fun getPairs(): Array<BracePair> = PAIRS

    override fun isPairedBracesAllowedBeforeType(lbraceType: IElementType, contextType: IElementType?): Boolean = true

    override fun getCodeConstructStart(file: PsiFile?, openingBraceOffset: Int): Int = openingBraceOffset
}
