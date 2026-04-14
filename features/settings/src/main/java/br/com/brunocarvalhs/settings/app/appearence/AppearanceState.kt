package br.com.brunocarvalhs.settings.app.appearence

import br.com.brunocarvalhs.friendssecrets.domain.services.ThemeService

internal data class AppearanceState(
    val themeSelected: String = ThemeService.Theme.LIGHT.type,
    val isDynamicThemeEnabled: Boolean = false
)
