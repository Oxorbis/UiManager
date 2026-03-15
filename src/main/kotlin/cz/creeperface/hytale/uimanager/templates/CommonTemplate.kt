package cz.creeperface.hytale.uimanager.templates

import cz.creeperface.hytale.uimanager.ChildNodeBuilder
import cz.creeperface.hytale.uimanager.Color
import cz.creeperface.hytale.uimanager.UiNodeWithChildren
import cz.creeperface.hytale.uimanager.UiPage
import cz.creeperface.hytale.uimanager.builder.*
import cz.creeperface.hytale.uimanager.enum.*
import cz.creeperface.hytale.uimanager.node.*
import cz.creeperface.hytale.uimanager.type.*

object CommonTemplate {
    
    const val UI_ROOT = "../../"

    fun ChildNodeBuilder.panel(`init`: UiGroup.() -> Unit = {}) = group {
        background = patchStyle {
            texturePath = UI_ROOT + "Common/ContainerFullPatch.png"
            border = 20
        }
        init()
    }

    fun ChildNodeBuilder.titleLabel(`init`: UiLabel.() -> Unit = {}) = label {
        style = labelStyle {
            fontSize = 40.0
            alignment = LabelAlignment.Center
        }
        init()
    }

    val defaultLabelStyle = labelStyle {
        fontSize = 16.0
        textColor = Color("#96a9be")
    }

    val primaryButtonHeight = 44
    val smallButtonHeight = 32
    val bigButtonHeight = 48
    val buttonPadding = 24
    val defaultButtonMinWidth = 172

    val defaultButtonHeight = primaryButtonHeight
    val defaultButtonPadding = buttonPadding

    // Buttons
    val buttonSounds = SoundsTemplate.buttonsLight
    val buttonsCancel = SoundsTemplate.buttonsCancel
    val buttonDestructiveSounds = SoundsTemplate.buttonsLight // Destructive sounds not available

    val buttonBorder = 12
    val defaultButtonDefaultBackground = patchStyle {
        texturePath = UI_ROOT + "Common/Buttons/Primary.png"
        verticalBorder = buttonBorder
        horizontalBorder = 80
    }

    val defaultButtonHoveredBackground = patchStyle {
        texturePath = UI_ROOT + "Common/Buttons/Primary_Hovered.png"
        verticalBorder = buttonBorder
        horizontalBorder = 80
    }

    val defaultButtonPressedBackground = patchStyle {
        texturePath = UI_ROOT + "Common/Buttons/Primary_Pressed.png"
        verticalBorder = buttonBorder
        horizontalBorder = 80
    }

    val defaultButtonDisabledBackground = patchStyle {
        texturePath = UI_ROOT + "Common/Buttons/Disabled.png"
        verticalBorder = buttonBorder
        horizontalBorder = 80
    }

    val defaultSquareButtonDefaultBackground = patchStyle {
        texturePath = UI_ROOT + "Common/Buttons/Primary_Square.png"
        border = buttonBorder
    }

    val defaultSquareButtonHoveredBackground = patchStyle {
        texturePath = UI_ROOT + "Common/Buttons/Primary_Square_Hovered.png"
        border = buttonBorder
    }

    val defaultSquareButtonPressedBackground = patchStyle {
        texturePath = UI_ROOT + "Common/Buttons/Primary_Square_Pressed.png"
        border = buttonBorder
    }

    val defaultSquareButtonDisabledBackground = patchStyle {
        texturePath = UI_ROOT + "Common/Buttons/Disabled.png"
        border = buttonBorder
    }

    val disabledColor = Color("#797b7c")

    val defaultButtonLabelStyle = labelStyle {
        fontSize = 17.0
        textColor = Color("#bfcdd5")
        renderBold = true
        renderUppercase = true
        horizontalAlignment = LabelAlignment.Center
        verticalAlignment = LabelAlignment.Center
    }

    val defaultButtonDisabledLabelStyle = defaultButtonLabelStyle.copy(
        textColor = disabledColor
    )

    val primaryButtonLabelStyle = defaultButtonLabelStyle
    val primaryButtonDisabledLabelStyle = defaultButtonDisabledLabelStyle

    val secondaryButtonLabelStyle = defaultButtonLabelStyle.copy(
        textColor = Color("#bdcbd3")
    )

    val secondaryButtonDisabledLabelStyle = secondaryButtonLabelStyle.copy(
        textColor = disabledColor
    )

    val smallButtonLabelStyle = defaultButtonLabelStyle.copy(
        fontSize = 14.0
    )

    val smallButtonDisabledLabelStyle = smallButtonLabelStyle.copy(
        textColor = disabledColor
    )

    val smallSecondaryButtonLabelStyle = secondaryButtonLabelStyle.copy(
        fontSize = 14.0
    )

    val smallSecondaryButtonDisabledLabelStyle = smallSecondaryButtonLabelStyle.copy(
        textColor = disabledColor
    )

