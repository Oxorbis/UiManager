# Hytale UI System Reference

## .ui File Format

Hytale uses a declarative markup language for UI definitions.

### Basic Syntax

```
// Comments use double-slash
Group #MyId {
  Anchor: (Width: 200, Height: 100);
  LayoutMode: Left;

  Label #Title {
    Text: "Hello World";
    Style: (FontSize: 18, TextColor: #ffffff);
  }

  Button #Submit {
    Anchor: (Height: 40);
  }
}
```

**Rules:**
- Nodes: `TypeName #OptionalId { properties; children; }`
- Properties: `PropertyName: value;` (semicolon-terminated)
- Nested objects: Parentheses `(Key: Value, Key2: Value2)`
- Strings: Double-quoted `"text"`
- Colors: Hex `#RRGGBB` or with alpha `#RRGGBB(0.5)`
- Identifiers (enums, variables, translations): No quotes
- Children: Nested inside parent braces

### Variables and Imports

```
// Local variable
@MyStyle = LabelStyle(FontSize: 16, TextColor: #96a9be);

// File import
$C = "../Common.ui";

// Use imported variable
Label { Style: $C.@DefaultLabelStyle; }

// Spread/merge operator
@CustomStyle = LabelStyle(
  ...@MyStyle,
  FontSize: 20
);
```

### Server-Side String Interpolation
```
Text: %server.customUI.title;
```

---

## UI Element Types

### Container Elements (accept children)
| Element | Description |
|---------|-------------|
| **Group** | Basic layout container |
| **Button** | Clickable container with states (Default/Hovered/Pressed/Disabled) |
| **TextButton** | Button with built-in text label |
| **Panel** | Scrollable container |
| **DynamicPane** | Dynamically-sized container |
| **DynamicPaneContainer** | Container for dynamic panes |
| **ReorderableList** | Drag-to-reorder list |
| **ItemGrid** | Grid layout for items |
| **CheckBoxContainer** | Container with checkbox behavior |

### Display Elements
| Element | Description |
|---------|-------------|
| **Label** | Text display (plain text or formatted spans) |
| **ProgressBar** | Linear progress indicator |
| **CircularProgressBar** | Circular progress indicator |
| **TimerLabel** | Countdown/countup timer display |
| **Sprite** | Animated sprite sheet display |
| **AssetImage** | Game asset image display |
| **ItemIcon** | Game item icon |
| **ItemSlot** | Inventory item display |
| **SceneBlur** | Background blur effect |
| **HotkeyLabel** | Keyboard shortcut display |

### Input Elements
| Element | Description |
|---------|-------------|
| **TextField** | Single-line text input |
| **MultilineTextField** | Multi-line text input |
| **CompactTextField** | Collapsible text input |
| **NumberField** | Numeric input with formatting |
| **Slider** | Integer range slider |
| **FloatSlider** | Float range slider |
| **SliderNumberField** | Slider with number input |
| **FloatSliderNumberField** | Float slider with number input |
| **CheckBox** | Boolean toggle |
| **LabeledCheckBox** | Checkbox with built-in label |
| **DropdownBox** | Selectable list (supports search, multi-select) |
| **DropdownEntry** | Entry within dropdown |
| **ColorPicker** | Color selection |
| **ColorPickerDropdownBox** | Color picker in dropdown |
| **ColorOptionGrid** | Grid of color options |
| **BlockSelector** | Block selection interface |
| **CodeEditor** | Code editing with syntax highlight |

### Interactive Elements
| Element | Description |
|---------|-------------|
| **ItemSlotButton** | Clickable item slot |
| **TabNavigation** | Tab switching |
| **TabButton** | Individual tab |
| **MenuItem** | Menu item |
| **ActionButton** | Action button with alignment |
| **ToggleButton** | Toggle on/off button |
| **BackButton** | Navigation back button |
| **ReorderableListGrip** | Drag handle for reorderable list |

### Specialized
| Element | Description |
|---------|-------------|
| **ItemPreviewComponent** | 3D item preview |
| **CharacterPreviewComponent** | 3D character preview |

---

## Common Properties

### Positioning & Sizing (Anchor)
```
Anchor: (
  Left: 10, Right: 10,     // Offset from parent edges
  Top: 10, Bottom: 10,
  Width: 200, Height: 100,  // Fixed dimensions
  Full: 0,                  // Fill parent (value = inset)
  Horizontal: 10,           // Left + Right shorthand
  Vertical: 10,             // Top + Bottom shorthand
  MinWidth: 50, MaxWidth: 300,
  MinHeight: 50, MaxHeight: 300
);
```

### Layout
```
LayoutMode: Top;          // How children are arranged
Padding: (Full: 10);     // Internal spacing
FlexWeight: 1;            // Distribute leftover space
```

**LayoutMode Values:**
- Directional: `Left`, `Right`, `Top`, `Bottom`
- Centered: `Center`, `Middle`, `CenterMiddle`, `MiddleCenter`
- Wrapping: `LeftCenterWrap`
- Scrolling: `LeftScrolling`, `RightScrolling`, `TopScrolling`, `BottomScrolling`
- `Full` - Child fills entire parent

### Visual
```
Background: (TexturePath: "path/to/texture.png", Border: 16);  // 9-patch
Background: (Color: #1a2530);                                    // Solid color
Style: ButtonStyle(...);                                          // Element-specific
OutlineColor: #ffffff;
OutlineSize: 1;
MaskTexturePath: "path/to/mask.png";
Visible: true;
```

### Interactivity
```
Disabled: false;
HitTestVisible: true;
TooltipText: "Hover text";
IsReadOnly: false;
```

---

## Property Types

