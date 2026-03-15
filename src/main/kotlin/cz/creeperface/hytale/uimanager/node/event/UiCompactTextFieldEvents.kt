package cz.creeperface.hytale.uimanager.node.event

import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType
import cz.creeperface.hytale.uimanager.event.EventContext
import cz.creeperface.hytale.uimanager.node.UiCompactTextField
import cz.creeperface.hytale.uimanager.special.UiFormContext
import kotlin.Any
import kotlin.String
import kotlin.Unit
import kotlin.reflect.KProperty0

public fun UiCompactTextField.onRightClick(vararg boundProperties: KProperty0<Any?>, action: (context: EventContext) -> Unit) {
  addEventBinding(CustomUIEventBindingType.RightClicking, requireNotNull(this.id), *boundProperties, action = action)
}

public fun UiCompactTextField.onRightClick(action: (`value`: String?) -> Unit) {
  addEventBinding(CustomUIEventBindingType.RightClicking, requireNotNull(this.id), this::value) {
    ctx -> action(ctx.getData(this::value))
  }
}

public fun UiCompactTextField.submitOnRightClick(formContext: UiFormContext<*>) {
  formContext.form.bindSubmit(this, CustomUIEventBindingType.RightClicking)
}

public fun <T1> UiCompactTextField.onRightClick(prop1: KProperty0<T1>, action: (pT1: T1) -> Unit) {
  addEventBinding(CustomUIEventBindingType.RightClicking, requireNotNull(this.id), prop1) {
    ctx -> action(
      ctx.getData(prop1)
    )
  }
}

public fun <T1, T2> UiCompactTextField.onRightClick(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  action: (pT1: T1, pT2: T2) -> Unit,
) {
  addEventBinding(CustomUIEventBindingType.RightClicking, requireNotNull(this.id), prop1, prop2) {
    ctx -> action(
      ctx.getData(prop1),
      ctx.getData(prop2)
    )
  }
}

public fun <T1, T2, T3> UiCompactTextField.onRightClick(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  action: (
    pT1: T1,
    pT2: T2,
    pT3: T3,
  ) -> Unit,
) {
  addEventBinding(CustomUIEventBindingType.RightClicking, requireNotNull(this.id), prop1, prop2, prop3) {
    ctx -> action(
      ctx.getData(prop1),
      ctx.getData(prop2),
      ctx.getData(prop3)
    )
  }
}

public fun <T1, T2, T3, T4> UiCompactTextField.onRightClick(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  prop4: KProperty0<T4>,
  action: (
    pT1: T1,
    pT2: T2,
    pT3: T3,
    pT4: T4,
  ) -> Unit,
) {
  addEventBinding(CustomUIEventBindingType.RightClicking, requireNotNull(this.id), prop1, prop2, prop3, prop4) {
    ctx -> action(
      ctx.getData(prop1),
      ctx.getData(prop2),
      ctx.getData(prop3),
      ctx.getData(prop4)
    )
  }
}

public fun <T1, T2, T3, T4, T5> UiCompactTextField.onRightClick(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  prop4: KProperty0<T4>,
  prop5: KProperty0<T5>,
  action: (
    pT1: T1,
    pT2: T2,
    pT3: T3,
    pT4: T4,
    pT5: T5,
  ) -> Unit,
) {
  addEventBinding(CustomUIEventBindingType.RightClicking, requireNotNull(this.id), prop1, prop2, prop3, prop4, prop5) {
    ctx -> action(
      ctx.getData(prop1),
      ctx.getData(prop2),
      ctx.getData(prop3),
      ctx.getData(prop4),
      ctx.getData(prop5)
    )
  }
}

public fun <T1> UiCompactTextField.onRightClick(prop1: KProperty0<T1>, action: (`value`: String?, p1: T1) -> Unit) {
  addEventBinding(CustomUIEventBindingType.RightClicking, requireNotNull(this.id), this::value, prop1) {
    ctx -> action(
      ctx.getData(this::value),
      ctx.getData(prop1)
    )
  }
}

public fun <T1, T2> UiCompactTextField.onRightClick(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  action: (
    `value`: String?,
    p1: T1,
    p2: T2,
  ) -> Unit,
) {
  addEventBinding(CustomUIEventBindingType.RightClicking, requireNotNull(this.id), this::value, prop1, prop2) {
    ctx -> action(
      ctx.getData(this::value),
      ctx.getData(prop1),
      ctx.getData(prop2)
    )
  }
}

