package br.com.brunocarvalhs.settings.app.appearence

import br.com.brunocarvalhs.core.remote.domain.ThemeService

internal data class AppearanceState(
    val themeSelected: String = ThemeService.Theme.LIGHT.type,
    val isDynamicThemeEnabled: Boolean = false
)
