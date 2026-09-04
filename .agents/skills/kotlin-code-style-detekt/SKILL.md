---
name: kotlin-code-style-detekt
description: Use this skill when writing Kotlin code or configuring lint/static analysis in the author's style. It defines naming conventions (PascalCase classes, camelCase functions/variables, SNAKE_CASE constants with feature_ flag prefix), code formatting (4-space indent, blank line between methods, max 120 columns), KDoc expectations, internal visibility discipline, and the exact detekt configuration (MagicNumber whitelist, LongParameterList 6, LongMethod 60, TooManyFunctions 12, Compose rules) plus version-catalog build conventions. Follow it for any new code so it passes the project's detekt pipeline.
metadata:
  author: bruno (FriendsSecrets style)
  last-updated: '2026-08-17'
  keywords:
  - Kotlin style
  - detekt
  - Naming conventions
  - KDoc
  - LongParameterList
  - MagicNumber
  - Compose detekt rules
  - Version catalog
---

## Purpose

Standardize Kotlin code style and the detekt configuration that enforces it, so generated code passes the static-analysis gate.

## Core principles

- **Naming:**
  - Classes/interfaces: `PascalCase` (`UserRepository`, `LoginViewModel`).
  - Functions/methods: `camelCase` (`getUserById`, `handleIntent`).
  - Variables: `camelCase` (`userName`, `isLoading`).
  - Constants: `SNAKE_CASE_UPPER` (`MAX_RETRY_COUNT`, `FEATURE_CHAT_ENABLED`).
  - Feature-flag keys: `feature_<name>_enabled` (snake_case runtime values).
- **Formatting:** 4-space indent (no tabs); max line length **120**; one blank line between methods; braces on new line for classes, same line for methods/control structures (Kotlin standard).
- **KDoc:** on public classes/functions (explain the "why"); internal code needs only when non-obvious.
- **Visibility:** `internal` default inside modules; `public` only for cross-module contracts.
- **detekt config (reference from the author's root detekt.yml):**
  ```yaml
  style:
    MaxLineLength: { maxLineLength: 120, excludeCommentStatements: true }
    MagicNumber:
      ignoreNumbers: ['-1','0','1','2','0xFF']
      ignorePropertyDeclaration: true
      ignoreAnnotation: true
      ignoreEnums: true
  complexity:
    CyclomaticComplexMethod: { threshold: 15, ignoreAnnotated: ['Composable'] }
    LongParameterList: { functionThreshold: 6, constructorThreshold: 6,
                          ignoreAnnotated: ['Composable', 'Inject', 'HiltViewModel'] }
    LongMethod: { threshold: 60, ignoreAnnotated: ['Composable'] }
    TooManyFunctions: { thresholdInClasses: 12 }
  Compose:
    active: true   # ComposeNaming, ComposeComposableModifier,
                   # ComposeModifierOrder, ComposeLambdaOffset
  ```
- **Consequences:** functions > 6 params → `@Suppress("LongParameterList")` (author uses it deliberately on ViewModels); > 12 functions per class → extract; magic numbers → named constants; method > 60 lines → split.

## Build conventions (version catalog)

- All versions/deps in `gradle/libs.versions.toml` (`[versions]`, `[libraries]`, `[plugins]`).
- Modules apply plugins via `alias(libs.plugins.<name>)`.
- Every module adds the detekt block:
  ```kotlin
  detekt { config.from(files("$rootDir/detekt.yml")) }
  ```
- JVM 17: `compileOptions { sourceCompatibility/targetCompatibility = JavaVersion.VERSION_17 }` + `kotlin { compilerOptions { jvmTarget.set(JvmTarget.JVM_17) } }`.
- Compose modules: `buildFeatures { compose = true }`.

## Step-by-Step

1. Follow naming + formatting above for all new code.
2. Add KDoc for public API; keep internal code lean.
3. Config changes → root `detekt.yml` (single source of truth).
4. Run `./gradlew detekt` before commit; fix findings in code (don't suppress without reason).

## Decision rules

- **> 6 constructor params?** → acceptable in ViewModels (annotated `@HiltViewModel`/`@Inject`) with `@Suppress("LongParameterList")`; otherwise split.
- **Magic number needed?** → extract `private const val`; whitelist already covers `-1,0,1,2,0xFF`.
- **Composable with many params?** → allowed (Composable ignored) but prefer a single state/model param.
- **New lint rule?** → add to `detekt.yml`, not per-file suppressions.