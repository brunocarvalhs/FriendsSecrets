package br.com.brunocarvalhs.friendssecrets.domain.services

interface ThemeService {
    suspend fun setDynamicThemeEnabled(enabled: Boolean)
    suspend fun setTheme(theme: Theme)
    fun getTheme(): Theme
    fun isDynamicThemeEnabled(): Boolean

    enum class Theme(val type: String) {
        LIGHT("Light"),
        DARK("Dark"),
        SYSTEM("System")
    }
}