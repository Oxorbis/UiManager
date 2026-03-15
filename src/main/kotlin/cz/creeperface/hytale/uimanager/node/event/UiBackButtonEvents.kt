package cz.creeperface.hytale.uimanager.node.event

import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType
import cz.creeperface.hytale.uimanager.event.EventContext
import cz.creeperface.hytale.uimanager.node.UiBackButton
import cz.creeperface.hytale.uimanager.special.UiFormContext
import kotlin.Any
import kotlin.Unit
import kotlin.reflect.KProperty0

public fun UiBackButton.onActivate(vararg boundProperties: KProperty0<Any?>, action: (context: EventContext) -> Unit) {
  addEventBinding(CustomUIEventBindingType.Activating, requireNotNull(this.id), *boundProperties, action = action)
}

public fun UiBackButton.submitOnActivate(formContext: UiFormContext<*>) {
  formContext.form.bindSubmit(this, CustomUIEventBindingType.Activating)
}

public fun <T1> UiBackButton.onActivate(prop1: KProperty0<T1>, action: (pT1: T1) -> Unit) {
  addEventBinding(CustomUIEventBindingType.Activating, requireNotNull(this.id), prop1) {
    ctx -> action(
      ctx.getData(prop1)
    )
  }
}

public fun <T1, T2> UiBackButton.onActivate(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  action: (pT1: T1, pT2: T2) -> Unit,
) {
  addEventBinding(CustomUIEventBindingType.Activating, requireNotNull(this.id), prop1, prop2) {
    ctx -> action(
      ctx.getData(prop1),
      ctx.getData(prop2)
    )
  }
}

public fun <T1, T2, T3> UiBackButton.onActivate(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  action: (
    pT1: T1,
    pT2: T2,
    pT3: T3,
  ) -> Unit,
) {
  addEventBinding(CustomUIEventBindingType.Activating, requireNotNull(this.id), prop1, prop2, prop3) {
    ctx -> action(
      ctx.getData(prop1),
      ctx.getData(prop2),
      ctx.getData(prop3)
    )
  }
}

public fun <T1, T2, T3, T4> UiBackButton.onActivate(
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
  addEventBinding(CustomUIEventBindingType.Activating, requireNotNull(this.id), prop1, prop2, prop3, prop4) {
    ctx -> action(
      ctx.getData(prop1),
      ctx.getData(prop2),
      ctx.getData(prop3),
      ctx.getData(prop4)
    )
  }
}

public fun <T1, T2, T3, T4, T5> UiBackButton.onActivate(
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
  addEventBinding(CustomUIEventBindingType.Activating, requireNotNull(this.id), prop1, prop2, prop3, prop4, prop5) {
    ctx -> action(
      ctx.getData(prop1),
      ctx.getData(prop2),
      ctx.getData(prop3),
      ctx.getData(prop4),
      ctx.getData(prop5)
    )
  }
}

public fun UiBackButton.onDoubleClick(vararg boundProperties: KProperty0<Any?>, action: (context: EventContext) -> Unit) {
  addEventBinding(CustomUIEventBindingType.DoubleClicking, requireNotNull(this.id), *boundProperties, action = action)
}

public fun UiBackButton.submitOnDoubleClick(formContext: UiFormContext<*>) {
  formContext.form.bindSubmit(this, CustomUIEventBindingType.DoubleClicking)
}

public fun <T1> UiBackButton.onDoubleClick(prop1: KProperty0<T1>, action: (pT1: T1) -> Unit) {
  addEventBinding(CustomUIEventBindingType.DoubleClicking, requireNotNull(this.id), prop1) {
    ctx -> action(
      ctx.getData(prop1)
    )
  }
}

public fun <T1, T2> UiBackButton.onDoubleClick(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  action: (pT1: T1, pT2: T2) -> Unit,
) {
  addEventBinding(CustomUIEventBindingType.DoubleClicking, requireNotNull(this.id), prop1, prop2) {
    ctx -> action(
      ctx.getData(prop1),
      ctx.getData(prop2)
    )
  }
}

public fun <T1, T2, T3> UiBackButton.onDoubleClick(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  action: (
    pT1: T1,
    pT2: T2,
    pT3: T3,
  ) -> Unit,
) {
  addEventBinding(CustomUIEventBindingType.DoubleClicking, requireNotNull(this.id), prop1, prop2, prop3) {
    ctx -> action(
      ctx.getData(prop1),
      ctx.getData(prop2),
      ctx.getData(prop3)
    )
  }
}

public fun <T1, T2, T3, T4> UiBackButton.onDoubleClick(
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
  addEventBinding(CustomUIEventBindingType.DoubleClicking, requireNotNull(this.id), prop1, prop2, prop3, prop4) {
    ctx -> action(
      ctx.getData(prop1),
      ctx.getData(prop2),
      ctx.getData(prop3),
      ctx.getData(prop4)
    )
  }
}

public fun <T1, T2, T3, T4, T5> UiBackButton.onDoubleClick(
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
  addEventBinding(CustomUIEventBindingType.DoubleClicking, requireNotNull(this.id), prop1, prop2, prop3, prop4, prop5) {
    ctx -> action(
      ctx.getData(prop1),
      ctx.getData(prop2),
      ctx.getData(prop3),
      ctx.getData(prop4),
      ctx.getData(prop5)
    )
  }
}

public fun UiBackButton.onRightClick(vararg boundProperties: KProperty0<Any?>, action: (context: EventContext) -> Unit) {
  addEventBinding(CustomUIEventBindingType.RightClicking, requireNotNull(this.id), *boundProperties, action = action)
}

