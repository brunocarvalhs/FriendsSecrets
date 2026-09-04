# ADR 0001: Use Clean Architecture and Modularization

## Status
Accepted

## Context
The Friends Secrets application needs to be scalable, testable, and maintainable. As a privacy-focused app, it's crucial to have a clear separation between business logic, data handling, and UI. Furthermore, to facilitate parallel development and minimize build times, a modular approach is required.

## Decision
We decided to implement **Clean Architecture** combined with a **Feature-based Modularization** strategy.

1.  **Clean Architecture:**
    *   **Domain Layer:** Contains pure business logic (Use Cases) and entities. No dependencies on Android frameworks.
    *   **Data Layer:** Handles data sources (Firestore, Local Storage) and repository implementations.
    *   **Presentation Layer:** Uses MVVM pattern with Jetpack Compose.

2.  **Modularization:**
    *   **`:app`**: Glue module for DI and navigation setup.
    *   **`:features:*`**: Each feature (e.g., chat, group creation) resides in its own module.
    *   **`:core:*`**: Shared infrastructure logic (e.g., network, security, biometric).

## Consequences
*   **Pros:** High testability, clear separation of concerns, faster incremental builds, and reduced merge conflicts.
*   **Cons:** Increased initial setup complexity and more boilerplate for small features.
*   **AI Context:** AI agents must respect these boundaries and avoid creating cross-module dependencies directly; instead, they should use `:core:navigation`.
