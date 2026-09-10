package br.com.brunocarvalhs.settings.app.appearence

internal sealed interface AppearanceIntent {
    data class SetTheme(val theme: String) : AppearanceIntent
    data class SetDynamicThemeEnabled(val enabled: Boolean) : AppearanceIntent
    data class SetPalette(val paletteId: String) : AppearanceIntent
    data class SetCustomColors(val primaryColor: Int, val secondaryColor: Int) : AppearanceIntent
    data class SetCustomThemeEnabled(val enabled: Boolean) : AppearanceIntent
}