    val defaultButtonStyle = buttonStyle {
        default = buttonStyleState { background = defaultButtonDefaultBackground }
        hovered = buttonStyleState { background = defaultButtonHoveredBackground }
        pressed = buttonStyleState { background = defaultButtonPressedBackground }
        disabled = buttonStyleState { background = defaultButtonDisabledBackground }
        sounds = SoundsTemplate.buttonsLight
    }

    val defaultTextButtonStyle = textButtonStyle {
        default = textButtonStyleState {
            background = defaultButtonDefaultBackground
            labelStyle = defaultButtonLabelStyle
        }
        hovered = textButtonStyleState {
            background = defaultButtonHoveredBackground
            labelStyle = defaultButtonLabelStyle
        }
        pressed = textButtonStyleState {
            background = defaultButtonPressedBackground
            labelStyle = defaultButtonLabelStyle
        }
        disabled = textButtonStyleState {
            background = defaultButtonDisabledBackground
            labelStyle = defaultButtonDisabledLabelStyle
        }
        sounds = SoundsTemplate.buttonsLight
    }

    val smallDefaultTextButtonStyle = textButtonStyle {
        default = textButtonStyleState {
            background = patchStyle { texturePath = UI_ROOT + "Common/ButtonSmall.png"; border = 6 }
            labelStyle = smallButtonLabelStyle
        }
        hovered = textButtonStyleState {
            background = patchStyle { texturePath = UI_ROOT + "Common/ButtonSmallHovered.png"; border = 6 }
            labelStyle = smallButtonLabelStyle
        }
        pressed = textButtonStyleState {
            background = patchStyle { texturePath = UI_ROOT + "Common/ButtonSmallPressed.png"; border = 6 }
            labelStyle = smallButtonLabelStyle
        }
        disabled = textButtonStyleState {
            background = defaultButtonDisabledBackground
            labelStyle = smallButtonDisabledLabelStyle
        }
        sounds = SoundsTemplate.buttonsLight
    }

    val cancelTextButtonStyle = textButtonStyle {
        default = textButtonStyleState {
            background = patchStyle { texturePath = UI_ROOT + "Common/Buttons/Destructive.png"; border = buttonBorder }
            labelStyle = defaultButtonLabelStyle
        }
        hovered = textButtonStyleState {
            background = patchStyle { texturePath = UI_ROOT + "Common/Buttons/Destructive_Hovered.png"; border = buttonBorder }
            labelStyle = defaultButtonLabelStyle
        }
        pressed = textButtonStyleState {
            background = patchStyle { texturePath = UI_ROOT + "Common/Buttons/Destructive_Pressed.png"; border = buttonBorder }
            labelStyle = defaultButtonLabelStyle
        }
        disabled = textButtonStyleState {
            background = patchStyle { texturePath = UI_ROOT + "Common/Buttons/Disabled.png"; border = buttonBorder }
            labelStyle = defaultButtonLabelStyle
        }
        sounds = SoundsTemplate.buttonsCancel
    }

    val cancelButtonStyle = buttonStyle {
        default = buttonStyleState { background = patchStyle { texturePath = UI_ROOT + "Common/Buttons/Destructive.png"; border = buttonBorder } }
        hovered = buttonStyleState { background = patchStyle { texturePath = UI_ROOT + "Common/Buttons/Destructive_Hovered.png"; border = buttonBorder } }
        pressed = buttonStyleState { background = patchStyle { texturePath = UI_ROOT + "Common/Buttons/Destructive_Pressed.png"; border = buttonBorder } }
        disabled = buttonStyleState { background = patchStyle { texturePath = UI_ROOT + "Common/Buttons/Disabled.png"; border = buttonBorder } }
        sounds = SoundsTemplate.buttonsLight
    }

    val secondaryButtonStyle = buttonStyle {
        default = buttonStyleState { background = patchStyle { texturePath = UI_ROOT + "Common/Buttons/Secondary.png"; border = buttonBorder } }
        hovered = buttonStyleState { background = patchStyle { texturePath = UI_ROOT + "Common/Buttons/Secondary_Hovered.png"; border = buttonBorder } }
        pressed = buttonStyleState { background = patchStyle { texturePath = UI_ROOT + "Common/Buttons/Secondary_Pressed.png"; border = buttonBorder } }
        disabled = buttonStyleState { background = patchStyle { texturePath = UI_ROOT + "Common/Buttons/Disabled.png"; border = buttonBorder } }
        sounds = SoundsTemplate.buttonsLight
    }

