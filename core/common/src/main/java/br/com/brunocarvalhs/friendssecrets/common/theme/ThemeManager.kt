package br.com.brunocarvalhs.friendssecrets.common.theme

import android.content.Context
import android.content.res.Configuration
import br.com.brunocarvalhs.friendssecrets.common.storage.StorageManager

class ThemeManager(
    private val context: Context,
    private val storage: StorageManager
) {
    var theme: Theme = Theme.SYSTEM
        private set

    private var isDynamicThemeEnabled = false

    suspend fun init() {
        val themeValue = storage.load(THEME_KEY, String::class.java) ?: Theme.SYSTEM.value
        theme = Theme.entries.firstOrNull { it.value == themeValue } ?: Theme.SYSTEM
        isDynamicThemeEnabled = storage.load(DYNAMIC_THEME_KEY, Boolean::class.java) ?: false
    }

    suspend fun setTheme(value: Theme) {
        theme = value
        storage.save(THEME_KEY, value.value)
    }

    private fun getSystemTheme(): Theme {
        return if ((context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES) {
            Theme.DARK
        } else {
            Theme.LIGHT
        }
    }

    fun isDarkTheme(): Boolean {
        return if (theme == Theme.SYSTEM) {
            getSystemTheme() == Theme.DARK
        } else {
            theme == Theme.DARK
        }
    }

    suspend fun setDynamicThemeEnabled(enabled: Boolean) {
        isDynamicThemeEnabled = enabled
        storage.save(DYNAMIC_THEME_KEY, enabled)
    }

    fun isDynamicThemeEnabled(): Boolean {
        return isDynamicThemeEnabled
    }

    enum class Theme(val value: String) {
        LIGHT("Light"),
        DARK("Dark"),
        SYSTEM("System")
    }

    companion object {
        private const val THEME_KEY = "theme_key"
        private const val DYNAMIC_THEME_KEY = "dynamic_theme_key"
    }
}