public fun <T1, T2, T3> UiCompactTextField.onRightClick(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  action: (
    `value`: String?,
    p1: T1,
    p2: T2,
    p3: T3,
  ) -> Unit,
) {
  addEventBinding(CustomUIEventBindingType.RightClicking, requireNotNull(this.id), this::value, prop1, prop2, prop3) {
    ctx -> action(
      ctx.getData(this::value),
      ctx.getData(prop1),
      ctx.getData(prop2),
      ctx.getData(prop3)
    )
  }
}

public fun <T1, T2, T3, T4> UiCompactTextField.onRightClick(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  prop4: KProperty0<T4>,
  action: (
    `value`: String?,
    p1: T1,
    p2: T2,
    p3: T3,
    p4: T4,
  ) -> Unit,
) {
  addEventBinding(CustomUIEventBindingType.RightClicking, requireNotNull(this.id), this::value, prop1, prop2, prop3, prop4) {
    ctx -> action(
      ctx.getData(this::value),
      ctx.getData(prop1),
      ctx.getData(prop2),
      ctx.getData(prop3),
      ctx.getData(prop4)
    )
  }
}

public fun <T1, T2, T3, T4, T5> UiCompactTextField.onRightClick(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  prop4: KProperty0<T4>,
  prop5: KProperty0<T5>,
  action: (
    `value`: String?,
    p1: T1,
    p2: T2,
    p3: T3,
    p4: T4,
    p5: T5,
  ) -> Unit,
) {
  addEventBinding(CustomUIEventBindingType.RightClicking, requireNotNull(this.id), this::value, prop1, prop2, prop3, prop4, prop5) {
    ctx -> action(
      ctx.getData(this::value),
      ctx.getData(prop1),
      ctx.getData(prop2),
      ctx.getData(prop3),
      ctx.getData(prop4),
      ctx.getData(prop5)
    )
  }
}

public fun UiCompactTextField.onValidate(vararg boundProperties: KProperty0<Any?>, action: (context: EventContext) -> Unit) {
  addEventBinding(CustomUIEventBindingType.Validating, requireNotNull(this.id), *boundProperties, action = action)
}

public fun UiCompactTextField.onValidate(action: (`value`: String?) -> Unit) {
  addEventBinding(CustomUIEventBindingType.Validating, requireNotNull(this.id), this::value) {
    ctx -> action(ctx.getData(this::value))
  }
}

public fun UiCompactTextField.submitOnValidate(formContext: UiFormContext<*>) {
  formContext.form.bindSubmit(this, CustomUIEventBindingType.Validating)
}

public fun <T1> UiCompactTextField.onValidate(prop1: KProperty0<T1>, action: (pT1: T1) -> Unit) {
  addEventBinding(CustomUIEventBindingType.Validating, requireNotNull(this.id), prop1) {
    ctx -> action(
      ctx.getData(prop1)
    )
  }
}

public fun <T1, T2> UiCompactTextField.onValidate(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  action: (pT1: T1, pT2: T2) -> Unit,
) {
  addEventBinding(CustomUIEventBindingType.Validating, requireNotNull(this.id), prop1, prop2) {
    ctx -> action(
      ctx.getData(prop1),
      ctx.getData(prop2)
    )
  }
}

public fun <T1, T2, T3> UiCompactTextField.onValidate(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  action: (
    pT1: T1,
    pT2: T2,
    pT3: T3,
  ) -> Unit,
) {
  addEventBinding(CustomUIEventBindingType.Validating, requireNotNull(this.id), prop1, prop2, prop3) {
    ctx -> action(
      ctx.getData(prop1),
      ctx.getData(prop2),
      ctx.getData(prop3)
    )
  }
}

public fun <T1, T2, T3, T4> UiCompactTextField.onValidate(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  prop4: KProperty0<T4>,
  action: (
    pT1: T1,
    pT2: T2,
    pT3: T3,
    pT4: T4,
  ) -> Unit,
) {
  addEventBinding(CustomUIEventBindingType.Validating, requireNotNull(this.id), prop1, prop2, prop3, prop4) {
    ctx -> action(
      ctx.getData(prop1),
      ctx.getData(prop2),
      ctx.getData(prop3),
      ctx.getData(prop4)
    )
  }
}

