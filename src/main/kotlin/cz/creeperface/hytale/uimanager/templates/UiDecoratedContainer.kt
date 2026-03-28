package cz.creeperface.hytale.uimanager.templates

import cz.creeperface.hytale.uimanager.ChildNodeBuilder
import cz.creeperface.hytale.uimanager.ExcludeProperty
import cz.creeperface.hytale.uimanager.IdGenerator
import cz.creeperface.hytale.uimanager.UiDsl
import cz.creeperface.hytale.uimanager.UiNode
import cz.creeperface.hytale.uimanager.UiPage
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

class UiDecoratedContainer(
    @ExcludeProperty
    val containerBuilder: (() -> Unit)? = null,
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
        val clone = UiDecoratedContainer(containerBuilder, titleBuilder, contentBuilder, closeButtonVisible)
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
fun ChildNodeBuilder.decoratedContainer(
    `init`: UiDecoratedContainer.() -> Unit
): UiGroup {
    val container = UiDecoratedContainer()

    val prefix = if (this is UiNode) (this.id ?: "Node") else "Node"
    container.id = IdGenerator.getNext(prefix + "Group")

    with(container) {
        init()

        group {
            anchor = anchor { height = titleHeight; top = 0 }
            background = patchStyle { texturePath = CommonTemplate.UI_ROOT + "Common/ContainerHeader.png"; horizontalBorder = 50; verticalBorder = 0 }
            padding = padding { top = 7 }

            group {
                anchor = anchor { width = 236; height = 11; top = -12 }
                background = patchStyle { texturePath = CommonTemplate.UI_ROOT + "Common/ContainerDecorationTop.png" }
            }

            container.titleBuilder?.invoke(this)
        }

        group {
            layoutMode = LayoutMode.Top
            anchor = anchor { top = titleHeight }
            padding = padding { full = fullPaddingValue }
            background = patchStyle { texturePath = CommonTemplate.UI_ROOT + "Common/ContainerPatch.png"; border = 23 }

            container.contentBuilder?.invoke(this)
        }

        group {
            anchor = anchor { width = 236; height = 11; bottom = -6 }
            background = patchStyle { texturePath = CommonTemplate.UI_ROOT + "Common/ContainerDecorationBottom.png" }
        }

        button {
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

    addNode(container)
    return container
}