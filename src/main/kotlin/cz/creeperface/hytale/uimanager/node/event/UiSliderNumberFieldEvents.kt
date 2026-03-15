package cz.creeperface.hytale.uimanager.node.event

import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType
import cz.creeperface.hytale.uimanager.event.EventContext
import cz.creeperface.hytale.uimanager.node.UiSliderNumberField
import cz.creeperface.hytale.uimanager.special.UiFormContext
import kotlin.Any
import kotlin.Int
import kotlin.Unit
import kotlin.reflect.KProperty0

public fun UiSliderNumberField.onValueChange(vararg boundProperties: KProperty0<Any?>, action: (context: EventContext) -> Unit) {
  addEventBinding(CustomUIEventBindingType.ValueChanged, requireNotNull(this.id), *boundProperties, action = action)
}

public fun UiSliderNumberField.onValueChange(action: (`value`: Int?) -> Unit) {
  addEventBinding(CustomUIEventBindingType.ValueChanged, requireNotNull(this.id), this::value) {
    ctx -> action(ctx.getData(this::value))
  }
}

public fun UiSliderNumberField.submitOnValueChange(formContext: UiFormContext<*>) {
  formContext.form.bindSubmit(this, CustomUIEventBindingType.ValueChanged)
}

public fun <T1> UiSliderNumberField.onValueChange(prop1: KProperty0<T1>, action: (pT1: T1) -> Unit) {
  addEventBinding(CustomUIEventBindingType.ValueChanged, requireNotNull(this.id), prop1) {
    ctx -> action(
      ctx.getData(prop1)
    )
  }
}

