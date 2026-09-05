package br.com.brunocarvalhs.core.remote.domain

import kotlinx.coroutines.flow.StateFlow

interface ThemeService {

    val theme: StateFlow<Theme>
    val isDynamicThemeEnabled: StateFlow<Boolean>
    val palette: StateFlow<String>

    suspend fun initialize()
    suspend fun setDynamicThemeEnabled(enabled: Boolean)
    suspend fun setTheme(theme: Theme)
    suspend fun setPalette(id: String)

    enum class Theme(val type: String) {
        LIGHT("Light"),
        DARK("Dark"),
        SYSTEM("System")
    }
}
