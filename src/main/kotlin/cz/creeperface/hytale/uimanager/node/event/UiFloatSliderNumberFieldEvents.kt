package cz.creeperface.hytale.uimanager.node.event

import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType
import cz.creeperface.hytale.uimanager.event.EventContext
import cz.creeperface.hytale.uimanager.node.UiFloatSliderNumberField
import cz.creeperface.hytale.uimanager.special.UiFormContext
import kotlin.Any
import kotlin.Double
import kotlin.Unit
import kotlin.reflect.KProperty0

public fun UiFloatSliderNumberField.onValueChange(vararg boundProperties: KProperty0<Any?>, action: (context: EventContext) -> Unit) {
  addEventBinding(CustomUIEventBindingType.ValueChanged, requireNotNull(this.id), *boundProperties, action = action)
}

public fun UiFloatSliderNumberField.onValueChange(action: (`value`: Double?) -> Unit) {
  addEventBinding(CustomUIEventBindingType.ValueChanged, requireNotNull(this.id), this::value) {
    ctx -> action(ctx.getData(this::value))
  }
}

public fun UiFloatSliderNumberField.submitOnValueChange(formContext: UiFormContext<*>) {
  formContext.form.bindSubmit(this, CustomUIEventBindingType.ValueChanged)
}

public fun <T1> UiFloatSliderNumberField.onValueChange(prop1: KProperty0<T1>, action: (pT1: T1) -> Unit) {
  addEventBinding(CustomUIEventBindingType.ValueChanged, requireNotNull(this.id), prop1) {
    ctx -> action(
      ctx.getData(prop1)
    )
  }
}

