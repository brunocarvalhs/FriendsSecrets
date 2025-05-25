package br.com.brunocarvalhs.friendssecrets.common.theme

import android.content.Context
import android.content.res.Configuration
import br.com.brunocarvalhs.friendssecrets.common.storage.StorageManager

class ThemeManager(
    private val context: Context,
    private val storage: StorageManager
) {

    private var themeState = storage.load<String>(THEME_KEY) ?: Theme.SYSTEM.value
    private var isDynamicThemeEnabled = storage.load<Boolean>(DYNAMIC_THEME_KEY) ?: false

    var theme: Theme = Theme.entries.firstOrNull { it.value == themeState } ?: Theme.SYSTEM
        set(value) {
            field = value
            storage.save(THEME_KEY, value.name)
            themeState = theme.name
        }

    private fun getSystemTheme(): Theme {
        return if ((context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES) {
            Theme.DARK
        } else {
            Theme.LIGHT
        }
    }

    internal fun setDynamicThemeEnabled(enabled: Boolean) {
        storage.save(DYNAMIC_THEME_KEY, enabled)
        isDynamicThemeEnabled = enabled
    }

    internal fun isDynamicThemeEnabled(): Boolean {
        return isDynamicThemeEnabled
    }

    internal fun isDarkTheme(): Boolean {
        return if (theme == Theme.SYSTEM) {
            getSystemTheme() == Theme.DARK
        } else {
            theme == Theme.DARK
        }
    }

    enum class Theme(val value: String) {
        LIGHT(value = "Light"),
        DARK(value = "Dark"),
        SYSTEM(value = "System")
    }

    companion object {
        private const val THEME_KEY = "theme_key"
        private const val DYNAMIC_THEME_KEY = "dynamic_theme_key"
    }
}
