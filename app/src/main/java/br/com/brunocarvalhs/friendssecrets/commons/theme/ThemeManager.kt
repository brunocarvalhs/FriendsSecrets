package br.com.brunocarvalhs.friendssecrets.commons.theme

import android.content.Context
import android.content.res.Configuration
import androidx.compose.runtime.mutableStateOf
import br.com.brunocarvalhs.friendssecrets.CustomApplication
import br.com.brunocarvalhs.friendssecrets.data.service.StorageService
import br.com.brunocarvalhs.friendssecrets.presentation.views.settings.appearence.components.Theme

object ThemeManager {

    private const val THEME_KEY = "theme_key"
    private const val DYNAMIC_THEME_KEY = "dynamic_theme_key"

    private val storage: StorageService = StorageService()

    private var themeState = mutableStateOf(storage.load<String>(THEME_KEY) ?: Theme.SYSTEM.name)
    private var isDynamicThemeEnabled = storage.load<Boolean>(DYNAMIC_THEME_KEY) ?: false

    internal val theme: Theme
        get() = Theme.valueOf(themeState.value)

    private fun getSystemTheme(context: Context): Theme {
        return if ((context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES) {
            Theme.DARK
        } else {
            Theme.LIGHT
        }
    }

    internal fun setTheme(theme: Theme) {
        storage.save(THEME_KEY, theme.name)
        themeState.value = theme.name
    }

    internal fun setDynamicThemeEnabled(enabled: Boolean) {
        setTheme(Theme.valueOf(themeState.value))
        isDynamicThemeEnabled = enabled
    }

    internal fun isDynamicThemeEnabled(): Boolean {
        return isDynamicThemeEnabled
    }

    internal fun isDarkTheme(): Boolean {
        return if (theme == Theme.SYSTEM) {
            getSystemTheme(CustomApplication.getInstance()) == Theme.DARK
        } else {
            theme == Theme.DARK
        }
    }
}
