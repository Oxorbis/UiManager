package cz.creeperface.hytale

import aster.amo.kytale.extension.info
import aster.amo.kytale.extension.sendMessage
import com.hypixel.hytale.logger.HytaleLogger
import com.hypixel.hytale.server.core.Message
import cz.creeperface.hytale.uimanager.ChildNodeBuilder
import cz.creeperface.hytale.uimanager.Color
import cz.creeperface.hytale.uimanager.UiDsl
import cz.creeperface.hytale.uimanager.builder.*
import cz.creeperface.hytale.uimanager.enum.LabelAlignment
import cz.creeperface.hytale.uimanager.enum.LayoutMode
import cz.creeperface.hytale.uimanager.model.BaseObservable
import cz.creeperface.hytale.uimanager.node.event.onActivate
import cz.creeperface.hytale.uimanager.special.*
import cz.creeperface.hytale.uimanager.templates.CommonTemplate
import cz.creeperface.hytale.uimanager.templates.CommonTemplate.defaultBackButton
import cz.creeperface.hytale.uimanager.templates.CommonTemplate.defaultCheckBox
import cz.creeperface.hytale.uimanager.templates.CommonTemplate.defaultDropdownBox
import cz.creeperface.hytale.uimanager.templates.CommonTemplate.defaultTextButton
import cz.creeperface.hytale.uimanager.templates.CommonTemplate.defaultTextField
import cz.creeperface.hytale.uimanager.templates.CommonTemplate.defaultTitle
import cz.creeperface.hytale.uimanager.templates.CommonTemplate.pageOverlay
import cz.creeperface.hytale.uimanager.templates.decoratedContainer
import cz.creeperface.hytale.uimanager.type.*
import cz.creeperface.hytale.uimanager.util.toMessage
import cz.creeperface.hytale.uimanager.util.translated

class UiData(
    var buttonText: Message,
    var label: String,
) {

}

val uiData = UiData(
    "Save".toMessage(),
    "Label1"
)

val page = customUi {
    val firstName = textField {
        id = "FirstName"
    }

    val lastName = textField {
        id = "LastName"
    }

    textButton {
        id = "MyButton"

        onActivate(
            firstName::value,
            lastName::value
        ) { firstName, lastName ->
            HytaleLogger.getLogger().info {
                "Button activated with first name: $firstName and last name: $lastName"
            }
        }
    }

    label {
        ::text.bind(uiData::buttonText)
    }
}

class BwData(time: Message) : BaseObservable() {
    var time: Message by observable(time)
}

