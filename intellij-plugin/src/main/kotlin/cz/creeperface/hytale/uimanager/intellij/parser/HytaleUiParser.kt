package cz.creeperface.hytale.uimanager.intellij.parser

import com.intellij.lang.ASTNode
import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiParser
import com.intellij.psi.tree.IElementType
import cz.creeperface.hytale.uimanager.intellij.psi.HytaleUiElementTypes
import cz.creeperface.hytale.uimanager.intellij.psi.HytaleUiTokenTypes

class HytaleUiParser : PsiParser {

    override fun parse(root: IElementType, builder: PsiBuilder): ASTNode {
        val rootMarker = builder.mark()
        while (!builder.eof()) {
            if (!parseStatement(builder)) {
                // Skip bad tokens
                val errorMarker = builder.mark()
                builder.advanceLexer()
                errorMarker.error("Unexpected token")
            }
        }
        rootMarker.done(root)
        return builder.treeBuilt
    }

    private fun parseStatement(builder: PsiBuilder): Boolean {
        return when (builder.tokenType) {
            HytaleUiTokenTypes.DOLLAR_IDENTIFIER -> parseDollarStatement(builder)
            HytaleUiTokenTypes.AT_IDENTIFIER -> parseVariableDeclOrTemplateInst(builder)
            HytaleUiTokenTypes.IDENTIFIER -> parseNodeDecl(builder)
            HytaleUiTokenTypes.HASH -> parseIdOverride(builder)
            HytaleUiTokenTypes.LINE_COMMENT -> { builder.advanceLexer(); true }
            else -> false
        }
    }

    /**
     * $C = "path.ui";   (file import)
     * $C.@Template { }  (template instantiation)
     */
    private fun parseDollarStatement(builder: PsiBuilder): Boolean {
        val marker = builder.mark()

        builder.advanceLexer() // consume $IDENTIFIER

        if (builder.tokenType == HytaleUiTokenTypes.EQUALS) {
            // File import: $C = "path.ui";
            builder.advanceLexer() // consume '='
            if (builder.tokenType != HytaleUiTokenTypes.STRING_LITERAL) {
                marker.error("Expected string literal for file import path")
                return true
            }
            builder.advanceLexer() // consume string
            expectSemicolon(builder)
            marker.done(HytaleUiElementTypes.FILE_IMPORT)
            return true
        }

        if (builder.tokenType == HytaleUiTokenTypes.DOT) {
            // Template instantiation: $C.@Template #id { }
            builder.advanceLexer() // consume '.'
            if (builder.tokenType != HytaleUiTokenTypes.AT_IDENTIFIER) {
                marker.error("Expected @variable after '.'")
                return true
            }
            builder.advanceLexer() // consume @IDENTIFIER

            parseOptionalNodeId(builder)

            if (builder.tokenType == HytaleUiTokenTypes.LBRACE) {
                parseNodeBody(builder)
            }
            expectOptionalSemicolon(builder)
            marker.done(HytaleUiElementTypes.TEMPLATE_INST)
            return true
        }

        marker.error("Expected '=' or '.' after \$ identifier")
        return true
    }

    /**
     * @name = value;    (variable declaration)
     * @Template #id { } (template instantiation using local variable)
     */
    private fun parseVariableDeclOrTemplateInst(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        builder.advanceLexer() // consume @IDENTIFIER

        if (builder.tokenType == HytaleUiTokenTypes.EQUALS) {
            // Variable declaration: @name = value;
            builder.advanceLexer() // consume '='
            parseValue(builder)
            expectSemicolon(builder)
            marker.done(HytaleUiElementTypes.VARIABLE_DECL)
            return true
        }

        // Template instantiation: @Template #id { }
        parseOptionalNodeId(builder)
        if (builder.tokenType == HytaleUiTokenTypes.LBRACE) {
            parseNodeBody(builder)
        }
        expectOptionalSemicolon(builder)
        marker.done(HytaleUiElementTypes.TEMPLATE_INST)
        return true
    }

    /**
     * NodeType #id { properties; children; }
     */
    private fun parseNodeDecl(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        builder.advanceLexer() // consume IDENTIFIER (node type name)

        parseOptionalNodeId(builder)

        if (builder.tokenType == HytaleUiTokenTypes.LBRACE) {
            parseNodeBody(builder)
        } else {
            // Missing brace — still finish the NODE_DECL so the tree stays well-formed.
            // This avoids wrapping in PsiErrorElement which breaks parent context detection.
            builder.error("Expected '{' after node type name")
        }

        expectOptionalSemicolon(builder)
        marker.done(HytaleUiElementTypes.NODE_DECL)
        return true
    }

