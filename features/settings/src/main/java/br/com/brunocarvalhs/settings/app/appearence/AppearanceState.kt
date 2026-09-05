package br.com.brunocarvalhs.settings.app.appearence

import androidx.compose.ui.graphics.toArgb
import br.com.brunocarvalhs.core.remote.domain.ThemeService
import br.com.brunocarvalhs.core.ui.theme.AppPalette

internal data class AppearanceState(
    val themeSelected: String = ThemeService.Theme.LIGHT.type,
    val isDynamicThemeEnabled: Boolean = false,
    val paletteSelected: String = AppPalette.Default.id,
    val customPrimaryColor: Int = AppPalette.Default.lightColorScheme.primary.toArgb(),
    val customSecondaryColor: Int = AppPalette.Default.lightColorScheme.secondary.toArgb()
)
