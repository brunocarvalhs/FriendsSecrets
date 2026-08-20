---
name: mvi-viewmodel
description: Use this skill when writing a ViewModel in the author's MVI style. It defines the non-negotiables: @Stable @HiltViewModel with constructor injection, a private MutableStateFlow exposed as StateFlow via asStateFlow, updates only through update { it.copy(...) }, handleIntent dispatch via when, Firebase Performance @AddTrace on every method, and analytics logging on every user action. Follow it to keep ViewModels uniform, observable, and measurable across the app.
metadata:
  author: bruno (FriendsSecrets style)
  last-updated: '2026-08-17'
  keywords:
  - ViewModel
  - MVI
  - StateFlow
  - Hilt
  - Firebase Performance
  - Analytics
  - Use cases
---

## Purpose

Standardize how ViewModels are written so every screen follows the same observable, instrumented pattern.

## Core principles

- **Annotations:** `@Stable` + `@HiltViewModel`; `@Suppress("LongParameterList")` when constructor exceeds 6 params (detekt threshold).
- **Constructor injection only** — `@Inject constructor(...)`; dependencies are use cases and core services, never concrete repositories.
- **State:** private `_uiState: MutableStateFlow<XUiState>` + public `val uiState: StateFlow<XUiState> = _uiState.asStateFlow()`.
- **State mutation only via `_uiState.update { it.copy(...) }`** — never direct assignment outside init.
- **Events:** public `fun handleIntent(intent: XIntent)` with a `when` — each branch delegates to a private per-action method.
- **Instrumentation is mandatory:** every method carries `@AddTrace(name = "Class.method", enabled = true)` and logs analytics (`analyticsService.logEvent(...)` with `AnalyticsParam.ACTION to "method_name"`) at its start.
- **Use cases:** invoked via `operator invoke` inside `viewModelScope.launch`; `Result` handled with `onSuccess(::handler).onFailure(::error)` or `runCatching`.
- **Navigation args** read once via `savedStateHandle.toRoute<Graph>(Graph.typeMap)`.
- **Logging** via `Timber` (`Timber.d/e`), never `android.util.Log`.
- Class and methods are `internal` (feature visibility).

## Code example (reference skeleton)

```kotlin
@Stable
@HiltViewModel
@Suppress("LongParameterList")
internal class ChatViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getMessagesUseCase: GetMessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val deviceService: DeviceService,
    private val analyticsService: AnalyticsService
) : ViewModel() {

    private val args = savedStateHandle.toRoute<ChatGraph>(ChatGraph.typeMap)
    private val _uiState = MutableStateFlow(ChatUiState(groupModel = args.group))
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    init { initializer() }

    @AddTrace(name = "ChatViewModel.initializer", enabled = true)
    private fun initializer() {
        analyticsService.logEvent(
            name = AnalyticsEvent.VIEW,
            params = mapOf(AnalyticsParam.ACTION to "initializer")
        )
        viewModelScope.launch { /* setup, then observeMessages() */ }
    }

    @AddTrace(name = "ChatViewModel.handleIntent", enabled = true)
    fun handleIntent(intent: ChatIntent) {
        when (intent) {
            is ChatIntent.UpdateInput -> updateInput(intent.text)
            is ChatIntent.SendMessage -> sendMessage()
            is ChatIntent.LoadMessages -> observeMessages()
        }
    }

    @AddTrace(name = "ChatViewModel.updateInput", enabled = true)
    private fun updateInput(text: String) {
        analyticsService.logEvent(
            name = AnalyticsEvent.SUBMIT,
            params = mapOf(AnalyticsParam.ACTION to "update_input", AnalyticsParam.PARAM to text)
        )
        _uiState.update { it.copy(inputText = text) }
    }
}
```

## Step-by-Step

1. Copy the skeleton above; rename `ChatX` → `<Feature>X`.
2. Declare `@Stable @HiltViewModel internal class <Feature>ViewModel @Inject constructor(...)`.
3. Add constructor params: `savedStateHandle` (if route args) + use cases + core services.
4. Define `_uiState`/`uiState`; initialize state (with route args when needed).
5. Add `init { initializer() }`; inside: analytics VIEW + async setup in `viewModelScope.launch`.
6. Implement `handleIntent` + private methods; every method: `@AddTrace` + analytics log + state update.
7. Async ops: `viewModelScope.launch { useCase(...).onSuccess(::x).onFailure(::y) }`.

## Decision rules

- **New dependency?** → inject interface contracts (use case / service), never instantiate.
- **Method mutating state?** → must carry `@AddTrace` + analytics `ACTION to "snake_case_method_name"`.
- **Flow-based data (Room/Firebase)?** → `onEach { ... }.launchIn(viewModelScope)` or collect into state; never block.
- **Success/failure divergence?** → `Result` + separate `success`/`error` private handlers.
- **One-shot setup?** → `init`/`initializer()`; **event-triggered?** → inside the handler for that Intent.
- **ViewModel tests:** author's style skips ViewModel unit tests (covered via use case/repository tests) — see `unit-testing-conventions`.