    /**
     * #ExistingId { properties; }
     */
    private fun parseIdOverride(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        builder.advanceLexer() // consume '#'

        if (builder.tokenType != HytaleUiTokenTypes.IDENTIFIER) {
            marker.error("Expected identifier after '#'")
            return true
        }
        builder.advanceLexer() // consume IDENTIFIER

        if (builder.tokenType == HytaleUiTokenTypes.LBRACE) {
            parseNodeBody(builder)
        }
        marker.done(HytaleUiElementTypes.ID_OVERRIDE)
        return true
    }

    /**
     * Parse optional #id
     */
    private fun parseOptionalNodeId(builder: PsiBuilder) {
        if (builder.tokenType == HytaleUiTokenTypes.HASH) {
            val idMarker = builder.mark()
            builder.advanceLexer() // consume '#'
            if (builder.tokenType == HytaleUiTokenTypes.IDENTIFIER) {
                builder.advanceLexer() // consume IDENTIFIER
            }
            idMarker.done(HytaleUiElementTypes.NODE_ID)
        }
    }

    /**
     * { (property | variableDecl | nodeDecl | templateInst | idOverride)* }
     */
    private fun parseNodeBody(builder: PsiBuilder) {
        val bodyMarker = builder.mark()
        builder.advanceLexer() // consume '{'

        while (!builder.eof() && builder.tokenType != HytaleUiTokenTypes.RBRACE) {
            if (!parseNodeBodyStatement(builder)) {
                val errorMarker = builder.mark()
                builder.advanceLexer()
                errorMarker.error("Unexpected token in node body")
            }
        }

        if (builder.tokenType == HytaleUiTokenTypes.RBRACE) {
            builder.advanceLexer() // consume '}'
        } else {
            builder.error("Expected '}'")
        }

        bodyMarker.done(HytaleUiElementTypes.NODE_BODY)
    }

    private fun parseNodeBodyStatement(builder: PsiBuilder): Boolean {
        return when (builder.tokenType) {
            HytaleUiTokenTypes.DOLLAR_IDENTIFIER -> parseDollarStatement(builder)
            HytaleUiTokenTypes.AT_IDENTIFIER -> parseVariableDeclOrTemplateInst(builder)
            HytaleUiTokenTypes.HASH -> parseIdOverride(builder)
            HytaleUiTokenTypes.LINE_COMMENT -> { builder.advanceLexer(); true }
            HytaleUiTokenTypes.IDENTIFIER -> parsePropertyOrNodeDecl(builder)
            else -> false
        }
    }

    /**
     * Disambiguate between property (Key: value;) and node declaration (NodeType #id { })
     * by looking ahead for ':' vs '{' or '#'
     */
    private fun parsePropertyOrNodeDecl(builder: PsiBuilder): Boolean {
        // Look ahead to determine if this is a property or node
        val marker = builder.mark()
        builder.advanceLexer() // consume IDENTIFIER

        return when (builder.tokenType) {
            HytaleUiTokenTypes.COLON -> {
                // Property: Key: value;
                marker.rollbackTo()
                parseProperty(builder)
            }
            HytaleUiTokenTypes.LBRACE, HytaleUiTokenTypes.HASH -> {
                // Node declaration: NodeType { } or NodeType #id { }
                marker.rollbackTo()
                parseNodeDecl(builder)
            }
            else -> {
                // Incomplete input (e.g., during completion) or standalone identifier.
                // Drop the marker to keep the IDENTIFIER as a bare token inside the
                // enclosing NODE_BODY, preserving parent context for completion.
                marker.drop()
                true
            }
        }
    }

    /**
     * PropertyName: value;
     */
    private fun parseProperty(builder: PsiBuilder): Boolean {
        val marker = builder.mark()

        val keyMarker = builder.mark()
        builder.advanceLexer() // consume IDENTIFIER (property name)
        keyMarker.done(HytaleUiElementTypes.PROPERTY_KEY)

        if (builder.tokenType != HytaleUiTokenTypes.COLON) {
            marker.error("Expected ':' after property name")
            return true
        }
        builder.advanceLexer() // consume ':'

        parseValue(builder)
        expectSemicolon(builder)
        marker.done(HytaleUiElementTypes.PROPERTY)
        return true
    }