fun ChildNodeBuilder.createBedWarsHud(data: BwData) {
        group {
            id = "BedWarsHudRow"
            anchor = UiAnchor(
                right = 10,
                top = 10,
                height = 40
            )
            layoutMode = LayoutMode.Right
            padding = UiPadding(horizontal = 6)

            // 1. Sword and Kills
            group {
                id = "KillsGroup"
                anchor = UiAnchor(height = 32)
                layoutMode = LayoutMode.Left
                padding = UiPadding(left = 8, right = 10, vertical = 4)
                background = UiPatchStyle(
                    texturePath = "../../Common/ContainerPanelPatch.png",
                    border = 4
                )

                group {
                    background = UiPatchStyle(texturePath = "../../Textures/BedWars/sword.png")
                    anchor = UiAnchor(width = 24, height = 24)
                }

                label {
                    text = "1".toMessage()
                    anchor = UiAnchor(width = 24, height = 24)
                    style = UiLabelStyle(
                        fontSize = 18.0,
                        renderBold = true,
                        textColor = Color("#ffffff"),
                        horizontalAlignment = LabelAlignment.Center,
                        verticalAlignment = LabelAlignment.Center
                    )
                }
            }

            // Spacer
            group { anchor = UiAnchor(width = 10) }

            // 2. Team and Bed Status
            group {
                id = "TeamStatusGroup"
                anchor = UiAnchor(height = 32)
                layoutMode = LayoutMode.Left
                padding = UiPadding(left = 8, right = 10, vertical = 4)
                background = UiPatchStyle(
                    texturePath = "../../Common/ContainerPanelPatch.png",
                    border = 4
                )

                group {
                    background = UiPatchStyle(texturePath = "../../Textures/BedWars/bed_red.png")
                    anchor = UiAnchor(width = 24, height = 24)
                }

                label {
                    text = "Red Team".toMessage()
                    anchor = UiAnchor(width = 100, height = 24)
                    style = UiLabelStyle(
                        fontSize = 18.0,
                        renderBold = true,
                        textColor = Color("#ff5555"),
                        horizontalAlignment = LabelAlignment.Center,
                        verticalAlignment = LabelAlignment.Center
                    )
                }
            }

            // Spacer
            group { anchor = UiAnchor(width = 10) }

            // 3. Other Teams Bed Status Row
            group {
                id = "BedsStatusRow"
                anchor = UiAnchor(height = 32)
                layoutMode = LayoutMode.Left
                padding = UiPadding(left = 8, right = 8, vertical = 4)
                background = UiPatchStyle(
                    texturePath = "../../Common/ContainerPanelPatch.png",
                    border = 4
                )

                val bedColors = listOf("orange", "magenta", "light_gray", "lime", "gray", "blue", "yellow")
                bedColors.forEachIndexed { index, color ->
                    group {
                        background = UiPatchStyle(texturePath = "../../Textures/BedWars/bed_$color.png")
                        anchor = UiAnchor(width = 24, height = 24)
                    }
                    if (index < bedColors.size - 1) {
                        group { anchor = UiAnchor(width = 4) }
                    }
                }
            }

            // Spacer
            group { anchor = UiAnchor(width = 10) }

            // 4. Timer
            group {
                id = "TimerGroup"
                anchor = UiAnchor(height = 32)
                layoutMode = LayoutMode.Left
                padding = UiPadding(left = 8, right = 10, vertical = 4)
                background = UiPatchStyle(
                    texturePath = "../../Common/ContainerPanelPatch.png",
                    border = 4
                )

                group {
                    background = UiPatchStyle(texturePath = "../../Textures/BedWars/clock.png")
                    anchor = UiAnchor(width = 24, height = 24)
                }

                label {
                    ::text.bind(data::time)

                    anchor = UiAnchor(width = 80, height = 24)
                    style = UiLabelStyle(
                        fontSize = 18.0,
                        renderBold = true,
                        textColor = Color("#ffffff"),
                        horizontalAlignment = LabelAlignment.Center,
                        verticalAlignment = LabelAlignment.Center
                    )
                }
            }
    }
}

data class MyFormData(
    var firstName: String? = null,
    var lastName: String? = null,
)

fun ChildNodeBuilder.createSampleForm() =
    group {
        layoutMode = LayoutMode.Middle

        group {
            anchor = UiAnchor(width = 400, height = 400)
            layoutMode = LayoutMode.Center

            form<MyFormData> {
                submitHandler = { playerRef, data ->
                    HytaleLogger.getLogger().atInfo().log("Form submitted with data: $data")
                }

                formGroup {
                    anchor = UiAnchor(height = 40)
                    layoutMode = LayoutMode.Center

                    boundTextField(MyFormData::firstName) {
                        anchor = UiAnchor(width = 100, height = 40)
                        placeholderText = "First Name".toMessage()
                    }
                }

                formGroup {
                    anchor = UiAnchor(height = 40)
                    layoutMode = LayoutMode.Center

                    boundTextField(MyFormData::lastName) {
                        anchor = UiAnchor(width = 100, height = 40)
                        placeholderText = "Last Name".toMessage()
                    }
                }

                formGroup {
                    anchor = UiAnchor(height = 40)
                    layoutMode = LayoutMode.Center

                    submitTextButton {
                        anchor = UiAnchor(width = 70, height = 40)
                        text = "Submit".toMessage()
                    }
                }

                formGroup {
                    anchor = UiAnchor(height = 40)
                    layoutMode = LayoutMode.Center

                    this@form.submitHandler = { playerRef, data ->
                        playerRef.sendMessage("Form submitted: ${data.firstName} ${data.lastName}")
                    }
                }
            }
        }
    }

fun ChildNodeBuilder.testPage() =
    decoratedContainer {
        title {
            label {
                text = "Form Title".toMessage()
            }
        }

        content {
            // content
        }
    }

