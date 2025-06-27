package br.com.brunocarvalhs.settings.app.list

sealed interface SettingsIntent {
    data class SetBiometricPromptEnabled(val state: Boolean): SettingsIntent
}