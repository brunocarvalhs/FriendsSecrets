package br.com.brunocarvalhs.settings.app.list

internal data class SettingsState(
    val isBiometricPromptEnabled: Boolean = false,
    val isBiometricSupported: Boolean = false,
    val isAppearanceEnabled: Boolean = true,
    val isFingerprintFeatureEnabled: Boolean = true,
    val versionName: String = "",
    val versionCode: String = ""
)
