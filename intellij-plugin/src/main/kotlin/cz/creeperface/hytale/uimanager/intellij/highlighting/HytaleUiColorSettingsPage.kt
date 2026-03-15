package cz.creeperface.hytale.uimanager.intellij.highlighting

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import cz.creeperface.hytale.uimanager.intellij.HytaleUiIcons
import javax.swing.Icon

class HytaleUiColorSettingsPage : ColorSettingsPage {

    companion object {
        private val DESCRIPTORS = arrayOf(
            AttributesDescriptor("Comment", HytaleUiSyntaxHighlighter.COMMENT),
            AttributesDescriptor("String", HytaleUiSyntaxHighlighter.STRING),
            AttributesDescriptor("Number", HytaleUiSyntaxHighlighter.NUMBER),
            AttributesDescriptor("Boolean", HytaleUiSyntaxHighlighter.BOOLEAN),
            AttributesDescriptor("Variable (@name)", HytaleUiSyntaxHighlighter.VARIABLE),
            AttributesDescriptor("File import (\$name)", HytaleUiSyntaxHighlighter.FILE_IMPORT),
            AttributesDescriptor("Color literal", HytaleUiSyntaxHighlighter.COLOR),
            AttributesDescriptor("Server string", HytaleUiSyntaxHighlighter.SERVER_STRING),
            AttributesDescriptor("Node type", HytaleUiSyntaxHighlighter.NODE_TYPE),
            AttributesDescriptor("Type name", HytaleUiSyntaxHighlighter.TYPE_NAME),
            AttributesDescriptor("Enum value", HytaleUiSyntaxHighlighter.ENUM_VALUE),
            AttributesDescriptor("Property key", HytaleUiSyntaxHighlighter.PROPERTY_KEY),
            AttributesDescriptor("Node ID (#name)", HytaleUiSyntaxHighlighter.NODE_ID),
            AttributesDescriptor("Braces", HytaleUiSyntaxHighlighter.BRACES),
            AttributesDescriptor("Parentheses", HytaleUiSyntaxHighlighter.PARENS),
            AttributesDescriptor("Operator", HytaleUiSyntaxHighlighter.OPERATOR),
            AttributesDescriptor("Semicolon", HytaleUiSyntaxHighlighter.SEMICOLON),
            AttributesDescriptor("Comma", HytaleUiSyntaxHighlighter.COMMA),
        )
    }

    override fun getIcon(): Icon = HytaleUiIcons.FILE
    override fun getHighlighter(): SyntaxHighlighter = HytaleUiSyntaxHighlighter()
    override fun getDemoText(): String = """
        // Hytale UI file example
        ${'$'}C = "../Common.ui";

        @ButtonBorder = 12;
        @DefaultLabelStyle = (FontSize: 16, TextColor: #96a9be);

        @DefaultButtonLabelStyle = LabelStyle(
          FontSize: 17,
          TextColor: #bfcdd5,
          RenderBold: true,
          HorizontalAlignment: Center
        );

        @TextButton = TextButton {
          Style: (...@DefaultTextButtonStyle, Sounds: ${'$'}C.@ButtonSounds);
          Anchor: (Height: 44);
          Text: @Text;
        };

        ${'$'}C.@PageOverlay {
          LayoutMode: Middle;

          Group #Content {
            LayoutMode: Top;
            Background: #000000(0.45);

            Label #Title {
              Text: %server.customUI.title;
              Style: @DefaultLabelStyle;
            }

            ${'$'}C.@TextButton #SaveButton {
              @Text = %server.customUI.save;
            }
          }
        }
    """.trimIndent()

    override fun getAdditionalHighlightingTagToDescriptorMap(): Map<String, TextAttributesKey>? = null
    override fun getAttributeDescriptors(): Array<AttributesDescriptor> = DESCRIPTORS
    override fun getColorDescriptors(): Array<ColorDescriptor> = ColorDescriptor.EMPTY_ARRAY
    override fun getDisplayName(): String = "Hytale UI"
}