fun ChildNodeBuilder.createBlockPageInstanceConfigurationPage() {
        @UiDsl
        fun ChildNodeBuilder.numberInput(
            id: String,
            labelText: Message,
            isVisible: Boolean = false,
            leftOffset: Int = 32
        ) =
            this.group {
                this.id = id
                visible = isVisible
                layoutMode = LayoutMode.Left
                anchor = UiAnchor(left = leftOffset, top = 6)

                label {
                    text = labelText
                    anchor = UiAnchor(top = 6, left = 6, right = 16)
                    style = CommonTemplate.defaultLabelStyle
                }

                numberField {
                    this.id = "Input"
                    anchor = UiAnchor(width = 60, left = 0, right = 16)

                    format = numberFieldFormat {
                        maxDecimalPlaces = 2
                        step = 0.5
                    }
                }
            }

        pageOverlay {
            layoutMode = LayoutMode.Middle

            decoratedContainer {
                anchor = UiAnchor(width = 532)

                title {
                    defaultTitle {
                        text = "server.customUI.configureInstanceBlockPage.title".translated()
                    }
                }

                content {
                    layoutMode = LayoutMode.Top
                    padding = UiPadding(full = 16)

                    group {
                        id = "Instance"
                        layoutMode = LayoutMode.Left
                        anchor = UiAnchor(bottom = 16)

                        label {
                            text = "server.customUI.configureInstanceBlockPage.instances".translated()
                            anchor = UiAnchor(top = 6, left = 6, right = 16)
                            style = CommonTemplate.defaultLabelStyle
                        }

                        defaultDropdownBox {
                            id = "Input"
                            anchor = UiAnchor(left = 0, right = 16)
                        }
                    }

                    group {
                        id = "InstanceKey"
                        layoutMode = LayoutMode.Left
                        anchor = UiAnchor(bottom = 16)

                        label {
                            text = "server.customUI.configureInstanceBlockPage.instanceKey".translated()
                            anchor = UiAnchor(top = 6, left = 6, right = 16)
                            style = CommonTemplate.defaultLabelStyle
                        }

                        defaultTextField {
                            id = "Input"
                            anchor = UiAnchor(width = 350, left = 0, right = 16)
                        }
                    }

                    group {
                        id = "PositionOffset"
                        layoutMode = LayoutMode.Top
                        anchor = UiAnchor(bottom = 16)

                        group {
                            id = "Use"
                            layoutMode = LayoutMode.Left
                            anchor = UiAnchor(left = 0, top = 6)

                            label {
                                text = "server.customUI.configureInstanceBlockPage.positionOffset".translated()
                                anchor = UiAnchor(top = 6, left = 6, right = 16)
                                style = CommonTemplate.defaultLabelStyle
                            }

                            defaultCheckBox {
                                id = "CheckBox"
                            }
                        }

                        numberInput("X", "server.customUI.configureInstanceBlockPage.positionOffset.x".translated())
                        numberInput("Y", "server.customUI.configureInstanceBlockPage.positionOffset.y".translated())
                        numberInput("Z", "server.customUI.configureInstanceBlockPage.positionOffset.z".translated())
                    }

                    group {
                        id = "Rotation"
                        layoutMode = LayoutMode.Top
                        anchor = UiAnchor(bottom = 16)

                        group {
                            id = "Use"
                            layoutMode = LayoutMode.Left
                            anchor = UiAnchor(left = 0, top = 6)

                            label {
                                text = "server.customUI.configureInstanceBlockPage.rotation".translated()
                                anchor = UiAnchor(top = 6, left = 6, right = 16)
                                style = CommonTemplate.defaultLabelStyle
                            }

                            defaultCheckBox {
                                id = "CheckBox"
                            }
                        }

                        numberInput("Pitch", "server.customUI.configureInstanceBlockPage.rotation.pitch".translated())
                        numberInput("Yaw", "server.customUI.configureInstanceBlockPage.rotation.yaw".translated())
                        numberInput("Roll", "server.customUI.configureInstanceBlockPage.rotation.roll".translated())
                    }

                    group {
                        id = "PersonalReturnPoint"
                        layoutMode = LayoutMode.Left
                        anchor = UiAnchor(bottom = 16)

                        label {
                            text = "server.customUI.configureInstanceBlockPage.personalReturnPoint".translated()
                            anchor = UiAnchor(top = 6, left = 6, right = 16)
                            style = CommonTemplate.defaultLabelStyle
                        }

                        defaultCheckBox {
                            id = "CheckBox"
                        }
                    }

                    group {
                        id = "CloseOnBlockRemove"
                        layoutMode = LayoutMode.Left
                        anchor = UiAnchor(bottom = 16)

                        label {
                            text = "server.customUI.configureInstanceBlockPage.closeOnBlockRemove".translated()
                            anchor = UiAnchor(top = 6, left = 6, right = 16)
                            style = CommonTemplate.defaultLabelStyle
                        }

                        defaultCheckBox {
                            id = "CheckBox"
                        }
                    }

                    numberInput(
                        id = "RemoveBlockAfter",
                        labelText = "server.customUI.configureInstanceBlockPage.removeBlockAfter".translated(),
                        isVisible = true,
                        leftOffset = 0
                    )

                    group {
                        anchor = UiAnchor(vertical = 16, height = 1)
                        background = UiPatchStyle(color = Color("#5e512c"))
                    }

                    defaultTextButton {
                        id = "SaveButton"
                        text = "server.customUI.saveChanges".translated()

//                    onMouseEntered {
//
//                    }
                    }
                }
            }
        }

        defaultBackButton {}
}

