---
name: clean-architecture-layers
description: Use this skill when organizing code inside an Android module into Clean Architecture layers or deciding where individual classes belong. It defines the author's per-feature layout — presentation, domain, data layers plus a cross-cutting commons package — with strict dependency direction and visibility rules. Follow it when adding use cases, repository contracts, or screens so the code stays testable and framework-independent at the core.
metadata:
  author: bruno (FriendsSecrets style)
  last-updated: '2026-08-17'
  keywords:
  - Clean Architecture
  - Layering
  - Domain layer
  - Data layer
  - Presentation layer
  - Repository contracts
  - Internal visibility
---

## Purpose

Standardize the internal package structure of a feature module and the dependency rules between layers. Applies when adding any class to a feature module.

## Core principles

- **Three layers** inside a feature: `app/presentation`, `app/domain`, `app/data`.
- **Cross-cutting feature concerns** live in `commons/`: `commons/di`, `commons/flags`, `commons/navigation`.
- **Dependency direction: presentation → domain → data.** Data implements domain contracts; presentation consumes them. Never the reverse.
- **Domain is pure.** No Android, no Firebase, no UI. Models, use cases, and repository/service interfaces only.
- **Visibility: `internal` by default** for everything inside a feature (ViewModel, Screen, Intent, UiState, flags, routers). Only contracts that cross module boundaries (or core module public APIs) are `public`.
- **Exceptions named after the domain condition**, placed in `data/exceptions` (e.g. `GroupAlreadyExistException`, `GroupNotFoundException`).

## Feature package layout (reference)

```
br.com.brunocarvalhs.<feature>/
├── <Feature>Initializer.kt          # navigation graph builder (see feature-initializer-pattern)
├── <Feature>InitializerImpl.kt      # FeatureInitializer impl, Hilt-injected
├── app/
│   ├── data/
│   │   ├── exceptions/              # domain exception types
│   │   ├── extensions/              # converters, formatters
│   │   ├── local/                   # Room database, DAOs, entities
│   │   ├── model/                   # DTOs / local models
│   │   ├── repository/              # <X>RepositoryImpl
│   │   └── services/                # Firebase/network managers
│   ├── domain/
│   │   ├── constants/
│   │   ├── model/                   # domain models (or imported from core:domain)
│   │   ├── repository/              # <X>Repository (interface)
│   │   ├── services/                # <X>Service (interface)
│   │   └── usecase/                 # <X>UseCase (invoke operator)
│   └── presentation/
│       ├── <X>Intent.kt             # sealed class (see mvi-intent-state)
│       ├── <X>Screen.kt             # @Composable (see compose-screens)
│       ├── <X>UiState.kt            # immutable data class
│       ├── <X>ViewModel.kt          # @Stable @HiltViewModel (see mvi-viewmodel)
│       └── components/              # reusable composables
└── commons/
    ├── di/<X>Module.kt              # Hilt module (see hilt-di-conventions)
    ├── flags/<X>FeatureFlags.kt     # feature flags (see feature-flags)
    └── navigation/<X>Router.kt      # @Serializable route
```

## Layer responsibilities

| Layer | Owns | Must not |
|---|---|---|
| presentation | Compose UI, UiState, Intent, ViewModel | business rules, data access |
| domain | use cases, entities, repository/service interfaces, constants | Android/UI/Firebase imports |
| data | repository impls, services (Firebase/RTDB), Room, DTOs, mappers, exceptions | decide UI behavior |

## Step-by-Step: add a use case

1. Define `app/domain/usecase/<X>UseCase.kt` — class with `operator fun invoke(...): Result<T>`.
2. Depend only on repository/service interfaces from `domain`.
3. Add an `app/domain/repository/<X>Repository.kt` interface if none exists.
4. Implement the repository in `app/data/repository/<X>RepositoryImpl.kt` and bind it via Hilt (`hilt-di-conventions`).
5. Test the use case with a mocked repository (`unit-testing-conventions`).

## Step-by-Step: add a screen

1. `presentation/<X>Intent.kt` + `<X>UiState.kt` (`mvi-intent-state`).
2. `presentation/<X>ViewModel.kt` (`mvi-viewmodel`).
3. `presentation/<X>Screen.kt` consuming `uiState.collectAsState()` (`compose-screens`).
4. Register route + initializer (`type-safe-navigation`, `feature-initializer-pattern`).

## Decision rules

- **Class touches Android/Firebase/Room?** → data layer.
- **Class contains business logic, no framework imports?** → domain layer.
- **Class renders state or converts events?** → presentation layer.
- **Needed by two layers?** → interface in domain, impl in data — never a concrete class both layers share.
- **Dto vs model split:** external/local payloads are DTOs (data/model); logic-facing objects are domain models; map between them with extensions/mappers.
- **Uncertain?** → put it in domain as an interface first; data impl follows.