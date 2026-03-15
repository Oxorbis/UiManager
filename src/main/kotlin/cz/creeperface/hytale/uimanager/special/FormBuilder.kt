package cz.creeperface.hytale.uimanager.special

import com.hypixel.hytale.logger.HytaleLogger
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType
import cz.creeperface.hytale.uimanager.ChildNodeBuilder
import cz.creeperface.hytale.uimanager.IdGenerator
import cz.creeperface.hytale.uimanager.UiDsl
import cz.creeperface.hytale.uimanager.UiNode
import cz.creeperface.hytale.uimanager.UiPage
import cz.creeperface.hytale.uimanager.builder.*
import cz.creeperface.hytale.uimanager.node.*
import cz.creeperface.hytale.uimanager.Color
import cz.creeperface.hytale.uimanager.special.UiForm.FormSubmitter
import cz.creeperface.hytale.uimanager.templates.CommonTemplate.defaultCheckBox
import cz.creeperface.hytale.uimanager.templates.CommonTemplate.defaultNumberField
import cz.creeperface.hytale.uimanager.templates.CommonTemplate.defaultTextButton
import cz.creeperface.hytale.uimanager.templates.CommonTemplate.defaultTextField
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KMutableProperty1

@UiDsl
inline fun <reified T: Any> ChildNodeBuilder.form(`init`: UiForm<T>.() -> Unit): UiForm<T> {
    val node = UiForm(T::class, this)
    node.init()
    this.addNode(node)
    return node
}

@UiDsl
fun <T: Any> UiForm<T>.boundTextField(property: KMutableProperty1<T, String?>, defaultValue: String? = null, init: UiTextField.() -> Unit): UiTextField {
    return textField {
        defaultValue?.let { value = it }
        init()

        this@boundTextField.bindToForm(this, ::value, property)
    }
}

@UiDsl
fun <T: Any> UiForm<T>.boundDefaultTextField(property: KMutableProperty1<T, String?>, defaultValue: String? = null, init: UiTextField.() -> Unit): UiTextField {
    return defaultTextField {
        defaultValue?.let { value = it }
        init()

        this@boundDefaultTextField.bindToForm(this, ::value, property)
    }
}

@UiDsl
fun <T: Any> UiFormGroup<T>.boundTextField(property: KMutableProperty1<T, String?>, defaultValue: String? = null, init: UiTextField.() -> Unit): UiTextField {
    return textField {
        defaultValue?.let { value = it }
        init()

        this@boundTextField.form.bindToForm(this, ::value, property)
    }
}

@UiDsl
fun <T: Any> UiFormGroup<T>.boundDefaultTextField(property: KMutableProperty1<T, String?>, defaultValue: String? = null, init: UiTextField.() -> Unit): UiTextField {
    return defaultTextField {
        defaultValue?.let { value = it }
        init()

        this@boundDefaultTextField.form.bindToForm(this, ::value, property)
    }
}

@UiDsl
fun <T : Any> UiForm<T>.boundNumberField(property: KMutableProperty1<T, Double?>, defaultValue: Double? = null, init: UiNumberField.() -> Unit): UiNumberField {
    return numberField {
        defaultValue?.let { value = it }
        init()

        this@boundNumberField.bindToForm(this, ::value, property)
    }
}

@JvmName("boundNumberFieldInt")
@UiDsl
fun <T : Any> UiForm<T>.boundNumberField(property: KMutableProperty1<T, Int?>, defaultValue: Int? = null, init: UiNumberField.() -> Unit): UiNumberField {
    return numberField {
        defaultValue?.let { value = it.toDouble() }
        init()

        this@boundNumberField.bindToForm(this, ::value, property as KMutableProperty1<T, Double?>)
    }
}

@UiDsl
fun <T : Any> UiForm<T>.boundDefaultNumberField(property: KMutableProperty1<T, Double?>, defaultValue: Double? = null, init: UiNumberField.() -> Unit): UiNumberField {
    return defaultNumberField {
        defaultValue?.let { value = it }
        init()

        this@boundDefaultNumberField.bindToForm(this, ::value, property)
    }
}

@JvmName("boundDefaultNumberFieldInt")
@UiDsl
fun <T : Any> UiForm<T>.boundDefaultNumberField(property: KMutableProperty1<T, Int?>, defaultValue: Int? = null, init: UiNumberField.() -> Unit): UiNumberField {
    return defaultNumberField {
        defaultValue?.let { value = it.toDouble() }
        init()

        this@boundDefaultNumberField.bindToForm(this, ::value, property as KMutableProperty1<T, Double?>)
    }
}

@UiDsl
fun <T : Any> UiFormGroup<T>.boundNumberField(property: KMutableProperty1<T, Double?>, defaultValue: Double? = null, init: UiNumberField.() -> Unit): UiNumberField {
    return numberField {
        defaultValue?.let { value = it }
        init()

        this@boundNumberField.form.bindToForm(this, ::value, property)
    }
}

@JvmName("boundNumberFieldInt")
@UiDsl
fun <T : Any> UiFormGroup<T>.boundNumberField(property: KMutableProperty1<T, Int?>, defaultValue: Int? = null, init: UiNumberField.() -> Unit): UiNumberField {
    return numberField {
        defaultValue?.let { value = it.toDouble() }
        init()

        this@boundNumberField.form.bindToForm(this, ::value, property as KMutableProperty1<T, Double?>)
    }
}

