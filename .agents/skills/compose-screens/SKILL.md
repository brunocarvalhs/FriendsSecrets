---
name: compose-screens
description: Use this skill when writing Jetpack Compose UI in the author's style. It defines conventions for screen composables (internal, viewModel-driven via collectAsState, Material 3 components, stringResource for text), component decomposition into a components/ package, detekt Compose rule compliance (modifier order, naming, lambda offsets), and state hoisting with zero business logic in UI. Follow it for any new screen or UI component.
metadata:
  author: bruno (FriendsSecrets style)
  last-updated: '2026-08-17'
  keywords:
  - Jetpack Compose
  - Material 3
  - Composable
  - Screen
  - Components
  - State hoisting
  - Detekt Compose
---

## Purpose

Standardize Compose UI so screens are passive, decomposable, and lint-clean.

## Core principles

- **Screen signature:** `internal @Composable fun <X>Screen(viewModel: <X>ViewModel, callbacks: ...)` — screen receives the ViewModel + navigation callbacks.
- **State read:** `val state by viewModel.uiState.collectAsState()`.
- **Events:** UI never mutates state directly — always `viewModel.handleIntent(<X>Intent.X(...))`.
- **Material 3** components (`Scaffold`, `TopAppBar`, `Button`, `Card`, `AlertDialog`, ...); `@OptIn(ExperimentalMaterial3Api::class)` when needed.
- **Strings:** `stringResource(R.string.x)` — no hardcoded UI text.
- **Components:** reusable pieces extracted to `app/presentation/components/` (e.g. `GroupCard.kt`, `LoadingProgress.kt`, `ErrorComponent.kt`).
- **Passive UI:** no business logic in composables; side effects via `LaunchedEffect` (e.g. scroll-to-bottom on new message).
- **detekt Compose compliance** (see `kotlin-code-style-detekt`): correct `Modifier` order (size → layout → visual), `@Composable` naming, lambda offsets, no composable as modifier.

## Code example (reference skeleton)

```kotlin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ChatScreen(
    viewModel: ChatViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()

    LaunchedEffect(state.messages.size) {
        if (state.messages.isNotEmpty()) listState.animateScrollToItem(state.messages.size - 1)
    }

    if (state.showIdentificationModal) {
        IdentificationDialog(
            onConfirm = { viewModel.handleIntent(ChatIntent.IdentifyUser(it)) },
            onDismiss = { viewModel.handleIntent(ChatIntent.DismissIdentification); onBack() }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.groupModel.name) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(Modifier.fillMaxSize().padding(paddingValues)) {
            // content
        }
    }
}
```

## Step-by-Step

1. Create `internal @Composable fun <X>Screen(viewModel, callbacks)`.
2. `collectAsState()` → `state`; render from state fields only.
3. Wire every interaction to `viewModel.handleIntent(...)`.
4. Extract repeated UI into `components/` composables with explicit parameters.
5. Add `@Preview` for components (allowed by detekt `allowedNames: '.*Preview'`).
6. Verify modifier order and naming against the Compose detekt rules.

## Decision rules

- **Reusable across screens?** → `components/` file; **screen-specific block > ~15 lines** → also extract.
- **Dialog/bottom-sheet visibility** → driven by `UiState` flag, rendered conditionally at screen level.
- **Async scroll/list effects** → `LaunchedEffect(key)`; never `remember` coroutines.
- **Text content** → `stringResource`; literal strings only in previews.
- **Icons** → `Icons.*` from material-icons-extended; always `contentDescription` (accessibility).
- **Hardcoded dp/sp** → inline modifiers or small constants — no magic numbers beyond detekt whitelist.