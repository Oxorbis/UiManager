package cz.creeperface.hytale.uimanager.intellij.completion

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiErrorElement
import com.intellij.psi.PsiFile
import com.intellij.util.ProcessingContext
import cz.creeperface.hytale.uimanager.intellij.HytaleUiSchemaService
import cz.creeperface.hytale.uimanager.intellij.psi.HytaleUiElementTypes
import cz.creeperface.hytale.uimanager.intellij.psi.HytaleUiFile
import cz.creeperface.hytale.uimanager.intellij.psi.HytaleUiPsiUtil
import cz.creeperface.hytale.uimanager.intellij.psi.HytaleUiTokenTypes
import cz.creeperface.hytale.uimanager.intellij.reference.HytaleUiFileResolver

class HytaleUiCompletionContributor : CompletionContributor() {

    init {
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement(HytaleUiTokenTypes.IDENTIFIER),
            IdentifierCompletionProvider()
        )

        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement(HytaleUiTokenTypes.AT_IDENTIFIER),
            VariableCompletionProvider()
        )

        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement(HytaleUiTokenTypes.DOLLAR_IDENTIFIER),
            FileImportCompletionProvider()
        )
    }

    private class IdentifierCompletionProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(
            parameters: CompletionParameters,
            context: ProcessingContext,
            result: CompletionResultSet
        ) {
            val position = parameters.position
            val schema = try { HytaleUiSchemaService.getInstance() } catch (_: Exception) { return }

            when {
                // Inside TypeName(...) or (...) — property list context
                isInPropertyList(position) -> {
                    if (isInValuePositionInPropertyList(position)) {
                        // After ':' inside property list entry — offer values
                        addValueCompletions(position, schema, result)
                    } else {
                        // Property name position inside type/object
                        addContextAwarePropertyListCompletions(position, schema, result)
                    }
                }

                // After ':' in node property — offer values
                isInValuePosition(position) -> {
                    addValueCompletions(position, schema, result)
                }

                // In node body or file level — offer properties + child nodes
                isInNodeBodyOrFileLevel(position) -> {
                    addNodeBodyCompletions(position, schema, result)
                }

                // Fallback
                else -> {
                    addNodeBodyCompletions(position, schema, result)
                }
            }

            // Always add variables with lower priority (without @ prefix)
            addVariablesWithLowPriority(position, result)
        }

        // ── Value completions (after ':') ──

        private fun addValueCompletions(position: PsiElement, schema: HytaleUiSchemaService, result: CompletionResultSet) {
            val expectedType = resolveExpectedValueType(position, schema)

            if (expectedType != null) {
                // Enum values for enum types
                val enumValues = schema.getEnumValues(expectedType)
                if (enumValues != null) {
                    for (value in enumValues) {
                        result.addElement(
                            PrioritizedLookupElement.withPriority(
                                LookupElementBuilder.create(value)
                                    .withTypeText(expectedType)
                                    .withIcon(AllIcons.Nodes.Enum)
                                    .withBoldness(true),
                                20.0
                            )
                        )
                    }
                    return // Only show matching enum values, nothing else
                }

                // Type constructor for complex types
                if (schema.isTypeName(expectedType)) {
                    result.addElement(
                        PrioritizedLookupElement.withPriority(
                            LookupElementBuilder.create(expectedType)
                                .withTypeText("Type")
                                .withIcon(AllIcons.Nodes.Type)
                                .withBoldness(true)
                                .withInsertHandler { ctx, _ ->
                                    ctx.document.insertString(ctx.tailOffset, "()")
                                    ctx.editor.caretModel.moveToOffset(ctx.tailOffset - 1)
                                },
                            15.0
                        )
                    )
                }

                // Boolean values for Boolean type
                if (expectedType == "Boolean") {
                    for (b in listOf("true", "false")) {
                        result.addElement(
                            PrioritizedLookupElement.withPriority(
                                LookupElementBuilder.create(b)
                                    .withTypeText("Boolean")
                                    .withIcon(AllIcons.Nodes.Constant)
                                    .withBoldness(true),
                                20.0
                            )
                        )
                    }
                    return
                }
            }

            // Fallback: offer all enum values + type constructors with lower priority
            for (enumName in schema.enumNames) {
                for (value in schema.getEnumValues(enumName) ?: emptyList()) {
                    result.addElement(
                        PrioritizedLookupElement.withPriority(
                            LookupElementBuilder.create(value)
                                .withTypeText(enumName)
                                .withIcon(AllIcons.Nodes.Enum),
                            5.0
                        )
                    )
                }
            }

            for (name in schema.typeNames) {
                result.addElement(
                    PrioritizedLookupElement.withPriority(
                        LookupElementBuilder.create(name)
                            .withTypeText("Type")
                            .withIcon(AllIcons.Nodes.Type)
                            .withInsertHandler { ctx, _ ->
                                ctx.document.insertString(ctx.tailOffset, "()")
                                ctx.editor.caretModel.moveToOffset(ctx.tailOffset - 1)
                            },
                        3.0
                    )
                )
            }
        }

        // ── Node body completions (properties + child nodes) ──

        private fun addNodeBodyCompletions(position: PsiElement, schema: HytaleUiSchemaService, result: CompletionResultSet) {
            val nodeType = findEnclosingNodeType(position)
            val nodeInfo = if (nodeType != null) schema.getNodeInfo(nodeType) else null

            if (nodeInfo != null) {
                // Properties of this node — high priority
                for ((propName, propTypes) in nodeInfo.properties) {
                    val typeHint = simplifyTypeName(propTypes.firstOrNull())
                    result.addElement(
                        PrioritizedLookupElement.withPriority(
                            LookupElementBuilder.create(propName)
                                .withTypeText(typeHint)
                                .withIcon(AllIcons.Nodes.Property)
                                .withInsertHandler { ctx, _ ->
                                    ctx.document.insertString(ctx.tailOffset, ": ")
                                    ctx.editor.caretModel.moveToOffset(ctx.tailOffset)
                                },
                            15.0
                        )
                    )
                }

                // Child nodes — only if this node supports children, with separator
                if (nodeInfo.hasChildren) {
                    addChildNodeCompletions(schema, result)
                }
            } else {
                // File level — only nodes
                addChildNodeCompletions(schema, result)
            }
        }

        private fun addChildNodeCompletions(schema: HytaleUiSchemaService, result: CompletionResultSet) {
            for (name in schema.nodeNames) {
                result.addElement(
                    PrioritizedLookupElement.withPriority(
                        LookupElementBuilder.create(name)
                            .withTypeText("Node")
                            .withIcon(AllIcons.Nodes.Class)
                            .withBoldness(true)
                            .withInsertHandler { ctx, _ ->
                                val indent = getIndentAtCaret(ctx)
                                val childIndent = "$indent  "
                                ctx.document.insertString(ctx.tailOffset, " {\n$childIndent\n$indent}")
                                ctx.editor.caretModel.moveToOffset(ctx.tailOffset - indent.length - 2)
                            },
                        10.0
                    )
                )
            }
        }

        // ── Property list completions (inside type constructors / anonymous objects) ──

        private fun addContextAwarePropertyListCompletions(position: PsiElement, schema: HytaleUiSchemaService, result: CompletionResultSet) {
            val contextType = HytaleUiPsiUtil.inferPropertyContextType(position)
                ?: HytaleUiPsiUtil.findEnclosingTypeConstructorName(position)

            if (contextType != null) {
                val properties = schema.getTypeProperties(contextType) ?: return
                for ((propName, propTypes) in properties) {
                    val typeHint = simplifyTypeName(propTypes.firstOrNull())
                    result.addElement(
                        PrioritizedLookupElement.withPriority(
                            LookupElementBuilder.create(propName)
                                .withTypeText("$typeHint ($contextType)")
                                .withIcon(AllIcons.Nodes.Property)
                                .withInsertHandler { ctx, _ ->
                                    ctx.document.insertString(ctx.tailOffset, ": ")
                                    ctx.editor.caretModel.moveToOffset(ctx.tailOffset)
                                },
                            15.0
                        )
                    )
                }
            }
        }

        // ── Variables with low priority (no @ prefix needed) ──

        private fun addVariablesWithLowPriority(position: PsiElement, result: CompletionResultSet) {
            val file = position.containingFile as? HytaleUiFile ?: return
            val seen = mutableSetOf<String>()

            // Local scope variables
            var current: PsiElement? = position.parent
            while (current != null && current !is HytaleUiFile) {
                if (current is PsiErrorElement) { current = current.parent; continue }
                for (child in current.children) {
                    if (child.node.elementType == HytaleUiElementTypes.VARIABLE_DECL) {
                        val name = HytaleUiPsiUtil.getVariableName(child)
                        if (name != null && seen.add(name)) {
                            result.addElement(
                                PrioritizedLookupElement.withPriority(
                                    LookupElementBuilder.create("@$name")
                                        .withTypeText("local var")
                                        .withIcon(AllIcons.Nodes.Variable)
                                        .withLookupString(name),
                                    2.0
                                )
                            )
                        }
                    }
                }
                current = current.parent
            }

            // File-level variables
            for (varDecl in HytaleUiPsiUtil.findVariableDeclarations(file)) {
                val name = HytaleUiPsiUtil.getVariableName(varDecl) ?: continue
                if (seen.add(name)) {
                    result.addElement(
                        PrioritizedLookupElement.withPriority(
                            LookupElementBuilder.create("@$name")
                                .withTypeText("variable")
                                .withIcon(AllIcons.Nodes.Variable)
                                .withLookupString(name),
                            1.0
                        )
                    )
                }
            }

            // Variables from imported files
            for (importElement in HytaleUiPsiUtil.findFileImports(file)) {
                val path = HytaleUiPsiUtil.getImportPath(importElement) ?: continue
                val alias = HytaleUiPsiUtil.getImportAlias(importElement) ?: continue
                val targetFile = HytaleUiFileResolver.resolveImportPath(file, path) ?: continue
                val psiFile = com.intellij.psi.PsiManager.getInstance(file.project)
                    .findFile(targetFile) as? HytaleUiFile ?: continue

                for (varDecl in HytaleUiPsiUtil.findVariableDeclarations(psiFile)) {
                    val varName = HytaleUiPsiUtil.getVariableName(varDecl) ?: continue
                    if (seen.add(varName)) {
                        result.addElement(
                            PrioritizedLookupElement.withPriority(
                                LookupElementBuilder.create("@$varName")
                                    .withTypeText("from \$$alias")
                                    .withIcon(AllIcons.Nodes.Variable)
                                    .withLookupString(varName),
                                0.5
                            )
                        )
                    }
                }
            }
        }

        // ── Expected value type resolution ──

        private fun resolveExpectedValueType(element: PsiElement, schema: HytaleUiSchemaService): String? {
            var current = element.parent
            while (current != null && current !is PsiFile) {
                val type = current.node?.elementType
                if (current is PsiErrorElement) { current = current.parent; continue }

                if (type == HytaleUiElementTypes.PROPERTY) {
                    val propName = current.node.findChildByType(HytaleUiElementTypes.PROPERTY_KEY)?.text ?: return null
                    val nodeType = findEnclosingNodeType(current)
                    if (nodeType != null) return schema.getPropertyExpectedType(nodeType, propName)
                    return null
                }

                if (type == HytaleUiElementTypes.PROPERTY_LIST_ENTRY) {
                    val propName = current.firstChild?.text ?: return null
                    val contextType = HytaleUiPsiUtil.inferPropertyContextType(current)
                    if (contextType != null) return schema.getPropertyExpectedType(contextType, propName)
                    return null
                }

                if (type == HytaleUiElementTypes.NODE_BODY) return null
                current = current.parent
            }
            return null
        }

        // ── Position detection helpers ──

        private fun structuralParent(element: PsiElement): PsiElement? {
            var current = element.parent
            while (current is PsiErrorElement) {
                current = current.parent
            }
            return current
        }

        private fun isInValuePosition(element: PsiElement): Boolean {
            var current = element.parent
            while (current != null) {
                if (current is PsiErrorElement) { current = current.parent; continue }
                val type = current.node?.elementType
                if (type == HytaleUiElementTypes.PROPERTY) {
                    val colon = current.node.findChildByType(HytaleUiTokenTypes.COLON)
                    return colon != null && element.textOffset > colon.textRange.endOffset
                }
                if (type == HytaleUiElementTypes.VARIABLE_DECL) {
                    val equals = current.node.findChildByType(HytaleUiTokenTypes.EQUALS)
                    return equals != null && element.textOffset > equals.textRange.endOffset
                }
                if (type == HytaleUiElementTypes.NODE_BODY || current is HytaleUiFile) return false
                current = current.parent
            }
            return false
        }

        private fun isInValuePositionInPropertyList(element: PsiElement): Boolean {
            var current = element.parent
            while (current != null) {
                if (current is PsiErrorElement) { current = current.parent; continue }
                val type = current.node?.elementType
                if (type == HytaleUiElementTypes.PROPERTY_LIST_ENTRY) {
                    val colon = current.node.findChildByType(HytaleUiTokenTypes.COLON)
                    return colon != null && element.textOffset > colon.textRange.endOffset
                }
                if (type == HytaleUiElementTypes.TYPE_CONSTRUCTOR ||
                    type == HytaleUiElementTypes.OBJECT_LITERAL) return false
                if (type == HytaleUiElementTypes.NODE_BODY) return false
                current = current.parent
            }
            return false
        }

        private fun isInNodeBodyOrFileLevel(element: PsiElement): Boolean {
            val parent = structuralParent(element) ?: return false
            val parentType = parent.node?.elementType
            return parentType == HytaleUiElementTypes.NODE_BODY ||
                    parentType == HytaleUiElementTypes.NODE_DECL ||
                    parent is HytaleUiFile
        }

        private fun isInPropertyList(element: PsiElement): Boolean {
            var current = element.parent
            while (current != null && current !is PsiFile) {
                if (current is PsiErrorElement) { current = current.parent; continue }
                val type = current.node?.elementType
                if (type == HytaleUiElementTypes.TYPE_CONSTRUCTOR ||
                    type == HytaleUiElementTypes.OBJECT_LITERAL) {
                    return true
                }
                if (type == HytaleUiElementTypes.NODE_BODY) return false
                current = current.parent
            }
            return false
        }

        private fun findEnclosingNodeType(element: PsiElement): String? {
            // Delegate to the shared utility which handles both NODE_DECL and TEMPLATE_INST
            return HytaleUiPsiUtil.findEnclosingNodeType(element)
        }

        private fun simplifyTypeName(type: String?): String {
            if (type == null) return "?"
            return type.substringAfterLast('.')
        }

        private fun getIndentAtCaret(ctx: InsertionContext): String {
            val doc = ctx.document
            val offset = ctx.startOffset
            val lineNum = doc.getLineNumber(offset)
            val lineStart = doc.getLineStartOffset(lineNum)
            val lineText = doc.getText(com.intellij.openapi.util.TextRange(lineStart, offset))
            return lineText.takeWhile { it == ' ' || it == '\t' }
        }
    }

    private class VariableCompletionProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(
            parameters: CompletionParameters,
            context: ProcessingContext,
            result: CompletionResultSet
        ) {
            val position = parameters.position
            val file = position.containingFile as? HytaleUiFile ?: return
            val prefix = result.prefixMatcher.prefix.removePrefix("@")
            val prefixedResult = result.withPrefixMatcher("@$prefix")

            val seen = mutableSetOf<String>()

            // 1. Local variables from enclosing scopes — highest priority
            var current: PsiElement? = position.parent
            while (current != null && current !is HytaleUiFile) {
                if (current is PsiErrorElement) { current = current.parent; continue }
                for (child in current.children) {
                    if (child.node.elementType == HytaleUiElementTypes.VARIABLE_DECL) {
                        val name = HytaleUiPsiUtil.getVariableName(child)
                        if (name != null && seen.add(name)) {
                            prefixedResult.addElement(
                                PrioritizedLookupElement.withPriority(
                                    LookupElementBuilder.create("@$name")
                                        .withTypeText("local var")
                                        .withIcon(AllIcons.Nodes.Variable)
                                        .withBoldness(true),
                                    30.0
                                )
                            )
                        }
                    }
                }
                current = current.parent
            }

            // 2. File-level variables
            for (varDecl in HytaleUiPsiUtil.findVariableDeclarations(file)) {
                val name = HytaleUiPsiUtil.getVariableName(varDecl) ?: continue
                if (seen.add(name)) {
                    prefixedResult.addElement(
                        PrioritizedLookupElement.withPriority(
                            LookupElementBuilder.create("@$name")
                                .withTypeText("variable")
                                .withIcon(AllIcons.Nodes.Variable)
                                .withBoldness(true),
                            25.0
                        )
                    )
                }
            }

            // 3. Variables from imported files
            for (importElement in HytaleUiPsiUtil.findFileImports(file)) {
                val path = HytaleUiPsiUtil.getImportPath(importElement) ?: continue
                val alias = HytaleUiPsiUtil.getImportAlias(importElement) ?: continue
                val targetFile = HytaleUiFileResolver.resolveImportPath(file, path) ?: continue
                val psiFile = com.intellij.psi.PsiManager.getInstance(file.project)
                    .findFile(targetFile) as? HytaleUiFile ?: continue

                for (varDecl in HytaleUiPsiUtil.findVariableDeclarations(psiFile)) {
                    val varName = HytaleUiPsiUtil.getVariableName(varDecl) ?: continue
                    if (seen.add(varName)) {
                        prefixedResult.addElement(
                            PrioritizedLookupElement.withPriority(
                                LookupElementBuilder.create("@$varName")
                                    .withTypeText("from \$$alias")
                                    .withIcon(AllIcons.Nodes.Variable),
                                20.0
                            )
                        )
                    }
                }
            }
        }
    }

    private class FileImportCompletionProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(
            parameters: CompletionParameters,
            context: ProcessingContext,
            result: CompletionResultSet
        ) {
            val position = parameters.position
            val file = position.containingFile as? HytaleUiFile ?: return

            for (importElement in HytaleUiPsiUtil.findFileImports(file)) {
                val alias = HytaleUiPsiUtil.getImportAlias(importElement) ?: continue
                result.addElement(
                    PrioritizedLookupElement.withPriority(
                        LookupElementBuilder.create("\$$alias")
                            .withTypeText("file import")
                            .withIcon(AllIcons.Nodes.Include),
                        25.0
                    )
                )
            }
        }
    }
}