    /**
     * Parse a value expression (with additive operators)
     */
    private fun parseValue(builder: PsiBuilder): Boolean {
        if (!parsePrimaryValue(builder)) return false

        // Handle additive expressions: value + value, value - value
        while (builder.tokenType == HytaleUiTokenTypes.PLUS || builder.tokenType == HytaleUiTokenTypes.MINUS) {
            builder.advanceLexer() // consume operator
            if (!parsePrimaryValue(builder)) {
                builder.error("Expected value after operator")
                return true
            }
        }

        return true
    }

    /**
     * Parse a primary value:
     * - string literal, number, boolean, color, server string
     * - @variable reference
     * - $C.@variable reference
     * - TypeName(...) constructor
     * - (...) object literal
     * - ...@var spread
     * - NodeType { } (node as value in variable assignment)
     */
    private fun parsePrimaryValue(builder: PsiBuilder): Boolean {
        return when (builder.tokenType) {
            HytaleUiTokenTypes.STRING_LITERAL -> {
                val strMarker = builder.mark()
                builder.advanceLexer()
                strMarker.done(HytaleUiElementTypes.STRING_VALUE)
                true
            }
            HytaleUiTokenTypes.INTEGER_LITERAL -> { builder.advanceLexer(); true }
            HytaleUiTokenTypes.FLOAT_LITERAL -> { builder.advanceLexer(); true }
            HytaleUiTokenTypes.BOOLEAN_LITERAL -> { builder.advanceLexer(); true }
            HytaleUiTokenTypes.SERVER_STRING -> { builder.advanceLexer(); true }

            HytaleUiTokenTypes.COLOR_LITERAL -> parseColorValue(builder)

            HytaleUiTokenTypes.AT_IDENTIFIER -> parseVariableRefValue(builder)
            HytaleUiTokenTypes.DOLLAR_IDENTIFIER -> parseDollarRefValue(builder)

            HytaleUiTokenTypes.IDENTIFIER -> parseIdentifierValue(builder)

            HytaleUiTokenTypes.LPAREN -> parseObjectLiteral(builder)

            HytaleUiTokenTypes.SPREAD -> parseSpreadExpr(builder)

            HytaleUiTokenTypes.MINUS -> {
                // Negative number
                builder.advanceLexer() // consume '-'
                if (builder.tokenType == HytaleUiTokenTypes.INTEGER_LITERAL ||
                    builder.tokenType == HytaleUiTokenTypes.FLOAT_LITERAL) {
                    builder.advanceLexer()
                    true
                } else {
                    builder.error("Expected number after '-'")
                    true
                }
            }

            else -> false
        }
    }

    private fun parseColorValue(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        builder.advanceLexer() // consume COLOR_LITERAL

        // Optional alpha: #RRGGBB(0.5)
        if (builder.tokenType == HytaleUiTokenTypes.LPAREN) {
            builder.advanceLexer() // consume '('
            parseValue(builder) // alpha value
            if (builder.tokenType == HytaleUiTokenTypes.RPAREN) {
                builder.advanceLexer() // consume ')'
            }
        }

        marker.done(HytaleUiElementTypes.COLOR_VALUE)
        return true
    }

    /**
     * @variable - could be just a reference or could be a template instantiation (in value context)
     */
    private fun parseVariableRefValue(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        builder.advanceLexer() // consume @IDENTIFIER

        // Check if this is a template instantiation: @Template { ... }
        if (builder.tokenType == HytaleUiTokenTypes.LBRACE) {
            parseOptionalNodeId(builder)
            parseNodeBody(builder)
            marker.done(HytaleUiElementTypes.TEMPLATE_INST)
            return true
        }

        marker.done(HytaleUiElementTypes.VARIABLE_REF)
        return true
    }

    /**
     * $C.@variable reference in value context
     */
    private fun parseDollarRefValue(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        builder.advanceLexer() // consume $IDENTIFIER

        if (builder.tokenType == HytaleUiTokenTypes.DOT) {
            builder.advanceLexer() // consume '.'
            if (builder.tokenType == HytaleUiTokenTypes.AT_IDENTIFIER) {
                builder.advanceLexer() // consume @IDENTIFIER
            } else {
                builder.error("Expected @variable after '.'")
            }
        }

        // Check if this is a template instantiation: $C.@Template { ... }
        if (builder.tokenType == HytaleUiTokenTypes.LBRACE) {
            parseOptionalNodeId(builder)
            parseNodeBody(builder)
            marker.done(HytaleUiElementTypes.TEMPLATE_INST)
            return true
        }

        marker.done(HytaleUiElementTypes.FILE_VAR_REF)
        return true
    }

