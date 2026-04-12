package br.com.brunocarvalhs.settings.app.list

data class SettingsState(
    val isBiometricPromptEnabled: Boolean = false,
    val isBiometricSupported: Boolean = false
)
