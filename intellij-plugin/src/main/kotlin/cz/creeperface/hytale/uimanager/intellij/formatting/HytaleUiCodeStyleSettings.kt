package cz.creeperface.hytale.uimanager.intellij.formatting

import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CustomCodeStyleSettings

class HytaleUiCodeStyleSettings(container: CodeStyleSettings) :
    CustomCodeStyleSettings("HytaleUiCodeStyleSettings", container) {

    /** Indent size for node bodies and nested objects. */
    @JvmField var INDENT_SIZE: Int = 2

    /** Whether to place each property on its own line inside objects/type constructors. */
    @JvmField var OBJECT_PROPERTIES_ON_SEPARATE_LINES: Boolean = false

    /** Space after colon in property assignments. */
    @JvmField var SPACE_AFTER_COLON: Boolean = true

    /** Space before colon in property assignments. */
    @JvmField var SPACE_BEFORE_COLON: Boolean = false

    /** Space after comma in property lists. */
    @JvmField var SPACE_AFTER_COMMA: Boolean = true

    /** Space inside parentheses: ( x ) vs (x). */
    @JvmField var SPACE_WITHIN_PARENS: Boolean = false

    /** Space before opening brace: Group { vs Group{. */
    @JvmField var SPACE_BEFORE_BRACE: Boolean = true

    /** Blank lines between top-level statements. */
    @JvmField var BLANK_LINES_BETWEEN_TOP_LEVEL: Int = 1

    /** Blank lines between properties inside node bodies. */
    @JvmField var BLANK_LINES_BETWEEN_PROPERTIES: Int = 0

    /** Blank lines before child nodes inside a parent node. */
    @JvmField var BLANK_LINES_BEFORE_CHILD_NODE: Int = 1
}