    val secondaryTextButtonStyle = textButtonStyle {
        default = textButtonStyleState {
            background = patchStyle { texturePath = UI_ROOT + "Common/Buttons/Secondary.png"; border = buttonBorder }
            labelStyle = secondaryButtonLabelStyle
        }
        hovered = textButtonStyleState {
            background = patchStyle { texturePath = UI_ROOT + "Common/Buttons/Secondary_Hovered.png"; border = buttonBorder }
            labelStyle = secondaryButtonLabelStyle
        }
        pressed = textButtonStyleState {
            background = patchStyle { texturePath = UI_ROOT + "Common/Buttons/Secondary_Pressed.png"; border = buttonBorder }
            labelStyle = secondaryButtonLabelStyle
        }
        disabled = textButtonStyleState {
            background = patchStyle { texturePath = UI_ROOT + "Common/Buttons/Disabled.png"; border = buttonBorder }
            labelStyle = secondaryButtonLabelStyle
        }
    }

    val tertiaryButtonStyle = buttonStyle {
        default = buttonStyleState { background = patchStyle { texturePath = UI_ROOT + "Common/Buttons/Tertiary.png"; border = buttonBorder } }
        hovered = buttonStyleState { background = patchStyle { texturePath = UI_ROOT + "Common/Buttons/Tertiary_Hovered.png"; border = buttonBorder } }
        pressed = buttonStyleState { background = patchStyle { texturePath = UI_ROOT + "Common/Buttons/Tertiary_Pressed.png"; border = buttonBorder } }
        disabled = buttonStyleState { background = patchStyle { texturePath = UI_ROOT + "Common/Buttons/Disabled.png"; border = buttonBorder } }
        sounds = SoundsTemplate.buttonsLight
    }

    val smallSecondaryTextButtonStyle = textButtonStyle {
        default = textButtonStyleState {
            background = patchStyle { texturePath = UI_ROOT + "Common/Buttons/Secondary.png"; border = buttonBorder }
            labelStyle = smallSecondaryButtonLabelStyle
        }
        hovered = textButtonStyleState {
            background = patchStyle { texturePath = UI_ROOT + "Common/Buttons/Secondary_Hovered.png"; border = buttonBorder }
            labelStyle = smallSecondaryButtonLabelStyle
        }
        pressed = textButtonStyleState {
            background = patchStyle { texturePath = UI_ROOT + "Common/Buttons/Secondary_Pressed.png"; border = buttonBorder }
            labelStyle = smallSecondaryButtonLabelStyle
        }
        disabled = textButtonStyleState {
            background = patchStyle { texturePath = UI_ROOT + "Common/Buttons/Disabled.png"; border = buttonBorder }
            labelStyle = smallSecondaryButtonLabelStyle
        }
    }

    val tertiaryTextButtonStyle = textButtonStyle {
        default = textButtonStyleState {
            background = patchStyle { texturePath = UI_ROOT + "Common/Buttons/Tertiary.png"; border = buttonBorder }
            labelStyle = secondaryButtonLabelStyle
        }
        hovered = textButtonStyleState {
            background = patchStyle { texturePath = UI_ROOT + "Common/Buttons/Tertiary_Hovered.png"; border = buttonBorder }
            labelStyle = secondaryButtonLabelStyle
        }
        pressed = textButtonStyleState {
            background = patchStyle { texturePath = UI_ROOT + "Common/Buttons/Tertiary_Pressed.png"; border = buttonBorder }
            labelStyle = secondaryButtonLabelStyle
        }
        disabled = textButtonStyleState {
            background = patchStyle { texturePath = UI_ROOT + "Common/Buttons/Disabled.png"; border = buttonBorder }
            labelStyle = secondaryButtonLabelStyle
        }
    }

    val defaultColorPickerStyle = colorPickerStyle {
        opacitySelectorBackground = patchStyle { texturePath = UI_ROOT + "Common/ColorPickerOpacitySelectorBackground.png" }
        buttonBackground = patchStyle { texturePath = UI_ROOT + "Common/ColorPickerButton.png" }
        buttonFill = patchStyle { texturePath = UI_ROOT + "Common/ColorPickerFill.png" }
        textFieldDecoration = inputFieldDecorationStyle {
            default = inputFieldDecorationStyleState {
                background = patchStyle { color = Color("#000000", 0.5) }
            }
        }
        textFieldPadding = padding { left = 7 }
        textFieldHeight = 32
    }

    val defaultColorPickerDropdownBoxStyle = colorPickerDropdownBoxStyle {
        colorPickerStyle = defaultColorPickerStyle
        background = colorPickerDropdownBoxStateBackground {
            default = patchStyle { texturePath = UI_ROOT + "Common/ColorPickerDropdownBoxBackground.png" }
        }
        arrowBackground = colorPickerDropdownBoxStateBackground {
            default = patchStyle { texturePath = UI_ROOT + "Common/ColorPickerDropdownBoxArrow.png" }
        }
        overlay = colorPickerDropdownBoxStateBackground {
            default = patchStyle { texturePath = UI_ROOT + "Common/ColorPickerDropdownBoxOverlay.png" }
        }
        panelBackground = patchStyle { texturePath = UI_ROOT + "Common/DropdownBox.png"; border = 16 }
        panelPadding = padding { full = 15 }
        panelOffset = 10
        arrowAnchor = anchor { width = 11; height = 7; right = 3; bottom = 3 }
    }

