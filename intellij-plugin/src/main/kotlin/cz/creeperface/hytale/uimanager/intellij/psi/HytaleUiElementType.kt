package cz.creeperface.hytale.uimanager.intellij.psi

import com.intellij.psi.tree.IElementType
import cz.creeperface.hytale.uimanager.intellij.HytaleUiLanguage

class HytaleUiElementType(debugName: String) : IElementType(debugName, HytaleUiLanguage.INSTANCE) {
    override fun toString(): String = "HytaleUiElementType.${super.toString()}"
}

object HytaleUiElementTypes {
    // Top-level statements
    @JvmField val FILE_IMPORT = HytaleUiElementType("FILE_IMPORT")
    @JvmField val VARIABLE_DECL = HytaleUiElementType("VARIABLE_DECL")
    @JvmField val NODE_DECL = HytaleUiElementType("NODE_DECL")
    @JvmField val TEMPLATE_INST = HytaleUiElementType("TEMPLATE_INST")
    @JvmField val ID_OVERRIDE = HytaleUiElementType("ID_OVERRIDE")

    // Parts
    @JvmField val NODE_ID = HytaleUiElementType("NODE_ID")
    @JvmField val NODE_BODY = HytaleUiElementType("NODE_BODY")
    @JvmField val PROPERTY = HytaleUiElementType("PROPERTY")
    @JvmField val PROPERTY_KEY = HytaleUiElementType("PROPERTY_KEY")

    // Values
    @JvmField val VALUE = HytaleUiElementType("VALUE")
    @JvmField val VARIABLE_REF = HytaleUiElementType("VARIABLE_REF")
    @JvmField val FILE_VAR_REF = HytaleUiElementType("FILE_VAR_REF")
    @JvmField val TYPE_CONSTRUCTOR = HytaleUiElementType("TYPE_CONSTRUCTOR")
    @JvmField val OBJECT_LITERAL = HytaleUiElementType("OBJECT_LITERAL")
    @JvmField val SPREAD_EXPR = HytaleUiElementType("SPREAD_EXPR")
    @JvmField val ADDITIVE_EXPR = HytaleUiElementType("ADDITIVE_EXPR")
    @JvmField val COLOR_VALUE = HytaleUiElementType("COLOR_VALUE")
    @JvmField val PROPERTY_LIST = HytaleUiElementType("PROPERTY_LIST")
    @JvmField val PROPERTY_LIST_ENTRY = HytaleUiElementType("PROPERTY_LIST_ENTRY")
    @JvmField val TEMPLATE_REF = HytaleUiElementType("TEMPLATE_REF")
    @JvmField val STRING_VALUE = HytaleUiElementType("STRING_VALUE")
}
