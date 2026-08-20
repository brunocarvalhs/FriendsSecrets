---
name: type-safe-navigation
description: Use this skill when implementing Navigation Compose routes in the author's type-safe style. It defines conventions for @Serializable routers and graphs, serializing non-primitive arguments via a typeMap with custom NavType serializers, centralizing shared graphs in a navigation core module, and navigating through a CommonNavigator interface backed by an AppNavigator implementation. Follow it for any new destination so navigation stays compile-time safe and consistent.
metadata:
  author: bruno (FriendsSecrets style)
  last-updated: '2026-08-17'
  keywords:
  - Navigation Compose
  - Type-safe navigation
  - @Serializable routes
  - NavTypeSerializer
  - typeMap
  - CommonNavigator
  - AppNavigator
---

## Purpose

Standardize type-safe navigation: every destination is a `@Serializable` type, arguments are typed, and navigation calls are routed through a navigator abstraction.

## Core principles

- **Destinations are `@Serializable`:**
  - `data object <X>Router` — no arguments.
  - `data class <X>Graph(val group: GroupModel)` — with typed arguments (`kotlinx.serialization`).
- **Non-primitive arguments need a `typeMap`** in a companion object, built from `navTypeSerializer<T>()` / `navTypeSerializerNullable<T>()` provided by the navigation core module.
- **Shared graphs live in the navigation core module** (`core:navigation/.../routers/Routers.kt`); per-feature leaf routers live in the feature (`commons/navigation/<X>Router.kt`).
- **Navigation calls go through `CommonNavigator`** (interface) → `AppNavigator` (`@Singleton` impl) using `launchSingleTop` / `popUpTo` as needed.
- **Route args are read in the ViewModel** via `savedStateHandle.toRoute<XGraph>(XGraph.typeMap)`.

## Code example (reference)

```kotlin
// shared graph (core:navigation/routers/Routers.kt)
@Serializable
data class ChatGraph(val group: GroupModel) {
    companion object {
        val typeMap = mapOf(
            typeOf<GroupModel>() to navTypeSerializer<GroupModel>()
        )
    }
}

// per-feature router (features/<x>/commons/navigation/)
@Serializable
internal data object ChatRouter
```

```kotlin
// navigator abstraction (core:navigation)
interface CommonNavigator {
    fun navigateToChat(navController: NavHostController, group: GroupModel)
}

@Singleton
class AppNavigator @Inject constructor() : CommonNavigator {
    override fun navigateToChat(navController: NavHostController, group: GroupModel) {
        navController.navigate(ChatGraph(group)) { launchSingleTop = true }
    }
}
```

## Step-by-Step

1. New destination with no args → `@Serializable data object <X>Router` in the feature.
2. New destination with args → `@Serializable data class <X>Graph(val <arg>: <Type>)` + `typeMap` with serializers for each non-primitive type.
3. Shared across features? Put in `core:navigation/routers/Routers.kt`; feature-only? keep in feature `commons/navigation/`.
4. Add a `navigateToX` method to `CommonNavigator` + implement in `AppNavigator` (add `launchSingleTop = true` to avoid back-stack duplication).
5. In the ViewModel, read args: `savedStateHandle.toRoute<XGraph>(XGraph.typeMap)`.
6. Register the destination in the feature's initializer (`feature-initializer-pattern`).

## Decision rules

- **Args needed?** → `data class` graph; **no args?** → `data object` router.
- **Graph shared by >1 feature?** → `core:navigation`; **feature-private?** → feature `commons/navigation/`.
- **Serialization of custom model?** → implement/test `NavTypeSerializer`; add to `typeMap`; nullable variant via `navTypeSerializerNullable<T>()`.
- **Duplicate destination on re-navigate?** → `launchSingleTop = true`.
- **Return-to-root flows** → `popUpTo(graph) { inclusive = ... }` in the navigate builder.
- **Navigation from UI** → composables receive callbacks (`onBack`, `onOpenX`), never a `NavHostController` — it stays in initializer/`mainApp`.