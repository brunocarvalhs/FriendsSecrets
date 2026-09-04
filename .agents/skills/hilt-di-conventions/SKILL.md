---
name: hilt-di-conventions
description: Use this skill when setting up or extending dependency injection with Hilt in the author's style. It defines conventions for app/activity/viewmodel entry points, per-module Hilt modules (abstract with @Binds and companion @Provides), @IntoSet multibinding for initializers, and strict interface-to-implementation binding. Follow it when adding a new module, service, or initializer so DI stays uniform and testable.
metadata:
  author: bruno (FriendsSecrets style)
  last-updated: '2026-08-17'
  keywords:
  - Hilt
  - Dependency injection
  - @Binds
  - @Provides
  - @IntoSet
  - SingletonComponent
  - HiltViewModel
  - AndroidEntryPoint
---

## Purpose

Standardize Hilt usage: entry points, module structure, binding style, and multibinding for the feature-initializer pattern.

## Core principles

- **Entry points:**
  - `@HiltAndroidApp` on the `Application` class.
  - `@AndroidEntryPoint` on `Activity` (project's activity extends `FragmentActivity`).
  - `@HiltViewModel` on every ViewModel, with `@Inject constructor`.
- **One Hilt module per feature/core unit:** `commons/di/<X>Module.kt` (feature) or `di/<X>Module.kt` (core).
- **Module shape — abstract class + companion `@Provides`:**
  ```kotlin
  @Module
  @InstallIn(SingletonComponent::class)
  abstract class ChatModule {
      @Binds
      @IntoSet
      abstract fun bindChatInitializer(impl: ChatInitializerImpl): FeatureInitializer

      @Binds
      abstract fun bindChatService(impl: FirebaseRealtimeManager): ChatService

      @Binds
      abstract fun bindChatRepository(impl: ChatRepositoryImpl): ChatRepository

      companion object {
          @Provides
          @Singleton
          fun provideFirebaseDatabase(): FirebaseDatabase =
              FirebaseDatabase.getInstance().apply { setPersistenceEnabled(true) }
      }
  }
  ```
- **`@Binds` for interface→implementation** (repository/service/initializer contracts).
- **`@Provides @Singleton` only for third-party objects** (e.g. `FirebaseDatabase`) or objects needing config; keep in `companion object`.
- **`@IntoSet` for `FeatureInitializer`** so the app injects `Set<FeatureInitializer>` once and all features self-register.
- **Names:** interface `XService`/`XRepository`; impl `XImpl`/`XManager`.
- **Binding method names:** `bind<X>(...)` (e.g. `bindChatService`, `bindChatRepository`).

## Step-by-Step

1. Feature/core module: create `commons/di/<X>Module.kt` (`core` modules: `di/<X>Module.kt`).
2. Declare `@Module @InstallIn(SingletonComponent::class) abstract class <X>Module`.
3. Add `@Binds` per interface contract (repository, service, initializer).
4. Feature module: always add the `@Binds @IntoSet` initializer binding (see `feature-initializer-pattern`).
5. Third-party object needing setup? Add `@Provides @Singleton` in the companion object.
6. KSP Hilt compiler configured in the module build file (`ksp(libs.hilt.compiler)`).

## Decision rules

- **Interface contract with impl?** → `@Binds` (constructor-injectable impl only).
- **Factory-style object (Android/Firebase class)** → `@Provides @Singleton` in companion.
- **Feature cross-cutting registration?** → `@IntoSet` multibinding, consumed as `Set<@JvmSuppressWildcards FeatureInitializer>`.
- **ViewModel dependency?** → inject use cases/services via `@Inject constructor` — never `viewModel()` factories.
- **Scoping:** `@Singleton` for services/databases per the codebase; avoid narrower scopes unless required.
- **Never `new` in feature code** — always constructor injection.