package cz.creeperface.hytale.uimanager.intellij.psi

import com.intellij.psi.tree.IElementType
import cz.creeperface.hytale.uimanager.intellij.HytaleUiLanguage

class HytaleUiTokenType(debugName: String) : IElementType(debugName, HytaleUiLanguage.INSTANCE) {
    override fun toString(): String = "HytaleUiTokenType.${super.toString()}"
}

object HytaleUiTokenTypes {
    // Literals
    @JvmField val STRING_LITERAL = HytaleUiTokenType("STRING_LITERAL")
    @JvmField val INTEGER_LITERAL = HytaleUiTokenType("INTEGER_LITERAL")
    @JvmField val FLOAT_LITERAL = HytaleUiTokenType("FLOAT_LITERAL")
    @JvmField val BOOLEAN_LITERAL = HytaleUiTokenType("BOOLEAN_LITERAL")
    @JvmField val COLOR_LITERAL = HytaleUiTokenType("COLOR_LITERAL")

    // Identifiers
    @JvmField val IDENTIFIER = HytaleUiTokenType("IDENTIFIER")
    @JvmField val AT_IDENTIFIER = HytaleUiTokenType("AT_IDENTIFIER")
    @JvmField val DOLLAR_IDENTIFIER = HytaleUiTokenType("DOLLAR_IDENTIFIER")
    @JvmField val SERVER_STRING = HytaleUiTokenType("SERVER_STRING")

    // Operators and punctuation
    @JvmField val LBRACE = HytaleUiTokenType("LBRACE")
    @JvmField val RBRACE = HytaleUiTokenType("RBRACE")
    @JvmField val LPAREN = HytaleUiTokenType("LPAREN")
    @JvmField val RPAREN = HytaleUiTokenType("RPAREN")
    @JvmField val COLON = HytaleUiTokenType("COLON")
    @JvmField val SEMICOLON = HytaleUiTokenType("SEMICOLON")
    @JvmField val COMMA = HytaleUiTokenType("COMMA")
    @JvmField val EQUALS = HytaleUiTokenType("EQUALS")
    @JvmField val DOT = HytaleUiTokenType("DOT")
    @JvmField val HASH = HytaleUiTokenType("HASH")
    @JvmField val SPREAD = HytaleUiTokenType("SPREAD")
    @JvmField val PLUS = HytaleUiTokenType("PLUS")
    @JvmField val MINUS = HytaleUiTokenType("MINUS")

    // Comments and whitespace
    @JvmField val LINE_COMMENT = HytaleUiTokenType("LINE_COMMENT")
    @JvmField val WHITE_SPACE = com.intellij.psi.TokenType.WHITE_SPACE
    @JvmField val BAD_CHARACTER = com.intellij.psi.TokenType.BAD_CHARACTER
}
