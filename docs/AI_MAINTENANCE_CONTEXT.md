# AI Maintenance Context: Friends Secrets

This document provides essential context for AI agents assisting in the maintenance, refactoring, or feature development of the Friends Secrets Android application.

---

## 1. Core Mission & Persona
Friends Secrets is a **privacy-first** social app for anonymous interactions and Secret Santa organization.
*   **Tone:** Professional, secure, modern, and user-centric.
*   **Key Constraint:** Privacy is non-negotiable. Data processing should be on-device whenever possible.

## 2. Architectural Guardrails (Mandatory)
Any AI-generated code or refactoring must strictly adhere to:

### 2.1 Clean Architecture & Modularization
*   **Layering:** Inward dependency flow: `Data` → `Domain` ← `Presentation`.
*   **Modularization:** 
    *   `:features:*` modules **must not** depend on each other. Communication happens via `:core:navigation`.
    *   Common logic goes to `:core:*` modules.
    *   New features must be modularized by default.

### 2.2 Unidirectional Data Flow (UDF)
*   **State:** ViewModels expose a single `uiState` using `StateFlow`.
*   **Events:** Views send actions/intents to ViewModels. No direct state manipulation from the View.
*   **Immutability:** All state objects must be immutable (`data class` with `val`).

## 3. Tech Stack (2026 Standards)
*   **Language:** Kotlin 2.x (Use context parameters and latest idioms).
*   **UI:** Jetpack Compose with Material 3 (Adaptive Layouts).
*   **DI:** Hilt (Standard for dependency injection).
*   **Async:** Kotlin Coroutines & Flow (Strictly no RxJava or LiveData).
*   **AI:** Google Gemini SDK for intelligent features (Gift suggestions).

## 4. Specific Instructions for AI Agents

### 4.1 When creating new Features:
1.  Check if a new `:features:<name>` module is needed.
2.  Define the `UseCase` in the domain layer before the `ViewModel`.
3.  Ensure `contentDescription` is provided for all UI elements (A11y).

### 4.2 When modifying the Data Layer:
1.  Always use Mappers to convert DTOs (Network/Storage) to Domain Entities.
2.  Sensitive data (like contacts) must be handled in `:core:security` or locally within the module.

### 4.3 Testing Requirements:
*   Use **MockK** for mocking.
*   Use **Turbine** for testing Flows.
*   Suggest Unit Tests for every new UseCase or ViewModel logic.

## 5. Security & Compliance
*   **GDPR/LGPD:** Never suggest logging PII (Personally Identifiable Information).
*   **Biometrics:** Use `:core:biometric` for any sensitive screen access.
*   **KeyStore:** Use for token storage, never hardcode keys or use plain `SharedPreferences`.

## 6. How to Run Tasks
*   **Build:** `./gradlew assembleDebug`
*   **Tests:** `./gradlew test`
*   **Lint:** `./gradlew detekt`

---
*Note: This document is a living guide. AI agents should prioritize these rules over generic Android patterns.*
