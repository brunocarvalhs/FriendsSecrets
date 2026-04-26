package br.com.brunocarvalhs.core.remote.domain

import androidx.compose.material3.ColorScheme

interface ThemeRemote {
    fun getLightColorScheme(): ColorScheme?
    fun getDarkColorScheme(): ColorScheme?
}
