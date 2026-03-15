package cz.creeperface.hytale.uimanager.intellij.formatting

import com.intellij.application.options.CodeStyleAbstractConfigurable
import com.intellij.application.options.CodeStyleAbstractPanel
import com.intellij.application.options.TabbedLanguageCodeStylePanel
import com.intellij.psi.codeStyle.*
import cz.creeperface.hytale.uimanager.intellij.HytaleUiLanguage

class HytaleUiCodeStyleSettingsProvider : CodeStyleSettingsProvider() {

    override fun getLanguage() = HytaleUiLanguage.INSTANCE

    override fun createCustomSettings(settings: CodeStyleSettings): CustomCodeStyleSettings {
        return HytaleUiCodeStyleSettings(settings)
    }

    override fun getConfigurableDisplayName(): String = "Hytale UI"

    override fun createConfigurable(
        settings: CodeStyleSettings,
        modelSettings: CodeStyleSettings
    ): CodeStyleConfigurable {
        return object : CodeStyleAbstractConfigurable(settings, modelSettings, configurableDisplayName) {
            override fun createPanel(settings: CodeStyleSettings): CodeStyleAbstractPanel {
                return HytaleUiCodeStylePanel(currentSettings, settings)
            }
        }
    }

    private class HytaleUiCodeStylePanel(
        currentSettings: CodeStyleSettings,
        settings: CodeStyleSettings
    ) : TabbedLanguageCodeStylePanel(HytaleUiLanguage.INSTANCE, currentSettings, settings)
}

class HytaleUiLanguageCodeStyleSettingsProvider : LanguageCodeStyleSettingsProvider() {

    override fun getLanguage() = HytaleUiLanguage.INSTANCE

    override fun getCodeSample(settingsType: SettingsType): String = """
        ${'$'}Sounds = "Sounds.ui";

        @DefaultLabelStyle = (FontSize: 16, TextColor: #96a9be);

        @DefaultButtonStyle = ButtonStyle(
          Default: (Background: @DefaultButtonBackground),
          Hovered: (Background: @HoveredButtonBackground),
          Pressed: (Background: @PressedButtonBackground),
          Sounds: @ButtonSounds,
        );

        @TextButton = TextButton {
          @Anchor = Anchor();
          Style: (
            ...@DefaultTextButtonStyle,
            Sounds: (
              ...${'$'}Sounds.@ButtonsLight,
              ...@Sounds
            )
          );
          Anchor: (...@Anchor, Height: 44);
          Text: @Text;
        };

        Group #Content {
          LayoutMode: Top;
          Background: #000000(0.45);

          Label #Title {
            Text: %server.customUI.title;
            Style: @DefaultLabelStyle;
          }

          @TextButton #SaveButton {
            @Text = %server.customUI.save;
          }
        }
    """.trimIndent()

    override fun customizeSettings(consumer: CodeStyleSettingsCustomizable, settingsType: SettingsType) {
        if (settingsType == SettingsType.INDENT_SETTINGS) {
            consumer.showStandardOptions("INDENT_SIZE", "TAB_SIZE", "USE_TAB_CHARACTER")
        }
        if (settingsType == SettingsType.SPACING_SETTINGS) {
            consumer.showCustomOption(
                HytaleUiCodeStyleSettings::class.java,
                "SPACE_AFTER_COLON",
                "After colon",
                "Spaces"
            )
            consumer.showCustomOption(
                HytaleUiCodeStyleSettings::class.java,
                "SPACE_BEFORE_COLON",
                "Before colon",
                "Spaces"
            )
            consumer.showCustomOption(
                HytaleUiCodeStyleSettings::class.java,
                "SPACE_AFTER_COMMA",
                "After comma",
                "Spaces"
            )
            consumer.showCustomOption(
                HytaleUiCodeStyleSettings::class.java,
                "SPACE_WITHIN_PARENS",
                "Within parentheses",
                "Spaces"
            )
            consumer.showCustomOption(
                HytaleUiCodeStyleSettings::class.java,
                "SPACE_BEFORE_BRACE",
                "Before opening brace",
                "Spaces"
            )
        }
        if (settingsType == SettingsType.WRAPPING_AND_BRACES_SETTINGS) {
            consumer.showCustomOption(
                HytaleUiCodeStyleSettings::class.java,
                "OBJECT_PROPERTIES_ON_SEPARATE_LINES",
                "Object properties on separate lines",
                "Wrapping"
            )
        }
        if (settingsType == SettingsType.BLANK_LINES_SETTINGS) {
            consumer.showCustomOption(
                HytaleUiCodeStyleSettings::class.java,
                "BLANK_LINES_BETWEEN_TOP_LEVEL",
                "Between top-level statements",
                "Blank Lines"
            )
            consumer.showCustomOption(
                HytaleUiCodeStyleSettings::class.java,
                "BLANK_LINES_BEFORE_CHILD_NODE",
                "Before child nodes",
                "Blank Lines"
            )
        }
    }

    override fun getIndentOptionsEditor() = com.intellij.application.options.SmartIndentOptionsEditor()
}