data class BarterTradeItem(
    val itemId: String,
    val quantity: Int
)

data class BarterTrade(
    val input: BarterTradeItem,
    val output: BarterTradeItem,
    val stock: Int,
    val playerInventoryCount: Int
)

data class BarterData(
    val trades: List<BarterTrade>,
)

fun ChildNodeBuilder.createBarterPage(data: BarterData?) {
        @UiDsl
        fun ChildNodeBuilder.barterTradeRow(trade: BarterTrade?) = group {
            anchor = anchor { width = 230; height = 185 }
            padding = padding { horizontal = 5; vertical = 5 }

            itemSlotButton {
                anchor = anchor { full = 0 }
                layoutMode = LayoutMode.Full
                padding = padding { full = 2 }
                style = buttonStyle {
                    default = buttonStyleState { background = patchStyle { color = Color("#252f3a") } }
                    hovered = buttonStyleState { background = patchStyle { color = Color("#c9a050") } }
                    pressed = buttonStyleState { background = patchStyle { color = Color("#a08040") } }
                    disabled = buttonStyleState { background = patchStyle { color = Color("#1a1e24") } }
                    sounds = CommonTemplate.buttonSounds
                }
                disabled = (trade?.stock ?: 0) <= 0

                if ((trade?.stock ?: 0) <= 0) {
                    background = patchStyle {
                        color = Color("#4a2020")
                    }
                }

                group {
                    anchor = anchor { full = 0 }
                    layoutMode = LayoutMode.Top
                    background = patchStyle { color = Color("#1c2835") }

                    group {
                        anchor = anchor { height = 80 }
                        padding = padding { top = 4 }
                        layoutMode = LayoutMode.CenterMiddle

                        group {
                            anchor = anchor { width = 68; height = 68 }

                            group {
                                anchor = anchor { full = 0 }
                                background = patchStyle { color = Color("#1a2530") }
                                padding = padding { full = 2 }

                                itemSlot {
                                    id = "OutputSlot"
                                    anchor = anchor { full = 0 }
                                    showQualityBackground = true

                                    trade?.let {
                                        itemId = trade.output.itemId
                                    }
                                }
                            }

                            label {
                                anchor = anchor { right = 8; bottom = 5 }
                                style = labelStyle {
                                    fontSize = 15.0
                                    textColor = Color("#ffffff")
                                    horizontalAlignment = LabelAlignment.End
                                    verticalAlignment = LabelAlignment.End
                                    renderBold = true
                                }

                                trade?.let {
                                    text = trade.output.quantity.toString().toMessage()
                                }
                            }
                        }
                    }

                    group {
                        anchor = anchor { height = 8 }
                        layoutMode = LayoutMode.CenterMiddle

                        group {
                            id = "DividerLine"
                            anchor = anchor { width = 140; height = 1 }
                            background = patchStyle { color = Color("#252F3A") }
                        }
                    }

                    group {
                        anchor = anchor { height = 64 }
                        padding = padding { top = 12 }
                        layoutMode = LayoutMode.Full

                        label {
                            anchor = anchor { left = 28; top = -1; width = 46; height = 48 }
                            style = labelStyle {
                                fontSize = 14.0
                                textColor = Color("#8a9aaa")
                                horizontalAlignment = LabelAlignment.End
                                verticalAlignment = LabelAlignment.Center
                            }
                            text = "Cost:".toMessage()
                        }

                        group {
                            anchor = anchor { full = 0 }
                            layoutMode = LayoutMode.MiddleCenter

                            group {
                                anchor = anchor { width = 52; height = 52 }

                                group {
                                    anchor = anchor { full = 0 }
                                    background = patchStyle { color = Color("#2a5a3a") }
                                    padding = padding { full = 2 }

                                    itemSlot {
                                        anchor = anchor { full = 0 }
                                        showQualityBackground = true

                                        trade?.let {
                                            itemId = trade.input.itemId
                                        }
                                    }
                                }

                                label {
                                    anchor = anchor { right = 6; bottom = 4 }
                                    style = labelStyle {
                                        fontSize = 12.0
                                        textColor = Color("#ffffff")
                                        horizontalAlignment = LabelAlignment.End
                                        verticalAlignment = LabelAlignment.End
                                        renderBold = true
                                    }

                                    trade?.let {
                                        text = trade.input.quantity.toString().toMessage()
                                    }
                                }
                            }

                            label {
                                anchor = anchor { height = 18 }
                                padding = padding { top = 4 }
                                style = labelStyle {
                                    fontSize = 13.0
                                    textColor = Color("#3d913f")
                                    horizontalAlignment = LabelAlignment.Center
                                    verticalAlignment = LabelAlignment.Center
                                }
                                text = ("Have: " + (trade?.playerInventoryCount ?: 0)).toMessage()
                            }
                        }
                    }

                    group {
                        anchor = anchor { height = 16 }
                        layoutMode = LayoutMode.Full
                        padding = padding { right = 8; top = 2 }

                        label {
                            id = "Stock"
                            visible = (trade?.stock ?: 0) > 0
                            anchor = anchor { right = 0; bottom = 0; height = 14 }
                            style = labelStyle {
                                fontSize = 12.0
                                textColor = Color("#7a8a9a")
                                horizontalAlignment = LabelAlignment.End
                                verticalAlignment = LabelAlignment.Center
                            }
                        }
                    }
                }

                group {
                    anchor = anchor { full = 0 }
                    background = patchStyle { color = Color("#0a0e12", 0.75) }
                    layoutMode = LayoutMode.CenterMiddle
                    visible = (trade?.stock ?: 0) <= 0

                    label {
                        style = labelStyle {
                            fontSize = 14.0
                            textColor = Color("#cc4444")
                            horizontalAlignment = LabelAlignment.Center
                            verticalAlignment = LabelAlignment.Center
                            renderBold = true
                        }
                        text = "OUT OF STOCK".toMessage()
                    }
                }
            }
        }

        pageOverlay {
        }

        decoratedContainer {
            anchor = anchor { width = 740; height = 480 }

            title {
                defaultTitle {
                    style = CommonTemplate.titleStyle.copy(
                        horizontalAlignment = LabelAlignment.Center
                    )
                    padding = padding { horizontal = 19 }
                }
            }

            content {
                layoutMode = LayoutMode.Top
                padding = padding { horizontal = 10 }

                group {
                    flexWeight = 1
                    layoutMode = LayoutMode.TopScrolling
                    scrollbarStyle = CommonTemplate.defaultScrollbarStyle
                    padding = padding { top = 10; left = 8; right = 8 }

                    listGroup {
                        layoutMode = LayoutMode.LeftCenterWrap

                        data?.trades?.forEach { trade ->
                            barterTradeRow(trade)
                        }
                    }
                }

                group {
                    anchor = anchor { height = 40 }
                    layoutMode = LayoutMode.Top
                    padding = padding { horizontal = -2 }

                    group {
                        anchor = anchor { height = 2 }
                        background = patchStyle { color = Color("#19252F") }
                    }

                    group {
                        flexWeight = 1
                        layoutMode = LayoutMode.CenterMiddle
                        padding = padding { bottom = 6 }

                        label {
                            style = labelStyle {
                                fontSize = 14.0
                                textColor = Color("#7caacc")
                                horizontalAlignment = LabelAlignment.Center
                                verticalAlignment = LabelAlignment.Center
                            }
                            text = "".toMessage()
                        }
                    }
                }
            }
        }

        defaultBackButton {}
}