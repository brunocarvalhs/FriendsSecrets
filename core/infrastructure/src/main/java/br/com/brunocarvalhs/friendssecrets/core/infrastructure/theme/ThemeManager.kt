package br.com.brunocarvalhs.friendssecrets.core.infrastructure.theme

import android.content.Context
import android.content.res.Configuration
import androidx.compose.runtime.Stable
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import br.com.brunocarvalhs.friendssecrets.domain.services.StorageService
import br.com.brunocarvalhs.friendssecrets.domain.services.ThemeService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Stable
@Singleton
class ThemeManager @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val storage: StorageService
) : ThemeService {

    private val _theme = MutableStateFlow(ThemeService.Theme.SYSTEM)
    override val theme: StateFlow<ThemeService.Theme> = _theme.asStateFlow()

    private val _isDynamicThemeEnabled = MutableStateFlow(false)
    override val isDynamicThemeEnabled: StateFlow<Boolean> = _isDynamicThemeEnabled.asStateFlow()

    init {
        ProcessLifecycleOwner.get().lifecycleScope.launch(Dispatchers.IO) {
            init()
        }
    }

    private suspend fun init() {
        Timber.tag(TAG).d("--> INIT THEME")
        val themeValue = storage.load(THEME_KEY, String::class) ?: ThemeService.Theme.SYSTEM.type
        _theme.value = ThemeService.Theme.entries.firstOrNull { it.type == themeValue }
            ?: ThemeService.Theme.SYSTEM

        val dynamic = storage.load(DYNAMIC_THEME_KEY, Boolean::class) ?: false
        _isDynamicThemeEnabled.value = dynamic
        Timber.tag(TAG).d(
            "<-- SUCCESS INIT | Theme: %s, Dynamic: %s",
            _theme.value,
            _isDynamicThemeEnabled.value
        )
    }

    override suspend fun setTheme(theme: ThemeService.Theme) {
        Timber.tag(TAG).d("--> SET THEME: %s", theme)
        _theme.value = theme
        storage.save(THEME_KEY, theme.type)
    }

    private fun getSystemTheme(): ThemeService.Theme {
        return if ((context.applicationContext.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES) {
            ThemeService.Theme.DARK
        } else {
            ThemeService.Theme.LIGHT
        }
    }

    fun isDarkTheme(): Boolean {
        val isDark = if (_theme.value == ThemeService.Theme.SYSTEM) {
            getSystemTheme() == ThemeService.Theme.DARK
        } else {
            _theme.value == ThemeService.Theme.DARK
        }
        Timber.tag(TAG).v("isDarkTheme? %s (Current: %s)", isDark, _theme.value)
        return isDark
    }

    override suspend fun setDynamicThemeEnabled(enabled: Boolean) {
        Timber.tag(TAG).d("--> SET DYNAMIC THEME: %s", enabled)
        _isDynamicThemeEnabled.value = enabled
        storage.save(DYNAMIC_THEME_KEY, enabled)
    }

    companion object {
        private const val TAG = "ThemeManager"
        private const val THEME_KEY = "theme_key"
        private const val DYNAMIC_THEME_KEY = "dynamic_theme_key"
    }
}