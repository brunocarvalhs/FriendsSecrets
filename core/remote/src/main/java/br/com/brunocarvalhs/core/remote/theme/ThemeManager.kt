package br.com.brunocarvalhs.core.remote.theme

import androidx.compose.runtime.Stable
import br.com.brunocarvalhs.core.remote.domain.ThemeService
import br.com.brunocarvalhs.storage.domain.StorageService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Stable
@Singleton
class ThemeManager @Inject constructor(
    private val storage: StorageService
) : ThemeService {

    private val _theme = MutableStateFlow(ThemeService.Theme.SYSTEM)
    override val theme = _theme.asStateFlow()

    private val _isDynamicThemeEnabled = MutableStateFlow(false)
    override val isDynamicThemeEnabled = _isDynamicThemeEnabled.asStateFlow()

    // Mirrors AppPalette.Default.id ("classic") from core:ui. Kept as a literal here since
    // core:remote must not depend on core:ui.
    private val _palette = MutableStateFlow(DEFAULT_PALETTE_ID)
    override val palette = _palette.asStateFlow()

    override suspend fun initialize() {
        val themeValue =
            storage.load("theme_key", String::class)
                ?: ThemeService.Theme.SYSTEM.type

        _theme.value =
            ThemeService.Theme.entries.firstOrNull { it.type == themeValue }
                ?: ThemeService.Theme.SYSTEM

        _isDynamicThemeEnabled.value =
            storage.load("dynamic_theme_key", Boolean::class) ?: false

        _palette.value =
            storage.load("palette_key", String::class) ?: DEFAULT_PALETTE_ID
    }

    override suspend fun setTheme(theme: ThemeService.Theme) {
        _theme.value = theme
        storage.save("theme_key", theme.type)
    }

    override suspend fun setDynamicThemeEnabled(enabled: Boolean) {
        _isDynamicThemeEnabled.value = enabled
        storage.save("dynamic_theme_key", enabled)
    }

    override suspend fun setPalette(id: String) {
        _palette.value = id
        storage.save("palette_key", id)
    }

    private companion object {
        const val DEFAULT_PALETTE_ID = "classic"
    }
}
