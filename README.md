# Hytale UI Manager

A Kotlin server-side plugin for Hytale that provides a type-safe DSL for building, serializing, and dynamically updating custom UI. It handles page lifecycle, automatic dirty tracking with partial updates, event handling, form binding, and HUD management.

## Getting Started

### Registering a Page

Pages are full-screen interactive UI views shown to a player. Register them during plugin `setup()`:

```kotlin
// Simple page with no context data
UiManager.registerPage("myPage", Unit) { playerRef, _ ->
    group {
        label { text = "Hello, ${playerRef?.username}" }
    }
}

// Page with typed context data
data class ShopData(val items: List<ShopItem>)

UiManager.registerPage("shop", ShopData(emptyList())) { playerRef, data ->
    group {
        data.items.forEach { item ->
            label { text = item.name }
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
    label { text = "Kills: ${stats.kills}" }
}

UiManager.showDynamicHud("stats", playerRef, playerStats)

// Later, trigger an update with new data
UiManager.update(playerRef, "stats", updatedStats)
```

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
            text = "Welcome"
            style = UiLabelStyle(
                fontSize = 24.0,
                textColor = Color("#ffffff"),
                renderBold = true,
                horizontalAlignment = LabelAlignment.Center
            )
        }

        textButton {
            text = "Click Me"
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

## Data Binding & Dynamic Updates

### Observable Data

Create observable data classes that automatically trigger UI updates when properties change:

```kotlin
class HudData(time: String, score: Int) : BaseObservable() {
    var time: String by observable(time)
    var score: Int by observable(score)
}
```

### Property Binding

Bind UI properties directly to data properties. When the data changes, the UI updates automatically:

```kotlin
val data = HudData("00:00", 0)

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

// Later, update the data — UI refreshes automatically
data.time = "05:30"
data.score = 42
```

### Dirty Tracking

All node properties use `rebindable()` delegates that track changes automatically. When any property changes, the node is marked dirty, and the auto-update system detects it and sends only the changed values to the client using diff-based updates.

## Events

Attach event handlers to interactive nodes. Events can capture values from other nodes:

```kotlin
val firstName = textField {
    id = "FirstName"
    placeholderText = "First name"
}

val lastName = textField {
    id = "LastName"
    placeholderText = "Last name"
}

textButton {
    text = "Submit"

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

### Event Overloads

Events support capturing 0-5 property values from other nodes:

```kotlin
// No captured values
button.onActivate { ctx ->
    // ctx.playerRef, ctx.response available
}

// Capture 1 value
button.onActivate(textField::value) { value ->
    // ...
}

// Capture 2 values
button.onActivate(field1::value, field2::value) { v1, v2 ->
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
                    placeholderText = "Username"
                    anchor = UiAnchor(width = 200, height = 38)
                }
            }

            formGroup {
                anchor = UiAnchor(height = 40)
                boundTextField(RegistrationData::email) {
                    placeholderText = "Email"
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
                    text = "Register"
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
            defaultTitle { text = "Settings" }
        }

        content {
            layoutMode = LayoutMode.Top
            padding = UiPadding(full = 16)

            defaultTextField {
                placeholderText = "Search..."
            }

            defaultTextButton {
                text = "Save"
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
        label { text = "Title" }
    }
    content {
        label { text = "Content goes here" }
    }
}

// Decorated container (with border decoration)
decoratedContainer {
    title {
        defaultTitle { text = "Decorated Title" }
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

## List Groups

For dynamic lists where items can be added, removed, or reordered. The diff engine handles list changes efficiently:

```kotlin
listGroup {
    layoutMode = LayoutMode.LeftCenterWrap

    data.items.forEach { item ->
        group {
            label { text = item.name }
            label { text = "${item.price} gold" }
        }
    }
}
```

When the page is updated with a different number of items, `UiDiffProcessor` generates minimal add/remove/reorder commands instead of replacing the entire list.

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

## Diff-Based Updates

UiManager uses a diff engine that compares the previous UI state with the current one and sends only the minimal set of commands needed:

- **Property changes** → `set` commands (e.g., update text, visibility, color)
- **Added children** → `append` / `insertBefore` commands
- **Removed children** → `remove` commands
- **List changes** → intelligent matching by node identity with add/remove/reorder

This minimizes network traffic, especially for HUDs that update frequently.

## Server-Side String Interpolation

Use the `%` prefix for strings resolved by the client:

```kotlin
label {
    text = "%server.customUI.title"
}
```

## Build & Test

```bash
./gradlew build          # Build the plugin (fat JAR)
./gradlew test           # Run tests
./gradlew generateUi     # Regenerate node/type/enum classes from ui_structure_report.json
```

**Requirements**: Kotlin JVM targeting Java 24, Hytale Server JAR (local dependency)