    val defaultScrollbarStyle = scrollbarStyle {
        spacing = 6
        size = 6
        background = patchStyle { texturePath = UI_ROOT + "Common/Scrollbar.png"; border = 3 }
        handle = patchStyle { texturePath = UI_ROOT + "Common/ScrollbarHandle.png"; border = 3 }
        hoveredHandle = patchStyle { texturePath = UI_ROOT + "Common/ScrollbarHandleHovered.png"; border = 3 }
        draggedHandle = patchStyle { texturePath = UI_ROOT + "Common/ScrollbarHandleDragged.png"; border = 3 }
    }

    val defaultCheckBoxStyle = checkBoxStyle {
        unchecked = checkBoxStyleState {
            defaultBackground = patchStyle { color = Color("#00000000") }
            hoveredBackground = patchStyle { color = Color("#00000000") }
            pressedBackground = patchStyle { color = Color("#00000000") }
            disabledBackground = patchStyle { color = Color("#424242") }
            changedSound = soundStyle {
                soundPath = SoundsTemplate.untick
                volume = 6.0
            }
        }
        checked = checkBoxStyleState {
            defaultBackground = patchStyle { texturePath = UI_ROOT + "Common/Checkmark.png" }
            hoveredBackground = patchStyle { texturePath = UI_ROOT + "Common/Checkmark.png" }
            pressedBackground = patchStyle { texturePath = UI_ROOT + "Common/Checkmark.png" }
            changedSound = soundStyle {
                soundPath = SoundsTemplate.tick
                volume = 6.0
            }
        }
    }

    val defaultInputFieldStyle = inputFieldStyle {
    }

    val defaultDropdownBoxStyle = dropdownBoxStyle {
        defaultBackground = patchStyle { texturePath = UI_ROOT + "Common/Dropdown.png"; border = 16 }
        hoveredBackground = patchStyle { texturePath = UI_ROOT + "Common/DropdownHovered.png"; border = 16 }
        pressedBackground = patchStyle { texturePath = UI_ROOT + "Common/DropdownPressed.png"; border = 16 }
        defaultArrowTexturePath = UI_ROOT + "Common/DropdownCaret.png"
        hoveredArrowTexturePath = UI_ROOT + "Common/DropdownCaret.png"
        pressedArrowTexturePath = UI_ROOT + "Common/DropdownPressedCaret.png"
        arrowWidth = 13
        arrowHeight = 18
        labelStyle = labelStyle {
            textColor = Color("#96a9be")
            renderUppercase = true
            verticalAlignment = LabelAlignment.Center
            fontSize = 13.0
        }
        entryLabelStyle = labelStyle {
            textColor = Color("#b7cedd")
            renderUppercase = true
            verticalAlignment = LabelAlignment.Center
            fontSize = 13.0
        }
        noItemsLabelStyle = labelStyle {
            textColor = Color("#b7cedd", 0.5)
            renderUppercase = true
            verticalAlignment = LabelAlignment.Center
            fontSize = 13.0
        }
        selectedEntryLabelStyle = labelStyle {
            textColor = Color("#b7cedd")
            renderBold = true
            renderUppercase = true
            verticalAlignment = LabelAlignment.Center
            fontSize = 13.0
        }
        horizontalPadding = 8
        panelScrollbarStyle = defaultScrollbarStyle
        panelBackground = patchStyle { texturePath = UI_ROOT + "Common/DropdownBox.png"; border = 16 }
        panelPadding = 6
        panelAlign = DropdownBoxAlign.Right
        panelOffset = 7
        entryHeight = 31
        entriesInViewport = 10
        horizontalEntryPadding = 7
        hoveredEntryBackground = patchStyle { color = Color("#0a0f17") }
        pressedEntryBackground = patchStyle { color = Color("#0f1621") }
        sounds = SoundsTemplate.dropdownBox
        entrySounds = SoundsTemplate.buttonsLight
        focusOutlineSize = 1
        focusOutlineColor = Color("#ffffff", 0.4)
    }

//    val defaultPopupMenuLayerStyle = popupMenuLayerStyle {
//        background = patchStyle { texturePath = UI_ROOT + "Common/Popup.png"; border = 16 }
//        padding = 2
//        baseHeight = 5
//        maxWidth = 200
//        titleStyle = labelStyle {
//            renderBold = true
//            renderUppercase = true
//            fontSize = 13
//            textColor = Color("#ccb588")
//        }
//        titleBackground = patchStyle { texturePath = UI_ROOT + "Common/PopupTitle.png" }
//        rowHeight = 25
//        itemLabelStyle = labelStyle {
//            renderBold = true
//            renderUppercase = true
//            fontSize = 11
//            textColor = Color("#96a9be", 0.8)
//        }
//        itemPadding = popupMenuLayerStyle_ItemPadding {
//            vertical = 5
//            horizontal = 8
//        }
//        itemBackground = patchStyle { texturePath = UI_ROOT + "Common/PopupItem.png" }
//        itemIconSize = 16
//        hoveredItemBackground = patchStyle { texturePath = UI_ROOT + "Common/HoveredPopupItem.png" }
//        pressedItemBackground = patchStyle { texturePath = UI_ROOT + "Common/PressedPopupItem.png" }
//        itemSounds = popupMenuLayerStyle_ItemSounds {
//            activate = SoundsTemplate.buttonsLight.activate
//            mouseHover = SoundsTemplate.buttonsLight.mouseHover
//        }
//    }

