---
name: firebase-services
description: Use this skill when integrating Firebase into an Android app in the author's style. It defines app-startup initialization via androidx.startup Initializer with chained dependencies, Firestore/Realtime Database access hidden behind services and compatibility converters, @AddTrace instrumentation for Firebase Performance, and Remote Config-backed feature flags. Follow it when adding Firebase products, initializers, or data services.
metadata:
  author: bruno (FriendsSecrets style)
  last-updated: '2026-08-17'
  keywords:
  - Firebase
  - androidx.startup
  - Initializer
  - Firestore
  - Realtime Database
  - Remote Config
  - Firebase Performance
  - Crashlytics
---

## Purpose

Standardize Firebase integration: startup initialization, service-wrapped data access, and performance instrumentation.

## Core principles

- **App startup via `androidx.startup.Initializer<T>`** — one initializer per Firebase product, chained via `dependencies()`:
  ```kotlin
  class FirebaseInitializer : Initializer<FirebaseApp> {
      override fun create(context: Context): FirebaseApp = FirebaseApp.getInstance()
      override fun dependencies(): List<Class<out Initializer<*>>> = mutableListOf()
  }

  class AnalyticsInitializer : Initializer<FirebaseAnalytics> {
      override fun create(context: Context): FirebaseAnalytics =
          FirebaseAnalytics.getInstance(context).apply {
              setAnalyticsCollectionEnabled(!BuildConfig.DEBUG)
              // wire device id: setUserId / setUserProperty
          }
      override fun dependencies(): List<Class<out Initializer<*>>> =
          mutableListOf(FirebaseInitializer::class.java)
  }
  ```
- **Data access behind services:** ViewModel/domain never touches Firebase APIs — `FirebaseRealtimeManager`, `NetworkManager` etc. implement `domain/services` contracts (see `repository-pattern`).
- **Compatibility conversions:** documents/snapshots → domain models via converters (e.g. `FirebaseCompatibilityConverter`) — no Firebase types crossing the domain boundary.
- **Performance:** `@AddTrace(name = "Class.method", enabled = true)` on instrumented operations across ViewModels, initializers, builders.
- **Feature flags:** Remote Config consumed through `FeatureFlagService.validate(key, default)` + per-feature `FeatureFlags` wrappers (`feature-flags` skill).
- **Modules:** app-level `initializers/` package for startup; per-feature `data/services` for Firebase managers; `core:network`/`core:remote`/`core:analytics`/`core:logger` for shared access.

## Step-by-Step

1. Add the Firebase dependency (BOM + product) to the module build file.
2. Create/extend an `Initializer<T>` in the app `initializers/` package; declare `dependencies()`.
3. Build the service contract (domain) + Firebase manager impl (data); bind via Hilt.
4. Convert Firebase types to domain models at the data edge.
5. Annotate hot paths `@AddTrace`; verify traces appear in Firebase Performance dashboard.

## Decision rules

- **New Firebase product?** → own `Initializer`; chain dependencies (e.g. analytics needs `FirebaseInitializer`).
- **ViewModel needs data from Firestore/RTDB?** → never inject Firebase classes — inject the service contract.
- **Remote-configurable behavior?** → flag via `FeatureFlagService` + `FeatureFlags`, never hardcoded.
- **Timber vs Crashlytics:** `Timber` for dev logs; Crashlytics for crash reporting; analytics events for user behavior.