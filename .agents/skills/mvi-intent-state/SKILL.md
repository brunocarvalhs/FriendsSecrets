---
name: mvi-intent-state
description: Use this skill when defining the MVI state contract for a screen — the sealed Intent class and the immutable UiState data class. It defines the author's conventions for event modeling (data class with payload vs object), state immutability with defaults, naming, and placement. Follow it before writing any ViewModel so events and state stay predictable and testable.
metadata:
  author: bruno (FriendsSecrets style)
  last-updated: '2026-08-17'
  keywords:
  - MVI
  - Intent
  - UiState
  - Sealed class
  - Immutable state
  - StateFlow
---

## Purpose

Standardize the two types that define a screen's contract: `Intent` (user/UI events) and `UiState` (immutable snapshot of what the UI renders).

## Core principles

- **Intent = sealed class**, one subtype per user action.
  - `object` when the action carries no payload (e.g. `SendMessage`).
  - `data class` when it carries payload (e.g. `UpdateInput(val text: String)`).
- **UiState = immutable `data class`** with default values for every optional field — the ViewModel constructs it with zero required args where possible.
- Both are `internal` to the feature.
- Placed in `app/presentation` next to the ViewModel/Screen, named `<Feature>Intent` / `<Feature>UiState`.
- UiState holds everything the screen renders: data lists, input text, flags controlling dialogs, loading/error markers.

## Code example (reference)

```kotlin
internal sealed class ChatIntent {
    data class UpdateInput(val text: String) : ChatIntent()
    object SendMessage : ChatIntent()
    object LoadMessages : ChatIntent()
    object ClearChat : ChatIntent()
    data class IdentifyUser(val name: String) : ChatIntent()
    object DismissIdentification : ChatIntent()
}
```

```kotlin
internal data class ChatUiState(
    val groupModel: GroupModel,
    val messages: List<ChatMessage> = emptyList(),
    val inputText: String = "",
    val isLoading: Boolean = false,
    val chatTitle: String = "Chat Secreto",
    val currentUserNickname: String = "",
    val showIdentificationModal: Boolean = false
)
```

## Step-by-Step

1. Enumerate every user action the screen supports → one `Intent` subtype each.
2. With payload? `data class`; without? `object`.
3. Enumerate everything the screen renders → UiState fields.
4. Default every field that can have a neutral initial value (`emptyList()`, `""`, `false`).
5. Non-default fields (required args, e.g. an entity the screen opens with) stay constructor-required.
6. Keep state flat: UI flags as booleans (e.g. `showIdentificationModal`), not nested state objects.

## Decision rules

- **Action carries user input?** → `data class` with the value as `val` property.
- **Action is a plain trigger?** → `object`.
- **Field changes on one event?** → belongs in UiState (e.g. `inputText`).
- **Modal/dialog visibility** → boolean flag in UiState, driven by events (show/dismiss) — never local `remember` state if it must survive config changes.
- **Derivable values?** → compute in ViewModel/Screen, don't store in UiState.
- **Null vs default:** prefer defaults over nullables; use nullable only when "absent" is meaningfully different from "empty".