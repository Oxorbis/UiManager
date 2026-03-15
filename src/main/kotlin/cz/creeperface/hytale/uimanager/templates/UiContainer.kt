package cz.creeperface.hytale.uimanager.templates

import cz.creeperface.hytale.uimanager.ChildNodeBuilder
import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.UiDsl
import cz.creeperface.hytale.uimanager.UiNode
import cz.creeperface.hytale.uimanager.builder.button
import cz.creeperface.hytale.uimanager.builder.group
import cz.creeperface.hytale.uimanager.enum.LayoutMode
import cz.creeperface.hytale.uimanager.node.UiGroup
import cz.creeperface.hytale.uimanager.templates.CommonTemplate.buttonsCancel
import cz.creeperface.hytale.uimanager.templates.CommonTemplate.fullPaddingValue
import cz.creeperface.hytale.uimanager.templates.CommonTemplate.titleHeight
import cz.creeperface.hytale.uimanager.type.anchor
import cz.creeperface.hytale.uimanager.type.buttonStyle
import cz.creeperface.hytale.uimanager.type.buttonStyleState
import cz.creeperface.hytale.uimanager.type.padding
import cz.creeperface.hytale.uimanager.type.patchStyle

class UiContainer(
    @ExcludeProperty
    var titleBuilder: (UiGroup.() -> Unit)? = null,
    @ExcludeProperty
    var contentBuilder: (UiGroup.() -> Unit)? = null,
    @ExcludeProperty
    var closeButtonVisible: Boolean = false
) : UiGroup() {

    fun title(`init`: UiGroup.() -> Unit) {
        titleBuilder = `init`
    }

    fun content(`init`: UiGroup.() -> Unit) {
        contentBuilder = `init`
    }

    override fun clone(): UiNode {
        val clone = UiContainer(titleBuilder, contentBuilder, closeButtonVisible)
        clone.id = this.id
        clone.omitName = this.omitName
        clone.anchor = this.anchor
        clone.background = this.background
        clone.flexWeight = this.flexWeight
        clone.hitTestVisible = this.hitTestVisible
        clone.layoutMode = this.layoutMode
        clone.padding = this.padding
        clone.scrollbarStyle = this.scrollbarStyle
        clone.textTooltipShowDelay = this.textTooltipShowDelay
        clone.textTooltipStyle = this.textTooltipStyle
        clone.tooltipText = this.tooltipText
        clone.visible = this.visible
        this.templates.forEach { clone.templates.add(it.clone()) }
        this.children.forEach { child ->
            clone.children.add(child.clone())
        }
        return clone
    }

    companion object {
        const val NODE_NAME: String = "Group"
    }
}

@UiDsl
fun ChildNodeBuilder.container(
    `init`: UiContainer.() -> Unit
): UiGroup {
    val container = UiContainer()

    container.init()

    return group {
        group {
            id = "Title"
            anchor = anchor { height = titleHeight; top = 0 }
            padding = padding { top = 7 }
            background = patchStyle { texturePath = CommonTemplate.UI_ROOT + "Common/ContainerHeaderNoRunes.png"; horizontalBorder = 35; verticalBorder = 0 }

            container.titleBuilder?.invoke(this)
        }

        group {
            id = "Content"
            layoutMode = LayoutMode.Top
            padding = padding { full = fullPaddingValue }
            anchor = anchor { top = titleHeight }
            background = patchStyle { texturePath = CommonTemplate.UI_ROOT + "Common/ContainerPatch.png"; border = 23 }

            container.contentBuilder?.invoke(this)
        }

        button {
            id = "CloseButton"
            anchor = anchor { width = 32; height = 32; top = -8; right = -8 }
            style = buttonStyle {
                default = buttonStyleState { background = patchStyle { texturePath = CommonTemplate.UI_ROOT + "Common/ContainerCloseButton.png" } }
                hovered = buttonStyleState { background = patchStyle { texturePath = CommonTemplate.UI_ROOT + "Common/ContainerCloseButtonHovered.png" } }
                pressed = buttonStyleState { background = patchStyle { texturePath = CommonTemplate.UI_ROOT + "Common/ContainerCloseButtonPressed.png" } }
                sounds = buttonsCancel
            }
            visible = container.closeButtonVisible
        }
    }
}
