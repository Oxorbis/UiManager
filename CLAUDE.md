# UiManager - Hytale UI Manager Plugin

## Project Overview

A Kotlin server-side plugin for Hytale that provides a type-safe DSL for building, serializing, and dynamically updating Hytale UI. It handles page lifecycle, automatic dirty tracking with partial updates, event handling, and form binding.

## Build & Test

```bash
./gradlew build          # Build the plugin (fat JAR)
./gradlew test           # Run tests
./gradlew generateUi     # Regenerate node/type/enum classes from ui_structure_report.json
```

- **Kotlin JVM** targeting Java 24
- Dependencies: Hytale Server JAR (local), Kotlin Reflect, KotlinPoet 2.0.0, GSON 2.11.0
- Output: Fat JAR excluding Kotlin stdlib duplicates

## Architecture Overview

See `CLAUDE-architecture.md` for detailed architecture documentation.
See `CLAUDE-hytale-ui.md` for Hytale UI system reference.

## Key Source Locations

| Area | Path |
|------|------|
| Core plugin | `src/main/kotlin/cz/creeperface/hytale/uimanager/` |
| Node classes (generated) | `src/main/kotlin/cz/creeperface/hytale/uimanager/node/` |
| Node events (generated) | `src/main/kotlin/cz/creeperface/hytale/uimanager/node/event/` |
| Type classes (generated) | `src/main/kotlin/cz/creeperface/hytale/uimanager/type/` |
| Enum classes (generated) | `src/main/kotlin/cz/creeperface/hytale/uimanager/enum/` |
| DSL builders (generated) | `src/main/kotlin/cz/creeperface/hytale/uimanager/builder/` |
| Property system | `src/main/kotlin/cz/creeperface/hytale/uimanager/property/` |
| Serializer & diff | `src/main/kotlin/cz/creeperface/hytale/uimanager/serializer/` |
| Forms | `src/main/kotlin/cz/creeperface/hytale/uimanager/special/` |
| Templates | `src/main/kotlin/cz/creeperface/hytale/uimanager/templates/` |
| Code generator | `src/uigen/` |
| Hytale UI docs | `src/main/resources/docs/custom-ui/` |
| Example .ui files | `src/main/resources/*.ui` |
| Tests | `src/test/kotlin/` |

## Code Generation

Node classes, type classes, enum classes, builder functions, and event builder functions are **auto-generated** from `src/uigen/resources/ui_structure_report.json` using `src/uigen/kotlin/cz/creeperface/hytale/uimanager/uiGenerator.kt`. Do NOT manually edit files in `node/`, `type/`, `enum/`, or `builder/` packages unless adding non-generated extensions.

## Key Patterns

- **DSL entry**: `customUi { ... }` creates a `UiPage` via `UiBuilder`
- **Node IDs**: Auto-generated hierarchically by `IdGenerator` (e.g., `ParentGroupLabel1`)
- **Properties**: All use `rebindable()` delegates for dirty tracking
- **Property binding**: `::text.bind(data::time)` binds a UI property to an observable data property
- **Events**: `onActivate(field1::value, field2::value) { v1, v2 -> ... }`
- **Forms**: `form<DataClass> { boundTextField(DataClass::field) { ... }; submitTextButton { ... } }`
- **Templates**: `CommonTemplate` provides pre-styled helpers (`defaultTextButton`, `defaultTextField`, `pageOverlay`, etc.)
- **Containers**: `container { title { ... }; content { ... } }` and `decoratedContainer { ... }`
- **List groups**: `listGroup { ... }` for ordered lists with proper diff handling (add/remove/reorder)

## Conventions

- Color format: `Color("#RRGGBB")` or `Color("#RRGGBB", alpha)` where alpha is 0.0-1.0
- Texture paths: Relative to asset root, typically `../../Common/...` or `../../Textures/...`
- Server-side string interpolation uses `%` prefix: `text = "%server.customUI.title"`
- Node names match Hytale element names (Group, Label, Button, TextButton, Panel, etc.)
- `@ExcludeProperty` annotation excludes properties from serialization
- `omitName = true` makes a node serialize without its type name (used for template merging)