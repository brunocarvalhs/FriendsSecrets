---
name: android-modular-architecture
description: Use this skill when structuring an Android project into multiple Gradle modules or deciding where code belongs. It defines the author's preferred layout — app entry module, reusable core modules, per-feature modules, and nested feature groups — plus build conventions (version catalog, plugin aliases, per-module detekt, JVM 17). Follow it to create new modules or place existing code so the project stays modular, testable, and scalable.
metadata:
  author: bruno (FriendsSecrets style)
  last-updated: '2026-08-17'
  keywords:
  - Android modules
  - Gradle structure
  - Multi-module
  - Modularization
  - Core vs feature modules
  - Build conventions
---

## Purpose

Standardize how Android projects are split into Gradle modules. Applies when creating a module, moving code between modules, or deciding where a new class belongs.

## Prerequisites

- Gradle project with version catalog (`gradle/libs.versions.toml`).
- Shared root configuration for detekt and JVM target.

## Core principles

- **One responsibility per module.** App module = entry point only. Core modules = reusable infrastructure. Feature modules = one user flow each.
- **Feature groups** for domain families: `:features:group:list`, `:features:group:create` share the `group` domain; keep that grouping instead of a single big `:features:group`.
- **Dependency direction is one-way: app → features → core.** Core modules never depend on features; features never depend on other features (share via core).
- **Core:domain is the base.** Pure Kotlin (no Android), holds shared models — every other module may depend on it.
- **Minimal public API.** Modules export only interfaces/classes other modules need; everything else stays `internal`.

## Module layout (reference)

```
:app                        # entry: Application, MainActivity, app-level initializers
:core:domain                # pure Kotlin: shared models
:core:<infra>               # e.g. core:analytics, core:navigation, core:network, core:security
:features:<flow>            # e.g. features:chat, features:settings, features:biometric
:features:<flow>:<sub>      # e.g. features:group:list, features:group:create, features:group:details
```

## Build conventions

- Versions live in `gradle/libs.versions.toml` under `[versions]`, `[libraries]`, `[plugins]`.
- Plugins applied via `alias(libs.plugins.<name>)` — never hardcoded ids in module build files.
- `compileSdk` / `minSdk` read from the catalog (`libs.versions.compileSdk.get().toInt()`).
- JVM 17: `compileOptions` + `kotlin.compilerOptions.jvmTarget`.
- Every module applies detekt against the root config:
  ```kotlin
  detekt {
      config.from(files("$rootDir/detekt.yml"))
  }
  ```
- Feature modules with Compose also add `buildFeatures { compose = true }` and the Compose plugin.

## Step-by-Step: create a feature module

1. Add `include(":features:<flow>")` to `settings.gradle.kts`.
2. Create `features/<flow>/build.gradle.kts` with library plugin + Compose + serialization + KSP + detekt.
3. Set `namespace = "br.com.brunocarvalhs.<flow>"`.
4. Add dependencies: the `:core:*` modules the feature needs — never other `:features:*`.
5. Add the feature's `Initializer` + `InitializerImpl` (see `feature-initializer-pattern` skill) so the app registers its navigation automatically.

## Step-by-Step: create a core module

1. Add `include(":core:<name>")`.
2. Decide: pure Kotlin (`core:domain` style, no Android plugin) vs Android library (needs `android`/`Context`).
3. Follow the core convention: public interface + implementation in `data/` + Hilt module (see `hilt-di-conventions` skill).
4. Keep the public surface minimal — interfaces only; implementations `internal` where possible.

## Decision rules

- **Where does this class go?** → Repository impl/DAO/Firebase service → `data`; contract/usecase/model → `domain`; composable/ViewModel → `presentation` (see `clean-architecture-layers`).
- **New shared util?** → prefer extending an existing `:core:` module over creating a new one; create a new `:core:` module only when the concern is genuinely reusable.
- **New screen?** → new `:features:` module or existing flow module, never `:app`.
- **Android-free logic?** → `core:domain` (or `domain` layer inside module) to keep it JVM-testable.
- **Does another feature need this code?** → promote to `:core:` first; never let features depend on each other.