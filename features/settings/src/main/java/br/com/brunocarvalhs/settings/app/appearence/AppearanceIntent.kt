package br.com.brunocarvalhs.settings.app.appearence

internal sealed interface AppearanceIntent {
    data class SetTheme(val theme: String) : AppearanceIntent
    data class SetDynamicThemeEnabled(val enabled: Boolean) : AppearanceIntent
}