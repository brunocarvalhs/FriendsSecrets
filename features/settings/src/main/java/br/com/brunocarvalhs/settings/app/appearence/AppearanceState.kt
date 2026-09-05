package br.com.brunocarvalhs.settings.app.appearence

import br.com.brunocarvalhs.core.remote.domain.ThemeService
import br.com.brunocarvalhs.core.ui.theme.AppPalette

internal data class AppearanceState(
    val themeSelected: String = ThemeService.Theme.LIGHT.type,
    val isDynamicThemeEnabled: Boolean = false,
    val paletteSelected: String = AppPalette.Default.id
)