    val defaultSliderStyle = sliderStyle {
        background = patchStyle { texturePath = UI_ROOT + "Common/SliderBackground.png"; border = 2 }
        handle = patchStyle { texturePath = UI_ROOT + "Common/SliderHandle.png" }
        handleWidth = 16
        handleHeight = 16
        sounds = buttonSounds {
            mouseHover = soundStyle {
                soundPath = SoundsTemplate.buttonsLightHover
                volume = 6.0
            }
        }
    }

    val defaultTextTooltipStyle = textTooltipStyle {
        background = patchStyle { texturePath = UI_ROOT + "Common/TooltipDefaultBackground.png"; border = 24 }
        maxWidth = 400
        labelStyle = labelStyle { wrap = true; fontSize = 16.0 }
        padding = padding {
            horizontal = 24
            vertical = 24
        }
    }

    val topTabAnchor = anchor { width = 82; height = 62; right = 5; bottom = -14 }

    val topTabStyle = tabStyleState {
        background = patchStyle { texturePath = UI_ROOT + "Common/Tab.png"}
        overlay = patchStyle { texturePath = UI_ROOT + "Common/TabOverlay.png"}
        iconAnchor = anchor { width = 44; height = 44 }
        anchor = topTabAnchor
    }

    val topTabsStyle = tabNavigationStyle {
        tabStyle = tabStyle {
            default = topTabStyle
            hovered = topTabStyle.copy(anchor = topTabAnchor.copy(bottom = -5))
            pressed = topTabStyle.copy(anchor = topTabAnchor.copy(bottom = -8))
        }
        selectedTabStyle = tabStyle {
            default = topTabStyle.copy(
                anchor = topTabAnchor.copy(bottom = 4),
                iconAnchor = anchor { width = 44; height = 44 },
                overlay = patchStyle { texturePath = UI_ROOT + "Common/TabSelectedOverlay.png" }
            )
        }
    }

    val headerTabStyle = tabStyleState {
        anchor = anchor { width = 34; height = 34 }
        iconOpacity = 0.25
    }

    val headerTabsStyle = tabNavigationStyle {
        tabStyle = tabStyle {
            default = headerTabStyle
        }
        selectedTabStyle = tabStyle {
            default = headerTabStyle.copy(iconOpacity = 1.0)
        }
        separatorAnchor = anchor { width = 5; height = 34 }
        separatorBackground = patchStyle {  texturePath = UI_ROOT + "Common/HeaderTabSeparator.png" }
    }

    fun ChildNodeBuilder.pageOverlay(`init`: UiGroup.() -> Unit = {}) = group {
        background = patchStyle { color = Color("#000000", 0.45) }
        init()
    }

    fun ChildNodeBuilder.closeButton(`init`: UiButton.() -> Unit = {}) = button {
        anchor = anchor { top = -16; right = -16; width = 32; height = 32 }
        style = buttonStyle {
            default = buttonStyleState { background = patchStyle { texturePath = UI_ROOT + "Common/ContainerCloseButton.png" } }
            hovered = buttonStyleState { background = patchStyle { texturePath = UI_ROOT + "Common/ContainerCloseButtonHovered.png" } }
            pressed = buttonStyleState { background = patchStyle { texturePath = UI_ROOT + "Common/ContainerCloseButtonPressed.png" } }
            sounds = SoundsTemplate.buttonsCancel
        }
        init()
    }

    val defaultExtraSpacingScrollbarStyle = defaultScrollbarStyle.copy(spacing = 12)

    val translucentScrollbarStyle = scrollbarStyle {
        spacing = 6
        size = 6
        onlyVisibleWhenHovered = true
        handle = patchStyle { texturePath = UI_ROOT + "Common/ScrollbarHandle.png"; border = 3 }
    }

    val defaultPlaceholderScrollbarStyle = scrollbarStyle {
        spacing = 12
        size = 10
    }

    val defaultSquareButtonStyle = buttonStyle {
        default = buttonStyleState { background = defaultSquareButtonDefaultBackground }
        hovered = buttonStyleState { background = defaultSquareButtonHoveredBackground }
        pressed = buttonStyleState { background = defaultSquareButtonPressedBackground }
        disabled = buttonStyleState { background = defaultSquareButtonDisabledBackground }
        sounds = SoundsTemplate.buttonsLight
    }

