package cz.creeperface.hytale.uimanager.intellij.highlighting

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import cz.creeperface.hytale.uimanager.intellij.HytaleUiSchemaService
import cz.creeperface.hytale.uimanager.intellij.psi.HytaleUiElementTypes
import cz.creeperface.hytale.uimanager.intellij.psi.HytaleUiTokenTypes

class HytaleUiAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        val elementType = element.node.elementType
        val parentType = element.parent?.node?.elementType

        when {
            // Node type name: IDENTIFIER as first child of NODE_DECL
            elementType == HytaleUiTokenTypes.IDENTIFIER && parentType == HytaleUiElementTypes.NODE_DECL -> {
                val prevSibling = findPrevMeaningfulSibling(element)
                if (prevSibling == null) {
                    // First child = node type name
                    annotateNodeTypeName(element, holder)
                }
            }

            // Property key highlighting
            elementType == HytaleUiElementTypes.PROPERTY_KEY -> {
                holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                    .textAttributes(HytaleUiSyntaxHighlighter.PROPERTY_KEY)
                    .create()
            }

            // IDENTIFIER in value context could be enum value or type constructor name
            elementType == HytaleUiTokenTypes.IDENTIFIER && parentType == HytaleUiElementTypes.TYPE_CONSTRUCTOR -> {
                val prevSibling = findPrevMeaningfulSibling(element)
                if (prevSibling == null) {
                    // First child = type constructor name
                    annotateTypeConstructorName(element, holder)
                }
            }

            // Bare IDENTIFIER in value context = enum value
            elementType == HytaleUiTokenTypes.IDENTIFIER && isInValueContext(element) -> {
                annotateEnumValue(element, holder)
            }

            // IDENTIFIER in property list entry = property key inside type constructor/object
            elementType == HytaleUiTokenTypes.IDENTIFIER && parentType == HytaleUiElementTypes.PROPERTY_LIST_ENTRY -> {
                val prevSibling = findPrevMeaningfulSibling(element)
                if (prevSibling == null) {
                    // First child = property key in tuple
                    holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                        .textAttributes(HytaleUiSyntaxHighlighter.PROPERTY_KEY)
                        .create()
                }
            }

            // Node ID highlighting (the identifier part after #)
            elementType == HytaleUiTokenTypes.IDENTIFIER && parentType == HytaleUiElementTypes.NODE_ID -> {
                holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                    .textAttributes(HytaleUiSyntaxHighlighter.NODE_ID)
                    .create()
            }

            // ID override: the identifier after # in ID_OVERRIDE
            elementType == HytaleUiTokenTypes.IDENTIFIER && parentType == HytaleUiElementTypes.ID_OVERRIDE -> {
                val prevSibling = findPrevMeaningfulSibling(element)
                if (prevSibling?.node?.elementType == HytaleUiTokenTypes.HASH) {
                    holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                        .textAttributes(HytaleUiSyntaxHighlighter.NODE_ID)
                        .create()
                }
            }
        }
    }

    private fun annotateNodeTypeName(element: PsiElement, holder: AnnotationHolder) {
        val schema = HytaleUiSchemaService.getInstance()
        val name = element.text
        if (schema.isNodeName(name)) {
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                .textAttributes(HytaleUiSyntaxHighlighter.NODE_TYPE)
                .create()
        }
    }

    private fun annotateTypeConstructorName(element: PsiElement, holder: AnnotationHolder) {
        val schema = HytaleUiSchemaService.getInstance()
        val name = element.text
        if (schema.isTypeName(name)) {
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                .textAttributes(HytaleUiSyntaxHighlighter.TYPE_NAME)
                .create()
        }
    }

    private fun annotateEnumValue(element: PsiElement, holder: AnnotationHolder) {
        val schema = HytaleUiSchemaService.getInstance()
        val name = element.text
        if (schema.isEnumValue(name)) {
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                .textAttributes(HytaleUiSyntaxHighlighter.ENUM_VALUE)
                .create()
        }
    }

    private fun isInValueContext(element: PsiElement): Boolean {
        val parent = element.parent ?: return false
        val parentType = parent.node.elementType
        // After ':' in a property, or after '=' in a variable decl, or as standalone value
        return parentType == HytaleUiElementTypes.PROPERTY ||
                parentType == HytaleUiElementTypes.VARIABLE_DECL ||
                parentType == HytaleUiElementTypes.VALUE
    }

    private fun findPrevMeaningfulSibling(element: PsiElement): PsiElement? {
        var sibling = element.prevSibling
        while (sibling != null) {
            val type = sibling.node.elementType
            if (type != HytaleUiTokenTypes.WHITE_SPACE && type != HytaleUiTokenTypes.LINE_COMMENT) {
                return sibling
            }
            sibling = sibling.prevSibling
        }
        return null
    }
}