public fun <T1, T2> UiSliderNumberField.onValueChange(
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

public fun <T1, T2, T3> UiSliderNumberField.onValueChange(
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

public fun <T1, T2, T3, T4> UiSliderNumberField.onValueChange(
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

public fun <T1, T2, T3, T4, T5> UiSliderNumberField.onValueChange(
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

public fun <T1> UiSliderNumberField.onValueChange(prop1: KProperty0<T1>, action: (`value`: Int?, p1: T1) -> Unit) {
  addEventBinding(CustomUIEventBindingType.ValueChanged, requireNotNull(this.id), this::value, prop1) {
    ctx -> action(
      ctx.getData(this::value),
      ctx.getData(prop1)
    )
  }
}

public fun <T1, T2> UiSliderNumberField.onValueChange(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  action: (
    `value`: Int?,
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

public fun <T1, T2, T3> UiSliderNumberField.onValueChange(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  action: (
    `value`: Int?,
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

public fun <T1, T2, T3, T4> UiSliderNumberField.onValueChange(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  prop4: KProperty0<T4>,
  action: (
    `value`: Int?,
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

public fun <T1, T2, T3, T4, T5> UiSliderNumberField.onValueChange(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  prop4: KProperty0<T4>,
  prop5: KProperty0<T5>,
  action: (
    `value`: Int?,
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

public fun UiSliderNumberField.onMouseButtonRelease(vararg boundProperties: KProperty0<Any?>, action: (context: EventContext) -> Unit) {
  addEventBinding(CustomUIEventBindingType.MouseButtonReleased, requireNotNull(this.id), *boundProperties, action = action)
}

public fun UiSliderNumberField.onMouseButtonRelease(action: (`value`: Int?) -> Unit) {
  addEventBinding(CustomUIEventBindingType.MouseButtonReleased, requireNotNull(this.id), this::value) {
    ctx -> action(ctx.getData(this::value))
  }
}

public fun UiSliderNumberField.submitOnMouseButtonRelease(formContext: UiFormContext<*>) {
  formContext.form.bindSubmit(this, CustomUIEventBindingType.MouseButtonReleased)
}

public fun <T1> UiSliderNumberField.onMouseButtonRelease(prop1: KProperty0<T1>, action: (pT1: T1) -> Unit) {
  addEventBinding(CustomUIEventBindingType.MouseButtonReleased, requireNotNull(this.id), prop1) {
    ctx -> action(
      ctx.getData(prop1)
    )
  }
}

public fun <T1, T2> UiSliderNumberField.onMouseButtonRelease(
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

public fun <T1, T2, T3> UiSliderNumberField.onMouseButtonRelease(
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

public fun <T1, T2, T3, T4> UiSliderNumberField.onMouseButtonRelease(
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

public fun <T1, T2, T3, T4, T5> UiSliderNumberField.onMouseButtonRelease(
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

public fun <T1> UiSliderNumberField.onMouseButtonRelease(prop1: KProperty0<T1>, action: (`value`: Int?, p1: T1) -> Unit) {
  addEventBinding(CustomUIEventBindingType.MouseButtonReleased, requireNotNull(this.id), this::value, prop1) {
    ctx -> action(
      ctx.getData(this::value),
      ctx.getData(prop1)
    )
  }
}

public fun <T1, T2> UiSliderNumberField.onMouseButtonRelease(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  action: (
    `value`: Int?,
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

public fun <T1, T2, T3> UiSliderNumberField.onMouseButtonRelease(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  action: (
    `value`: Int?,
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

public fun <T1, T2, T3, T4> UiSliderNumberField.onMouseButtonRelease(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  prop4: KProperty0<T4>,
  action: (
    `value`: Int?,
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

public fun <T1, T2, T3, T4, T5> UiSliderNumberField.onMouseButtonRelease(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  prop4: KProperty0<T4>,
  prop5: KProperty0<T5>,
  action: (
    `value`: Int?,
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

public fun UiSliderNumberField.onValidate(vararg boundProperties: KProperty0<Any?>, action: (context: EventContext) -> Unit) {
  addEventBinding(CustomUIEventBindingType.Validating, requireNotNull(this.id), *boundProperties, action = action)
}

public fun UiSliderNumberField.onValidate(action: (`value`: Int?) -> Unit) {
  addEventBinding(CustomUIEventBindingType.Validating, requireNotNull(this.id), this::value) {
    ctx -> action(ctx.getData(this::value))
  }
}

public fun UiSliderNumberField.submitOnValidate(formContext: UiFormContext<*>) {
  formContext.form.bindSubmit(this, CustomUIEventBindingType.Validating)
}

public fun <T1> UiSliderNumberField.onValidate(prop1: KProperty0<T1>, action: (pT1: T1) -> Unit) {
  addEventBinding(CustomUIEventBindingType.Validating, requireNotNull(this.id), prop1) {
    ctx -> action(
      ctx.getData(prop1)
    )
  }
}

public fun <T1, T2> UiSliderNumberField.onValidate(
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

public fun <T1, T2, T3> UiSliderNumberField.onValidate(
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

public fun <T1, T2, T3, T4> UiSliderNumberField.onValidate(
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

public fun <T1, T2, T3, T4, T5> UiSliderNumberField.onValidate(
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

public fun <T1> UiSliderNumberField.onValidate(prop1: KProperty0<T1>, action: (`value`: Int?, p1: T1) -> Unit) {
  addEventBinding(CustomUIEventBindingType.Validating, requireNotNull(this.id), this::value, prop1) {
    ctx -> action(
      ctx.getData(this::value),
      ctx.getData(prop1)
    )
  }
}

public fun <T1, T2> UiSliderNumberField.onValidate(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  action: (
    `value`: Int?,
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

public fun <T1, T2, T3> UiSliderNumberField.onValidate(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  action: (
    `value`: Int?,
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

public fun <T1, T2, T3, T4> UiSliderNumberField.onValidate(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  prop4: KProperty0<T4>,
  action: (
    `value`: Int?,
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

public fun <T1, T2, T3, T4, T5> UiSliderNumberField.onValidate(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  prop4: KProperty0<T4>,
  prop5: KProperty0<T5>,
  action: (
    `value`: Int?,
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

public fun UiSliderNumberField.onDismiss(vararg boundProperties: KProperty0<Any?>, action: (context: EventContext) -> Unit) {
  addEventBinding(CustomUIEventBindingType.Dismissing, requireNotNull(this.id), *boundProperties, action = action)
}

public fun UiSliderNumberField.onDismiss(action: (`value`: Int?) -> Unit) {
  addEventBinding(CustomUIEventBindingType.Dismissing, requireNotNull(this.id), this::value) {
    ctx -> action(ctx.getData(this::value))
  }
}

public fun UiSliderNumberField.submitOnDismiss(formContext: UiFormContext<*>) {
  formContext.form.bindSubmit(this, CustomUIEventBindingType.Dismissing)
}

public fun <T1> UiSliderNumberField.onDismiss(prop1: KProperty0<T1>, action: (pT1: T1) -> Unit) {
  addEventBinding(CustomUIEventBindingType.Dismissing, requireNotNull(this.id), prop1) {
    ctx -> action(
      ctx.getData(prop1)
    )
  }
}

public fun <T1, T2> UiSliderNumberField.onDismiss(
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

public fun <T1, T2, T3> UiSliderNumberField.onDismiss(
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

public fun <T1, T2, T3, T4> UiSliderNumberField.onDismiss(
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

public fun <T1, T2, T3, T4, T5> UiSliderNumberField.onDismiss(
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

public fun <T1> UiSliderNumberField.onDismiss(prop1: KProperty0<T1>, action: (`value`: Int?, p1: T1) -> Unit) {
  addEventBinding(CustomUIEventBindingType.Dismissing, requireNotNull(this.id), this::value, prop1) {
    ctx -> action(
      ctx.getData(this::value),
      ctx.getData(prop1)
    )
  }
}

public fun <T1, T2> UiSliderNumberField.onDismiss(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  action: (
    `value`: Int?,
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

public fun <T1, T2, T3> UiSliderNumberField.onDismiss(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  action: (
    `value`: Int?,
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

public fun <T1, T2, T3, T4> UiSliderNumberField.onDismiss(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  prop4: KProperty0<T4>,
  action: (
    `value`: Int?,
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

public fun <T1, T2, T3, T4, T5> UiSliderNumberField.onDismiss(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  prop4: KProperty0<T4>,
  prop5: KProperty0<T5>,
  action: (
    `value`: Int?,
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

public fun UiSliderNumberField.onFocusLost(vararg boundProperties: KProperty0<Any?>, action: (context: EventContext) -> Unit) {
  addEventBinding(CustomUIEventBindingType.FocusLost, requireNotNull(this.id), *boundProperties, action = action)
}

public fun UiSliderNumberField.onFocusLost(action: (`value`: Int?) -> Unit) {
  addEventBinding(CustomUIEventBindingType.FocusLost, requireNotNull(this.id), this::value) {
    ctx -> action(ctx.getData(this::value))
  }
}

public fun UiSliderNumberField.submitOnFocusLost(formContext: UiFormContext<*>) {
  formContext.form.bindSubmit(this, CustomUIEventBindingType.FocusLost)
}

public fun <T1> UiSliderNumberField.onFocusLost(prop1: KProperty0<T1>, action: (pT1: T1) -> Unit) {
  addEventBinding(CustomUIEventBindingType.FocusLost, requireNotNull(this.id), prop1) {
    ctx -> action(
      ctx.getData(prop1)
    )
  }
}

public fun <T1, T2> UiSliderNumberField.onFocusLost(
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

public fun <T1, T2, T3> UiSliderNumberField.onFocusLost(
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

public fun <T1, T2, T3, T4> UiSliderNumberField.onFocusLost(
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

public fun <T1, T2, T3, T4, T5> UiSliderNumberField.onFocusLost(
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

public fun <T1> UiSliderNumberField.onFocusLost(prop1: KProperty0<T1>, action: (`value`: Int?, p1: T1) -> Unit) {
  addEventBinding(CustomUIEventBindingType.FocusLost, requireNotNull(this.id), this::value, prop1) {
    ctx -> action(
      ctx.getData(this::value),
      ctx.getData(prop1)
    )
  }
}

public fun <T1, T2> UiSliderNumberField.onFocusLost(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  action: (
    `value`: Int?,
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

public fun <T1, T2, T3> UiSliderNumberField.onFocusLost(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  action: (
    `value`: Int?,
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

public fun <T1, T2, T3, T4> UiSliderNumberField.onFocusLost(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  prop4: KProperty0<T4>,
  action: (
    `value`: Int?,
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

public fun <T1, T2, T3, T4, T5> UiSliderNumberField.onFocusLost(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  prop4: KProperty0<T4>,
  prop5: KProperty0<T5>,
  action: (
    `value`: Int?,
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

public fun UiSliderNumberField.onFocusGain(vararg boundProperties: KProperty0<Any?>, action: (context: EventContext) -> Unit) {
  addEventBinding(CustomUIEventBindingType.FocusGained, requireNotNull(this.id), *boundProperties, action = action)
}

public fun UiSliderNumberField.onFocusGain(action: (`value`: Int?) -> Unit) {
  addEventBinding(CustomUIEventBindingType.FocusGained, requireNotNull(this.id), this::value) {
    ctx -> action(ctx.getData(this::value))
  }
}

public fun UiSliderNumberField.submitOnFocusGain(formContext: UiFormContext<*>) {
  formContext.form.bindSubmit(this, CustomUIEventBindingType.FocusGained)
}

public fun <T1> UiSliderNumberField.onFocusGain(prop1: KProperty0<T1>, action: (pT1: T1) -> Unit) {
  addEventBinding(CustomUIEventBindingType.FocusGained, requireNotNull(this.id), prop1) {
    ctx -> action(
      ctx.getData(prop1)
    )
  }
}

public fun <T1, T2> UiSliderNumberField.onFocusGain(
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

public fun <T1, T2, T3> UiSliderNumberField.onFocusGain(
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

public fun <T1, T2, T3, T4> UiSliderNumberField.onFocusGain(
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

public fun <T1, T2, T3, T4, T5> UiSliderNumberField.onFocusGain(
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

public fun <T1> UiSliderNumberField.onFocusGain(prop1: KProperty0<T1>, action: (`value`: Int?, p1: T1) -> Unit) {
  addEventBinding(CustomUIEventBindingType.FocusGained, requireNotNull(this.id), this::value, prop1) {
    ctx -> action(
      ctx.getData(this::value),
      ctx.getData(prop1)
    )
  }
}

public fun <T1, T2> UiSliderNumberField.onFocusGain(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  action: (
    `value`: Int?,
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

public fun <T1, T2, T3> UiSliderNumberField.onFocusGain(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  action: (
    `value`: Int?,
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

public fun <T1, T2, T3, T4> UiSliderNumberField.onFocusGain(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  prop4: KProperty0<T4>,
  action: (
    `value`: Int?,
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

public fun <T1, T2, T3, T4, T5> UiSliderNumberField.onFocusGain(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  prop4: KProperty0<T4>,
  prop5: KProperty0<T5>,
  action: (
    `value`: Int?,
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
