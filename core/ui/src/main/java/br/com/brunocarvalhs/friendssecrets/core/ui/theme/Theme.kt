package br.com.brunocarvalhs.friendssecrets.core.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.core.view.WindowCompat
import br.com.brunocarvalhs.friendssecrets.core.infrastructure.domain.ThemeRemote
import br.com.brunocarvalhs.friendssecrets.core.infrastructure.domain.ThemeService

private val lightScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
)

private val darkScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
)

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

    val darkTheme = calculateDarkTheme(theme)
    val colorScheme = pickColorScheme(
        darkTheme = darkTheme,
        dynamicColorEnabled = dynamicColorEnabled,
        isThemeRemote = isThemeRemote,
        themeRemoteProvider = themeRemoteProvider
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
    themeRemoteProvider: ThemeRemote?
): androidx.compose.material3.ColorScheme {
    val context = LocalContext.current
    return when {
        dynamicColorEnabled && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> if (isThemeRemote) {
            themeRemoteProvider?.getDarkColorScheme() ?: darkScheme
        } else {
            darkScheme
        }

        else -> if (isThemeRemote) {
            themeRemoteProvider?.getLightColorScheme() ?: lightScheme
        } else {
            lightScheme
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
