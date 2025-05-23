package br.com.brunocarvalhs.friendssecrets.commons.theme

import android.content.Context
import android.content.res.Configuration
import androidx.compose.runtime.mutableStateOf
import br.com.brunocarvalhs.friendssecrets.CustomApplication
import br.com.brunocarvalhs.friendssecrets.data.service.StorageService
import br.com.brunocarvalhs.friendssecrets.presentation.views.settings.appearence.components.Theme
import com.google.firebase.perf.metrics.AddTrace

object ThemeManager {

    private const val THEME_KEY = "theme_key"
    private const val DYNAMIC_THEME_KEY = "dynamic_theme_key"

    private val storage: StorageService = StorageService()

    private var themeState = mutableStateOf(storage.load<String>(THEME_KEY) ?: Theme.SYSTEM.name)
    private var isDynamicThemeEnabled = storage.load<Boolean>(DYNAMIC_THEME_KEY) ?: false

    internal val theme: Theme
        get() = Theme.valueOf(themeState.value)

    /**
     * This method is used to get the current theme.
     * @return The current theme.
     */
    @JvmName("getTheme")
    @AddTrace(name = "ThemeManager.getTheme")
    private fun getSystemTheme(context: Context): Theme {
        return if ((context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES) {
            Theme.DARK
        } else {
            Theme.LIGHT
        }
    }

    /**
     * This method is used to get the current theme.
     * @return The current theme.
     */
    @JvmName("getTheme")
    @AddTrace(name = "ThemeManager.getTheme")
    internal fun setTheme(theme: Theme) {
        storage.save(THEME_KEY, theme.name)
        themeState.value = theme.name
    }

    /**
     * This method is used to set the dynamic theme enabled or not.
     * @param enabled true if the dynamic theme is enabled, false otherwise.
     */
    @JvmName("setDynamicThemeEnabled")
    @AddTrace(name = "ThemeManager.setDynamicThemeEnabled")
    internal fun setDynamicThemeEnabled(enabled: Boolean) {
        storage.save(DYNAMIC_THEME_KEY, enabled)
        isDynamicThemeEnabled = enabled
    }

    /**
     * This method is used to check if the dynamic theme is enabled or not.
     * @return true if the dynamic theme is enabled, false otherwise.
     */
    @JvmName("isDynamicThemeEnabled")
    @AddTrace(name = "ThemeManager.isDynamicThemeEnabled")
    internal fun isDynamicThemeEnabled(): Boolean {
        return isDynamicThemeEnabled
    }

    /**
     * This method is used to check if the current theme is dark or not.
     * @return true if the current theme is dark, false otherwise.
     */
    @JvmName("isDarkTheme")
    @AddTrace(name = "ThemeManager.isDarkTheme")
    internal fun isDarkTheme(): Boolean {
        return if (theme == Theme.SYSTEM) {
            getSystemTheme(CustomApplication.getInstance()) == Theme.DARK
        } else {
            theme == Theme.DARK
        }
    }
}