public fun <T1, T2, T3, T4, T5> UiCompactTextField.onValidate(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  prop4: KProperty0<T4>,
  prop5: KProperty0<T5>,
  action: (
    pT1: T1,
    pT2: T2,
    pT3: T3,
    pT4: T4,
    pT5: T5,
  ) -> Unit,
) {
  addEventBinding(CustomUIEventBindingType.Validating, requireNotNull(this.id), prop1, prop2, prop3, prop4, prop5) {
    ctx -> action(
      ctx.getData(prop1),
      ctx.getData(prop2),
      ctx.getData(prop3),
      ctx.getData(prop4),
      ctx.getData(prop5)
    )
  }
}

public fun <T1> UiCompactTextField.onValidate(prop1: KProperty0<T1>, action: (`value`: String?, p1: T1) -> Unit) {
  addEventBinding(CustomUIEventBindingType.Validating, requireNotNull(this.id), this::value, prop1) {
    ctx -> action(
      ctx.getData(this::value),
      ctx.getData(prop1)
    )
  }
}

public fun <T1, T2> UiCompactTextField.onValidate(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  action: (
    `value`: String?,
    p1: T1,
    p2: T2,
  ) -> Unit,
) {
  addEventBinding(CustomUIEventBindingType.Validating, requireNotNull(this.id), this::value, prop1, prop2) {
    ctx -> action(
      ctx.getData(this::value),
      ctx.getData(prop1),
      ctx.getData(prop2)
    )
  }
}

public fun <T1, T2, T3> UiCompactTextField.onValidate(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  action: (
    `value`: String?,
    p1: T1,
    p2: T2,
    p3: T3,
  ) -> Unit,
) {
  addEventBinding(CustomUIEventBindingType.Validating, requireNotNull(this.id), this::value, prop1, prop2, prop3) {
    ctx -> action(
      ctx.getData(this::value),
      ctx.getData(prop1),
      ctx.getData(prop2),
      ctx.getData(prop3)
    )
  }
}

public fun <T1, T2, T3, T4> UiCompactTextField.onValidate(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  prop4: KProperty0<T4>,
  action: (
    `value`: String?,
    p1: T1,
    p2: T2,
    p3: T3,
    p4: T4,
  ) -> Unit,
) {
  addEventBinding(CustomUIEventBindingType.Validating, requireNotNull(this.id), this::value, prop1, prop2, prop3, prop4) {
    ctx -> action(
      ctx.getData(this::value),
      ctx.getData(prop1),
      ctx.getData(prop2),
      ctx.getData(prop3),
      ctx.getData(prop4)
    )
  }
}

public fun <T1, T2, T3, T4, T5> UiCompactTextField.onValidate(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  prop4: KProperty0<T4>,
  prop5: KProperty0<T5>,
  action: (
    `value`: String?,
    p1: T1,
    p2: T2,
    p3: T3,
    p4: T4,
    p5: T5,
  ) -> Unit,
) {
  addEventBinding(CustomUIEventBindingType.Validating, requireNotNull(this.id), this::value, prop1, prop2, prop3, prop4, prop5) {
    ctx -> action(
      ctx.getData(this::value),
      ctx.getData(prop1),
      ctx.getData(prop2),
      ctx.getData(prop3),
      ctx.getData(prop4),
      ctx.getData(prop5)
    )
  }
}

public fun UiCompactTextField.onDismiss(vararg boundProperties: KProperty0<Any?>, action: (context: EventContext) -> Unit) {
  addEventBinding(CustomUIEventBindingType.Dismissing, requireNotNull(this.id), *boundProperties, action = action)
}

public fun UiCompactTextField.onDismiss(action: (`value`: String?) -> Unit) {
  addEventBinding(CustomUIEventBindingType.Dismissing, requireNotNull(this.id), this::value) {
    ctx -> action(ctx.getData(this::value))
  }
}

public fun UiCompactTextField.submitOnDismiss(formContext: UiFormContext<*>) {
  formContext.form.bindSubmit(this, CustomUIEventBindingType.Dismissing)
}

public fun <T1> UiCompactTextField.onDismiss(prop1: KProperty0<T1>, action: (pT1: T1) -> Unit) {
  addEventBinding(CustomUIEventBindingType.Dismissing, requireNotNull(this.id), prop1) {
    ctx -> action(
      ctx.getData(prop1)
    )
  }
}

