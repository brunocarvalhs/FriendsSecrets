# Global Best Practices: Friends Secrets (2026 Edition)

This document outlines the professional engineering standards and best practices for the Friends Secrets platform. Adhering to these guidelines ensures high-quality code, global compliance, and a superior user experience.

---

## 1. Development Standards

### 1.1 Kotlin & Jetpack Compose
*   **Kotlin 2.x Idioms:** Utilize the latest language features (Context Parameters, context-receivers, etc.) for concise and safe code.
*   **Declarative UI:** All new features must be built using Jetpack Compose with Material 3 Adaptive design.
*   **Recomposition Optimization:** Use `@Stable`, `@Immutable`, and `remember { derivedStateOf { ... } }` to minimize unnecessary UI updates.

### 1.2 Naming Conventions
*   **Modules:** Feature modules follow the pattern `:features:<name>` and core modules `:core:<name>`.
*   **Semantic UI:** Use meaningful names for Compose states (e.g., `uiState` rather than `data`).
*   **API Design:** Internal APIs should follow the "explicit-by-default" principle using Kotlin visibility modifiers.

---

## 2. Privacy & Security-First Engineering

### 2.1 Zero-Knowledge Principles
*   **On-Device Processing:** Any sensitive data (Contacts, Device Identifiers) must be processed locally within the appropriate module. Never transmit raw contact lists to external servers.
*   **Encryption at Rest:** Use `EncryptedSharedPreferences` and the Android Keystore system for all persistent sensitive tokens.

### 2.2 Global Compliance (GDPR/LGPD/CCPA)
*   **Consent Management:** Always use the centralized Consent API before initializing tracking or analytics services.
*   **Data Minimization:** Only request permissions (`READ_CONTACTS`, `BIOMETRIC`) at the exact moment they are required for a functional feature.

---

## 3. Architecture & State Management

### 3.1 Unidirectional Data Flow (UDF)
All UI components must strictly follow UDF. Events flow up to the ViewModel; immutable state flows down to the View.

### 3.2 Modularization
*   **Independence:** Feature modules must not depend on other feature modules.
*   **Core Logic:** Shared logic resides in `:core` modules.
*   **Dependency Injection:** Use Hilt for dependency management to ensure high testability.

---

## 4. Performance & Observability

### 4.1 Benchmarking
*   **Baseline Profiles:** Maintain and update Baseline Profiles to ensure optimal startup and scroll performance.
*   **Vitals Monitoring:** Monitor ANR (App Not Responding) rates and Crash-free sessions via Firebase Crashlytics.

### 4.2 Energy Efficiency
*   **WorkManager:** Use `WorkManager` for background tasks with appropriate constraints (e.g., `Charging`, `Idle`) to preserve battery life.

---

## 5. Quality Assurance

### 5.1 Testing Strategy
*   **Unit Tests:** Target 85%+ coverage for Domain logic and ViewModels.
*   **Integration Tests:** Verify module-to-module communication.
*   **Screenshot Testing:** Use screenshot tests to prevent UI regressions in the Compose layer.

### 5.2 CI/CD
*   **Danger & Lint:** Every Pull Request is automatically analyzed by Danger and Detekt for code quality and style violations.
*   **Automated Tests:** CI pipelines must pass all unit and instrumented tests before merging.

---

## 6. Global Accessibility (A11y)
*   **Screen Readers:** Every interactive element must have a clear `contentDescription`.
*   **Color Contrast:** Maintain WCAG 2.2 AA compliant contrast ratios in both Light and Dark modes.
*   **Dynamic Type:** Ensure the UI scales correctly with user-defined system font sizes.

---
© 2026 Brunocarvalhs. All rights reserved.
