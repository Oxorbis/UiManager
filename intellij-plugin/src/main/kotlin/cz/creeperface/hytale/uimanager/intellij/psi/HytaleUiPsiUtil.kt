package cz.creeperface.hytale.uimanager.intellij.psi

import com.intellij.lang.ASTNode
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiErrorElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileFactory
import cz.creeperface.hytale.uimanager.intellij.HytaleUiLanguage
import cz.creeperface.hytale.uimanager.intellij.HytaleUiSchemaService

object HytaleUiPsiUtil {

    /**
     * Finds the enclosing node type name for a given PSI element.
     * Walks up the tree to find the nearest NODE_DECL and returns its type identifier.
     */
    fun findEnclosingNodeTypeName(element: PsiElement): String? {
        var current = element.parent
        while (current != null && current !is HytaleUiFile) {
            val type = current.node.elementType
            if (type == HytaleUiElementTypes.NODE_DECL) {
                val firstChild = current.firstChild
                if (firstChild?.node?.elementType == HytaleUiTokenTypes.IDENTIFIER) {
                    return firstChild.text
                }
            }
            current = current.parent
        }
        return null
    }

    /**
     * Finds all variable declarations (@name = value) in the given file (top-level only).
     */
    fun findVariableDeclarations(file: HytaleUiFile): List<PsiElement> {
        val result = mutableListOf<PsiElement>()
        file.node.getChildren(null).forEach { child ->
            if (child.elementType == HytaleUiElementTypes.VARIABLE_DECL) {
                result.add(child.psi)
            }
        }
        return result
    }

    /**
     * Finds all file import declarations ($name = "path") in the given file.
     */
    fun findFileImports(file: HytaleUiFile): List<PsiElement> {
        val result = mutableListOf<PsiElement>()
        file.node.getChildren(null).forEach { child ->
            if (child.elementType == HytaleUiElementTypes.FILE_IMPORT) {
                result.add(child.psi)
            }
        }
        return result
    }

    /**
     * Gets the variable name from a VARIABLE_DECL element (returns the @name part without @).
     */
    fun getVariableName(variableDecl: PsiElement): String? {
        val atIdent = variableDecl.node.findChildByType(HytaleUiTokenTypes.AT_IDENTIFIER)
        return atIdent?.text?.removePrefix("@")
    }

    /**
     * Gets the import alias from a FILE_IMPORT element (returns the $name part without $).
     */
    fun getImportAlias(fileImport: PsiElement): String? {
        val dollarIdent = fileImport.node.findChildByType(HytaleUiTokenTypes.DOLLAR_IDENTIFIER)
        return dollarIdent?.text?.removePrefix("$")
    }

    /**
     * Gets the import path from a FILE_IMPORT element.
     */
    fun getImportPath(fileImport: PsiElement): String? {
        val stringLit = fileImport.node.findChildByType(HytaleUiTokenTypes.STRING_LITERAL)
        return stringLit?.text?.removeSurrounding("\"")
    }

    // ── Context inference helpers ──

    /**
     * Finds the enclosing node type name (walking through NODE_BODY to NODE_DECL or TEMPLATE_INST).
     * Skips through PsiErrorElement wrappers created by parser error recovery.
     *
     * For NODE_DECL: returns the node type name directly (e.g., "Label" from `Label { }`).
     * For TEMPLATE_INST: resolves the variable to find what node type it holds
     * (e.g., `@TextField { }` where `@TextField = TextField { ... };` → "TextField").
     */
    fun findEnclosingNodeType(element: PsiElement): String? {
        var current = element.parent
        while (current != null && current !is PsiFile) {
            if (current is PsiErrorElement) { current = current.parent; continue }
            val type = current.node.elementType

            if (type == HytaleUiElementTypes.NODE_DECL) {
                val firstChild = current.firstChild
                if (firstChild?.node?.elementType == HytaleUiTokenTypes.IDENTIFIER) {
                    return firstChild.text
                }
            }

            if (type == HytaleUiElementTypes.TEMPLATE_INST) {
                return resolveTemplateInstNodeType(current)
            }

            current = current.parent
        }
        return null
    }

