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

    // Mirror the CLASSIC palette's primary/secondary from core:ui's Color.kt as literals, for
    // the same reason as DEFAULT_PALETTE_ID above.
    private val _customPrimaryColor = MutableStateFlow(DEFAULT_CUSTOM_PRIMARY_COLOR)
    override val customPrimaryColor = _customPrimaryColor.asStateFlow()

    private val _customSecondaryColor = MutableStateFlow(DEFAULT_CUSTOM_SECONDARY_COLOR)
    override val customSecondaryColor = _customSecondaryColor.asStateFlow()

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

        _customPrimaryColor.value =
            storage.load("custom_primary_color_key", Int::class) ?: DEFAULT_CUSTOM_PRIMARY_COLOR

        _customSecondaryColor.value =
            storage.load("custom_secondary_color_key", Int::class) ?: DEFAULT_CUSTOM_SECONDARY_COLOR
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

    override suspend fun setCustomColors(primaryColor: Int, secondaryColor: Int) {
        _customPrimaryColor.value = primaryColor
        _customSecondaryColor.value = secondaryColor
        storage.save("custom_primary_color_key", primaryColor)
        storage.save("custom_secondary_color_key", secondaryColor)
    }

    private companion object {
        const val DEFAULT_PALETTE_ID = "classic"
        const val DEFAULT_CUSTOM_PRIMARY_COLOR = 0xFF1D4ED8.toInt()
        const val DEFAULT_CUSTOM_SECONDARY_COLOR = 0xFFB75C00.toInt()
    }
}