    /**
     * Could be:
     * - enum value (bare identifier like "Top", "Center")
     * - TypeName(...) constructor
     * - NodeType { ... } in value context (for variable assignments)
     */
    private fun parseIdentifierValue(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        builder.advanceLexer() // consume IDENTIFIER

        if (builder.tokenType == HytaleUiTokenTypes.LPAREN) {
            // Type constructor: TypeName(prop: val, ...)
            parsePropertyListInParens(builder)
            marker.done(HytaleUiElementTypes.TYPE_CONSTRUCTOR)
            return true
        }

        if (builder.tokenType == HytaleUiTokenTypes.LBRACE) {
            // Node declaration as value (for @Template = Group { ... };)
            parseOptionalNodeId(builder)
            parseNodeBody(builder)
            marker.done(HytaleUiElementTypes.NODE_DECL)
            return true
        }

        if (builder.tokenType == HytaleUiTokenTypes.HASH) {
            // Node declaration with ID as value: Group #id { ... }
            parseOptionalNodeId(builder)
            if (builder.tokenType == HytaleUiTokenTypes.LBRACE) {
                parseNodeBody(builder)
            }
            marker.done(HytaleUiElementTypes.NODE_DECL)
            return true
        }

        // Just an identifier (enum value or type reference)
        marker.drop()
        return true
    }

    /**
     * (...) - object literal or empty tuple
     */
    private fun parseObjectLiteral(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        parsePropertyListInParens(builder)
        marker.done(HytaleUiElementTypes.OBJECT_LITERAL)
        return true
    }

    /**
     * Parse parenthesized property list: (Key: value, Key2: value2, ...@spread)
     */
    private fun parsePropertyListInParens(builder: PsiBuilder) {
        builder.advanceLexer() // consume '('

        while (!builder.eof() && builder.tokenType != HytaleUiTokenTypes.RPAREN) {
            when (builder.tokenType) {
                HytaleUiTokenTypes.SPREAD -> parseSpreadExpr(builder)

                HytaleUiTokenTypes.IDENTIFIER -> {
                    val entryMarker = builder.mark()
                    builder.advanceLexer() // consume IDENTIFIER (key)

                    if (builder.tokenType == HytaleUiTokenTypes.COLON) {
                        builder.advanceLexer() // consume ':'
                        parseValue(builder)
                        entryMarker.done(HytaleUiElementTypes.PROPERTY_LIST_ENTRY)
                    } else {
                        // Bare identifier as value in list
                        entryMarker.drop()
                    }
                }

                HytaleUiTokenTypes.COMMA -> builder.advanceLexer() // skip comma

                else -> {
                    // Try parsing as value
                    if (!parseValue(builder)) {
                        val errorMarker = builder.mark()
                        builder.advanceLexer()
                        errorMarker.error("Unexpected token in property list")
                    }
                }
            }
        }

        if (builder.tokenType == HytaleUiTokenTypes.RPAREN) {
            builder.advanceLexer() // consume ')'
        } else {
            builder.error("Expected ')'")
        }
    }

    /**
     * ...@var or ...$C.@var or ...(...) spread expression
     */
    private fun parseSpreadExpr(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        builder.advanceLexer() // consume '...'

        when (builder.tokenType) {
            HytaleUiTokenTypes.AT_IDENTIFIER -> builder.advanceLexer()
            HytaleUiTokenTypes.DOLLAR_IDENTIFIER -> {
                builder.advanceLexer()
                if (builder.tokenType == HytaleUiTokenTypes.DOT) {
                    builder.advanceLexer()
                    if (builder.tokenType == HytaleUiTokenTypes.AT_IDENTIFIER) {
                        builder.advanceLexer()
                    }
                }
            }
            HytaleUiTokenTypes.LPAREN -> parseObjectLiteral(builder)
            else -> builder.error("Expected variable reference or object after '...'")
        }

        marker.done(HytaleUiElementTypes.SPREAD_EXPR)
        return true
    }

    private fun expectSemicolon(builder: PsiBuilder) {
        if (builder.tokenType == HytaleUiTokenTypes.SEMICOLON) {
            builder.advanceLexer()
        }
        // Semicolons are sometimes optional in practice, so don't error
    }

    private fun expectOptionalSemicolon(builder: PsiBuilder) {
        if (builder.tokenType == HytaleUiTokenTypes.SEMICOLON) {
            builder.advanceLexer()
        }
    }
}