    /**
     * Resolves the node type for a TEMPLATE_INST element by finding what the variable is defined as.
     * E.g., `@TextField { }` → looks up `@TextField = TextField { ... };` → returns "TextField".
     * Also handles `$C.@Template { }` by resolving across files.
     */
    fun resolveTemplateInstNodeType(templateInst: PsiElement): String? {
        val atIdent = templateInst.node.findChildByType(HytaleUiTokenTypes.AT_IDENTIFIER)
        val varName = atIdent?.text?.removePrefix("@") ?: return null

        val dollarIdent = templateInst.node.findChildByType(HytaleUiTokenTypes.DOLLAR_IDENTIFIER)

        val file = templateInst.containingFile as? HytaleUiFile ?: return null

        val varDecl = if (dollarIdent != null) {
            // Cross-file: $C.@Template — resolve via import
            val alias = dollarIdent.text.removePrefix("$")
            findVariableDeclInImport(file, alias, varName)
        } else {
            // Local: @Template — search local scopes then file level
            findVariableDeclLocally(templateInst, varName, file)
        }

        return varDecl?.let { extractNodeTypeFromVariableDecl(it) }
    }

    /**
     * Extracts the node type name from a VARIABLE_DECL's value.
     * E.g., `@TextField = TextField { ... };` → "TextField"
     * E.g., `@MyLabel = Label { ... };` → "Label"
     */
    private fun extractNodeTypeFromVariableDecl(varDecl: PsiElement): String? {
        val schema = try { HytaleUiSchemaService.getInstance() } catch (_: Exception) { return null }

        // Find the value part (after '=')
        val equals = varDecl.node.findChildByType(HytaleUiTokenTypes.EQUALS) ?: return null
        var child = equals.psi.nextSibling
        while (child != null) {
            val childType = child.node.elementType
            if (childType == HytaleUiTokenTypes.WHITE_SPACE || childType == HytaleUiTokenTypes.LINE_COMMENT) {
                child = child.nextSibling
                continue
            }

            // NODE_DECL as value: `TextField { ... }`
            if (childType == HytaleUiElementTypes.NODE_DECL) {
                val nodeTypeName = child.firstChild
                if (nodeTypeName?.node?.elementType == HytaleUiTokenTypes.IDENTIFIER) {
                    return nodeTypeName.text
                }
            }

            // Direct identifier that is a node name: rare but possible
            if (childType == HytaleUiTokenTypes.IDENTIFIER && schema.isNodeName(child.text)) {
                return child.text
            }

            break
        }

        return null
    }

    private fun findVariableDeclLocally(from: PsiElement, varName: String, file: HytaleUiFile): PsiElement? {
        // Walk up scopes
        var current = from.parent
        while (current != null && current !is HytaleUiFile) {
            for (child in current.children) {
                if (child.node.elementType == HytaleUiElementTypes.VARIABLE_DECL) {
                    if (getVariableName(child) == varName) return child
                }
            }
            current = current.parent
        }
        // File level
        for (varDecl in findVariableDeclarations(file)) {
            if (getVariableName(varDecl) == varName) return varDecl
        }
        // Imports
        for (importElement in findFileImports(file)) {
            val path = getImportPath(importElement) ?: continue
            val targetVFile = cz.creeperface.hytale.uimanager.intellij.reference.HytaleUiFileResolver.resolveImportPath(file, path) ?: continue
            val targetPsi = com.intellij.psi.PsiManager.getInstance(file.project).findFile(targetVFile) as? HytaleUiFile ?: continue
            for (varDecl in findVariableDeclarations(targetPsi)) {
                if (getVariableName(varDecl) == varName) return varDecl
            }
        }
        return null
    }

    private fun findVariableDeclInImport(file: HytaleUiFile, alias: String, varName: String): PsiElement? {
        for (importElement in findFileImports(file)) {
            if (getImportAlias(importElement) != alias) continue
            val path = getImportPath(importElement) ?: continue
            val targetVFile = cz.creeperface.hytale.uimanager.intellij.reference.HytaleUiFileResolver.resolveImportPath(file, path) ?: continue
            val targetPsi = com.intellij.psi.PsiManager.getInstance(file.project).findFile(targetVFile) as? HytaleUiFile ?: continue
            for (varDecl in findVariableDeclarations(targetPsi)) {
                if (getVariableName(varDecl) == varName) return varDecl
            }
        }
        return null
    }

