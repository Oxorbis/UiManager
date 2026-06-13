# Hytale UI Manager

A Kotlin server-side plugin for Hytale that provides a type-safe DSL for building, serializing, and dynamically updating custom UI. It handles page lifecycle, automatic dirty tracking with partial updates, event handling, form binding, and HUD management.

## Features

- [**Type-Safe DSL**](#building-ui-with-the-dsl) - Compose UI trees using Kotlin DSL with auto-generated node classes,
  properties, and builders
- [**Pages**](#registering-a-page) - Full-screen interactive UI views with typed context data and configurable lifetime
- [**HUDs**](#huds) - Persistent overlay elements with static, auto-updating, and manually updated modes
- [**Automatic Dirty Tracking & Diff Updates**](#diff-based-updates) - Only changed properties are sent to the client,
  minimizing network traffic
- [**Async & Thread-Safe Updates**](#async-updates--threading) - Move the build + diff off the world thread with
  `updateAsync` / `updatePageAsync`; registries are safe to use concurrently across worlds
- [**Data Binding**](#data-binding--dynamic-updates) - Bind UI properties to observable data for automatic UI refresh on
  change
- [**Events**](#events) - Type-safe event handlers with the ability to capture values from other nodes
- [**Forms**](#forms) - Structured data collection with automatic field binding to data classes and submit handling
- [**Templates**](#templates) - Pre-styled components matching Hytale's native UI look
- [**Dynamic Nodes**](#dynamic-nodes) - Conditional visibility and fixed-size list rendering for dynamic content
- [**Text & Translations**](#text--translations) - `Message` type for raw text and server-side translation keys

## Building UI with the DSL

All UI is built using the `customUi { }` DSL. Inside it you compose a tree of nodes:

```kotlin
val page = customUi {
    group {
        id = "MainPanel"
        anchor = UiAnchor(width = 400, height = 300)
        layoutMode = LayoutMode.Top
        padding = UiPadding(full = 16)
        background = UiPatchStyle(
            texturePath = "../../Common/ContainerPanelPatch.png",
            border = 6
        )

        label {
            text = "Welcome".toMessage()
            style = UiLabelStyle(
                fontSize = 24.0,
                textColor = Color("#ffffff"),
                renderBold = true,
                horizontalAlignment = LabelAlignment.Center
            )
        }

        textButton {
            text = "Click Me".toMessage()
            anchor = UiAnchor(height = 44)
        }
    }
}
```

### Available Nodes

**Containers** (accept children):
`group`, `button`, `textButton`, `panel`, `dynamicPane`, `dynamicPaneContainer`, `reorderableList`, `itemGrid`, `checkBoxContainer`, `tabNavigation`

**Display**:
`label`, `progressBar`, `circularProgressBar`, `timerLabel`, `sprite`, `assetImage`, `itemIcon`, `itemSlot`, `sceneBlur`, `hotkeyLabel`

**Input**:
`textField`, `multilineTextField`, `compactTextField`, `numberField`, `slider`, `floatSlider`, `sliderNumberField`, `floatSliderNumberField`, `checkBox`, `labeledCheckBox`, `dropdownBox`, `dropdownEntry`, `colorPicker`, `colorPickerDropdownBox`, `colorOptionGrid`, `blockSelector`, `codeEditor`

**Interactive**:
`itemSlotButton`, `tabButton`, `menuItem`, `actionButton`, `toggleButton`, `backButton`, `reorderableListGrip`

**Specialized**:
`itemPreviewComponent`, `characterPreviewComponent`

### Common Properties

Every node supports:
- `id` - Unique identifier (auto-generated if not set)
- `anchor` - Position and size (`UiAnchor(left, right, top, bottom, width, height)`)
- `visible` - Show/hide the node
- `background` - Background style (`UiPatchStyle`)
- `padding` - Inner padding (`UiPadding`)
- `layoutMode` - Child layout (`LayoutMode.Top`, `Left`, `Center`, `Middle`, `CenterMiddle`, `Full`, etc.)
- `flexWeight` - Flex weight for layout
- `templates` - Style templates to inherit from

### Styling

```kotlin
// Colors
Color("#FF0000")           // hex
Color("#FF0000", 0.5)      // hex with alpha (0.0-1.0)

// Patch style (9-patch textures or solid colors)
UiPatchStyle(texturePath = "../../Common/Panel.png", border = 6)
UiPatchStyle(color = Color("#1c2835"))

// Label style
UiLabelStyle(
    fontSize = 16.0,
    textColor = Color("#96a9be"),
    renderBold = true,
    renderUppercase = true,
    horizontalAlignment = LabelAlignment.Center,
    verticalAlignment = LabelAlignment.Center
)
```

For Hytale's default/vanilla look, `CommonTemplate` provides pre-defined styles (see [Templates](#templates)):

```kotlin
// Use vanilla Hytale button styling
defaultTextButton {
    text = "Save".toMessage()
}

// Decorated container with Hytale-styled border and title
decoratedContainer {
    title { defaultTitle { text = "Settings".toMessage() } }
    content {
        // ...
    }
}
```

### ID Generation

Node IDs are generated automatically using a hierarchical scheme. The parent node's ID becomes a prefix:

```kotlin
group {
    id = "Stats"         // ID: "Stats"
    label { }            // ID: "StatsLabel1"
    label { }            // ID: "StatsLabel2"
    group {              // ID: "StatsGroup1"
        label { }        // ID: "StatsGroup1Label1"
    }
}
```

You can override IDs manually with `id = "MyCustomId"`.

## Text & Translations

Text properties use the `Message` type. Two extension functions make it easy to create them:

```kotlin
// Raw text
label { text = "Hello, world!".toMessage() }

// Server-side translation key (serializes as %server.customUI.title)
label { text = "server.customUI.title".translated() }
```

## Registering a Page

Pages are full-screen interactive UI views shown to a player. Register them during plugin `setup()`:

```kotlin
// Simple page with no context data
UiManager.registerPage("myPage", Unit) { playerRef, _ ->
    group {
        label { text = "Hello, ${playerRef?.username}".toMessage() }
    }
}

// Page with typed context data
data class ShopData(val items: List<ShopItem>)

UiManager.registerPage("shop", ShopData(emptyList())) { playerRef, data ->
    group {
        data.items.forEach { item ->
            label { text = item.name.toMessage() }
        }
    }
}
```

### Showing a Page to a Player

```kotlin
// Show a page (opens as a modal UI)
UiManager.showPage(playerRef, "myPage", Unit)

// Show with context data
UiManager.showPage(playerRef, "shop", ShopData(myItems))

// Show with specific lifetime
UiManager.showPage(
    playerRef, "shop", shopData,
    lifetime = CustomPageLifetime.CanDismissOrCloseThroughInteraction
)
```

### Updating an Open Page

Send new data to an already-open page. Only the differences are transmitted to the client:

```kotlin
UiManager.updatePage(playerRef, "shop", ShopData(updatedItems))
```

By default, every update resends all input field values (text fields, checkboxes, sliders, etc.) even if they haven't
changed on the server side. This is because the client can modify input values locally (e.g., user typing in a text
field), and the server needs to overwrite them to keep the UI in sync.

If your page has no user-editable inputs, or you intentionally want to preserve the client's local input state, disable
this with `UpdateOptions`:

```kotlin
UiManager.updatePage(
    playerRef, "shop", ShopData(updatedItems),
    options = UpdateOptions(resendInputs = false)
)
```

For frequently-updated pages, prefer `updatePageAsync`, which moves the build + diff off the calling thread and returns
a `CompletableFuture<Void>`. It takes the same arguments as `updatePage` plus an optional `executor`:

```kotlin
UiManager.updatePageAsync(playerRef, "shop", ShopData(updatedItems))
```

See [Async Updates & Threading](#async-updates--threading) for the contract and best practices.

## HUDs

HUDs are persistent overlay elements that remain on screen. Three registration modes are available:

### Static HUD

Content that never changes after being shown:

```kotlin
UiManager.registerStaticHud("logo") {
    group {
        anchor = UiAnchor(right = 10, top = 10, width = 64, height = 64)
        background = UiPatchStyle(texturePath = "../../Textures/logo.png")
    }
}

// Show to a player
UiManager.showStaticHud("logo", playerRef)
```

### Auto-Updating HUD

Automatically detects property changes and sends updates at a configurable frequency:

```kotlin
UiManager.registerDynamicHudWithAutoUpdate(
    "timer",
    { playerRef ->
        label {
            ::text.bind(timerData::time) // binds to observable data
        }
    },
    minUpdateFrequency = 500.milliseconds // default
)

UiManager.showDynamicHud("timer", playerRef)
```

### Manually Updated HUD

You control when updates are sent:

```kotlin
UiManager.registerDynamicHudWithManualUpdate("stats", initialStats) { playerRef, stats ->
    label { text = "Kills: ${stats.kills}".toMessage() }
}

UiManager.showDynamicHud("stats", playerRef, playerStats)

// Later, trigger an update with new data
UiManager.update(playerRef, "stats", updatedStats)
```

For HUDs refreshed every tick, prefer the async variant — it builds + diffs off the world thread and returns a
`CompletableFuture<Void>`:

```kotlin
UiManager.updateAsync(playerRef, "stats", updatedStats)
```

See [Async Updates & Threading](#async-updates--threading).

## Data Binding & Dynamic Updates

### Observable Data

Create observable data classes that automatically trigger UI updates when properties change:

```kotlin
class HudData(time: Message, score: Int) : BaseObservable() {
    var time: Message by observable(time)
    var score: Int by observable(score)
}
```

### Property Binding

Bind UI properties directly to data properties. When the data changes, the UI updates automatically:

```kotlin
val data = HudData("00:00".toMessage(), 0)

UiManager.registerDynamicHudWithAutoUpdate("gameHud", { _ ->
    group {
        label {
            ::text.bind(data::time)    // updates when data.time changes
        }
        label {
            ::text.bind(data::score)   // updates when data.score changes
        }
    }
})

// Later, update the data - UI refreshes automatically
data.time = "05:30".toMessage()
data.score = 42
```

### Dirty Tracking

All node properties use `rebindable()` delegates that track changes automatically. When any property changes, the node is marked dirty, and the auto-update system detects it and sends only the changed values to the client using diff-based updates.

## Async Updates & Threading

Building and diffing a UI tree is pure CPU work — rebuild the tree, clone it, diff against the last-sent state. For a
frequently-refreshed UI (a game HUD updated every tick, say) doing that on the world thread can blow the server's
per-tick budget. The async variants move that work onto a background executor; only the packet send stays on the
engine's schedule.

### `updateAsync` (HUD) and `updatePageAsync` (page)

```kotlin
// HUD hot path — build + diff off the world thread
UiManager.updateAsync(playerRef, "gameHud", hudData)

// Page equivalent (same arguments as updatePage)
UiManager.updatePageAsync(playerRef, "shop", ShopData(updatedItems))
```

Both return a `CompletableFuture<Void>` that completes once the result is published (HUD) / the packet is sent (page),
or exceptionally if the build fails — on failure no UI state is mutated, so the next diff is unaffected. Both accept an
optional `executor` to override the pool per call. The default is a small bounded daemon pool, replaceable globally:

```kotlin
UiManager.updateAsync(playerRef, "gameHud", hudData, executor = myExecutor)

UiManager.uiBuildExecutor = myExecutor   // global override
```

### Coalescing (latest-wins)

If you call `updateAsync` for the same instance faster than builds complete, builds are serialized per instance and only
the **newest** pending one runs next — intermediate frames are dropped. This is the correct behaviour for a per-tick
HUD: you always converge on the latest state without queueing stale work.

### Best practices

- **Build your data snapshot first, then pass it in.** The page factory runs on a background thread, so it must read
  only the `context` / `userData` snapshot you pass plus immutable/registered data — **never** live ECS / world / entity
  state. Gather what you need on your own thread, wrap it in a data class, and hand that to `updateAsync`.
- **Pick sync or async per instance — don't mix them.** Async builds are serialized against each other but not against
  the synchronous `update` / `updatePage`. Driving the same HUD/page through both concurrently races on internal state.
  Choose one path per instance.
- **Async for the hot path, sync for one-offs.** A per-tick HUD benefits from `updateAsync`; a one-time update in
  response to a click is fine synchronously.

### Calling from multiple worlds

Different Hytale worlds tick on different threads. UiManager's registries are thread-safe, so `show*` / `update*` /
`hideHud` may be used concurrently for players in different worlds. The contract:

- **Drive a given player's UI from that player's own world thread.** Per-player state stays consistent because a player
  lives in one world (one thread); concurrency is only *between* different players in different worlds.
- **`showPage` is safe to call from any thread.** It schedules the actual open onto the player's world thread, so it
  **returns before the page is open** (end of the current tick if you are already on that world thread, otherwise the
  next tick).
- The factory contract above (no live world state) applies to `showPage` / `showDynamicHud` initial builds too.

## Events

Attach event handlers to interactive nodes. Events can capture current values from other nodes at the time the event
fires:

```kotlin
val firstName = textField {
    id = "FirstName"
    placeholderText = "First name".toMessage()
}

val lastName = textField {
    id = "LastName"
    placeholderText = "Last name".toMessage()
}

textButton {
    text = "Submit".toMessage()

    // Capture values from other fields when button is clicked
    onActivate(firstName::value, lastName::value) { first, last ->
        playerRef.sendMessage("Hello, $first $last!")
    }
}
```

### Available Event Types

Each node type has its own generated event extensions. Common events:
- **Buttons**: `onActivate`, `onDoubleClick`, `onRightClick`, `onMouseEntered`, `onMouseExited`
- **Input fields**: `onValueChanged`, `onFocusGained`, `onFocusLost`
- **Sliders**: `onValueChanged`, `onMouseButtonReleased`
- **Checkboxes**: `onValueChanged`
- **Dropdowns**: `onDropdownToggled`

### Event Value Binding

Events support capturing 0-5 property values from other nodes. The values are read from the client at the moment the
event fires, giving you up-to-date input state:

```kotlin
// No captured values
button.onActivate { ctx ->
    // ctx.playerRef, ctx.shiftHeld available
}

// Capture 1 value
button.onActivate(textField::value) { value ->
    // value contains the current text field content
}

// Capture 2 values
button.onActivate(field1::value, field2::value) { v1, v2 ->
    // v1, v2 contain current values at the time of click
}

// Up to 5 values
button.onActivate(f1::value, f2::value, f3::value, f4::value, f5::value) { a, b, c, d, e ->
    // ...
}
```

## Forms

Forms provide structured data collection with automatic binding between UI fields and a data class:

```kotlin
data class RegistrationData(
    var username: String? = null,
    var email: String? = null,
    var age: Double? = null,
    var agreeToTerms: Boolean? = null
)

UiManager.registerPage("register", Unit) { _, _ ->
    group {
        layoutMode = LayoutMode.Center

        form<RegistrationData> {
            submitHandler = { playerRef, data ->
                playerRef.sendMessage(
                    "Registered: ${data.username}, ${data.email}"
                )
            }

            formGroup {
                anchor = UiAnchor(height = 40)
                boundTextField(RegistrationData::username) {
                    placeholderText = "Username".toMessage()
                    anchor = UiAnchor(width = 200, height = 38)
                }
            }

            formGroup {
                anchor = UiAnchor(height = 40)
                boundTextField(RegistrationData::email) {
                    placeholderText = "Email".toMessage()
                    anchor = UiAnchor(width = 200, height = 38)
                }
            }

            formGroup {
                anchor = UiAnchor(height = 40)
                boundNumberField(RegistrationData::age) {
                    anchor = UiAnchor(width = 200, height = 38)
                }
            }

            formGroup {
                anchor = UiAnchor(height = 30)
                boundCheckBox(RegistrationData::agreeToTerms) {}
            }

            formGroup {
                submitTextButton {
                    text = "Register".toMessage()
                    anchor = UiAnchor(width = 120, height = 44)
                }
            }
        }
    }
}
```

### Bound Field Types

- `boundTextField(DataClass::stringProp) { }` - Text input
- `boundNumberField(DataClass::doubleProp) { }` - Number input
- `boundCheckBox(DataClass::boolProp) { }` - Checkbox
- `boundColorPicker(DataClass::colorProp) { }` - Color picker
- `boundColorPickerDropdownBox(DataClass::stringProp) { }` - Color picker dropdown

Each also has a `boundDefault*` variant that applies `CommonTemplate` styling (e.g., `boundDefaultTextField`).

### Submit Buttons

Use `submitTextButton { }` or `submitDefaultTextButton { }` inside a form or form group. Clicking the button collects all bound field values and calls the `submitHandler`.

### Pre-filling Forms

Pass existing data when showing a page:

```kotlin
val existingData = RegistrationData(username = "Player1")
UiManager.showPage(playerRef, "register", Unit, formData = listOf(existingData))
```

## Templates

`CommonTemplate` provides pre-styled components matching Hytale's native UI look:

```kotlin
import cz.creeperface.hytale.uimanager.templates.CommonTemplate.defaultTextButton
import cz.creeperface.hytale.uimanager.templates.CommonTemplate.defaultTextField
import cz.creeperface.hytale.uimanager.templates.CommonTemplate.pageOverlay
import cz.creeperface.hytale.uimanager.templates.CommonTemplate.defaultTitle
import cz.creeperface.hytale.uimanager.templates.decoratedContainer

// Full-screen overlay with a decorated container
pageOverlay {
    layoutMode = LayoutMode.Middle

    decoratedContainer {
        anchor = UiAnchor(width = 532)

        title {
            defaultTitle { text = "Settings".toMessage() }
        }

        content {
            layoutMode = LayoutMode.Top
            padding = UiPadding(full = 16)

            defaultTextField {
                placeholderText = "Search...".toMessage()
            }

            defaultTextButton {
                text = "Save".toMessage()
            }
        }
    }
}
```

### Available Template Components

**Buttons**: `defaultTextButton`, `defaultCancelTextButton`, `secondaryTextButton`, `tertiaryTextButton`, `defaultButton`, `defaultSmallSecondaryTextButton`

**Inputs**: `defaultTextField`, `defaultNumberField`, `defaultCheckBox`, `defaultDropdownBox`, `headerSearch`

**Layout**: `pageOverlay`, `panel`, `contentSeparator`, `verticalSeparator`, `headerSeparator`, `defaultBackButton`, `closeButton`, `actionButtonContainer`

**Text**: `titleLabel`, `defaultTitle`, `subtitle`, `panelTitle`

**Other**: `spinner` (loading animation), `headerTextButton`

### Containers

Structured layout with title/content sections:

```kotlin
// Basic container
container {
    title {
        label { text = "Title".toMessage() }
    }
    content {
        label { text = "Content goes here".toMessage() }
    }
}

// Decorated container (with border decoration)
decoratedContainer {
    title {
        defaultTitle { text = "Decorated Title".toMessage() }
    }
    content {
        // ...
    }
}
```

### Pre-defined Styles

`CommonTemplate` exposes reusable style objects:
- `defaultTextButtonStyle`, `cancelTextButtonStyle`, `secondaryTextButtonStyle`, `tertiaryTextButtonStyle`
- `defaultCheckBoxStyle`, `defaultDropdownBoxStyle`, `defaultSliderStyle`
- `defaultScrollbarStyle`, `defaultColorPickerStyle`
- `defaultLabelStyle`, `titleStyle`, `subtitleStyle`
- `topTabsStyle`, `headerTabsStyle`
- `buttonSounds`

## Dynamic Nodes

The UI tree structure is fixed at registration time — you cannot add or remove nodes when updating a page. The diff
engine can only update **property values** of existing nodes. To handle dynamic content, use `conditionalBlock` and
`listBlock` from `DynamicUiBuilders`, which pre-create all possible nodes and toggle their `visible` property based on
the current data.

### Conditional Visibility

Show or hide nodes based on a condition:

```kotlin
// Show a block only when condition is true
conditionalBlock(team.isFull) {
    label { text = "Full".toMessage() }
}

// Show one block or the other based on a condition
conditionalBlock(
    team.isFull,
    trueBuilder = {
        label { text = "Full".toMessage() }
    },
    falseBuilder = {
        defaultTextButton {
            text = "Join".toMessage()
            onActivate { /* ... */ }
        }
    }
)
```

Both branches are always present in the UI tree. The builder sets `visible = condition` on nodes from the first block
and `visible = !condition` on nodes from the second block.

### Fixed-Size Lists

Render a list of items with a pre-allocated maximum size. Slots beyond the actual data are hidden:

```kotlin
listBlock(data.teams, maxItems = 16) { team, index ->
    group {
        label { text = (team?.name ?: "").toMessage() }
        label { text = "${team?.playerCount ?: 0} players".toMessage() }
    }
}
```

`listBlock` iterates `maxItems` times, always creating the full set of nodes. For each slot, the item is `null` if the
index exceeds the data size, and the node's `visible` is set to `item != null`. On update, the existing nodes get new
property values and visibility — no structural changes needed.

### Full Example

A team selection page combining both `listBlock` and `conditionalBlock`:

```kotlin
UiManager.registerPage("teamSelect", TeamSelectData()) { playerRef, data ->
    pageOverlay {
        decoratedContainer {
            title { defaultTitle { text = "Select a Team".toMessage() } }

            content {
                layoutMode = LayoutMode.Top

                listBlock(data.teams, maxItems = 16) { team, i ->
                    group {
                        anchor = UiAnchor(bottom = 6, height = 44)
                        layoutMode = LayoutMode.Left

                        label {
                            text = (team?.name ?: "").toMessage()
                        }

                        // Show "Join" button or "Full" label depending on team state
                        conditionalBlock(
                            team?.isFull != true,
                            {
                                defaultTextButton {
                                    text = "Join".toMessage()
                                    onActivate { /* handle join */ }
                                }
                            },
                            {
                                label { text = "Full".toMessage() }
                            }
                        )
                    }
                }
            }
        }
    }
}
```

## Diff-Based Updates

UiManager uses a diff engine that compares the previous UI state with the current one and sends only the minimal set of
commands needed:

- **Property changes** - `set` commands (e.g., update text, visibility, color)
- **Added children** - `append` / `insertBefore` commands
- **Removed children** - `remove` commands
- **List changes** - intelligent matching by node identity with add/remove/reorder

This minimizes network traffic, especially for HUDs that update frequently.

## Color Utilities

### Standard Colors

A palette of 23 color families with step-based interpolation (0 = white, 500 = base, 1000 = black):

```kotlin
import cz.creeperface.hytale.uimanager.util.StandardColors

val lightBlue = StandardColors.blue.step(200)   // light blue
val baseRed = StandardColors.red.step(500)       // base red
val darkGreen = StandardColors.green.step(800)   // dark green
```

Available families: `slate`, `gray`, `zinc`, `neutral`, `stone`, `red`, `orange`, `amber`, `yellow`, `lime`, `green`, `emerald`, `teal`, `cyan`, `sky`, `blue`, `indigo`, `violet`, `purple`, `fuchsia`, `pink`, `rose`

## Build & Test

```bash
./gradlew build          # Build the plugin (fat JAR)
./gradlew test           # Run tests
./gradlew generateUi     # Regenerate node/type/enum classes from ui_structure_report.json
```

**Requirements**: JDK 25 to build & run (the Hytale Server JAR is compiled for Java 25; Kotlin emits Java 24 bytecode,
which runs on 25), Hytale Server JAR (local dependency)
