package br.com.brunocarvalhs.core.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.core.view.WindowCompat
import br.com.brunocarvalhs.core.remote.domain.ThemeRemote
import br.com.brunocarvalhs.core.remote.domain.ThemeService

@Composable
fun FriendsSecretsTheme(
    isThemeRemote: Boolean = false,
    themeService: ThemeService? = null,
    themeRemoteProvider: ThemeRemote? = null,
    content: @Composable () -> Unit,
) {
    val isInPreview = LocalInspectionMode.current

    val theme by rememberThemeState(themeService, isInPreview)
    val dynamicColorEnabled by rememberDynamicColorState(themeService, isInPreview)
    val paletteId by rememberPaletteState(themeService, isInPreview)

    val darkTheme = calculateDarkTheme(theme)
    val colorScheme = pickColorScheme(
        darkTheme = darkTheme,
        dynamicColorEnabled = dynamicColorEnabled,
        isThemeRemote = isThemeRemote,
        themeRemoteProvider = themeRemoteProvider,
        palette = AppPalette.fromId(paletteId)
    )

    SideEffectWindow(darkTheme)

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}

@Composable
private fun rememberThemeState(
    themeService: ThemeService?,
    isInPreview: Boolean
) = if (!isInPreview && themeService != null) {
    themeService.theme.collectAsState()
} else {
    remember { androidx.compose.runtime.mutableStateOf(ThemeService.Theme.SYSTEM) }
}

@Composable
private fun rememberDynamicColorState(
    themeService: ThemeService?,
    isInPreview: Boolean
) = if (!isInPreview && themeService != null) {
    themeService.isDynamicThemeEnabled.collectAsState()
} else {
    remember { androidx.compose.runtime.mutableStateOf(false) }
}

@Composable
private fun rememberPaletteState(
    themeService: ThemeService?,
    isInPreview: Boolean
) = if (!isInPreview && themeService != null) {
    themeService.palette.collectAsState()
} else {
    remember { androidx.compose.runtime.mutableStateOf(AppPalette.Default.id) }
}

@Composable
private fun calculateDarkTheme(theme: ThemeService.Theme) = when (theme) {
    ThemeService.Theme.DARK -> true
    ThemeService.Theme.LIGHT -> false
    ThemeService.Theme.SYSTEM -> isSystemInDarkTheme()
}

@Composable
private fun pickColorScheme(
    darkTheme: Boolean,
    dynamicColorEnabled: Boolean,
    isThemeRemote: Boolean,
    themeRemoteProvider: ThemeRemote?,
    palette: AppPalette
): androidx.compose.material3.ColorScheme {
    val context = LocalContext.current
    return when {
        dynamicColorEnabled && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> if (isThemeRemote) {
            themeRemoteProvider?.getDarkColorScheme() ?: palette.darkColorScheme
        } else {
            palette.darkColorScheme
        }

        else -> if (isThemeRemote) {
            themeRemoteProvider?.getLightColorScheme() ?: palette.lightColorScheme
        } else {
            palette.lightColorScheme
        }
    }
}

@Composable
private fun SideEffectWindow(darkTheme: Boolean) {
    val view = LocalContext.current as Activity
    SideEffect {
        val window = view.window ?: return@SideEffect
        val controller = WindowCompat.getInsetsController(window, window.decorView)

        controller.isAppearanceLightStatusBars = !darkTheme
    }
}
