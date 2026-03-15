package cz.creeperface.hytale.uimanager.intellij.reference

import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*
import com.intellij.psi.tree.TokenSet
import com.intellij.util.ProcessingContext
import cz.creeperface.hytale.uimanager.intellij.psi.HytaleUiElementTypes
import cz.creeperface.hytale.uimanager.intellij.psi.HytaleUiTokenTypes

class HytaleUiReferenceContributor : PsiReferenceContributor() {

    companion object {
        private val VARIABLE_REF_SET = TokenSet.create(HytaleUiElementTypes.VARIABLE_REF)
        private val FILE_VAR_REF_SET = TokenSet.create(HytaleUiElementTypes.FILE_VAR_REF)
        private val FILE_IMPORT_SET = TokenSet.create(HytaleUiElementTypes.FILE_IMPORT)
        private val TEMPLATE_INST_SET = TokenSet.create(HytaleUiElementTypes.TEMPLATE_INST)
    }

    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        // Local variable reference: VARIABLE_REF wraps @var in value position
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement().withElementType(VARIABLE_REF_SET),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(
                    element: PsiElement,
                    context: ProcessingContext
                ): Array<PsiReference> {
                    return arrayOf(HytaleUiVariableReference(element))
                }
            }
        )

        // Cross-file variable reference: FILE_VAR_REF wraps $C.@var
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement().withElementType(FILE_VAR_REF_SET),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(
                    element: PsiElement,
                    context: ProcessingContext
                ): Array<PsiReference> {
                    val dollarIdent = element.node.findChildByType(HytaleUiTokenTypes.DOLLAR_IDENTIFIER)
                    val alias = dollarIdent?.text?.removePrefix("$") ?: return PsiReference.EMPTY_ARRAY
                    return arrayOf(HytaleUiCrossFileVariableReference(element, alias))
                }
            }
        )

        // File import: FILE_IMPORT wraps `$C = "path.ui";`
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement().withElementType(FILE_IMPORT_SET),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(
                    element: PsiElement,
                    context: ProcessingContext
                ): Array<PsiReference> {
                    val stringLit = element.node.findChildByType(HytaleUiTokenTypes.STRING_LITERAL)
                        ?: return PsiReference.EMPTY_ARRAY
                    return arrayOf(HytaleUiFileImportReference(element, stringLit.psi))
                }
            }
        )

        // Template instantiation with @var or $C.@var
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement().withElementType(TEMPLATE_INST_SET),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(
                    element: PsiElement,
                    context: ProcessingContext
                ): Array<PsiReference> {
                    val dollarIdent = element.node.findChildByType(HytaleUiTokenTypes.DOLLAR_IDENTIFIER)
                    val atIdent = element.node.findChildByType(HytaleUiTokenTypes.AT_IDENTIFIER)

                    if (dollarIdent != null && atIdent != null) {
                        val alias = dollarIdent.text.removePrefix("$")
                        return arrayOf(HytaleUiCrossFileVariableReference(element, alias))
                    }

                    if (atIdent != null) {
                        return arrayOf(HytaleUiVariableReference(element))
                    }

                    return PsiReference.EMPTY_ARRAY
                }
            }
        )

        // Note: String path references (TexturePath, SoundPath, etc.) are handled by
        // HytaleUiStringValueElement.getReference() in the PSI layer, not via PsiReferenceContributor,
        // because PsiReferenceContributor patterns don't match leaf tokens in custom languages.
    }
}

