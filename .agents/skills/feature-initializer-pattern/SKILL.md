---
name: feature-initializer-pattern
description: Use this skill when wiring a feature module's navigation into the app. It defines the author's FeatureInitializer pattern — a FeatureInitializer interface, a feature Initializer with a fluent Builder for callbacks, an InitializerImpl registered into Hilt via @Binds @IntoSet, and discovery through a set injected at the app entry point. Follow it whenever you create or modify a feature module so navigation self-registers without manual wiring in the app.
metadata:
  author: bruno (FriendsSecrets style)
  last-updated: '2026-08-17'
  keywords:
  - FeatureInitializer
  - Builder pattern
  - Navigation registration
  - Hilt IntoSet
  - NavGraphBuilder
  - hiltViewModel
---

## Purpose

Standardize how features register their navigation graphs: zero manual wiring in the app, automatic discovery via Hilt multibinding.

## Core principles

- **`FeatureInitializer` interface** (in navigation core module):
  ```kotlin
  interface FeatureInitializer {
      fun register(navGraphBuilder: NavGraphBuilder, navController: NavHostController)
  }
  ```
- **`<Feature>Initializer`** — class holding a nested fluent `Builder`; `build(navGraphBuilder)` registers a `navigation<XGraph>(startDestination = XRouter, typeMap = XGraph.typeMap) { composable<XRouter> { ... } }` block.
- **Builder pattern** for callbacks: `navController()`, `onBack()`, `build()` — each builder method annotated `@AddTrace`.
- **`<Feature>InitializerImpl`** — `@Inject constructor() : FeatureInitializer`; implementation calls `XInitializer.Builder().navController(...).onBack { navController.popBackStack() }.build(navGraphBuilder)`.
- **Registration via Hilt multibinding:** `@Binds @IntoSet` inside the feature's Hilt module (`hilt-di-conventions`).
- **Discovery at entry point:** app injects `Set<@JvmSuppressWildcards FeatureInitializer>` (Activity) and registers all in `NavHostController.mainApp`:
  ```kotlin
  initializers.forEach { it.register(this, this@mainApp) }
  ```
- ViewModel instantiation inside the graph uses `hiltViewModel<XViewModel>()`.

## Code example (reference skeleton)

```kotlin
class ChatInitializer(private val builder: Builder) {
    fun build(navGraphBuilder: NavGraphBuilder) {
        navGraphBuilder.navigation<ChatGraph>(
            startDestination = ChatRouter,
            typeMap = ChatGraph.typeMap,
        ) {
            composable<ChatRouter> {
                val viewModel = hiltViewModel<ChatViewModel>()
                ChatScreen(viewModel = viewModel, onBack = builder.onBack)
            }
        }
    }

    class Builder {
        internal var navController: NavHostController by Delegates.notNull()
        internal var onBack: () -> Unit = {}

        @AddTrace(name = "ChatInitializer.Builder.navController", enabled = true)
        fun navController(navController: NavHostController) = apply { this.navController = navController }

        @AddTrace(name = "ChatInitializer.Builder.onBack", enabled = true)
        fun onBack(onBack: () -> Unit) = apply { this.onBack = onBack }

        @AddTrace(name = "ChatInitializer.Builder.build", enabled = true)
        fun build(navGraphBuilder: NavGraphBuilder): ChatInitializer =
            ChatInitializer(this).also { it.build(navGraphBuilder) }
    }
}
```

```kotlin
class ChatInitializerImpl @Inject constructor() : FeatureInitializer {
    override fun register(navGraphBuilder: NavGraphBuilder, navController: NavHostController) {
        ChatInitializer.Builder()
            .navController(navController)
            .onBack { navController.popBackStack() }
            .build(navGraphBuilder)
    }
}
```

## Step-by-Step

1. Create `<Feature>Initializer.kt` (as above) with the nested `Builder`.
2. Register the graph: `navigation<XGraph>(startDestination = XRouter, typeMap = XGraph.typeMap)`.
3. Create `<Feature>InitializerImpl.kt` implementing `FeatureInitializer`.
4. Add `@Binds @IntoSet fun bind<X>Initializer(impl: <X>InitializerImpl): FeatureInitializer` to the feature Hilt module.
5. App side already handles registration via the injected `Set<FeatureInitializer>` — no changes needed for new features.

## Decision rules

- **New feature module?** → always ship `Initializer` + `InitializerImpl` + `@IntoSet` binding; never hand-edit `mainApp`/`MainActivity`.
- **Callbacks beyond onBack?** (e.g. `onOpenDetails`) → add a builder method mirroring the `navController`/`onBack` style.
- **Feature needs DI at graph level?** → `hiltViewModel()` inside `composable<>`; screens never construct ViewModels manually.
- **Annotate builder methods `@AddTrace`** — consistent with the codebase's performance instrumentation.