    fun ChildNodeBuilder.defaultTextButton(`init`: UiTextButton.() -> Unit = {}): UiTextButton = this.textButton {
        style = defaultTextButtonStyle
        anchor = anchor { height = defaultButtonHeight }
        padding = padding { horizontal = defaultButtonPadding }
        init()
    }

    fun ChildNodeBuilder.defaultButton(`init`: UiButton.() -> Unit = {}): UiButton = this.button {
        style = defaultSquareButtonStyle
        anchor = anchor { height = defaultButtonHeight; width = defaultButtonHeight }
        padding = padding { horizontal = defaultButtonPadding }
        init()
    }

    fun ChildNodeBuilder.defaultCancelTextButton(`init`: UiTextButton.() -> Unit = {}): UiTextButton = this.textButton {
        style = cancelTextButtonStyle
        anchor = anchor { height = defaultButtonHeight }
        padding = padding { horizontal = defaultButtonPadding }
        init()
    }

    fun ChildNodeBuilder.defaultCancelButton(`init`: UiButton.() -> Unit = {}): UiButton = this.button {
        style = cancelButtonStyle
        anchor = anchor { height = defaultButtonHeight; width = defaultButtonHeight }
        init()
    }

    fun ChildNodeBuilder.defaultSmallSecondaryTextButton(`init`: UiTextButton.() -> Unit = {}): UiTextButton = this.textButton {
        style = smallSecondaryTextButtonStyle
        anchor = anchor { height = smallButtonHeight }
        padding = padding { horizontal = 16 }
        init()
    }

    fun ChildNodeBuilder.defaultSmallTertiaryTextButton(`init`: UiTextButton.() -> Unit = {}): UiTextButton = this.textButton {
        style = tertiaryTextButtonStyle
        anchor = anchor { height = smallButtonHeight }
        padding = padding { horizontal = 16 }
        init()
    }

    fun ChildNodeBuilder.defaultSecondaryTextButton(`init`: UiTextButton.() -> Unit = {}): UiTextButton = this.textButton {
        style = secondaryTextButtonStyle
        anchor = anchor { height = defaultButtonHeight }
        padding = padding { horizontal = defaultButtonPadding }
        init()
    }

    fun ChildNodeBuilder.defaultSecondaryButton(`init`: UiButton.() -> Unit = {}): UiButton = this.button {
        style = secondaryButtonStyle
        anchor = anchor { height = defaultButtonHeight; width = defaultButtonHeight }
        init()
    }

    fun ChildNodeBuilder.defaultTertiaryTextButton(`init`: UiTextButton.() -> Unit = {}): UiTextButton = this.textButton {
        style = tertiaryTextButtonStyle
        anchor = anchor { height = defaultButtonHeight }
        padding = padding { horizontal = defaultButtonPadding }
        init()
    }

    fun ChildNodeBuilder.defaultTertiaryButton(`init`: UiButton.() -> Unit = {}): UiButton = this.button {
        style = tertiaryButtonStyle
        anchor = anchor { height = defaultButtonHeight; width = defaultButtonHeight }
        init()
    }

    fun ChildNodeBuilder.secondaryTextButton(`init`: UiTextButton.() -> Unit = {}) = this.textButton {
        style = secondaryTextButtonStyle
        anchor = anchor { height = defaultButtonHeight }
        padding = padding { horizontal = defaultButtonPadding }
        init()
    }

    fun ChildNodeBuilder.secondaryButton(`init`: UiButton.() -> Unit = {}) = this.button {
        style = secondaryButtonStyle
        anchor = anchor { height = defaultButtonHeight; width = defaultButtonHeight }
        init()
    }

    fun ChildNodeBuilder.tertiaryTextButton(`init`: UiTextButton.() -> Unit = {}) = this.textButton {
        style = tertiaryTextButtonStyle
        anchor = anchor { height = defaultButtonHeight }
        padding = padding { horizontal = defaultButtonPadding }
        init()
    }

    fun ChildNodeBuilder.tertiaryButton(`init`: UiButton.() -> Unit = {}) = this.button {
        style = tertiaryButtonStyle
        anchor = anchor { height = defaultButtonHeight; width = defaultButtonHeight }
        init()
    }

    fun ChildNodeBuilder.defaultCheckBox(`init`: UiCheckBox.() -> Unit = {}): UiCheckBox = checkBox {
        anchor = anchor { width = 22; height = 22 }
        background = patchStyle { texturePath = UI_ROOT + "Common/CheckBoxFrame.png"; border = 7 }
        padding = padding { full = 4 }
        style = defaultCheckBoxStyle
        init()
    }

    val defaultInputFieldPlaceholderStyle = inputFieldStyle { textColor = Color("#6e7da1") }

    fun ChildNodeBuilder.defaultTextField(`init`: UiTextField.() -> Unit = {}): UiTextField = textField {
        style = defaultInputFieldStyle
        placeholderStyle = defaultInputFieldPlaceholderStyle
        background = patchStyle { texturePath = UI_ROOT + "Common/InputBox.png"; border = 16 }
        anchor = anchor { height = 38 }
        padding = padding { horizontal = 10 }
        init()
    }

