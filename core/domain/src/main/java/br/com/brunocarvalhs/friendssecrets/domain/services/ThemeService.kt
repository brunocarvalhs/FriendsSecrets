package br.com.brunocarvalhs.friendssecrets.domain.services

interface ThemeService {
    fun isDarkTheme(): Boolean
    fun isDynamicThemeEnabled(): Boolean
}