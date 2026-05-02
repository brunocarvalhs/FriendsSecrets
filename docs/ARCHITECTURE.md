# Architecture Documentation: Friends Secrets

## 1. Executive Summary
Friends Secrets is built on the principles of **Clean Architecture** combined with the **MVVM (Model-View-ViewModel)** pattern. This architectural choice ensures a strict separation of concerns, high testability, and the modularity required for a global-scale Android application.

## 2. Architectural Principles

### 2.1 Clean Architecture
The codebase is organized into concentric layers, where dependencies flow inward toward the business logic.
1.  **Presentation Layer:** UI components and state management.
2.  **Domain Layer:** Pure business logic and entity definitions (Framework-independent).
3.  **Data Layer:** Data sources, repositories, and persistence logic.

### 2.2 Modularization Strategy
To support scalability, the project is divided into feature-based and core-logic modules:
*   **`:app`**: The main entry point, handling dependency injection and global configuration.
*   **`:features:*`**: Self-contained modules (e.g., `:features:chat`, `:features:group:create`) containing their own UI and ViewModels.
*   **`:core:*`**: Shared utilities and low-level logic (e.g., `:core:network`, `:core:biometric`, `:core:analytics`).

## 3. Layer Breakdown

### 3.1 Presentation Layer (Jetpack Compose)
*   **Views:** Built entirely with declarative UI (Jetpack Compose) using Material 3.
*   **ViewModels:** Utilize `StateFlow` to expose immutable UI states. They handle user intent and communicate with Use Cases.
*   **UI State Pattern:**
    ```kotlin
    data class UiState<out T>(
        val isLoading: Boolean = false,
        val data: T? = null,
        val error: Throwable? = null
    )
    ```

### 3.2 Domain Layer (Pure Kotlin)
*   **Entities:** Business models that are consistent across the entire application.
*   **Use Cases (Interactors):** Single-purpose classes that encapsulate specific business rules (e.g., `DrawSecretSantaUseCase`).
*   **Repository Interfaces:** Define the data contracts required by the domain.

### 3.3 Data Layer
*   **Repository Implementations:** Coordinate data between multiple sources (Local vs. Remote).
*   **Data Sources:**
    *   **Remote:** Firebase Firestore, Cloud Functions, and Gemini AI API.
    *   **Local:** Encrypted Shared Preferences and Room (if applicable).
*   **Mappers:** Convert Data Transfer Objects (DTOs) into Domain Entities.

## 4. Technical Specifications (2026 Stack)

### 4.1 Dependency Injection
We use a modularized approach to DI (Hilt/Koin) to ensure that components are decoupled and easily swappable for testing.

### 4.2 Asynchronous Programming
**Kotlin Coroutines and Flow** are the standard for all background operations, ensuring non-blocking UI and efficient resource management.

### 4.3 Security & Privacy
*   **Biometric Integration:** Secured via the `:core:biometric` module using the Android Biometric Security hardware.
*   **On-Device Processing:** Sensitive data like contact filtering is performed locally to comply with global privacy standards (GDPR/LGPD).

## 5. Data Flow (Unidirectional Data Flow)
1.  **User Action** → View captures the event.
2.  **Intent** → ViewModel receives the action.
3.  **Execution** → Use Case processes business logic.
4.  **Data Retrieval** → Repository fetches data from Source.
5.  **State Update** → ViewModel updates `StateFlow`.
6.  **Rendering** → View recomposes based on the new state.

## 6. Testing Strategy
*   **Unit Tests:** Target Use Cases and ViewModels using MockK/Turbine.
*   **Integration Tests:** Verify Repository-to-DataSource interactions.
*   **UI Tests:** Screenshot testing and Compose UI tests for critical user journeys.

---
© 2026 Brunocarvalhs. All rights reserved.
