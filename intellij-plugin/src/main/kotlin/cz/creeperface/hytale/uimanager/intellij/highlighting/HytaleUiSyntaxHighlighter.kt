package cz.creeperface.hytale.uimanager.intellij.highlighting

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import cz.creeperface.hytale.uimanager.intellij.lexer.HytaleUiLexer
import cz.creeperface.hytale.uimanager.intellij.psi.HytaleUiTokenTypes

class HytaleUiSyntaxHighlighter : SyntaxHighlighterBase() {

    companion object {
        val COMMENT = createTextAttributesKey("HYTALE_UI_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT)
        val STRING = createTextAttributesKey("HYTALE_UI_STRING", DefaultLanguageHighlighterColors.STRING)
        val NUMBER = createTextAttributesKey("HYTALE_UI_NUMBER", DefaultLanguageHighlighterColors.NUMBER)
        val BOOLEAN = createTextAttributesKey("HYTALE_UI_BOOLEAN", DefaultLanguageHighlighterColors.KEYWORD)
        // Variables (@name) - green (set via color scheme XML, falls back to global variable)
        val VARIABLE = createTextAttributesKey("HYTALE_UI_VARIABLE", DefaultLanguageHighlighterColors.GLOBAL_VARIABLE)
        // File imports ($name) - teal/cyan
        val FILE_IMPORT = createTextAttributesKey("HYTALE_UI_FILE_IMPORT", DefaultLanguageHighlighterColors.METADATA)
        val COLOR = createTextAttributesKey("HYTALE_UI_COLOR", DefaultLanguageHighlighterColors.CONSTANT)
        val SERVER_STRING = createTextAttributesKey("HYTALE_UI_SERVER_STRING", DefaultLanguageHighlighterColors.LABEL)
        val IDENTIFIER = createTextAttributesKey("HYTALE_UI_IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER)
        val NODE_TYPE = createTextAttributesKey("HYTALE_UI_NODE_TYPE", DefaultLanguageHighlighterColors.KEYWORD)
        // Data types stay white/default identifier color
        val TYPE_NAME = createTextAttributesKey("HYTALE_UI_TYPE_NAME", DefaultLanguageHighlighterColors.IDENTIFIER)
        val ENUM_VALUE = createTextAttributesKey("HYTALE_UI_ENUM_VALUE", DefaultLanguageHighlighterColors.CONSTANT)
        val PROPERTY_KEY = createTextAttributesKey("HYTALE_UI_PROPERTY_KEY", DefaultLanguageHighlighterColors.INSTANCE_FIELD)
        val NODE_ID = createTextAttributesKey("HYTALE_UI_NODE_ID", DefaultLanguageHighlighterColors.FUNCTION_DECLARATION)
        val BRACES = createTextAttributesKey("HYTALE_UI_BRACES", DefaultLanguageHighlighterColors.BRACES)
        val PARENS = createTextAttributesKey("HYTALE_UI_PARENS", DefaultLanguageHighlighterColors.PARENTHESES)
        val OPERATOR = createTextAttributesKey("HYTALE_UI_OPERATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN)
        val SEMICOLON = createTextAttributesKey("HYTALE_UI_SEMICOLON", DefaultLanguageHighlighterColors.SEMICOLON)
        val COMMA = createTextAttributesKey("HYTALE_UI_COMMA", DefaultLanguageHighlighterColors.COMMA)
        val BAD_CHAR = createTextAttributesKey("HYTALE_UI_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER)
    }

    override fun getHighlightingLexer(): Lexer = HytaleUiLexer()

    override fun getTokenHighlights(tokenType: IElementType?): Array<TextAttributesKey> {
        return when (tokenType) {
            HytaleUiTokenTypes.LINE_COMMENT -> arrayOf(COMMENT)
            HytaleUiTokenTypes.STRING_LITERAL -> arrayOf(STRING)
            HytaleUiTokenTypes.INTEGER_LITERAL, HytaleUiTokenTypes.FLOAT_LITERAL -> arrayOf(NUMBER)
            HytaleUiTokenTypes.BOOLEAN_LITERAL -> arrayOf(BOOLEAN)
            HytaleUiTokenTypes.AT_IDENTIFIER -> arrayOf(VARIABLE)
            HytaleUiTokenTypes.DOLLAR_IDENTIFIER -> arrayOf(FILE_IMPORT)
            HytaleUiTokenTypes.COLOR_LITERAL -> arrayOf(COLOR)
            HytaleUiTokenTypes.SERVER_STRING -> arrayOf(SERVER_STRING)
            HytaleUiTokenTypes.IDENTIFIER -> arrayOf(IDENTIFIER)
            HytaleUiTokenTypes.LBRACE, HytaleUiTokenTypes.RBRACE -> arrayOf(BRACES)
            HytaleUiTokenTypes.LPAREN, HytaleUiTokenTypes.RPAREN -> arrayOf(PARENS)
            HytaleUiTokenTypes.SPREAD, HytaleUiTokenTypes.PLUS, HytaleUiTokenTypes.MINUS -> arrayOf(OPERATOR)
            HytaleUiTokenTypes.COLON, HytaleUiTokenTypes.EQUALS, HytaleUiTokenTypes.DOT -> arrayOf(OPERATOR)
            HytaleUiTokenTypes.SEMICOLON -> arrayOf(SEMICOLON)
            HytaleUiTokenTypes.COMMA -> arrayOf(COMMA)
            HytaleUiTokenTypes.HASH -> arrayOf(NODE_ID)
            HytaleUiTokenTypes.BAD_CHARACTER -> arrayOf(BAD_CHAR)
            else -> emptyArray()
        }
    }
}