    fun ChildNodeBuilder.defaultNumberField(`init`: UiNumberField.() -> Unit = {}): UiNumberField = numberField {
        style = defaultInputFieldStyle
        placeholderStyle = defaultInputFieldPlaceholderStyle
        background = patchStyle { texturePath = UI_ROOT + "Common/InputBox.png"; border = 16 }
        anchor = anchor { height = 38 }
        padding = padding { horizontal = 10 }
        init()
    }

    val defaultDropdownBoxLabelStyle = labelStyle {
        textColor = Color("#96a9be")
        renderUppercase = true
        verticalAlignment = LabelAlignment.Center
        fontSize = 13.0
    }

    val defaultDropdownBoxEntryLabelStyle = defaultDropdownBoxLabelStyle.copy(textColor = Color("#b7cedd"))

    val dropdownBoxHeight = 32

    fun ChildNodeBuilder.defaultDropdownBox(`init`: UiDropdownBox.() -> Unit = {}): UiDropdownBox = dropdownBox {
        anchor = anchor { width = 330; height = dropdownBoxHeight }
        style = defaultDropdownBoxStyle
        init()
    }

    val defaultFileDropdownBoxStyle = dropdownBoxStyle {
        defaultBackground = patchStyle { texturePath = UI_ROOT + "Common/Dropdown.png"; border = 16 }
        hoveredBackground = patchStyle { texturePath = UI_ROOT + "Common/DropdownHovered.png"; border = 16 }
        pressedBackground = patchStyle { texturePath = UI_ROOT + "Common/DropdownPressed.png"; border = 16 }
        defaultArrowTexturePath = UI_ROOT + "Common/DropdownCaret.png"
        hoveredArrowTexturePath = UI_ROOT + "Common/DropdownCaret.png"
        pressedArrowTexturePath = UI_ROOT + "Common/DropdownPressedCaret.png"
        arrowWidth = 9
        arrowHeight = 18
        labelStyle = labelStyle {
            textColor = Color("#96a9be")
            renderBold = true
            verticalAlignment = LabelAlignment.Center
            fontSize = 18.0
        }
        horizontalPadding = 14
        panelAlign = DropdownBoxAlign.Bottom
        panelOffset = 7
        sounds = SoundsTemplate.dropdownBox
    }

    val popupTitleStyle = labelStyle {
        fontSize = 38.0
        letterSpacing = 2.0
        renderUppercase = true
        renderBold = true
        horizontalAlignment = LabelAlignment.Center
        verticalAlignment = LabelAlignment.Center
    }

    fun ChildNodeBuilder.contentSeparator(`init`: UiGroup.() -> Unit = {}) = group {
        anchor = anchor { height = 1 }
        background = patchStyle { color = Color("#2b3542") }
        init()
    }

    fun ChildNodeBuilder.spinner(`init`: UiSprite.() -> Unit = {}) = sprite {
        anchor = anchor { width = 32; height = 32 }
        texturePath = UI_ROOT + "Common/Spinner.png"
        frame = spriteFrame {
            width = 32
            height = 32
            perRow = 8
            count = 72
        }
        framesPerSecond = 30
        init()
    }

    fun ChildNodeBuilder.actionButtonContainer(`init`: UiGroup.() -> Unit = {}) = group {
        layoutMode = LayoutMode.Right
        anchor = anchor { right = 50; bottom = 50; height = 27 }
        init()
    }

    fun ChildNodeBuilder.actionButtonSeparator(`init`: UiGroup.() -> Unit = {}) = group {
        anchor = anchor { width = 35 }
        init()
    }

    fun ChildNodeBuilder.verticalActionButtonSeparator(`init`: UiGroup.() -> Unit = {}) = group {
        anchor = anchor { height = 20 }
        init()
    }

    val subtitleStyle = labelStyle {
        fontSize = 15.0
        renderUppercase = true
        textColor = Color("#96a9be")
    }

    fun ChildNodeBuilder.subtitle(`init`: UiLabel.() -> Unit = {}) = label {
        style = subtitleStyle
        anchor = anchor { bottom = 10 }
        init()
    }

    val titleStyle = labelStyle {
        fontSize = 15.0
        verticalAlignment = LabelAlignment.Center
        renderUppercase = true
        textColor = Color("#b4c8c9")
//        fontName = "Secondary" TODO: rollback
        renderBold = true
        letterSpacing = 0.0
    }

    fun ChildNodeBuilder.defaultTitle(`init`: UiLabel.() -> Unit = {}) = label {
        style = titleStyle
        padding = padding { horizontal = 19 }
        init()
    }

    val titleHeight = 38
    val titleOffset = 4
    val contentPaddingFull = 9 + 8
    val contentPadding = padding { full = contentPaddingFull; top = 8 }