@UiDsl
fun <T : Any> UiFormGroup<T>.boundDefaultNumberField(property: KMutableProperty1<T, Double?>, defaultValue: Double? = null, init: UiNumberField.() -> Unit): UiNumberField {
    return defaultNumberField {
        defaultValue?.let { value = it }
        init()

        this@boundDefaultNumberField.form.bindToForm(this, ::value, property)
    }
}

@JvmName("boundDefaultNumberFieldInt")
@UiDsl
fun <T : Any> UiFormGroup<T>.boundDefaultNumberField(property: KMutableProperty1<T, Int?>, defaultValue: Int? = null, init: UiNumberField.() -> Unit): UiNumberField {
    return defaultNumberField {
        defaultValue?.let { value = it.toDouble() }
        init()

        this@boundDefaultNumberField.form.bindToForm(
            this,
            ::value,
            property as KMutableProperty1<T, Double?>
        )
    }
}

@UiDsl
fun <T : Any> UiForm<T>.boundCheckBox(property: KMutableProperty1<T, Boolean?>, defaultValue: Boolean? = null, init: UiCheckBox.() -> Unit): UiCheckBox {
    return checkBox {
        defaultValue?.let { value = it }
        init()

        this@boundCheckBox.bindToForm(this, ::value, property)
    }
}

@UiDsl
fun <T : Any> UiForm<T>.boundDefaultCheckBox(property: KMutableProperty1<T, Boolean?>, defaultValue: Boolean? = null, init: UiCheckBox.() -> Unit): UiCheckBox {
    return defaultCheckBox {
        defaultValue?.let { value = it }
        init()

        this@boundDefaultCheckBox.bindToForm(this, ::value, property)
    }
}

@UiDsl
fun <T : Any> UiFormGroup<T>.boundCheckBox(property: KMutableProperty1<T, Boolean?>, defaultValue: Boolean? = null, init: UiCheckBox.() -> Unit): UiCheckBox {
    return checkBox {
        defaultValue?.let { value = it }
        init()

        this@boundCheckBox.form.bindToForm(this, ::value, property)
    }
}

@UiDsl
fun <T : Any> UiFormGroup<T>.boundDefaultCheckBox(property: KMutableProperty1<T, Boolean?>, defaultValue: Boolean? = null, init: UiCheckBox.() -> Unit): UiCheckBox {
    return defaultCheckBox {
        defaultValue?.let { value = it }
        init()

        this@boundDefaultCheckBox.form.bindToForm(this, ::value, property)
    }
}

@UiDsl
fun <T : Any> UiForm<T>.boundColorPicker(property: KMutableProperty1<T, Color?>, defaultValue: Color? = null, init: UiColorPicker.() -> Unit): UiColorPicker {
    return colorPicker {
        defaultValue?.let { value = it }
        init()

        this@boundColorPicker.bindToForm(this, ::value, property)
    }
}

@UiDsl
fun <T : Any> UiFormGroup<T>.boundColorPicker(property: KMutableProperty1<T, Color?>, defaultValue: Color? = null, init: UiColorPicker.() -> Unit): UiColorPicker {
    return colorPicker {
        defaultValue?.let { value = it }
        init()

        this@boundColorPicker.form.bindToForm(this, ::value, property)
    }
}

@UiDsl
fun <T : Any> UiForm<T>.boundColorPickerDropdownBox(property: KMutableProperty1<T, String?>, defaultValue: String? = null, init: UiColorPickerDropdownBox.() -> Unit): UiColorPickerDropdownBox {
    return colorPickerDropdownBox {
        defaultValue?.let { color = Color(it) }
        init()

        this@boundColorPickerDropdownBox.bindToForm(this, ::color, property) { it?.let { Color(it) } }
    }
}

@UiDsl
fun <T : Any> UiFormGroup<T>.boundColorPickerDropdownBox(property: KMutableProperty1<T, String?>, defaultValue: String? = null, init: UiColorPickerDropdownBox.() -> Unit): UiColorPickerDropdownBox {
    return colorPickerDropdownBox {
        defaultValue?.let { color = Color(it) }
        init()

        this@boundColorPickerDropdownBox.form.bindToForm(this, ::color, property) { it?.let { Color(it) } }
    }
}

@UiDsl
fun UiFormContext<*>.submitTextButton(init: UiTextButton.() -> Unit): UiTextButton {
    return textButton {
        init()
        this@submitTextButton.form.bindSubmit(this, CustomUIEventBindingType.Activating)
    }
}

@UiDsl
fun UiFormContext<*>.submitDefaultTextButton(init: UiTextButton.() -> Unit): UiTextButton {
    return defaultTextButton {
        init()
        this@submitDefaultTextButton.form.bindSubmit(this, CustomUIEventBindingType.Activating)
    }
}

@UiDsl
fun <T: Any> UiForm<T>.formGroup(init: UiFormGroup<T>.() -> Unit): UiFormGroup<T> {
    val node = UiFormGroup<T>(this)
    node.id = IdGenerator.getNext("Group")
    node.init()

    this.addNode(node)
    return node
}

@UiDsl
fun <T: Any> UiFormGroup<T>.formGroup(init: UiFormGroup<T>.() -> Unit): UiFormGroup<T> {
    val node = UiFormGroup<T>(this.form)
    node.id = IdGenerator.getNext("Group")
    node.init()

    this.addNode(node)
    return node
}