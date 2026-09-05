@file:Suppress("MagicNumber")
package br.com.brunocarvalhs.core.ui.theme

import android.graphics.Color as AndroidColor
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb

/**
 * Curated, vivid colors offered as swatches when the user builds a [AppPalette.CUSTOM] theme by
 * picking their own primary and secondary color. Any combination is allowed - these are just a
 * starting set of pre-vetted, distinct hues that all read well as an M3 "primary" color.
 */
val CUSTOM_COLOR_SWATCHES: List<Color> = listOf(
    Color(0xFFD32F2F), // Red
    Color(0xFFEF6C00), // Orange
    Color(0xFFF9A825), // Amber
    Color(0xFF7CB342), // Lime
    Color(0xFF2E7D32), // Green
    Color(0xFF00897B), // Teal
    Color(0xFF0097A7), // Cyan
    Color(0xFF1565C0), // Blue
    Color(0xFF303F9F), // Indigo
    Color(0xFF6A1B9A), // Purple
    Color(0xFFAD1457), // Pink
    Color(0xFF5D4037), // Brown
)

/**
 * Builds a full M3 [ColorScheme] from a user-picked primary and secondary color.
 *
 * Tertiary is derived by rotating the primary's hue (there's no third color picker), on-colors
 * are chosen as black/white by relative luminance, and container/on-container tones are derived
 * by adjusting saturation/value in HSV space. Neutrals (background/surface/outline/error) reuse
 * the same shared values as every pre-selected [AppPalette], so a custom theme still fits the
 * app's base legibility.
 */
fun customColorScheme(primary: Color, secondary: Color, dark: Boolean): ColorScheme {
    val tertiary = rotateHue(primary, TERTIARY_HUE_SHIFT_DEGREES)

    return if (dark) {
        darkColorScheme(
            primary = primary,
            onPrimary = onColorFor(primary),
            primaryContainer = tone(primary, CONTAINER_SATURATION_DARK, CONTAINER_VALUE_DARK),
            onPrimaryContainer = tone(primary, ON_CONTAINER_SATURATION_DARK, ON_CONTAINER_VALUE_DARK),
            secondary = secondary,
            onSecondary = onColorFor(secondary),
            secondaryContainer = tone(secondary, CONTAINER_SATURATION_DARK, CONTAINER_VALUE_DARK),
            onSecondaryContainer = tone(secondary, ON_CONTAINER_SATURATION_DARK, ON_CONTAINER_VALUE_DARK),
            tertiary = tertiary,
            onTertiary = onColorFor(tertiary),
            tertiaryContainer = tone(tertiary, CONTAINER_SATURATION_DARK, CONTAINER_VALUE_DARK),
            onTertiaryContainer = tone(tertiary, ON_CONTAINER_SATURATION_DARK, ON_CONTAINER_VALUE_DARK),
            error = errorDark,
            onError = onErrorDark,
            background = backgroundDark,
            onBackground = onBackgroundDark,
            surface = surfaceDark,
            onSurface = onSurfaceDark,
            surfaceVariant = surfaceVariantDark,
            onSurfaceVariant = onSurfaceVariantDark,
            outline = outlineDark,
        ).withTintedContainers(primary, surfaceDark)
    } else {
        lightColorScheme(
            primary = primary,
            onPrimary = onColorFor(primary),
            primaryContainer = tone(primary, CONTAINER_SATURATION_LIGHT, CONTAINER_VALUE_LIGHT),
            onPrimaryContainer = tone(primary, ON_CONTAINER_SATURATION_LIGHT, ON_CONTAINER_VALUE_LIGHT),
            secondary = secondary,
            onSecondary = onColorFor(secondary),
            secondaryContainer = tone(secondary, CONTAINER_SATURATION_LIGHT, CONTAINER_VALUE_LIGHT),
            onSecondaryContainer = tone(secondary, ON_CONTAINER_SATURATION_LIGHT, ON_CONTAINER_VALUE_LIGHT),
            tertiary = tertiary,
            onTertiary = onColorFor(tertiary),
            tertiaryContainer = tone(tertiary, CONTAINER_SATURATION_LIGHT, CONTAINER_VALUE_LIGHT),
            onTertiaryContainer = tone(tertiary, ON_CONTAINER_SATURATION_LIGHT, ON_CONTAINER_VALUE_LIGHT),
            error = errorLight,
            onError = onErrorLight,
            background = backgroundLight,
            onBackground = onBackgroundLight,
            surface = surfaceLight,
            onSurface = onSurfaceLight,
            surfaceVariant = surfaceVariantLight,
            onSurfaceVariant = onSurfaceVariantLight,
            outline = outlineLight,
        ).withTintedContainers(primary, surfaceLight)
    }
}

private const val LUMINANCE_THRESHOLD = 0.5f
private const val TERTIARY_HUE_SHIFT_DEGREES = 120f

private const val CONTAINER_SATURATION_LIGHT = 0.22f
private const val CONTAINER_VALUE_LIGHT = 0.94f
private const val ON_CONTAINER_SATURATION_LIGHT = 0.55f
private const val ON_CONTAINER_VALUE_LIGHT = 0.32f

private const val CONTAINER_SATURATION_DARK = 0.35f
private const val CONTAINER_VALUE_DARK = 0.34f
private const val ON_CONTAINER_SATURATION_DARK = 0.30f
private const val ON_CONTAINER_VALUE_DARK = 0.90f

private fun onColorFor(color: Color): Color =
    if (color.luminance() > LUMINANCE_THRESHOLD) Color.Black else Color.White

private fun tone(color: Color, saturation: Float, value: Float): Color {
    val hsv = FloatArray(3)
    AndroidColor.colorToHSV(color.toArgb(), hsv)
    hsv[1] = saturation.coerceIn(0f, 1f)
    hsv[2] = value.coerceIn(0f, 1f)
    return Color(AndroidColor.HSVToColor(hsv))
}

private fun rotateHue(color: Color, degrees: Float): Color {
    val hsv = FloatArray(3)
    AndroidColor.colorToHSV(color.toArgb(), hsv)
    hsv[0] = (hsv[0] + degrees) % 360f
    return Color(AndroidColor.HSVToColor(hsv))
}
