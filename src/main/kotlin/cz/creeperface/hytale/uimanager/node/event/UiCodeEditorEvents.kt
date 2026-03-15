package cz.creeperface.hytale.uimanager.node.event

import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType
import cz.creeperface.hytale.uimanager.event.EventContext
import cz.creeperface.hytale.uimanager.node.UiCodeEditor
import cz.creeperface.hytale.uimanager.special.UiFormContext
import kotlin.Any
import kotlin.String
import kotlin.Unit
import kotlin.reflect.KProperty0

public fun UiCodeEditor.onValidate(vararg boundProperties: KProperty0<Any?>, action: (context: EventContext) -> Unit) {
  addEventBinding(CustomUIEventBindingType.Validating, requireNotNull(this.id), *boundProperties, action = action)
}

public fun UiCodeEditor.onValidate(action: (`value`: String?) -> Unit) {
  addEventBinding(CustomUIEventBindingType.Validating, requireNotNull(this.id), this::value) {
    ctx -> action(ctx.getData(this::value))
  }
}

public fun UiCodeEditor.submitOnValidate(formContext: UiFormContext<*>) {
  formContext.form.bindSubmit(this, CustomUIEventBindingType.Validating)
}

public fun <T1> UiCodeEditor.onValidate(prop1: KProperty0<T1>, action: (pT1: T1) -> Unit) {
  addEventBinding(CustomUIEventBindingType.Validating, requireNotNull(this.id), prop1) {
    ctx -> action(
      ctx.getData(prop1)
    )
  }
}

