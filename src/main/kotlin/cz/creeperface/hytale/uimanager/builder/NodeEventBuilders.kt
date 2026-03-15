package cz.creeperface.hytale.uimanager.builder

import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType
import cz.creeperface.hytale.uimanager.BaseUiNode
import cz.creeperface.hytale.uimanager.IdGenerator
import cz.creeperface.hytale.uimanager.UiDsl
import cz.creeperface.hytale.uimanager.event.EventBindable
import cz.creeperface.hytale.uimanager.event.EventBinding
import cz.creeperface.hytale.uimanager.node.UiBackButton
import cz.creeperface.hytale.uimanager.node.UiCheckBox
import cz.creeperface.hytale.uimanager.node.UiNumberField
import cz.creeperface.hytale.uimanager.node.UiTextButton
import cz.creeperface.hytale.uimanager.node.UiTextField

//interface ButtonEventBindings : EventBindable {
//    @UiDsl
//    public fun onMouseEntered(action: () -> Unit) {
//        this.addEventBinding(EventBinding(CustomUIEventBindingType.MouseEntered, action))
//    }
//
//    @UiDsl
//    public fun onMouseExited(action: () -> Unit) {
//        this.addEventBinding(EventBinding(CustomUIEventBindingType.MouseExited, action))
//    }
//
//    @UiDsl
//    public fun onActivate(action: () -> Unit) {
//        this.addEventBinding(EventBinding(CustomUIEventBindingType.Activating, action))
//    }
//
//    @UiDsl
//    public fun onDoubleClick(action: () -> Unit) {
//        this.addEventBinding(EventBinding(CustomUIEventBindingType.DoubleClicking, action))
//    }
//
//    @UiDsl
//    public fun onRightClick(action: () -> Unit) {
//        this.addEventBinding(EventBinding(CustomUIEventBindingType.RightClicking, action))
//    }
//}
//
//interface InputEventBindings : EventBindable {
//    @UiDsl
//    public fun onRightClick(action: () -> Unit) {
//        this.addEventBinding(EventBinding(CustomUIEventBindingType.RightClicking, action))
//    }
//
//    @UiDsl
//    public fun onValidate(action: () -> Unit) {
//        this.addEventBinding(EventBinding(CustomUIEventBindingType.Validating, action))
//    }
//
//    @UiDsl
//    public fun onDismiss(action: () -> Unit) {
//        this.addEventBinding(EventBinding(CustomUIEventBindingType.Dismissing, action))
//    }
//
//    @UiDsl
//    public fun onFocusLost(action: () -> Unit) {
//        this.addEventBinding(EventBinding(CustomUIEventBindingType.FocusLost, action))
//    }
//
//    @UiDsl
//    public fun onFocusGained(action: () -> Unit) {
//        this.addEventBinding(EventBinding(CustomUIEventBindingType.FocusGained, action))
//    }
//
//    @UiDsl
//    public fun onValueChange(action: () -> Unit) {
//        this.addEventBinding(EventBinding(CustomUIEventBindingType.ValueChanged, action))
//    }
//}
//
//
//// back button
//
//@UiDsl
//public fun UiBackButton.onMouseEntered(action: () -> Unit) {
//    this.addEventBinding(EventBinding(CustomUIEventBindingType.MouseEntered, action))
//}
//
//@UiDsl
//public fun UiBackButton.onMouseExited(action: () -> Unit) {
//    this.addEventBinding(EventBinding(CustomUIEventBindingType.MouseExited, action))
//}
//
//@UiDsl
//public fun UiBackButton.onActivate(action: () -> Unit) {
//    this.addEventBinding(EventBinding(CustomUIEventBindingType.Activating, action))
//}
//
//@UiDsl
//public fun UiBackButton.onDoubleClick(action: () -> Unit) {
//    this.addEventBinding(EventBinding(CustomUIEventBindingType.DoubleClicking, action))
//}
//
//@UiDsl
//public fun UiBackButton.onRightClick(action: () -> Unit) {
//    this.addEventBinding(EventBinding(CustomUIEventBindingType.RightClicking, action))
//}
//
//// text field
//@UiDsl
//public fun UiTextField.onRightClick(action: () -> Unit) {
//    this.addEventBinding(EventBinding(CustomUIEventBindingType.RightClicking, action))
//}
//
//@UiDsl
//public fun UiTextField.onValidate(action: () -> Unit) {
//    this.addEventBinding(EventBinding(CustomUIEventBindingType.Validating, action))
//}
//
//@UiDsl
//public fun UiTextField.onDismiss(action: () -> Unit) {
//    this.addEventBinding(EventBinding(CustomUIEventBindingType.Dismissing, action))
//}
//
//@UiDsl
//public fun UiTextField.onFocusLost(action: () -> Unit) {
//    this.addEventBinding(EventBinding(CustomUIEventBindingType.FocusLost, action))
//}
//
//@UiDsl
//public fun UiTextField.onFocusGained(action: () -> Unit) {
//    this.addEventBinding(EventBinding(CustomUIEventBindingType.FocusGained, action))
//}
//
//@UiDsl
//public fun UiTextField.onValueChange(action: () -> Unit) {
//    this.addEventBinding(EventBinding(CustomUIEventBindingType.ValueChanged, action))
//}
//
//// number field
//@UiDsl
//public fun UiNumberField.onRightClick(action: () -> Unit) {
//    this.addEventBinding(EventBinding(CustomUIEventBindingType.RightClicking, action))
//}
//
//@UiDsl
//public fun UiNumberField.onValidate(action: () -> Unit) {
//    this.addEventBinding(EventBinding(CustomUIEventBindingType.Validating, action))
//}
//
//@UiDsl
//public fun UiNumberField.onDismiss(action: () -> Unit) {
//    this.addEventBinding(EventBinding(CustomUIEventBindingType.Dismissing, action))
//}
//
//@UiDsl
//public fun UiNumberField.onFocusLost(action: () -> Unit) {
//    this.addEventBinding(EventBinding(CustomUIEventBindingType.FocusLost, action))
//}
//
//@UiDsl
//public fun UiNumberField.onFocusGained(action: () -> Unit) {
//    this.addEventBinding(EventBinding(CustomUIEventBindingType.FocusGained, action))
//}
//
//@UiDsl
//public fun UiNumberField.onValueChange(action: () -> Unit) {
//    this.addEventBinding(EventBinding(CustomUIEventBindingType.ValueChanged, action))
//}
//
//// checkbox
//
//@UiDsl
//public fun UiCheckBox.onValueChange(action: () -> Unit) {
//    this.addEventBinding(EventBinding(CustomUIEventBindingType.ValueChanged, action))
//}