public fun UiBackButton.submitOnRightClick(formContext: UiFormContext<*>) {
  formContext.form.bindSubmit(this, CustomUIEventBindingType.RightClicking)
}

public fun <T1> UiBackButton.onRightClick(prop1: KProperty0<T1>, action: (pT1: T1) -> Unit) {
  addEventBinding(CustomUIEventBindingType.RightClicking, requireNotNull(this.id), prop1) {
    ctx -> action(
      ctx.getData(prop1)
    )
  }
}

public fun <T1, T2> UiBackButton.onRightClick(
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

public fun <T1, T2, T3> UiBackButton.onRightClick(
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

public fun <T1, T2, T3, T4> UiBackButton.onRightClick(
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

public fun <T1, T2, T3, T4, T5> UiBackButton.onRightClick(
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

public fun UiBackButton.onMouseEnter(vararg boundProperties: KProperty0<Any?>, action: (context: EventContext) -> Unit) {
  addEventBinding(CustomUIEventBindingType.MouseEntered, requireNotNull(this.id), *boundProperties, action = action)
}

public fun UiBackButton.submitOnMouseEnter(formContext: UiFormContext<*>) {
  formContext.form.bindSubmit(this, CustomUIEventBindingType.MouseEntered)
}

public fun <T1> UiBackButton.onMouseEnter(prop1: KProperty0<T1>, action: (pT1: T1) -> Unit) {
  addEventBinding(CustomUIEventBindingType.MouseEntered, requireNotNull(this.id), prop1) {
    ctx -> action(
      ctx.getData(prop1)
    )
  }
}

public fun <T1, T2> UiBackButton.onMouseEnter(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  action: (pT1: T1, pT2: T2) -> Unit,
) {
  addEventBinding(CustomUIEventBindingType.MouseEntered, requireNotNull(this.id), prop1, prop2) {
    ctx -> action(
      ctx.getData(prop1),
      ctx.getData(prop2)
    )
  }
}

public fun <T1, T2, T3> UiBackButton.onMouseEnter(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  action: (
    pT1: T1,
    pT2: T2,
    pT3: T3,
  ) -> Unit,
) {
  addEventBinding(CustomUIEventBindingType.MouseEntered, requireNotNull(this.id), prop1, prop2, prop3) {
    ctx -> action(
      ctx.getData(prop1),
      ctx.getData(prop2),
      ctx.getData(prop3)
    )
  }
}

public fun <T1, T2, T3, T4> UiBackButton.onMouseEnter(
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
  addEventBinding(CustomUIEventBindingType.MouseEntered, requireNotNull(this.id), prop1, prop2, prop3, prop4) {
    ctx -> action(
      ctx.getData(prop1),
      ctx.getData(prop2),
      ctx.getData(prop3),
      ctx.getData(prop4)
    )
  }
}

public fun <T1, T2, T3, T4, T5> UiBackButton.onMouseEnter(
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
  addEventBinding(CustomUIEventBindingType.MouseEntered, requireNotNull(this.id), prop1, prop2, prop3, prop4, prop5) {
    ctx -> action(
      ctx.getData(prop1),
      ctx.getData(prop2),
      ctx.getData(prop3),
      ctx.getData(prop4),
      ctx.getData(prop5)
    )
  }
}

public fun UiBackButton.onMouseExit(vararg boundProperties: KProperty0<Any?>, action: (context: EventContext) -> Unit) {
  addEventBinding(CustomUIEventBindingType.MouseExited, requireNotNull(this.id), *boundProperties, action = action)
}

public fun UiBackButton.submitOnMouseExit(formContext: UiFormContext<*>) {
  formContext.form.bindSubmit(this, CustomUIEventBindingType.MouseExited)
}

public fun <T1> UiBackButton.onMouseExit(prop1: KProperty0<T1>, action: (pT1: T1) -> Unit) {
  addEventBinding(CustomUIEventBindingType.MouseExited, requireNotNull(this.id), prop1) {
    ctx -> action(
      ctx.getData(prop1)
    )
  }
}

public fun <T1, T2> UiBackButton.onMouseExit(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  action: (pT1: T1, pT2: T2) -> Unit,
) {
  addEventBinding(CustomUIEventBindingType.MouseExited, requireNotNull(this.id), prop1, prop2) {
    ctx -> action(
      ctx.getData(prop1),
      ctx.getData(prop2)
    )
  }
}

public fun <T1, T2, T3> UiBackButton.onMouseExit(
  prop1: KProperty0<T1>,
  prop2: KProperty0<T2>,
  prop3: KProperty0<T3>,
  action: (
    pT1: T1,
    pT2: T2,
    pT3: T3,
  ) -> Unit,
) {
  addEventBinding(CustomUIEventBindingType.MouseExited, requireNotNull(this.id), prop1, prop2, prop3) {
    ctx -> action(
      ctx.getData(prop1),
      ctx.getData(prop2),
      ctx.getData(prop3)
    )
  }
}

public fun <T1, T2, T3, T4> UiBackButton.onMouseExit(
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
  addEventBinding(CustomUIEventBindingType.MouseExited, requireNotNull(this.id), prop1, prop2, prop3, prop4) {
    ctx -> action(
      ctx.getData(prop1),
      ctx.getData(prop2),
      ctx.getData(prop3),
      ctx.getData(prop4)
    )
  }
}

public fun <T1, T2, T3, T4, T5> UiBackButton.onMouseExit(
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
  addEventBinding(CustomUIEventBindingType.MouseExited, requireNotNull(this.id), prop1, prop2, prop3, prop4, prop5) {
    ctx -> action(
      ctx.getData(prop1),
      ctx.getData(prop2),
      ctx.getData(prop3),
      ctx.getData(prop4),
      ctx.getData(prop5)
    )
  }
}
