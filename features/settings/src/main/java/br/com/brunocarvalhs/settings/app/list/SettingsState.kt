package br.com.brunocarvalhs.settings.app.list

internal data class SettingsState(
    val isBiometricPromptEnabled: Boolean = false,
    val isBiometricSupported: Boolean = false
)
