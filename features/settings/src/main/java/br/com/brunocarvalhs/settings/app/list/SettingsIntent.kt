package br.com.brunocarvalhs.settings.app.list

internal sealed interface SettingsIntent {
    data class SetBiometricPromptEnabled(val state: Boolean): SettingsIntent
}