public fun <T1, T2> UiCodeEditor.onValidate(
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

public fun <T1, T2, T3> UiCodeEditor.onValidate(
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

public fun <T1, T2, T3, T4> UiCodeEditor.onValidate(
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

public fun <T1, T2, T3, T4, T5> UiCodeEditor.onValidate(
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

public fun <T1> UiCodeEditor.onValidate(prop1: KProperty0<T1>, action: (`value`: String?, p1: T1) -> Unit) {
  addEventBinding(CustomUIEventBindingType.Validating, requireNotNull(this.id), this::value, prop1) {
    ctx -> action(
      ctx.getData(this::value),
      ctx.getData(prop1)
    )
  }
}

public fun <T1, T2> UiCodeEditor.onValidate(
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

public fun <T1, T2, T3> UiCodeEditor.onValidate(
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

public fun <T1, T2, T3, T4> UiCodeEditor.onValidate(
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

public fun <T1, T2, T3, T4, T5> UiCodeEditor.onValidate(
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

public fun UiCodeEditor.onDismiss(vararg boundProperties: KProperty0<Any?>, action: (context: EventContext) -> Unit) {
  addEventBinding(CustomUIEventBindingType.Dismissing, requireNotNull(this.id), *boundProperties, action = action)
}

public fun UiCodeEditor.onDismiss(action: (`value`: String?) -> Unit) {
  addEventBinding(CustomUIEventBindingType.Dismissing, requireNotNull(this.id), this::value) {
    ctx -> action(ctx.getData(this::value))
  }
}

public fun UiCodeEditor.submitOnDismiss(formContext: UiFormContext<*>) {
  formContext.form.bindSubmit(this, CustomUIEventBindingType.Dismissing)
}

public fun <T1> UiCodeEditor.onDismiss(prop1: KProperty0<T1>, action: (pT1: T1) -> Unit) {
  addEventBinding(CustomUIEventBindingType.Dismissing, requireNotNull(this.id), prop1) {
    ctx -> action(
      ctx.getData(prop1)
    )
  }
}

public fun <T1, T2> UiCodeEditor.onDismiss(
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

public fun <T1, T2, T3> UiCodeEditor.onDismiss(
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

public fun <T1, T2, T3, T4> UiCodeEditor.onDismiss(
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

public fun <T1, T2, T3, T4, T5> UiCodeEditor.onDismiss(
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

public fun <T1> UiCodeEditor.onDismiss(prop1: KProperty0<T1>, action: (`value`: String?, p1: T1) -> Unit) {
  addEventBinding(CustomUIEventBindingType.Dismissing, requireNotNull(this.id), this::value, prop1) {
    ctx -> action(
      ctx.getData(this::value),
      ctx.getData(prop1)
    )
  }
}

public fun <T1, T2> UiCodeEditor.onDismiss(
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

public fun <T1, T2, T3> UiCodeEditor.onDismiss(
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

public fun <T1, T2, T3, T4> UiCodeEditor.onDismiss(
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

public fun <T1, T2, T3, T4, T5> UiCodeEditor.onDismiss(
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

public fun UiCodeEditor.onFocusLost(vararg boundProperties: KProperty0<Any?>, action: (context: EventContext) -> Unit) {
  addEventBinding(CustomUIEventBindingType.FocusLost, requireNotNull(this.id), *boundProperties, action = action)
}

public fun UiCodeEditor.onFocusLost(action: (`value`: String?) -> Unit) {
  addEventBinding(CustomUIEventBindingType.FocusLost, requireNotNull(this.id), this::value) {
    ctx -> action(ctx.getData(this::value))
  }
}

public fun UiCodeEditor.submitOnFocusLost(formContext: UiFormContext<*>) {
  formContext.form.bindSubmit(this, CustomUIEventBindingType.FocusLost)
}

public fun <T1> UiCodeEditor.onFocusLost(prop1: KProperty0<T1>, action: (pT1: T1) -> Unit) {
  addEventBinding(CustomUIEventBindingType.FocusLost, requireNotNull(this.id), prop1) {
    ctx -> action(
      ctx.getData(prop1)
    )
  }
}

public fun <T1, T2> UiCodeEditor.onFocusLost(
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

public fun <T1, T2, T3> UiCodeEditor.onFocusLost(
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

public fun <T1, T2, T3, T4> UiCodeEditor.onFocusLost(
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

public fun <T1, T2, T3, T4, T5> UiCodeEditor.onFocusLost(
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

public fun <T1> UiCodeEditor.onFocusLost(prop1: KProperty0<T1>, action: (`value`: String?, p1: T1) -> Unit) {
  addEventBinding(CustomUIEventBindingType.FocusLost, requireNotNull(this.id), this::value, prop1) {
    ctx -> action(
      ctx.getData(this::value),
      ctx.getData(prop1)
    )
  }
}

public fun <T1, T2> UiCodeEditor.onFocusLost(
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

public fun <T1, T2, T3> UiCodeEditor.onFocusLost(
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

public fun <T1, T2, T3, T4> UiCodeEditor.onFocusLost(
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

public fun <T1, T2, T3, T4, T5> UiCodeEditor.onFocusLost(
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

public fun UiCodeEditor.onFocusGain(vararg boundProperties: KProperty0<Any?>, action: (context: EventContext) -> Unit) {
  addEventBinding(CustomUIEventBindingType.FocusGained, requireNotNull(this.id), *boundProperties, action = action)
}

public fun UiCodeEditor.onFocusGain(action: (`value`: String?) -> Unit) {
  addEventBinding(CustomUIEventBindingType.FocusGained, requireNotNull(this.id), this::value) {
    ctx -> action(ctx.getData(this::value))
  }
}

public fun UiCodeEditor.submitOnFocusGain(formContext: UiFormContext<*>) {
  formContext.form.bindSubmit(this, CustomUIEventBindingType.FocusGained)
}

public fun <T1> UiCodeEditor.onFocusGain(prop1: KProperty0<T1>, action: (pT1: T1) -> Unit) {
  addEventBinding(CustomUIEventBindingType.FocusGained, requireNotNull(this.id), prop1) {
    ctx -> action(
      ctx.getData(prop1)
    )
  }
}

public fun <T1, T2> UiCodeEditor.onFocusGain(
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

public fun <T1, T2, T3> UiCodeEditor.onFocusGain(
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

public fun <T1, T2, T3, T4> UiCodeEditor.onFocusGain(
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

public fun <T1, T2, T3, T4, T5> UiCodeEditor.onFocusGain(
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

public fun <T1> UiCodeEditor.onFocusGain(prop1: KProperty0<T1>, action: (`value`: String?, p1: T1) -> Unit) {
  addEventBinding(CustomUIEventBindingType.FocusGained, requireNotNull(this.id), this::value, prop1) {
    ctx -> action(
      ctx.getData(this::value),
      ctx.getData(prop1)
    )
  }
}

public fun <T1, T2> UiCodeEditor.onFocusGain(
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

public fun <T1, T2, T3> UiCodeEditor.onFocusGain(
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

public fun <T1, T2, T3, T4> UiCodeEditor.onFocusGain(
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

public fun <T1, T2, T3, T4, T5> UiCodeEditor.onFocusGain(
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

public fun UiCodeEditor.onValueChange(vararg boundProperties: KProperty0<Any?>, action: (context: EventContext) -> Unit) {
  addEventBinding(CustomUIEventBindingType.ValueChanged, requireNotNull(this.id), *boundProperties, action = action)
}

public fun UiCodeEditor.onValueChange(action: (`value`: String?) -> Unit) {
  addEventBinding(CustomUIEventBindingType.ValueChanged, requireNotNull(this.id), this::value) {
    ctx -> action(ctx.getData(this::value))
  }
}

public fun UiCodeEditor.submitOnValueChange(formContext: UiFormContext<*>) {
  formContext.form.bindSubmit(this, CustomUIEventBindingType.ValueChanged)
}

public fun <T1> UiCodeEditor.onValueChange(prop1: KProperty0<T1>, action: (pT1: T1) -> Unit) {
  addEventBinding(CustomUIEventBindingType.ValueChanged, requireNotNull(this.id), prop1) {
    ctx -> action(
      ctx.getData(prop1)
    )
  }
}

public fun <T1, T2> UiCodeEditor.onValueChange(
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

public fun <T1, T2, T3> UiCodeEditor.onValueChange(
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

public fun <T1, T2, T3, T4> UiCodeEditor.onValueChange(
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

public fun <T1, T2, T3, T4, T5> UiCodeEditor.onValueChange(
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

public fun <T1> UiCodeEditor.onValueChange(prop1: KProperty0<T1>, action: (`value`: String?, p1: T1) -> Unit) {
  addEventBinding(CustomUIEventBindingType.ValueChanged, requireNotNull(this.id), this::value, prop1) {
    ctx -> action(
      ctx.getData(this::value),
      ctx.getData(prop1)
    )
  }
}

public fun <T1, T2> UiCodeEditor.onValueChange(
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

public fun <T1, T2, T3> UiCodeEditor.onValueChange(
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

public fun <T1, T2, T3, T4> UiCodeEditor.onValueChange(
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

public fun <T1, T2, T3, T4, T5> UiCodeEditor.onValueChange(
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