public fun <T1, T2> UiFloatSliderNumberField.onValueChange(
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

public fun <T1, T2, T3> UiFloatSliderNumberField.onValueChange(
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

public fun <T1, T2, T3, T4> UiFloatSliderNumberField.onValueChange(
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

public fun <T1, T2, T3, T4, T5> UiFloatSliderNumberField.onValueChange(
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

public fun <T1> UiFloatSliderNumberField.onValueChange(prop1: KProperty0<T1>, action: (`value`: Double?, p1: T1) -> Unit) {
  addEventBinding(CustomUIEventBindingType.ValueChanged, requireNotNull(this.id), this::value, prop1) {
    ctx -> action(
      ctx.getData(this::value),
      ctx.getData(prop1)
    )
  }
}

public fun <T1, T2> UiFloatSliderNumberField.onValueChange(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  action: (
    `value`: Double?,
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

public fun <T1, T2, T3> UiFloatSliderNumberField.onValueChange(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  action: (
    `value`: Double?,
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

public fun <T1, T2, T3, T4> UiFloatSliderNumberField.onValueChange(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  prop4: KProperty0<T4>,
  action: (
    `value`: Double?,
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

public fun <T1, T2, T3, T4, T5> UiFloatSliderNumberField.onValueChange(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  prop4: KProperty0<T4>,
  prop5: KProperty0<T5>,
  action: (
    `value`: Double?,
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

public fun UiFloatSliderNumberField.onMouseButtonRelease(vararg boundProperties: KProperty0<Any?>, action: (context: EventContext) -> Unit) {
  addEventBinding(CustomUIEventBindingType.MouseButtonReleased, requireNotNull(this.id), *boundProperties, action = action)
}

public fun UiFloatSliderNumberField.onMouseButtonRelease(action: (`value`: Double?) -> Unit) {
  addEventBinding(CustomUIEventBindingType.MouseButtonReleased, requireNotNull(this.id), this::value) {
    ctx -> action(ctx.getData(this::value))
  }
}

public fun UiFloatSliderNumberField.submitOnMouseButtonRelease(formContext: UiFormContext<*>) {
  formContext.form.bindSubmit(this, CustomUIEventBindingType.MouseButtonReleased)
}

public fun <T1> UiFloatSliderNumberField.onMouseButtonRelease(prop1: KProperty0<T1>, action: (pT1: T1) -> Unit) {
  addEventBinding(CustomUIEventBindingType.MouseButtonReleased, requireNotNull(this.id), prop1) {
    ctx -> action(
      ctx.getData(prop1)
    )
  }
}

public fun <T1, T2> UiFloatSliderNumberField.onMouseButtonRelease(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  action: (pT1: T1, pT2: T2) -> Unit,
) {
  addEventBinding(CustomUIEventBindingType.MouseButtonReleased, requireNotNull(this.id), prop1, prop2) {
    ctx -> action(
      ctx.getData(prop1),
      ctx.getData(prop2)
    )
  }
}

public fun <T1, T2, T3> UiFloatSliderNumberField.onMouseButtonRelease(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  action: (
    pT1: T1,
    pT2: T2,
    pT3: T3,
  ) -> Unit,
) {
  addEventBinding(CustomUIEventBindingType.MouseButtonReleased, requireNotNull(this.id), prop1, prop2, prop3) {
    ctx -> action(
      ctx.getData(prop1),
      ctx.getData(prop2),
      ctx.getData(prop3)
    )
  }
}

public fun <T1, T2, T3, T4> UiFloatSliderNumberField.onMouseButtonRelease(
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
  addEventBinding(CustomUIEventBindingType.MouseButtonReleased, requireNotNull(this.id), prop1, prop2, prop3, prop4) {
    ctx -> action(
      ctx.getData(prop1),
      ctx.getData(prop2),
      ctx.getData(prop3),
      ctx.getData(prop4)
    )
  }
}

public fun <T1, T2, T3, T4, T5> UiFloatSliderNumberField.onMouseButtonRelease(
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
  addEventBinding(CustomUIEventBindingType.MouseButtonReleased, requireNotNull(this.id), prop1, prop2, prop3, prop4, prop5) {
    ctx -> action(
      ctx.getData(prop1),
      ctx.getData(prop2),
      ctx.getData(prop3),
      ctx.getData(prop4),
      ctx.getData(prop5)
    )
  }
}

public fun <T1> UiFloatSliderNumberField.onMouseButtonRelease(prop1: KProperty0<T1>, action: (`value`: Double?, p1: T1) -> Unit) {
  addEventBinding(CustomUIEventBindingType.MouseButtonReleased, requireNotNull(this.id), this::value, prop1) {
    ctx -> action(
      ctx.getData(this::value),
      ctx.getData(prop1)
    )
  }
}

public fun <T1, T2> UiFloatSliderNumberField.onMouseButtonRelease(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  action: (
    `value`: Double?,
    p1: T1,
    p2: T2,
  ) -> Unit,
) {
  addEventBinding(CustomUIEventBindingType.MouseButtonReleased, requireNotNull(this.id), this::value, prop1, prop2) {
    ctx -> action(
      ctx.getData(this::value),
      ctx.getData(prop1),
      ctx.getData(prop2)
    )
  }
}

public fun <T1, T2, T3> UiFloatSliderNumberField.onMouseButtonRelease(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  action: (
    `value`: Double?,
    p1: T1,
    p2: T2,
    p3: T3,
  ) -> Unit,
) {
  addEventBinding(CustomUIEventBindingType.MouseButtonReleased, requireNotNull(this.id), this::value, prop1, prop2, prop3) {
    ctx -> action(
      ctx.getData(this::value),
      ctx.getData(prop1),
      ctx.getData(prop2),
      ctx.getData(prop3)
    )
  }
}

public fun <T1, T2, T3, T4> UiFloatSliderNumberField.onMouseButtonRelease(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  prop4: KProperty0<T4>,
  action: (
    `value`: Double?,
    p1: T1,
    p2: T2,
    p3: T3,
    p4: T4,
  ) -> Unit,
) {
  addEventBinding(CustomUIEventBindingType.MouseButtonReleased, requireNotNull(this.id), this::value, prop1, prop2, prop3, prop4) {
    ctx -> action(
      ctx.getData(this::value),
      ctx.getData(prop1),
      ctx.getData(prop2),
      ctx.getData(prop3),
      ctx.getData(prop4)
    )
  }
}

public fun <T1, T2, T3, T4, T5> UiFloatSliderNumberField.onMouseButtonRelease(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  prop4: KProperty0<T4>,
  prop5: KProperty0<T5>,
  action: (
    `value`: Double?,
    p1: T1,
    p2: T2,
    p3: T3,
    p4: T4,
    p5: T5,
  ) -> Unit,
) {
  addEventBinding(CustomUIEventBindingType.MouseButtonReleased, requireNotNull(this.id), this::value, prop1, prop2, prop3, prop4, prop5) {
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

public fun UiFloatSliderNumberField.onValidate(vararg boundProperties: KProperty0<Any?>, action: (context: EventContext) -> Unit) {
  addEventBinding(CustomUIEventBindingType.Validating, requireNotNull(this.id), *boundProperties, action = action)
}

public fun UiFloatSliderNumberField.onValidate(action: (`value`: Double?) -> Unit) {
  addEventBinding(CustomUIEventBindingType.Validating, requireNotNull(this.id), this::value) {
    ctx -> action(ctx.getData(this::value))
  }
}

public fun UiFloatSliderNumberField.submitOnValidate(formContext: UiFormContext<*>) {
  formContext.form.bindSubmit(this, CustomUIEventBindingType.Validating)
}

public fun <T1> UiFloatSliderNumberField.onValidate(prop1: KProperty0<T1>, action: (pT1: T1) -> Unit) {
  addEventBinding(CustomUIEventBindingType.Validating, requireNotNull(this.id), prop1) {
    ctx -> action(
      ctx.getData(prop1)
    )
  }
}

public fun <T1, T2> UiFloatSliderNumberField.onValidate(
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

public fun <T1, T2, T3> UiFloatSliderNumberField.onValidate(
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

public fun <T1, T2, T3, T4> UiFloatSliderNumberField.onValidate(
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

public fun <T1, T2, T3, T4, T5> UiFloatSliderNumberField.onValidate(
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

public fun <T1> UiFloatSliderNumberField.onValidate(prop1: KProperty0<T1>, action: (`value`: Double?, p1: T1) -> Unit) {
  addEventBinding(CustomUIEventBindingType.Validating, requireNotNull(this.id), this::value, prop1) {
    ctx -> action(
      ctx.getData(this::value),
      ctx.getData(prop1)
    )
  }
}

public fun <T1, T2> UiFloatSliderNumberField.onValidate(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  action: (
    `value`: Double?,
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

public fun <T1, T2, T3> UiFloatSliderNumberField.onValidate(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  action: (
    `value`: Double?,
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

public fun <T1, T2, T3, T4> UiFloatSliderNumberField.onValidate(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  prop4: KProperty0<T4>,
  action: (
    `value`: Double?,
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

public fun <T1, T2, T3, T4, T5> UiFloatSliderNumberField.onValidate(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  prop4: KProperty0<T4>,
  prop5: KProperty0<T5>,
  action: (
    `value`: Double?,
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

public fun UiFloatSliderNumberField.onDismiss(vararg boundProperties: KProperty0<Any?>, action: (context: EventContext) -> Unit) {
  addEventBinding(CustomUIEventBindingType.Dismissing, requireNotNull(this.id), *boundProperties, action = action)
}

public fun UiFloatSliderNumberField.onDismiss(action: (`value`: Double?) -> Unit) {
  addEventBinding(CustomUIEventBindingType.Dismissing, requireNotNull(this.id), this::value) {
    ctx -> action(ctx.getData(this::value))
  }
}

public fun UiFloatSliderNumberField.submitOnDismiss(formContext: UiFormContext<*>) {
  formContext.form.bindSubmit(this, CustomUIEventBindingType.Dismissing)
}

public fun <T1> UiFloatSliderNumberField.onDismiss(prop1: KProperty0<T1>, action: (pT1: T1) -> Unit) {
  addEventBinding(CustomUIEventBindingType.Dismissing, requireNotNull(this.id), prop1) {
    ctx -> action(
      ctx.getData(prop1)
    )
  }
}

public fun <T1, T2> UiFloatSliderNumberField.onDismiss(
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

public fun <T1, T2, T3> UiFloatSliderNumberField.onDismiss(
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

public fun <T1, T2, T3, T4> UiFloatSliderNumberField.onDismiss(
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

public fun <T1, T2, T3, T4, T5> UiFloatSliderNumberField.onDismiss(
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

public fun <T1> UiFloatSliderNumberField.onDismiss(prop1: KProperty0<T1>, action: (`value`: Double?, p1: T1) -> Unit) {
  addEventBinding(CustomUIEventBindingType.Dismissing, requireNotNull(this.id), this::value, prop1) {
    ctx -> action(
      ctx.getData(this::value),
      ctx.getData(prop1)
    )
  }
}

public fun <T1, T2> UiFloatSliderNumberField.onDismiss(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  action: (
    `value`: Double?,
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

public fun <T1, T2, T3> UiFloatSliderNumberField.onDismiss(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  action: (
    `value`: Double?,
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

public fun <T1, T2, T3, T4> UiFloatSliderNumberField.onDismiss(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  prop4: KProperty0<T4>,
  action: (
    `value`: Double?,
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

public fun <T1, T2, T3, T4, T5> UiFloatSliderNumberField.onDismiss(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  prop4: KProperty0<T4>,
  prop5: KProperty0<T5>,
  action: (
    `value`: Double?,
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

public fun UiFloatSliderNumberField.onFocusLost(vararg boundProperties: KProperty0<Any?>, action: (context: EventContext) -> Unit) {
  addEventBinding(CustomUIEventBindingType.FocusLost, requireNotNull(this.id), *boundProperties, action = action)
}

public fun UiFloatSliderNumberField.onFocusLost(action: (`value`: Double?) -> Unit) {
  addEventBinding(CustomUIEventBindingType.FocusLost, requireNotNull(this.id), this::value) {
    ctx -> action(ctx.getData(this::value))
  }
}

public fun UiFloatSliderNumberField.submitOnFocusLost(formContext: UiFormContext<*>) {
  formContext.form.bindSubmit(this, CustomUIEventBindingType.FocusLost)
}

public fun <T1> UiFloatSliderNumberField.onFocusLost(prop1: KProperty0<T1>, action: (pT1: T1) -> Unit) {
  addEventBinding(CustomUIEventBindingType.FocusLost, requireNotNull(this.id), prop1) {
    ctx -> action(
      ctx.getData(prop1)
    )
  }
}

public fun <T1, T2> UiFloatSliderNumberField.onFocusLost(
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

public fun <T1, T2, T3> UiFloatSliderNumberField.onFocusLost(
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

public fun <T1, T2, T3, T4> UiFloatSliderNumberField.onFocusLost(
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

public fun <T1, T2, T3, T4, T5> UiFloatSliderNumberField.onFocusLost(
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

public fun <T1> UiFloatSliderNumberField.onFocusLost(prop1: KProperty0<T1>, action: (`value`: Double?, p1: T1) -> Unit) {
  addEventBinding(CustomUIEventBindingType.FocusLost, requireNotNull(this.id), this::value, prop1) {
    ctx -> action(
      ctx.getData(this::value),
      ctx.getData(prop1)
    )
  }
}

public fun <T1, T2> UiFloatSliderNumberField.onFocusLost(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  action: (
    `value`: Double?,
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

public fun <T1, T2, T3> UiFloatSliderNumberField.onFocusLost(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  action: (
    `value`: Double?,
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

public fun <T1, T2, T3, T4> UiFloatSliderNumberField.onFocusLost(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  prop4: KProperty0<T4>,
  action: (
    `value`: Double?,
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

public fun <T1, T2, T3, T4, T5> UiFloatSliderNumberField.onFocusLost(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  prop4: KProperty0<T4>,
  prop5: KProperty0<T5>,
  action: (
    `value`: Double?,
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

public fun UiFloatSliderNumberField.onFocusGain(vararg boundProperties: KProperty0<Any?>, action: (context: EventContext) -> Unit) {
  addEventBinding(CustomUIEventBindingType.FocusGained, requireNotNull(this.id), *boundProperties, action = action)
}

public fun UiFloatSliderNumberField.onFocusGain(action: (`value`: Double?) -> Unit) {
  addEventBinding(CustomUIEventBindingType.FocusGained, requireNotNull(this.id), this::value) {
    ctx -> action(ctx.getData(this::value))
  }
}

public fun UiFloatSliderNumberField.submitOnFocusGain(formContext: UiFormContext<*>) {
  formContext.form.bindSubmit(this, CustomUIEventBindingType.FocusGained)
}

public fun <T1> UiFloatSliderNumberField.onFocusGain(prop1: KProperty0<T1>, action: (pT1: T1) -> Unit) {
  addEventBinding(CustomUIEventBindingType.FocusGained, requireNotNull(this.id), prop1) {
    ctx -> action(
      ctx.getData(prop1)
    )
  }
}

public fun <T1, T2> UiFloatSliderNumberField.onFocusGain(
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

public fun <T1, T2, T3> UiFloatSliderNumberField.onFocusGain(
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

public fun <T1, T2, T3, T4> UiFloatSliderNumberField.onFocusGain(
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

public fun <T1, T2, T3, T4, T5> UiFloatSliderNumberField.onFocusGain(
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

public fun <T1> UiFloatSliderNumberField.onFocusGain(prop1: KProperty0<T1>, action: (`value`: Double?, p1: T1) -> Unit) {
  addEventBinding(CustomUIEventBindingType.FocusGained, requireNotNull(this.id), this::value, prop1) {
    ctx -> action(
      ctx.getData(this::value),
      ctx.getData(prop1)
    )
  }
}

public fun <T1, T2> UiFloatSliderNumberField.onFocusGain(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  action: (
    `value`: Double?,
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

public fun <T1, T2, T3> UiFloatSliderNumberField.onFocusGain(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  action: (
    `value`: Double?,
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

public fun <T1, T2, T3, T4> UiFloatSliderNumberField.onFocusGain(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  prop4: KProperty0<T4>,
  action: (
    `value`: Double?,
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

public fun <T1, T2, T3, T4, T5> UiFloatSliderNumberField.onFocusGain(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  prop4: KProperty0<T4>,
  prop5: KProperty0<T5>,
  action: (
    `value`: Double?,
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