### PatchStyle (Backgrounds)
```kotlin
patchStyle {
    texturePath = "../../Common/ContainerPatch.png"
    border = 16              // Uniform 9-patch border
    horizontalBorder = 80    // Horizontal 9-patch border
    verticalBorder = 12      // Vertical 9-patch border
    color = Color("#1a2530") // Solid color (alternative to texture)
}
```

### LabelStyle
```kotlin
labelStyle {
    fontSize = 16.0
    textColor = Color("#96a9be")
    renderBold = true
    renderUppercase = true
    renderItalic = false
    wrap = true
    horizontalAlignment = LabelAlignment.Center
    verticalAlignment = LabelAlignment.Center
    fontName = "Default"      // or "Secondary"
    letterSpacing = 1.0
}
```

### ButtonStyle (State-based)
```kotlin
buttonStyle {
    default = buttonStyleState { background = patchStyle { ... } }
    hovered = buttonStyleState { background = patchStyle { ... } }
    pressed = buttonStyleState { background = patchStyle { ... } }
    disabled = buttonStyleState { background = patchStyle { ... } }
    sounds = buttonSounds { activate = soundStyle { ... }; mouseHover = soundStyle { ... } }
}
```

### TextButtonStyle (Button + Label per state)
```kotlin
textButtonStyle {
    default = textButtonStyleState {
        background = patchStyle { ... }
        labelStyle = labelStyle { ... }
    }
    // hovered, pressed, disabled...
    sounds = buttonSounds { ... }
}
```

### Other Style Types
- **CheckBoxStyle**: `checked`/`unchecked` states with `defaultBackground`/`hoveredBackground`/`pressedBackground`
- **InputFieldStyle**: `textColor`, `fontSize`
- **SliderStyle**: `background`, `handle`, `handleWidth`, `handleHeight`, `sounds`
- **ScrollbarStyle**: `spacing`, `size`, `background`, `handle`, `hoveredHandle`, `draggedHandle`
- **DropdownBoxStyle**: Complete dropdown config (backgrounds, arrows, label styles, panel, entries)
- **TabNavigationStyle**: `tabStyle`, `selectedTabStyle`, `separatorAnchor`
- **ColorPickerStyle**: Selector backgrounds, button fills, text field config
- **TextTooltipStyle**: `background`, `maxWidth`, `labelStyle`, `padding`

### Padding
```kotlin
padding {
    left = 10; right = 10; top = 10; bottom = 10
    full = 10          // All sides
    horizontal = 10    // Left + Right
    vertical = 10      // Top + Bottom
}
```

### Anchor
```kotlin
anchor {
    left = 10; right = 10; top = 10; bottom = 10
    width = 200; height = 100
    full = 0           // Fill parent
    horizontal = 10    // Left + Right
    vertical = 10      // Top + Bottom
    minWidth = 50; maxWidth = 300
}
```

---

## Enums

| Enum | Values |
|------|--------|
| **LayoutMode** | Full, Left, Right, Top, Bottom, Center, Middle, CenterMiddle, MiddleCenter, LeftCenterWrap, LeftScrolling, RightScrolling, TopScrolling, BottomScrolling |
| **LabelAlignment** | Start, Center, End |
| **ProgressBarDirection** | LeftToRight, RightToLeft, TopToBottom, BottomToTop |
| **ProgressBarAlignment** | Start, Center, End |
| **TimerDirection** | Up, Down |
| **TooltipAlignment** | Left, Right, Top, Bottom |
| **ColorFormat** | RGB, HSV (and others) |
| **DropdownBoxAlign** | Left, Right, Top, Bottom |
| **InputFieldButtonSide** | Left, Right |
| **InputFieldIconSide** | Left, Right |
| **ResizeType** | (resize behavior options) |
| **MouseWheelScrollBehaviourType** | (scroll behavior options) |
| **ActionButtonAlignment** | (action button alignment options) |
| **CodeEditorLanguage** | (syntax highlighting languages) |
| **ItemGridInfoDisplayMode** | (item info display options) |

---

## Event System

### Event Types by Element

**Buttons/Interactive:**
- `Activating` - Click/activate
- `DoubleClicking` - Double-click
- `RightClicking` - Right-click
- `MouseEntered` / `MouseExited` - Hover

**Input Fields:**
- `ValueChanged` - Value modified
- `FocusGained` / `FocusLost` - Focus changes
- `Validating` - Enter key pressed
- `Dismissing` - Escape key pressed

**Sliders:**
- `ValueChanged` - Slider moved
- `MouseButtonReleased` - Drag ended

**Containers:**
- `Scrolled` - Scroll event
- `DropdownToggled` - Dropdown open/close

**Labels:**
- `LinkActivating` - Hyperlink clicked
- `TagMouseEntered` - Formatted text tag hover

---

## Update/Diff Mechanism

**Initial load:** Full `.ui` file sent once to client.

**Updates:** Server sends targeted commands to modify existing UI:
- Property change: `set("#NodeId.PropertyName", value)`
- Add child: `append("#ParentId", "path/to/node.ui")`
- Remove child: `remove("#ChildId")`
- Clear children: `clear("#ParentId")`
- Insert before: `insertBefore("#SiblingId", "path/to/node.ui")`

**Selectors:** Use `#NodeId` for identified nodes, `#Parent[index]` for positional references, chained as `#Parent #Child.Property`.

---

## .ui File Organization

- **Common.ui** - Central style/component definitions, imports Sounds.ui
- **Sounds.ui** - Sound asset definitions with paths/volumes
- **Page files** - Import Common.ui via `$C = "../Common.ui"`, reference as `$C.@VariableName`
- Assets stored at: `Common/UI/Custom/Pages/UiManager/`
