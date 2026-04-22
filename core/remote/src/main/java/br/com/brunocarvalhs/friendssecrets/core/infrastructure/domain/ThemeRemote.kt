package br.com.brunocarvalhs.friendssecrets.core.infrastructure.domain

import androidx.compose.material3.ColorScheme

interface ThemeRemote {
    fun getLightColorScheme(): ColorScheme?
    fun getDarkColorScheme(): ColorScheme?
}