    /**
     * Finds the enclosing type constructor name (e.g., "LabelStyle" in `LabelStyle(FontSize: 17)`).
     */
    fun findEnclosingTypeConstructorName(element: PsiElement): String? {
        var current = element.parent
        while (current != null && current !is PsiFile) {
            if (current.node.elementType == HytaleUiElementTypes.TYPE_CONSTRUCTOR) {
                val firstChild = current.firstChild
                if (firstChild?.node?.elementType == HytaleUiTokenTypes.IDENTIFIER) {
                    return firstChild.text
                }
            }
            current = current.parent
        }
        return null
    }

    /**
     * Finds the property name that the current element is a value of.
     * E.g., for `Style: (FontSize: 17)`, when called from inside the `(...)`,
     * returns "Style".
     */
    fun findEnclosingPropertyName(element: PsiElement): String? {
        var current = element.parent
        while (current != null && current !is PsiFile) {
            val type = current.node.elementType
            if (type == HytaleUiElementTypes.PROPERTY) {
                val keyNode = current.node.findChildByType(HytaleUiElementTypes.PROPERTY_KEY)
                return keyNode?.text
            }
            if (type == HytaleUiElementTypes.PROPERTY_LIST_ENTRY) {
                // Inside Identifier: value within a property list
                val firstChild = current.firstChild
                if (firstChild?.node?.elementType == HytaleUiTokenTypes.IDENTIFIER) {
                    return firstChild.text
                }
            }
            // Stop at node body boundaries
            if (type == HytaleUiElementTypes.NODE_BODY) return null
            current = current.parent
        }
        return null
    }

    /**
     * Infers the expected type name for properties at the current position.
     * Works for:
     * - Direct node properties: `Label { Style: ... }` → infers from Label.Style → LabelStyle
     * - Type constructor properties: `LabelStyle(FontSize: ...)` → infers from LabelStyle.FontSize → Integer
     * - Anonymous objects as property values: `Style: (FontSize: ...)` → infers Label.Style → LabelStyle, then LabelStyle properties
     *
     * Returns the type name whose properties should be offered/validated.
     */
    fun inferPropertyContextType(element: PsiElement): String? {
        val schema = HytaleUiSchemaService.getInstance()

        // Check if we're inside a named type constructor like LabelStyle(...)
        val typeConstructorName = findImmediateTypeConstructorName(element)
        if (typeConstructorName != null && schema.isTypeName(typeConstructorName)) {
            return typeConstructorName
        }

        // Check if we're inside an anonymous object (...) that is the value of a property
        val objectLiteral = findImmediateObjectLiteral(element)
        if (objectLiteral != null) {
            // Walk up from the object literal to find what property it belongs to
            return inferTypeFromPropertyContext(objectLiteral, schema)
        }

        // Check if we're directly in a node body
        val nodeType = findEnclosingNodeType(element)
        if (nodeType != null && schema.isNodeName(nodeType)) {
            return nodeType
        }

        return null
    }

    /**
     * Finds the immediate enclosing TYPE_CONSTRUCTOR (not through any object/node boundaries).
     * Stops at OBJECT_LITERAL because each (...) introduces its own type context.
     */
    private fun findImmediateTypeConstructorName(element: PsiElement): String? {
        var current = element.parent
        while (current != null && current !is PsiFile) {
            val type = current.node.elementType
            if (type == HytaleUiElementTypes.TYPE_CONSTRUCTOR) {
                val firstChild = current.firstChild
                if (firstChild?.node?.elementType == HytaleUiTokenTypes.IDENTIFIER) {
                    return firstChild.text
                }
            }
            // Stop at boundaries — each of these introduces a new scope/context
            if (type == HytaleUiElementTypes.OBJECT_LITERAL ||
                type == HytaleUiElementTypes.NODE_BODY ||
                type == HytaleUiElementTypes.NODE_DECL ||
                type == HytaleUiElementTypes.TEMPLATE_INST) {
                return null
            }
            current = current.parent
        }
        return null
    }

