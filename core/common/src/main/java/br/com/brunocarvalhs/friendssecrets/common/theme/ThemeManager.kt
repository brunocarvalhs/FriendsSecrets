package br.com.brunocarvalhs.friendssecrets.common.theme

import android.content.Context
import android.content.res.Configuration
import br.com.brunocarvalhs.friendssecrets.common.storage.StorageManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ThemeManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val storage: StorageManager,
    dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private val scope = CoroutineScope(dispatcher)
    private val _theme = MutableStateFlow(Theme.SYSTEM)
    val theme: StateFlow<Theme> = _theme

    private val _isDynamicThemeEnabled = MutableStateFlow(false)
    val isDynamicThemeEnabled: StateFlow<Boolean> = _isDynamicThemeEnabled

    init {
        scope.launch {
            init()
        }
    }

    private suspend fun init() {
        val themeValue = storage.load(THEME_KEY, String::class.java) ?: Theme.SYSTEM.type
        _theme.value = Theme.entries.firstOrNull { it.type == themeValue } ?: Theme.SYSTEM

        val dynamic = storage.load(DYNAMIC_THEME_KEY, Boolean::class.java) ?: false
        _isDynamicThemeEnabled.value = dynamic
    }

    suspend fun setTheme(value: Theme) {
        _theme.value = value
        storage.save(THEME_KEY, value.type)
    }

    private fun getSystemTheme(): Theme {
        return if ((context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES) {
            Theme.DARK
        } else {
            Theme.LIGHT
        }
    }

    fun isDarkTheme(): Boolean {
        return if (_theme.value == Theme.SYSTEM) {
            getSystemTheme() == Theme.DARK
        } else {
            _theme.value == Theme.DARK
        }
    }

    suspend fun setDynamicThemeEnabled(enabled: Boolean) {
        _isDynamicThemeEnabled.value = enabled
        storage.save(DYNAMIC_THEME_KEY, enabled)
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