public fun <T1, T2> UiCompactTextField.onDismiss(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  action: (pT1: T1, pT2: T2) -> Unit,
) {
  addEventBinding(CustomUIEventBindingType.Dismissing, requireNotNull(this.id), prop1, prop2) {
    ctx -> action(
      ctx.getData(prop1),
      ctx.getData(prop2)
    )
  }
}

public fun <T1, T2, T3> UiCompactTextField.onDismiss(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  action: (
    pT1: T1,
    pT2: T2,
    pT3: T3,
  ) -> Unit,
) {
  addEventBinding(CustomUIEventBindingType.Dismissing, requireNotNull(this.id), prop1, prop2, prop3) {
    ctx -> action(
      ctx.getData(prop1),
      ctx.getData(prop2),
      ctx.getData(prop3)
    )
  }
}

public fun <T1, T2, T3, T4> UiCompactTextField.onDismiss(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  prop4: KProperty0<T4>,
  action: (
    pT1: T1,
    pT2: T2,
    pT3: T3,
    pT4: T4,
  ) -> Unit,
) {
  addEventBinding(CustomUIEventBindingType.Dismissing, requireNotNull(this.id), prop1, prop2, prop3, prop4) {
    ctx -> action(
      ctx.getData(prop1),
      ctx.getData(prop2),
      ctx.getData(prop3),
      ctx.getData(prop4)
    )
  }
}

public fun <T1, T2, T3, T4, T5> UiCompactTextField.onDismiss(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  prop4: KProperty0<T4>,
  prop5: KProperty0<T5>,
  action: (
    pT1: T1,
    pT2: T2,
    pT3: T3,
    pT4: T4,
    pT5: T5,
  ) -> Unit,
) {
  addEventBinding(CustomUIEventBindingType.Dismissing, requireNotNull(this.id), prop1, prop2, prop3, prop4, prop5) {
    ctx -> action(
      ctx.getData(prop1),
      ctx.getData(prop2),
      ctx.getData(prop3),
      ctx.getData(prop4),
      ctx.getData(prop5)
    )
  }
}

public fun <T1> UiCompactTextField.onDismiss(prop1: KProperty0<T1>, action: (`value`: String?, p1: T1) -> Unit) {
  addEventBinding(CustomUIEventBindingType.Dismissing, requireNotNull(this.id), this::value, prop1) {
    ctx -> action(
      ctx.getData(this::value),
      ctx.getData(prop1)
    )
  }
}

public fun <T1, T2> UiCompactTextField.onDismiss(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  action: (
    `value`: String?,
    p1: T1,
    p2: T2,
  ) -> Unit,
) {
  addEventBinding(CustomUIEventBindingType.Dismissing, requireNotNull(this.id), this::value, prop1, prop2) {
    ctx -> action(
      ctx.getData(this::value),
      ctx.getData(prop1),
      ctx.getData(prop2)
    )
  }
}

public fun <T1, T2, T3> UiCompactTextField.onDismiss(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  action: (
    `value`: String?,
    p1: T1,
    p2: T2,
    p3: T3,
  ) -> Unit,
) {
  addEventBinding(CustomUIEventBindingType.Dismissing, requireNotNull(this.id), this::value, prop1, prop2, prop3) {
    ctx -> action(
      ctx.getData(this::value),
      ctx.getData(prop1),
      ctx.getData(prop2),
      ctx.getData(prop3)
    )
  }
}

public fun <T1, T2, T3, T4> UiCompactTextField.onDismiss(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  prop4: KProperty0<T4>,
  action: (
    `value`: String?,
    p1: T1,
    p2: T2,
    p3: T3,
    p4: T4,
  ) -> Unit,
) {
  addEventBinding(CustomUIEventBindingType.Dismissing, requireNotNull(this.id), this::value, prop1, prop2, prop3, prop4) {
    ctx -> action(
      ctx.getData(this::value),
      ctx.getData(prop1),
      ctx.getData(prop2),
      ctx.getData(prop3),
      ctx.getData(prop4)
    )
  }
}

public fun <T1, T2, T3, T4, T5> UiCompactTextField.onDismiss(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  prop4: KProperty0<T4>,
  prop5: KProperty0<T5>,
  action: (
    `value`: String?,
    p1: T1,
    p2: T2,
    p3: T3,
    p4: T4,
    p5: T5,
  ) -> Unit,
) {
  addEventBinding(CustomUIEventBindingType.Dismissing, requireNotNull(this.id), this::value, prop1, prop2, prop3, prop4, prop5) {
    ctx -> action(
      ctx.getData(this::value),
      ctx.getData(prop1),
      ctx.getData(prop2),
      ctx.getData(prop3),
      ctx.getData(prop4),
      ctx.getData(prop5)
    )
  }
}

