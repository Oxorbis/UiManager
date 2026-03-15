# UiManager Architecture

## Core Components

### UiManager (Singleton Orchestrator)
**File**: `UiManager.kt` (~1009 lines)

Central manager for all UI pages and HUDs across the server.

**Page Registration Methods:**
- `registerStaticHud(pageId, pageFactory)` - Static pages, no dynamic context
- `registerPage<T>(pageId, initialData, pageFactory)` - Pages with typed context data
- `registerDynamicHudWithManualUpdate<T>()` - HUDs updated on demand
- `registerDynamicHudWithAutoUpdate()` - Auto-updating HUDs (default 500ms frequency)

**Key Data Structures:**
- `PageData` - Template for a registered page (asset name, path, factory, forms, initial data class)
- `DynamicPageData` - Wraps PageData with update mode and frequency
- `PageInstance` - Active page shown to a player (includes userData, event bindings, lifetime)
- `HudPageInstance` - Active HUD page with scheduled commands and update tracking

**Page Lifecycle:**
1. **Registration**: Factory creates template page -> serialized to `.ui` asset -> added to `CommonAssetModule` as `DynamicAsset` -> stored in `pages` map
2. **Display**: Factory called with actual PlayerRef -> event bindings extracted -> diff computed (template vs player version) -> `InteractiveCustomUIPage` created -> page opened
3. **Update**: Properties modified -> page regenerated -> `UiDiffProcessor.generateUpdateCommands()` compares old vs new -> only deltas sent via `CustomPage` packet
4. **Cleanup**: `onPlayerDisconnect()` removes player's pages and HUDs

**HUD Update Cycle:**
- Background thread at 100ms interval calls `UiManager.update()`
- Checks auto-update pages for dirty flag + frequency threshold
- Batches all player updates into single `CustomHud` packet
- First render creates `#MultipleHUD` group container; subsequent renders append/update

**Concurrency:** Thread-safe via `ConcurrentHashMap` and `ReentrantReadWriteLock`

### UiPage
**File**: `UiPage.kt`

Mutable container for a complete UI page:
- `children: MutableList<UiNode>` - Root nodes
- `variables: Map<String, Any>` - Page-level variables
- `clone()` - Deep clone for player-specific variants
- `isDirty` / `resetDirty()` - Dirty tracking across all children
- Implements `ChildNodeBuilder` for DSL support

### Infrastructure
**File**: `Infrastructure.kt`

**Annotations:**
- `@UiDsl` - DSL marker annotation
- `@ExcludeProperty` - Exclude from serialization

**Core Interfaces:**
- `UiNode` - Base interface: `id`, `omitName`, `templates`, `isDirty`, `markDirty()`, `resetDirty()`, `clone()`, `getEventBindings()`
- `UiNodeWithChildren` - Adds `children: MutableList<UiNode>` + `addNodeToChildren()`
- `BaseUiNode` - Abstract class implementing `UiNode` + `HasDelegates`, manages property delegates and event bindings

**Color:** `Color(hex: String, alpha: Double?)` - data class, `"#FF0000".color` extension

---

## Property System

### Rebindable Delegates
**File**: `property/property.kt`

`Rebindable<T>` provides property binding with dirty tracking:
- Stores local value or delegates to bound target property
- `bindTo(property)` establishes binding; if target owner is `Observable`, auto-subscribes to changes
- `isDirty` flag set on value change, propagated to owner node via `markDirty()`

### Observable Model
**File**: `model/Observable.kt`

`BaseObservable` provides `observable(initialValue)` delegate that notifies listeners on change.
Used for data classes that drive UI updates (e.g., `BwData` with `var time: String by observable(time)`).

### Binding Syntax
```kotlin
// Bind UI property to data property
label { ::text.bind(data::time) }

// When data.time changes -> label marks dirty -> auto-update sends diff
```

---

## DSL Builder System

### UiBuilder
**File**: `builder/UiBuilder.kt`

Entry point: `customUi { ... }` resets `IdGenerator` and creates a `UiPage`.

### NodeBuilders (Generated)
**File**: `builder/NodeBuilders.kt`

Two variants per node type:
1. `ChildNodeBuilder.label { ... }` - Extension, adds to parent, generates hierarchical ID
2. `label { ... }` - Standalone, generates simple ID

ID generation: Parent-aware prefixes (e.g., `getNext("ParentGroupLabel")` -> `ParentGroupLabel1`)

### NodeEventBuilders (Generated)
**File**: `builder/NodeEventBuilders.kt`

Extension functions per node type per event with overloads for 0-5 bound properties:
```kotlin
fun UiButton.onActivate(vararg boundProperties: KProperty0<Any?>, action: (EventContext) -> Unit)
fun UiButton.onActivate(prop1: KProperty0<T1>, action: (T1) -> Unit)
fun UiButton.onActivate(prop1: KProperty0<T1>, prop2: KProperty0<T2>, action: (T1, T2) -> Unit)
```

---

## Serialization

### UiSerializer
**File**: `serializer/UiSerializer.kt`

Converts in-memory UI tree to Hytale `.ui` text format:
1. Converts `UiNode` to `GenericNode` intermediate representation (handles template merging)
2. Serializes `GenericNode` tree to text with proper formatting
3. Properties in sorted map for consistent output
4. Handles nested objects, identifiers, colors, enums, strings

### UiDiffProcessor
**File**: `serializer/UiDiffProcessor.kt`

