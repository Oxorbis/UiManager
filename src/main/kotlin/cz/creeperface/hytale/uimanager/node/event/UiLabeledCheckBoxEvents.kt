package cz.creeperface.hytale.uimanager.node.event

import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType
import cz.creeperface.hytale.uimanager.event.EventContext
import cz.creeperface.hytale.uimanager.node.UiLabeledCheckBox
import cz.creeperface.hytale.uimanager.special.UiFormContext
import kotlin.Any
import kotlin.Unit
import kotlin.reflect.KProperty0

public fun UiLabeledCheckBox.onValueChange(vararg boundProperties: KProperty0<Any?>, action: (context: EventContext) -> Unit) {
  addEventBinding(CustomUIEventBindingType.ValueChanged, requireNotNull(this.id), *boundProperties, action = action)
}

public fun UiLabeledCheckBox.submitOnValueChange(formContext: UiFormContext<*>) {
  formContext.form.bindSubmit(this, CustomUIEventBindingType.ValueChanged)
}

public fun <T1> UiLabeledCheckBox.onValueChange(prop1: KProperty0<T1>, action: (pT1: T1) -> Unit) {
  addEventBinding(CustomUIEventBindingType.ValueChanged, requireNotNull(this.id), prop1) {
    ctx -> action(
      ctx.getData(prop1)
    )
  }
}

public fun <T1, T2> UiLabeledCheckBox.onValueChange(
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

public fun <T1, T2, T3> UiLabeledCheckBox.onValueChange(
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

public fun <T1, T2, T3, T4> UiLabeledCheckBox.onValueChange(
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

public fun <T1, T2, T3, T4, T5> UiLabeledCheckBox.onValueChange(
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