public fun UiCompactTextField.onFocusLost(vararg boundProperties: KProperty0<Any?>, action: (context: EventContext) -> Unit) {
  addEventBinding(CustomUIEventBindingType.FocusLost, requireNotNull(this.id), *boundProperties, action = action)
}

public fun UiCompactTextField.onFocusLost(action: (`value`: String?) -> Unit) {
  addEventBinding(CustomUIEventBindingType.FocusLost, requireNotNull(this.id), this::value) {
    ctx -> action(ctx.getData(this::value))
  }
}

public fun UiCompactTextField.submitOnFocusLost(formContext: UiFormContext<*>) {
  formContext.form.bindSubmit(this, CustomUIEventBindingType.FocusLost)
}

public fun <T1> UiCompactTextField.onFocusLost(prop1: KProperty0<T1>, action: (pT1: T1) -> Unit) {
  addEventBinding(CustomUIEventBindingType.FocusLost, requireNotNull(this.id), prop1) {
    ctx -> action(
      ctx.getData(prop1)
    )
  }
}

public fun <T1, T2> UiCompactTextField.onFocusLost(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  action: (pT1: T1, pT2: T2) -> Unit,
) {
  addEventBinding(CustomUIEventBindingType.FocusLost, requireNotNull(this.id), prop1, prop2) {
    ctx -> action(
      ctx.getData(prop1),
      ctx.getData(prop2)
    )
  }
}

public fun <T1, T2, T3> UiCompactTextField.onFocusLost(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  action: (
    pT1: T1,
    pT2: T2,
    pT3: T3,
  ) -> Unit,
) {
  addEventBinding(CustomUIEventBindingType.FocusLost, requireNotNull(this.id), prop1, prop2, prop3) {
    ctx -> action(
      ctx.getData(prop1),
      ctx.getData(prop2),
      ctx.getData(prop3)
    )
  }
}

public fun <T1, T2, T3, T4> UiCompactTextField.onFocusLost(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  prop4: KProperty0<T4>,
  action: (
    pT1: T1,
    pT2: T2,
    pT3: T3,
    pT4: T4,
  ) -> Unit,
) {
  addEventBinding(CustomUIEventBindingType.FocusLost, requireNotNull(this.id), prop1, prop2, prop3, prop4) {
    ctx -> action(
      ctx.getData(prop1),
      ctx.getData(prop2),
      ctx.getData(prop3),
      ctx.getData(prop4)
    )
  }
}

public fun <T1, T2, T3, T4, T5> UiCompactTextField.onFocusLost(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  prop4: KProperty0<T4>,
  prop5: KProperty0<T5>,
  action: (
    pT1: T1,
    pT2: T2,
    pT3: T3,
    pT4: T4,
    pT5: T5,
  ) -> Unit,
) {
  addEventBinding(CustomUIEventBindingType.FocusLost, requireNotNull(this.id), prop1, prop2, prop3, prop4, prop5) {
    ctx -> action(
      ctx.getData(prop1),
      ctx.getData(prop2),
      ctx.getData(prop3),
      ctx.getData(prop4),
      ctx.getData(prop5)
    )
  }
}

public fun <T1> UiCompactTextField.onFocusLost(prop1: KProperty0<T1>, action: (`value`: String?, p1: T1) -> Unit) {
  addEventBinding(CustomUIEventBindingType.FocusLost, requireNotNull(this.id), this::value, prop1) {
    ctx -> action(
      ctx.getData(this::value),
      ctx.getData(prop1)
    )
  }
}

public fun <T1, T2> UiCompactTextField.onFocusLost(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  action: (
    `value`: String?,
    p1: T1,
    p2: T2,
  ) -> Unit,
) {
  addEventBinding(CustomUIEventBindingType.FocusLost, requireNotNull(this.id), this::value, prop1, prop2) {
    ctx -> action(
      ctx.getData(this::value),
      ctx.getData(prop1),
      ctx.getData(prop2)
    )
  }
}

public fun <T1, T2, T3> UiCompactTextField.onFocusLost(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  action: (
    `value`: String?,
    p1: T1,
    p2: T2,
    p3: T3,
  ) -> Unit,
) {
  addEventBinding(CustomUIEventBindingType.FocusLost, requireNotNull(this.id), this::value, prop1, prop2, prop3) {
    ctx -> action(
      ctx.getData(this::value),
      ctx.getData(prop1),
      ctx.getData(prop2),
      ctx.getData(prop3)
    )
  }
}

