package br.com.brunocarvalhs.friendssecrets.commons.theme

import android.content.res.Configuration
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class ThemeManager constructor(
    private val activity: ComponentActivity,
    dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private val TAG = "ThemeManager"

    private val _theme = MutableStateFlow(Theme.SYSTEM)
    val theme: StateFlow<Theme> = _theme

    private val _isDynamicThemeEnabled = MutableStateFlow(false)
    val isDynamicThemeEnabled: StateFlow<Boolean> = _isDynamicThemeEnabled

    init {
        activity.lifecycleScope.launch(dispatcher) {
            init()
        }
    }

    private suspend fun init() {
        Timber.tag(TAG).d("--> INIT THEME")
        val themeValue = Theme.SYSTEM.type
        _theme.value = Theme.entries.firstOrNull { it.type == themeValue } ?: Theme.SYSTEM

        val dynamic = false
        _isDynamicThemeEnabled.value = dynamic
        Timber.tag(TAG).d("<-- SUCCESS INIT | Theme: %s, Dynamic: %s", _theme.value, _isDynamicThemeEnabled.value)
    }

    suspend fun setTheme(value: Theme) {
        Timber.tag(TAG).d("--> SET THEME: %s", value)
        _theme.value = value
    }

    private fun getSystemTheme(): Theme {
        return if ((activity.applicationContext.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES) {
            Theme.DARK
        } else {
            Theme.LIGHT
        }
    }

    fun isDarkTheme(): Boolean {
        val isDark = if (_theme.value == Theme.SYSTEM) {
            getSystemTheme() == Theme.DARK
        } else {
            _theme.value == Theme.DARK
        }
        Timber.tag(TAG).v("isDarkTheme? %s (Current: %s)", isDark, _theme.value)
        return isDark
    }

    suspend fun setDynamicThemeEnabled(enabled: Boolean) {
        Timber.tag(TAG).d("--> SET DYNAMIC THEME: %s", enabled)
        _isDynamicThemeEnabled.value = enabled
    }

    enum class Theme(val type: String) {
        LIGHT("Light"),
        DARK("Dark"),
        SYSTEM("System")
    }

    companion object {
        private const val THEME_KEY = "theme_key"
        private const val DYNAMIC_THEME_KEY = "dynamic_theme_key"
    }
}
