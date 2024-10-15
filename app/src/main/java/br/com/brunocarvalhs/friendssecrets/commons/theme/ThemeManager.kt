package br.com.brunocarvalhs.friendssecrets.commons.theme

import android.content.Context
import android.content.res.Configuration
import androidx.compose.runtime.mutableStateOf
import br.com.brunocarvalhs.friendssecrets.CustomApplication
import br.com.brunocarvalhs.friendssecrets.presentation.views.settings.appearence.components.Theme

object ThemeManager {

    private var themeState = mutableStateOf(Theme.SYSTEM)
    private var isDynamicThemeEnabled = false

    private fun getSystemTheme(context: Context): Theme {
        return if ((context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES) {
            Theme.DARK
        } else {
            Theme.LIGHT
        }
    }

    internal val theme: Theme
        get() = themeState.value

    internal fun setTheme(theme: Theme) {
        themeState.value = theme
    }

    internal fun setDynamicThemeEnabled(enabled: Boolean) {
        setTheme(themeState.value)
        isDynamicThemeEnabled = enabled
    }

    internal fun isDynamicThemeEnabled(): Boolean {
        return isDynamicThemeEnabled
    }

    internal fun isDarkTheme(): Boolean {
        return if (theme == Theme.SYSTEM) {
            getSystemTheme(CustomApplication.instance) == Theme.DARK
        } else {
            theme == Theme.DARK
        }
    }
}