public fun <T1, T2, T3, T4> UiCompactTextField.onFocusLost(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  prop4: KProperty0<T4>,
  action: (
    `value`: String?,
    p1: T1,
    p2: T2,
    p3: T3,
    p4: T4,
  ) -> Unit,
) {
  addEventBinding(CustomUIEventBindingType.FocusLost, requireNotNull(this.id), this::value, prop1, prop2, prop3, prop4) {
    ctx -> action(
      ctx.getData(this::value),
      ctx.getData(prop1),
      ctx.getData(prop2),
      ctx.getData(prop3),
      ctx.getData(prop4)
    )
  }
}

public fun <T1, T2, T3, T4, T5> UiCompactTextField.onFocusLost(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  prop4: KProperty0<T4>,
  prop5: KProperty0<T5>,
  action: (
    `value`: String?,
    p1: T1,
    p2: T2,
    p3: T3,
    p4: T4,
    p5: T5,
  ) -> Unit,
) {
  addEventBinding(CustomUIEventBindingType.FocusLost, requireNotNull(this.id), this::value, prop1, prop2, prop3, prop4, prop5) {
    ctx -> action(
      ctx.getData(this::value),
      ctx.getData(prop1),
      ctx.getData(prop2),
      ctx.getData(prop3),
      ctx.getData(prop4),
      ctx.getData(prop5)
    )
  }
}

public fun UiCompactTextField.onFocusGain(vararg boundProperties: KProperty0<Any?>, action: (context: EventContext) -> Unit) {
  addEventBinding(CustomUIEventBindingType.FocusGained, requireNotNull(this.id), *boundProperties, action = action)
}

public fun UiCompactTextField.onFocusGain(action: (`value`: String?) -> Unit) {
  addEventBinding(CustomUIEventBindingType.FocusGained, requireNotNull(this.id), this::value) {
    ctx -> action(ctx.getData(this::value))
  }
}

public fun UiCompactTextField.submitOnFocusGain(formContext: UiFormContext<*>) {
  formContext.form.bindSubmit(this, CustomUIEventBindingType.FocusGained)
}

public fun <T1> UiCompactTextField.onFocusGain(prop1: KProperty0<T1>, action: (pT1: T1) -> Unit) {
  addEventBinding(CustomUIEventBindingType.FocusGained, requireNotNull(this.id), prop1) {
    ctx -> action(
      ctx.getData(prop1)
    )
  }
}

public fun <T1, T2> UiCompactTextField.onFocusGain(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  action: (pT1: T1, pT2: T2) -> Unit,
) {
  addEventBinding(CustomUIEventBindingType.FocusGained, requireNotNull(this.id), prop1, prop2) {
    ctx -> action(
      ctx.getData(prop1),
      ctx.getData(prop2)
    )
  }
}

public fun <T1, T2, T3> UiCompactTextField.onFocusGain(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  action: (
    pT1: T1,
    pT2: T2,
    pT3: T3,
  ) -> Unit,
) {
  addEventBinding(CustomUIEventBindingType.FocusGained, requireNotNull(this.id), prop1, prop2, prop3) {
    ctx -> action(
      ctx.getData(prop1),
      ctx.getData(prop2),
      ctx.getData(prop3)
    )
  }
}

public fun <T1, T2, T3, T4> UiCompactTextField.onFocusGain(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  prop4: KProperty0<T4>,
  action: (
    pT1: T1,
    pT2: T2,
    pT3: T3,
    pT4: T4,
  ) -> Unit,
) {
  addEventBinding(CustomUIEventBindingType.FocusGained, requireNotNull(this.id), prop1, prop2, prop3, prop4) {
    ctx -> action(
      ctx.getData(prop1),
      ctx.getData(prop2),
      ctx.getData(prop3),
      ctx.getData(prop4)
    )
  }
}

public fun <T1, T2, T3, T4, T5> UiCompactTextField.onFocusGain(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  prop4: KProperty0<T4>,
  prop5: KProperty0<T5>,
  action: (
    pT1: T1,
    pT2: T2,
    pT3: T3,
    pT4: T4,
    pT5: T5,
  ) -> Unit,
) {
  addEventBinding(CustomUIEventBindingType.FocusGained, requireNotNull(this.id), prop1, prop2, prop3, prop4, prop5) {
    ctx -> action(
      ctx.getData(prop1),
      ctx.getData(prop2),
      ctx.getData(prop3),
      ctx.getData(prop4),
      ctx.getData(prop5)
    )
  }
}

