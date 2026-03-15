package cz.creeperface.hytale.uimanager.intellij.lexer

import com.intellij.lexer.LexerBase
import com.intellij.psi.tree.IElementType
import cz.creeperface.hytale.uimanager.intellij.psi.HytaleUiTokenTypes

class HytaleUiLexer : LexerBase() {
    private var buffer: CharSequence = ""
    private var startOffset = 0
    private var endOffset = 0
    private var pos = 0
    private var tokenStart = 0
    private var tokenEnd = 0
    private var tokenType: IElementType? = null

    override fun start(buffer: CharSequence, startOffset: Int, endOffset: Int, initialState: Int) {
        this.buffer = buffer
        this.startOffset = startOffset
        this.endOffset = endOffset
        this.pos = startOffset
        advance()
    }

    override fun getState(): Int = 0

    override fun getTokenType(): IElementType? = tokenType

    override fun getTokenStart(): Int = tokenStart

    override fun getTokenEnd(): Int = tokenEnd

    override fun getBufferSequence(): CharSequence = buffer

    override fun getBufferEnd(): Int = endOffset

    override fun advance() {
        if (pos >= endOffset) {
            tokenType = null
            return
        }

        tokenStart = pos

        val c = buffer[pos]

        tokenType = when {
            c.isWhitespace() -> readWhitespace()
            c == '/' && peek(1) == '/' -> readLineComment()
            c == '"' -> readString()
            c == '#' -> readHashToken()
            c == '@' -> readAtIdentifier()
            c == '$' -> readDollarIdentifier()
            c == '%' -> readServerString()
            c == '.' && peek(1) == '.' && peek(2) == '.' -> { pos += 3; HytaleUiTokenTypes.SPREAD }
            c == '{' -> { pos++; HytaleUiTokenTypes.LBRACE }
            c == '}' -> { pos++; HytaleUiTokenTypes.RBRACE }
            c == '(' -> { pos++; HytaleUiTokenTypes.LPAREN }
            c == ')' -> { pos++; HytaleUiTokenTypes.RPAREN }
            c == ':' -> { pos++; HytaleUiTokenTypes.COLON }
            c == ';' -> { pos++; HytaleUiTokenTypes.SEMICOLON }
            c == ',' -> { pos++; HytaleUiTokenTypes.COMMA }
            c == '=' -> { pos++; HytaleUiTokenTypes.EQUALS }
            c == '.' -> { pos++; HytaleUiTokenTypes.DOT }
            c == '+' -> { pos++; HytaleUiTokenTypes.PLUS }
            c == '-' -> readMinusOrNumber()
            c.isDigit() -> readNumber()
            c.isLetter() || c == '_' -> readIdentifierOrKeyword()
            else -> { pos++; HytaleUiTokenTypes.BAD_CHARACTER }
        }

        tokenEnd = pos
    }

    private fun peek(offset: Int): Char? {
        val idx = pos + offset
        return if (idx < endOffset) buffer[idx] else null
    }

    private fun readWhitespace(): IElementType {
        while (pos < endOffset && buffer[pos].isWhitespace()) pos++
        return HytaleUiTokenTypes.WHITE_SPACE
    }

    private fun readLineComment(): IElementType {
        while (pos < endOffset && buffer[pos] != '\n') pos++
        return HytaleUiTokenTypes.LINE_COMMENT
    }

    private fun readString(): IElementType {
        pos++ // skip opening quote
        while (pos < endOffset) {
            val c = buffer[pos]
            if (c == '\\') {
                pos += 2 // skip escape sequence
            } else if (c == '"') {
                pos++ // skip closing quote
                break
            } else if (c == '\n') {
                // Allow multiline strings (they appear in the .ui files)
                pos++
            } else {
                pos++
            }
        }
        return HytaleUiTokenTypes.STRING_LITERAL
    }

    private fun readHashToken(): IElementType {
        pos++ // skip '#'
        if (pos >= endOffset) return HytaleUiTokenTypes.HASH

        // Try to read as color literal: #RRGGBB (exactly 6 hex chars, not followed by alnum)
        val colorStart = pos
        var hexCount = 0
        while (pos < endOffset && isHexChar(buffer[pos]) && hexCount < 8) {
            pos++
            hexCount++
        }

        if (hexCount == 6 || hexCount == 8) {
            // Check it's not followed by more alphanumeric chars (which would make it an identifier)
            if (pos >= endOffset || !buffer[pos].isLetterOrDigit()) {
                return HytaleUiTokenTypes.COLOR_LITERAL
            }
        }

        // Not a valid color literal - reset pos to just after '#'
        // Return HASH as a standalone token; the identifier after it
        // (e.g., "Title" in "#Title") will be read as a separate IDENTIFIER token
        // on the next advance() call.
        pos = colorStart
        return HytaleUiTokenTypes.HASH
    }

    private fun isHexChar(c: Char): Boolean =
        c in '0'..'9' || c in 'a'..'f' || c in 'A'..'F'

    private fun readAtIdentifier(): IElementType {
        pos++ // skip '@'
        while (pos < endOffset && (buffer[pos].isLetterOrDigit() || buffer[pos] == '_')) pos++
        return HytaleUiTokenTypes.AT_IDENTIFIER
    }

    private fun readDollarIdentifier(): IElementType {
        pos++ // skip '$'
        while (pos < endOffset && (buffer[pos].isLetterOrDigit() || buffer[pos] == '_')) pos++
        return HytaleUiTokenTypes.DOLLAR_IDENTIFIER
    }

    private fun readServerString(): IElementType {
        pos++ // skip '%'
        while (pos < endOffset && (buffer[pos].isLetterOrDigit() || buffer[pos] == '_' || buffer[pos] == '.')) pos++
        return HytaleUiTokenTypes.SERVER_STRING
    }

    private fun readMinusOrNumber(): IElementType {
        // Check if minus is followed by a digit (negative number)
        if (peek(1)?.isDigit() == true) {
            pos++ // skip '-'
            return readNumber()
        }
        pos++
        return HytaleUiTokenTypes.MINUS
    }

    private fun readNumber(): IElementType {
        while (pos < endOffset && buffer[pos].isDigit()) pos++
        if (pos < endOffset && buffer[pos] == '.' && peek(1)?.isDigit() == true) {
            pos++ // skip '.'
            while (pos < endOffset && buffer[pos].isDigit()) pos++
            return HytaleUiTokenTypes.FLOAT_LITERAL
        }
        return HytaleUiTokenTypes.INTEGER_LITERAL
    }

    private fun readIdentifierOrKeyword(): IElementType {
        while (pos < endOffset && (buffer[pos].isLetterOrDigit() || buffer[pos] == '_')) pos++
        val text = buffer.subSequence(tokenStart, pos).toString()
        return when (text) {
            "true", "false" -> HytaleUiTokenTypes.BOOLEAN_LITERAL
            else -> HytaleUiTokenTypes.IDENTIFIER
        }
    }
}
