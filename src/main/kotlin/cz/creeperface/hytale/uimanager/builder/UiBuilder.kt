package cz.creeperface.hytale.uimanager.builder

import cz.creeperface.hytale.uimanager.IdGenerator
import cz.creeperface.hytale.uimanager.UiPage

class UiBuilder {
    fun build(init: UiPage.() -> Unit): UiPage {
        IdGenerator.reset()
        val page = UiPage()
        page.init()
        return page
    }
}

fun customUi(init: UiPage.() -> Unit): UiPage {
    return UiBuilder().build(init)
}