public fun <T1> UiCompactTextField.onFocusGain(prop1: KProperty0<T1>, action: (`value`: String?, p1: T1) -> Unit) {
  addEventBinding(CustomUIEventBindingType.FocusGained, requireNotNull(this.id), this::value, prop1) {
    ctx -> action(
      ctx.getData(this::value),
      ctx.getData(prop1)
    )
  }
}

public fun <T1, T2> UiCompactTextField.onFocusGain(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  action: (
    `value`: String?,
    p1: T1,
    p2: T2,
  ) -> Unit,
) {
  addEventBinding(CustomUIEventBindingType.FocusGained, requireNotNull(this.id), this::value, prop1, prop2) {
    ctx -> action(
      ctx.getData(this::value),
      ctx.getData(prop1),
      ctx.getData(prop2)
    )
  }
}

public fun <T1, T2, T3> UiCompactTextField.onFocusGain(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  action: (
    `value`: String?,
    p1: T1,
    p2: T2,
    p3: T3,
  ) -> Unit,
) {
  addEventBinding(CustomUIEventBindingType.FocusGained, requireNotNull(this.id), this::value, prop1, prop2, prop3) {
    ctx -> action(
      ctx.getData(this::value),
      ctx.getData(prop1),
      ctx.getData(prop2),
      ctx.getData(prop3)
    )
  }
}

public fun <T1, T2, T3, T4> UiCompactTextField.onFocusGain(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  prop4: KProperty0<T4>,
  action: (
    `value`: String?,
    p1: T1,
    p2: T2,
    p3: T3,
    p4: T4,
  ) -> Unit,
) {
  addEventBinding(CustomUIEventBindingType.FocusGained, requireNotNull(this.id), this::value, prop1, prop2, prop3, prop4) {
    ctx -> action(
      ctx.getData(this::value),
      ctx.getData(prop1),
      ctx.getData(prop2),
      ctx.getData(prop3),
      ctx.getData(prop4)
    )
  }
}

public fun <T1, T2, T3, T4, T5> UiCompactTextField.onFocusGain(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  prop4: KProperty0<T4>,
  prop5: KProperty0<T5>,
  action: (
    `value`: String?,
    p1: T1,
    p2: T2,
    p3: T3,
    p4: T4,
    p5: T5,
  ) -> Unit,
) {
  addEventBinding(CustomUIEventBindingType.FocusGained, requireNotNull(this.id), this::value, prop1, prop2, prop3, prop4, prop5) {
    ctx -> action(
      ctx.getData(this::value),
      ctx.getData(prop1),
      ctx.getData(prop2),
      ctx.getData(prop3),
      ctx.getData(prop4),
      ctx.getData(prop5)
    )
  }
}

public fun UiCompactTextField.onValueChange(vararg boundProperties: KProperty0<Any?>, action: (context: EventContext) -> Unit) {
  addEventBinding(CustomUIEventBindingType.ValueChanged, requireNotNull(this.id), *boundProperties, action = action)
}

public fun UiCompactTextField.onValueChange(action: (`value`: String?) -> Unit) {
  addEventBinding(CustomUIEventBindingType.ValueChanged, requireNotNull(this.id), this::value) {
    ctx -> action(ctx.getData(this::value))
  }
}

public fun UiCompactTextField.submitOnValueChange(formContext: UiFormContext<*>) {
  formContext.form.bindSubmit(this, CustomUIEventBindingType.ValueChanged)
}

public fun <T1> UiCompactTextField.onValueChange(prop1: KProperty0<T1>, action: (pT1: T1) -> Unit) {
  addEventBinding(CustomUIEventBindingType.ValueChanged, requireNotNull(this.id), prop1) {
    ctx -> action(
      ctx.getData(prop1)
    )
  }
}

public fun <T1, T2> UiCompactTextField.onValueChange(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  action: (pT1: T1, pT2: T2) -> Unit,
) {
  addEventBinding(CustomUIEventBindingType.ValueChanged, requireNotNull(this.id), prop1, prop2) {
    ctx -> action(
      ctx.getData(prop1),
      ctx.getData(prop2)
    )
  }
}