    /**
     * Finds the immediate enclosing OBJECT_LITERAL.
     */
    private fun findImmediateObjectLiteral(element: PsiElement): PsiElement? {
        var current = element.parent
        while (current != null && current !is PsiFile) {
            val type = current.node.elementType
            if (type == HytaleUiElementTypes.OBJECT_LITERAL) {
                return current
            }
            if (type == HytaleUiElementTypes.TYPE_CONSTRUCTOR ||
                type == HytaleUiElementTypes.NODE_BODY ||
                type == HytaleUiElementTypes.NODE_DECL ||
                type == HytaleUiElementTypes.TEMPLATE_INST) {
                return null
            }
            current = current.parent
        }
        return null
    }

    /**
     * Given an OBJECT_LITERAL, infer the type from its property assignment context.
     * E.g., `Style: (...)` in a Label → Label.Style is LabelStyle → return "LabelStyle"
     * E.g., `Default: (...)` in ButtonStyle(...) → ButtonStyle.Default is ButtonStyleState → return "ButtonStyleState"
     */
    private fun inferTypeFromPropertyContext(objectLiteral: PsiElement, schema: HytaleUiSchemaService): String? {
        // Walk up to find the property or property list entry this object is assigned to
        var current = objectLiteral.parent
        while (current != null && current !is PsiFile) {
            val type = current.node.elementType

            if (type == HytaleUiElementTypes.PROPERTY) {
                // We're in `PropertyName: (...)` inside a node
                val propName = current.node.findChildByType(HytaleUiElementTypes.PROPERTY_KEY)?.text ?: return null
                val nodeType = findEnclosingNodeType(current)
                if (nodeType != null) {
                    return schema.getPropertyExpectedType(nodeType, propName)
                }
                return null
            }

            if (type == HytaleUiElementTypes.PROPERTY_LIST_ENTRY) {
                // We're in `Key: (...)` inside a type constructor or another object
                val propName = current.firstChild?.text ?: return null
                // Find the enclosing type constructor or infer from parent object
                val parentType = inferPropertyContextType(current)
                if (parentType != null) {
                    return schema.getPropertyExpectedType(parentType, propName)
                }
                return null
            }

            // Stop at boundaries
            if (type == HytaleUiElementTypes.NODE_BODY ||
                type == HytaleUiElementTypes.NODE_DECL ||
                type == HytaleUiElementTypes.TEMPLATE_INST) {
                return null
            }
            current = current.parent
        }
        return null
    }

    // ── Element factory helpers for rename ──

    /**
     * Creates a new AT_IDENTIFIER ASTNode with the text `@newName`.
     * Parses a dummy file `@newName = 1;` and extracts the AT_IDENTIFIER token.
     */
    fun createAtIdentifierNode(project: Project, newName: String): ASTNode {
        val dummyFile = PsiFileFactory.getInstance(project)
            .createFileFromText("dummy.ui", HytaleUiLanguage.INSTANCE, "@$newName = 1;")
        // VARIABLE_DECL → first child is AT_IDENTIFIER
        val varDecl = dummyFile.node.firstChildNode
        val atIdent = varDecl.findChildByType(HytaleUiTokenTypes.AT_IDENTIFIER)
            ?: throw IllegalStateException("Failed to create AT_IDENTIFIER node for '@$newName'")
        return atIdent
    }

    /**
     * Creates a new DOLLAR_IDENTIFIER ASTNode with the text `$newName`.
     * Parses a dummy file `$newName = "x.ui";` and extracts the DOLLAR_IDENTIFIER token.
     */
    fun createDollarIdentifierNode(project: Project, newName: String): ASTNode {
        val dummyFile = PsiFileFactory.getInstance(project)
            .createFileFromText("dummy.ui", HytaleUiLanguage.INSTANCE, "\$$newName = \"x.ui\";")
        val fileImport = dummyFile.node.firstChildNode
        val dollarIdent = fileImport.findChildByType(HytaleUiTokenTypes.DOLLAR_IDENTIFIER)
            ?: throw IllegalStateException("Failed to create DOLLAR_IDENTIFIER node for '\$$newName'")
        return dollarIdent
    }
}