Generates minimal update commands by comparing initial vs current page state:
- Matches nodes by ID or index+name
- Property comparison: generates `set` commands for changed values
- Child comparison: Two strategies:
  - **List nodes**: Match by name+ID, detect add/remove/reorder
  - **Regular containers**: If >70% unchanged, surgical updates; otherwise clear + re-append
- Dynamic asset generation for new nodes (sent as temporary `.ui` assets)

**Command Types:** `set`, `append`, `appendInline`, `insertBefore`, `insertBeforeInline`, `remove`, `clear`

### GenericNode
**File**: `serializer/GenericNode.kt`

Intermediate representation: name, id, sorted properties map, children list. Bridges typed nodes to serialization.

---

## Event System

### EventBinding
**File**: `event/EventBinding.kt`

```kotlin
EventBinding(type, nodeId, boundProperties, action)
```
- `type`: `CustomUIEventBindingType` (Activating, ValueChanged, MouseEntered, etc.)
- `boundProperties`: List of `BoundPropertyEntry(nodeInstance, nodeProperty)` - properties to capture from client
- `action`: `(EventContext) -> Unit` handler

### EventContext
**File**: `event/EventContext.kt`

Provides `getData(property)` to extract values from client response by property path (`@nodeId.PropertyName`).

### EventResponse
**File**: `event/EventResponse.kt`

Deserializes client event JSON: `eventId`, `shiftHeld`, `values` map (`@propertyPath -> value`).

**Event Flow:** Client interaction -> `EventResponse` JSON -> server deserializes -> finds handler by eventId -> creates `EventContext` -> action executes

---

## Form System

### UiForm<T>
**File**: `special/UiForm.kt`

Generic form container binding UI elements to data class `T`:
- `boundProperties` - List of `BoundPropertyEntry` mapping node properties to data class properties
- `submitters` - Buttons that trigger form submission
- `submitHandler: (PlayerRef, T) -> Unit` - Called on submission with populated data class

### FormBuilder Extensions
**File**: `special/FormBuilder.kt`

DSL extensions for forms:
- `boundTextField(DataClass::field) { ... }` - Binds TextField value to String property
- `boundNumberField(DataClass::field) { ... }` - Binds NumberField to Double/Int
- `boundCheckBox(DataClass::field) { ... }` - Binds CheckBox to Boolean
- `boundColorPicker(DataClass::field) { ... }` - Binds ColorPicker to Color
- `submitTextButton { ... }` - Marks button as form submitter
- `formGroup { ... }` - Groups form fields (renders as Group)

### UiFormGroup<T> / UiListGroup
- `UiFormGroup<T>` - Group that maintains form context for nested bound fields
- `UiListGroup` - Ordered list container with proper diff handling (add/remove/reorder children)

---

## Templates

### CommonTemplate
**File**: `templates/CommonTemplate.kt`

Pre-styled component factories and style definitions:
- **Buttons**: `defaultTextButton`, `defaultCancelTextButton`, `secondaryTextButton`, `tertiaryTextButton` (+ square variants, small variants)
- **Inputs**: `defaultTextField`, `defaultNumberField`, `defaultCheckBox`, `defaultDropdownBox`
- **Layout**: `pageOverlay`, `panel`, `contentSeparator`, `verticalSeparator`, `panelSeparatorFancy`
- **Text**: `titleLabel`, `defaultTitle`, `subtitle`, `panelTitle`
- **Navigation**: `closeButton`, `defaultBackButton`, `actionButtonContainer`, `headerSearch`, `headerTextButton`
- **Other**: `spinner` (animated loading), `headerSeparator`
- **Styles**: `defaultButtonStyle`, `defaultTextButtonStyle`, `defaultCheckBoxStyle`, `defaultSliderStyle`, `defaultScrollbarStyle`, `defaultDropdownBoxStyle`, `defaultTextTooltipStyle`, `topTabsStyle`, `headerTabsStyle`, etc.

### SoundsTemplate
**File**: `templates/SoundsTemplate.kt`

Sound definitions: `buttonsLight`, `buttonsCancel`, `dropdownBox`, `tick`, `untick`, etc.

### UiContainer / UiDecoratedContainer
**Files**: `templates/UiContainer.kt`, `templates/UiDecoratedContainer.kt`

`container { title { ... }; content { ... } }` - Creates header + content + close button layout
`decoratedContainer { ... }` - Adds decorative top/bottom borders

---

## Utilities

- **StandardColors** (`util/StandardColors.kt`): Dynamic color palette with 23 families, `step(0-1000)` interpolation
- **CustomHudHelper** (`util/CustomHudHelper.kt`): Reflection-based interaction with Hytale's `CustomUIHud`
- **DelegatedChannel** (`util/DelegatedChannel.kt`): Netty channel wrapper for monitoring outgoing HUD commands
- **MapUtil** (`util/MapUtil.kt`): `extract()` - removes and returns matching entries from mutable map
- **PlayerExtension** (`util/playerExtension.kt`): `player`/`ref` extensions, `component<T>()` generic
- **ReflectionUtil** (`util/reflectionUtil.kt`): `toUnboundOrNull()`, `boundReceiverOrNull()`

---

## Code Generator

**File**: `src/uigen/kotlin/cz/creeperface/hytale/uimanager/uiGenerator.kt`

Reads `ui_structure_report.json` and generates:
1. Enum classes from enum definitions
2. Type/style data classes with builder functions
3. Node classes with `rebindable` properties, `clone()`, dirty tracking
4. Event extension functions with typed overloads
5. Builder factory functions (extension + standalone)

Overrides via `ui_type_overrides.json` for custom field types.