public fun <T1, T2, T3> UiCompactTextField.onValueChange(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  action: (
    pT1: T1,
    pT2: T2,
    pT3: T3,
  ) -> Unit,
) {
  addEventBinding(CustomUIEventBindingType.ValueChanged, requireNotNull(this.id), prop1, prop2, prop3) {
    ctx -> action(
      ctx.getData(prop1),
      ctx.getData(prop2),
      ctx.getData(prop3)
    )
  }
}

public fun <T1, T2, T3, T4> UiCompactTextField.onValueChange(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  prop4: KProperty0<T4>,
  action: (
    pT1: T1,
    pT2: T2,
    pT3: T3,
    pT4: T4,
  ) -> Unit,
) {
  addEventBinding(CustomUIEventBindingType.ValueChanged, requireNotNull(this.id), prop1, prop2, prop3, prop4) {
    ctx -> action(
      ctx.getData(prop1),
      ctx.getData(prop2),
      ctx.getData(prop3),
      ctx.getData(prop4)
    )
  }
}

public fun <T1, T2, T3, T4, T5> UiCompactTextField.onValueChange(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  prop4: KProperty0<T4>,
  prop5: KProperty0<T5>,
  action: (
    pT1: T1,
    pT2: T2,
    pT3: T3,
    pT4: T4,
    pT5: T5,
  ) -> Unit,
) {
  addEventBinding(CustomUIEventBindingType.ValueChanged, requireNotNull(this.id), prop1, prop2, prop3, prop4, prop5) {
    ctx -> action(
      ctx.getData(prop1),
      ctx.getData(prop2),
      ctx.getData(prop3),
      ctx.getData(prop4),
      ctx.getData(prop5)
    )
  }
}

public fun <T1> UiCompactTextField.onValueChange(prop1: KProperty0<T1>, action: (`value`: String?, p1: T1) -> Unit) {
  addEventBinding(CustomUIEventBindingType.ValueChanged, requireNotNull(this.id), this::value, prop1) {
    ctx -> action(
      ctx.getData(this::value),
      ctx.getData(prop1)
    )
  }
}

public fun <T1, T2> UiCompactTextField.onValueChange(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  action: (
    `value`: String?,
    p1: T1,
    p2: T2,
  ) -> Unit,
) {
  addEventBinding(CustomUIEventBindingType.ValueChanged, requireNotNull(this.id), this::value, prop1, prop2) {
    ctx -> action(
      ctx.getData(this::value),
      ctx.getData(prop1),
      ctx.getData(prop2)
    )
  }
}

public fun <T1, T2, T3> UiCompactTextField.onValueChange(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  action: (
    `value`: String?,
    p1: T1,
    p2: T2,
    p3: T3,
  ) -> Unit,
) {
  addEventBinding(CustomUIEventBindingType.ValueChanged, requireNotNull(this.id), this::value, prop1, prop2, prop3) {
    ctx -> action(
      ctx.getData(this::value),
      ctx.getData(prop1),
      ctx.getData(prop2),
      ctx.getData(prop3)
    )
  }
}

public fun <T1, T2, T3, T4> UiCompactTextField.onValueChange(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  prop4: KProperty0<T4>,
  action: (
    `value`: String?,
    p1: T1,
    p2: T2,
    p3: T3,
    p4: T4,
  ) -> Unit,
) {
  addEventBinding(CustomUIEventBindingType.ValueChanged, requireNotNull(this.id), this::value, prop1, prop2, prop3, prop4) {
    ctx -> action(
      ctx.getData(this::value),
      ctx.getData(prop1),
      ctx.getData(prop2),
      ctx.getData(prop3),
      ctx.getData(prop4)
    )
  }
}

public fun <T1, T2, T3, T4, T5> UiCompactTextField.onValueChange(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  prop4: KProperty0<T4>,
  prop5: KProperty0<T5>,
  action: (
    `value`: String?,
    p1: T1,
    p2: T2,
    p3: T3,
    p4: T4,
    p5: T5,
  ) -> Unit,
) {
  addEventBinding(CustomUIEventBindingType.ValueChanged, requireNotNull(this.id), this::value, prop1, prop2, prop3, prop4, prop5) {
    ctx -> action(
      ctx.getData(this::value),
      ctx.getData(prop1),
      ctx.getData(prop2),
      ctx.getData(prop3),
      ctx.getData(prop4),
      ctx.getData(prop5)
    )
  }
}