    val clearButtonStyle = inputFieldButtonStyle {
        texture = patchStyle { texturePath = UI_ROOT + "Common/ClearInputIcon.png"; color = Color("#ffffff", 0.3) }
        hoveredTexture = patchStyle { texturePath = UI_ROOT + "Common/ClearInputIcon.png"; color = Color("#ffffff", 0.5) }
        pressedTexture = patchStyle { texturePath = UI_ROOT + "Common/ClearInputIcon.png"; color = Color("#ffffff", 0.4) }
        width = 16
        height = 16
        side = InputFieldButtonSide.Right
        offset = 10
    }

    fun ChildNodeBuilder.headerSearch(`init`: UiCompactTextField.() -> Unit = {}) = group {
        anchor = anchor { width = 200; right = 0 }

        compactTextField {
            id = "SearchInput"
            anchor = anchor { height = 30; right = 10 }
            collapsedWidth = 34
            expandedWidth = 200
            placeholderText = "server.customUI.searchPlaceholder"
            style = inputFieldStyle { fontSize = 16.0 }
            placeholderStyle = inputFieldStyle { textColor = Color("#3d5a85"); renderUppercase = true; fontSize = 14.0 }
            padding = padding { horizontal = 12; left = 34 }
            decoration = inputFieldDecorationStyle {
                default = inputFieldDecorationStyleState {
                    icon = inputFieldIcon {
                        texture = patchStyle {
                            texturePath = UI_ROOT + "Common/SearchIcon.png"
                        }
                        width = 16;
                        height = 16;
                        offset = 9
                    }
                    clearButtonStyle = this@CommonTemplate.clearButtonStyle
                }
            }
            init()
        }
    }

    val headerTextButtonLabelStyle = titleStyle.copy(
        verticalAlignment = LabelAlignment.Center,
        textColor = Color("#d3d6db"),
        fontName = "Default",
        renderBold = true,
        letterSpacing = 1.0
    )

    val headerTextButtonStyle = textButtonStyle {
        default = textButtonStyleState { labelStyle = headerTextButtonLabelStyle }
        hovered = textButtonStyleState { labelStyle = headerTextButtonLabelStyle.copy(textColor = Color("#eaebee")) }
        pressed = textButtonStyleState { labelStyle = headerTextButtonLabelStyle.copy(textColor = Color("#b6bbc2")) }
    }

    fun ChildNodeBuilder.headerTextButton(`init`: UiTextButton.() -> Unit = {}) = textButton {
        style = headerTextButtonStyle
        padding = padding { right = 22; left = 15; bottom = 1 }
        init()
    }

    fun ChildNodeBuilder.headerSeparator(`init`: UiGroup.() -> Unit = {}) = group {
        anchor = anchor { width = 5; height = 34 }
        background = patchStyle { texturePath = UI_ROOT + "Common/HeaderTabSeparator.png" }
        init()
    }

    fun ChildNodeBuilder.panelTitle(`init`: UiLabel.() -> Unit = {}, alignment: LabelAlignment = LabelAlignment.Start) = group {
        layoutMode = LayoutMode.Top

        label {
            id = "PanelTitle"
            style = labelStyle { renderBold = true; verticalAlignment = LabelAlignment.Center; fontSize = 15.0; textColor = Color("#afc2c3"); horizontalAlignment = alignment }
            anchor = anchor { height = 35; horizontal = 8 }
            init()
        }

        group {
            background = patchStyle { color = Color("#393426", 0.5) }
            anchor = anchor { height = 1 }
        }
    }

    fun ChildNodeBuilder.verticalSeparator(`init`: UiGroup.() -> Unit = {}) = group {
        background = patchStyle { texturePath = UI_ROOT + "Common/ContainerVerticalSeparator.png" }
        anchor = anchor { width = 6; top = -2 }
        init()
    }

    fun ChildNodeBuilder.panelSeparatorFancy(`init`: UiGroup.() -> Unit = {}) = group {
        layoutMode = LayoutMode.Left
        anchor = anchor { height = 8 }

        group {
            flexWeight = 1
            background = patchStyle { texturePath = UI_ROOT + "Common/ContainerPanelSeparatorFancyLine.png" }
        }

        group {
            anchor = anchor { width = 11 }
            background = patchStyle { texturePath = UI_ROOT + "Common/ContainerPanelSeparatorFancyDecoration.png" }
        }

        group {
            flexWeight = 1
            background = patchStyle { texturePath = UI_ROOT + "Common/ContainerPanelSeparatorFancyLine.png" }
        }
        init()
    }

    val innerPaddingValue = 8
    val fullPaddingValue = innerPaddingValue + 9


    fun ChildNodeBuilder.defaultBackButton(`init`: UiGroup.() -> Unit) = group {
        layoutMode = LayoutMode.Left
        anchor = anchor { left = 50; bottom = 50; width = 110; height = 27 }

        backButton {

        }

        init()
    }
}