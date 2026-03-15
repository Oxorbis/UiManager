package cz.creeperface.hytale.uimanager.special

import cz.creeperface.hytale.uimanager.ChildNodeBuilder

interface UiFormContext<T : Any> : ChildNodeBuilder {
    val form: UiForm<*>
}