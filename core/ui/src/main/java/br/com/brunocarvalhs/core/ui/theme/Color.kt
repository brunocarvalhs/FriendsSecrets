@file:Suppress("MagicNumber")
package br.com.brunocarvalhs.core.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp

// Strong Christmas red — fits a Secret Santa app. Contrast against white text
// measures ~5.0:1 (WCAG AA).
val primaryLight = Color(0xFFD32F2F)
val onPrimaryLight = Color(0xFFFFFFFF)
val primaryContainerLight = Color(0xFFFADBD8)
val onPrimaryContainerLight = Color(0xFF7A1315)
// NOTE: darkened from the original #F57C00 to meet WCAG AA (4.5:1) contrast
// against white text (onSecondaryLight) — the original combination measured ~2.7:1.
val secondaryLight = Color(0xFFB75C00)
val onSecondaryLight = Color(0xFFFFFFFF)
val secondaryContainerLight = Color(0xFFFFE0B2)
val onSecondaryContainerLight = Color(0xFF3E1F00)
// NOTE: darkened from the original #4CAF50 to meet WCAG AA (4.5:1) contrast
// against white text (onTertiaryLight) — the original combination measured ~2.8:1.
val tertiaryLight = Color(0xFF3A843D)
val onTertiaryLight = Color(0xFFFFFFFF)
val tertiaryContainerLight = Color(0xFFC8E6C9)
val onTertiaryContainerLight = Color(0xFF0F2E12)
val errorLight = Color(0xFFB3261E)
val onErrorLight = Color(0xFFFFFFFF)
val backgroundLight = Color(0xFFFAFAFA)
val onBackgroundLight = Color(0xFF1C1B1F)
val surfaceLight = Color(0xFFFFFFFF)
val onSurfaceLight = Color(0xFF1C1B1F)
val surfaceVariantLight = Color(0xFFE7E0EC)
val onSurfaceVariantLight = Color(0xFF49454F)
val outlineLight = Color(0xFF79747E)


val primaryDark = Color(0xFFE57373)
val onPrimaryDark = Color(0xFF4A0E0E)
val primaryContainerDark = Color(0xFF6B1A1A)
val onPrimaryContainerDark = Color(0xFFFFDAD6)
val secondaryDark = Color(0xFFFFB74D)
val onSecondaryDark = Color(0xFF3E1F00)
val secondaryContainerDark = Color(0xFF7A3E00)
val onSecondaryContainerDark = Color(0xFFFFE0B2)
val tertiaryDark = Color(0xFFA5D6A7)
val onTertiaryDark = Color(0xFF0F2E12)
// NOTE: darkened slightly from the original #2E7D32 to meet WCAG AA (4.5:1)
// contrast against onTertiaryContainerDark — the original combination measured ~3.8:1.
val tertiaryContainerDark = Color(0xFF296E2C)
val onTertiaryContainerDark = Color(0xFFC8E6C9)
val errorDark = Color(0xFFFFB4AB)
val onErrorDark = Color(0xFF690005)
val backgroundDark = Color(0xFF121212)
val onBackgroundDark = Color(0xFFE6E1E5)
val surfaceDark = Color(0xFF121212)
val onSurfaceDark = Color(0xFFE6E1E5)
val surfaceVariantDark = Color(0xFF49454F)
val onSurfaceVariantDark = Color(0xFFCAC4D0)
val outlineDark = Color(0xFF938F99)

private const val CONTAINER_LOWEST_TINT = 0.02f
private const val CONTAINER_LOW_TINT = 0.04f
private const val CONTAINER_TINT = 0.07f
private const val CONTAINER_HIGH_TINT = 0.10f
private const val CONTAINER_HIGHEST_TINT = 0.14f

/**
 * Blends a touch of [primary] into a neutral [surface] color. Used to derive the
 * `surfaceContainer*` roles below, so that cards, top app bars and other components that use
 * the default M3 container roles (instead of `background`/`surface` directly) pick up a subtle
 * tint of whichever palette - including a [AppPalette.CUSTOM] one - is currently selected.
 */
private fun tintSurface(surface: Color, primary: Color, fraction: Float): Color =
    lerp(surface, primary, fraction)

/**
 * Returns a copy of this [ColorScheme] with [ColorScheme.surfaceTint] and the
 * `surfaceContainer*` roles derived from [primary], instead of the fixed M3 baseline values
 * `lightColorScheme`/`darkColorScheme` fall back to when they aren't explicitly passed in.
 */
fun ColorScheme.withTintedContainers(primary: Color, surface: Color): ColorScheme = copy(
    surfaceTint = primary,
    surfaceContainerLowest = tintSurface(surface, primary, CONTAINER_LOWEST_TINT),
    surfaceContainerLow = tintSurface(surface, primary, CONTAINER_LOW_TINT),
    surfaceContainer = tintSurface(surface, primary, CONTAINER_TINT),
    surfaceContainerHigh = tintSurface(surface, primary, CONTAINER_HIGH_TINT),
    surfaceContainerHighest = tintSurface(surface, primary, CONTAINER_HIGHEST_TINT),
)
