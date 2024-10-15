package br.com.brunocarvalhs.friendssecrets.commons.theme

import androidx.compose.runtime.mutableStateOf
import br.com.brunocarvalhs.friendssecrets.presentation.views.settings.appearence.components.Theme

object ThemeManager {

    private var themeState = mutableStateOf(Theme.SYSTEM)

    internal val theme: Theme
        get() = themeState.value

    private var isDynamicThemeEnabled = false

    internal fun setTheme(theme: Theme) {
        themeState.value = theme
    }

    internal fun setDynamicThemeEnabled(enabled: Boolean) {
        isDynamicThemeEnabled = enabled
    }

    internal fun isDynamicThemeEnabled(): Boolean {
        return isDynamicThemeEnabled
    }

    internal fun isDarkTheme(): Boolean {
        return themeState.value == Theme.DARK
    }
}