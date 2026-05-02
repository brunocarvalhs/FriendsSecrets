# Setup & Installation Guide

This document provides detailed instructions for setting up the development environment and running the Friends Secrets application locally.

## 1. System Requirements

### Software
- **Android Studio:** Ladybug (2024.2.1) or newer (Recommended: Latest stable version as of 2026).
- **JDK:** Java 21 or higher.
- **Gradle:** Version 8.10 or higher (managed via Gradle Wrapper).
- **Git:** Latest stable version.

### Hardware (Recommended)
- **Processor:** Apple M-series or Intel Core i7/i9 (or equivalent).
- **RAM:** 16GB minimum (32GB recommended for large-scale builds).
- **Storage:** 20GB+ of free SSD space.
- **Android Device/Emulator:** 
    - Physical device running Android 10 (API 29) or newer.
    - Emulator configured with an x86_64 or arm64 system image.

## 2. Environment Configuration

### Android Studio & SDK
1. Download and install the latest **Android Studio**.
2. Through the SDK Manager, ensure you have:
    - Android SDK Platform (latest stable API).
    - Android SDK Build-Tools.
    - Android Emulator & SDK Platform-Tools.

### Java Development Kit (JDK)
1. Verify your JDK version:
   ```bash
   java -version
   ```
2. We recommend using the **JetBrains Runtime** bundled with Android Studio or **OpenJDK 21+**.

## 3. Project Initialization

### Clone the Repository
```bash
git clone https://github.com/brunocarvalhs/FriendsSecrets.git
cd FriendsSecrets
```

### Firebase Configuration
Friends Secrets relies on Firebase for authentication, database, and analytics.
1. Create a project in the [Firebase Console](https://console.firebase.google.com/).
2. Add an Android App with package name: `br.com.brunocarvalhs.friendssecrets`.
3. Download `google-services.json` and place it in the `app/` directory.
4. Enable the following services in the console:
    - **Authentication:** Enable Phone provider.
    - **Cloud Firestore:** Use production mode with appropriate rules.
    - **Cloud Storage.**
    - **Crashlytics & Analytics.**
    - **Remote Config.**

### Generative AI Setup (Gemini)
1. Obtain an API Key from the [Google AI Studio](https://aistudio.google.com/).
2. Enable the **Generative AI API** in your Google Cloud Console.

### Secrets Management
Create a `local.properties` file in the root directory and add your keys:
```properties
GEMINI_API_KEY=your_api_key_here
```

## 4. Building and Running

1. **Sync Gradle:** Open the project in Android Studio and click "Sync Project with Gradle Files".
2. **Select Target:** Choose your connected device or emulator from the device manager.
3. **Run:** Click the "Run" icon or press `Shift + F10`.

## 5. Development Workflow

### Branching Model
We follow a structured branching strategy:
- `main`: Production-ready code.
- `develop`: Integration branch for features.
- `feature/*`: New functionality.
- `fix/*`: Bug fixes.

### Quality Standards
Before submitting a PR, ensure:
1. All unit tests pass: `./gradlew test`.
2. Static analysis is clean: `./gradlew detekt`.
3. Code is formatted: `./gradlew spotlessApply`.

## 6. Troubleshooting

- **Gradle Sync Issues:** Try `File > Invalidate Caches...` and restart.
- **Firebase Errors:** Ensure your `google-services.json` is up to date and SHA-1 fingerprints are registered in the Firebase Console.
- **Gemini API Errors:** Verify your quota and regional availability for the Generative AI SDK.

---
© 2026 Brunocarvalhs. All rights reserved.
