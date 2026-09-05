@file:Suppress("MagicNumber")
package br.com.brunocarvalhs.core.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

/**
 * Pre-selected color palettes offered to the user in Settings > Appearance.
 *
 * Every palette defines its own primary/secondary/tertiary accent colors while sharing the
 * same neutral background/surface/outline and error colors defined in Color.kt, so switching
 * palettes only changes the app's "personality" colors, not its base legibility.
 *
 * All base/on-base and container/on-container pairs below were validated to meet WCAG AA
 * contrast (>= 4.5:1) for normal text.
 */
enum class AppPalette(val id: String) {
    /** The app's original identity: purple, orange and green. */
    CLASSIC("classic"),

    /** Warm red, green and gold — evokes the end-of-year "Amigo Secreto" gift exchange. */
    FESTIVE("festive"),

    /** Vibrant pink, sunny yellow and teal — a playful, summery Brazilian carnival feel. */
    TROPICAL("tropical"),

    /** Calm teal, coral and sunny yellow — a fresh, breezy coastal feel. */
    OCEAN("ocean"),

    /** Bold berry pink, gold and teal — festive and a little more sophisticated. */
    BERRY("berry"),

    /** Elegant indigo, gold and emerald — a "gala night" evening-party feel. */
    MIDNIGHT_GOLD("midnight_gold");

    val lightColorScheme: ColorScheme
        get() = when (this) {
            CLASSIC -> classicLightScheme
            FESTIVE -> festiveLightScheme
            TROPICAL -> tropicalLightScheme
            OCEAN -> oceanLightScheme
            BERRY -> berryLightScheme
            MIDNIGHT_GOLD -> midnightGoldLightScheme
        }

    val darkColorScheme: ColorScheme
        get() = when (this) {
            CLASSIC -> classicDarkScheme
            FESTIVE -> festiveDarkScheme
            TROPICAL -> tropicalDarkScheme
            OCEAN -> oceanDarkScheme
            BERRY -> berryDarkScheme
            MIDNIGHT_GOLD -> midnightGoldDarkScheme
        }

    companion object {
        val Default = CLASSIC

        fun fromId(id: String?): AppPalette = entries.firstOrNull { it.id == id } ?: Default
    }
}

private val classicLightScheme = lightColorScheme(
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

private val classicDarkScheme = darkColorScheme(
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

// region Festive — red / green / gold
private val festiveLightScheme = lightColorScheme(
    primary = Color(0xFFDD2F3E),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFF4D7DA),
    onPrimaryContainer = Color(0xFFBB212E),
    secondary = Color(0xFF16873C),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFD7F4E1),
    onSecondaryContainer = Color(0xFF167A37),
    tertiary = Color(0xFF956F18),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFF4EBD7),
    onTertiaryContainer = Color(0xFF856418),
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

private val festiveDarkScheme = darkColorScheme(
    primary = Color(0xFFE3505C),
    onPrimary = Color(0xFF1A1A1A),
    primaryContainer = Color(0xFF6F2A30),
    onPrimaryContainer = Color(0xFFE69AA0),
    secondary = Color(0xFF199843),
    onSecondary = Color(0xFF1A1A1A),
    secondaryContainer = Color(0xFF2A6F41),
    onSecondaryContainer = Color(0xFFB5EDC8),
    tertiary = Color(0xFFA67D1B),
    onTertiary = Color(0xFF1A1A1A),
    tertiaryContainer = Color(0xFF6F5A2A),
    onTertiaryContainer = Color(0xFFE9D6A8),
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
// endregion

// region Tropical — pink / yellow / teal
private val tropicalLightScheme = lightColorScheme(
    primary = Color(0xFFDC207E),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFF4D7E6),
    onPrimaryContainer = Color(0xFFB6206B),
    secondary = Color(0xFF8E7114),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFF4EDD7),
    onSecondaryContainer = Color(0xFF806717),
    tertiary = Color(0xFF13837A),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFD7F4F1),
    onTertiaryContainer = Color(0xFF15776F),
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

private val tropicalDarkScheme = darkColorScheme(
    primary = Color(0xFFE44494),
    onPrimary = Color(0xFF1A1A1A),
    primaryContainer = Color(0xFF6F2A4D),
    onPrimaryContainer = Color(0xFFE69AC0),
    secondary = Color(0xFF9F8017),
    onSecondary = Color(0xFF1A1A1A),
    secondaryContainer = Color(0xFF6F5F2A),
    onSecondaryContainer = Color(0xFFEBDDAE),
    tertiary = Color(0xFF159388),
    onTertiary = Color(0xFF1A1A1A),
    tertiaryContainer = Color(0xFF2A6F69),
    onTertiaryContainer = Color(0xFFB9EEE9),
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
// endregion

// region Ocean — teal / coral / sunny yellow
private val oceanLightScheme = lightColorScheme(
    primary = Color(0xFF1A8094),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFD7EFF4),
    onPrimaryContainer = Color(0xFF177285),
    secondary = Color(0xFFC64F23),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFF4DFD7),
    onSecondaryContainer = Color(0xFFAA441E),
    tertiary = Color(0xFF8A7318),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFF4EED7),
    onTertiaryContainer = Color(0xFF7E6916),
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

private val oceanDarkScheme = darkColorScheme(
    primary = Color(0xFF1D90A6),
    onPrimary = Color(0xFF1A1A1A),
    primaryContainer = Color(0xFF2A636F),
    onPrimaryContainer = Color(0xFFA8DFE9),
    secondary = Color(0xFFDA5C2E),
    onSecondary = Color(0xFF1A1A1A),
    secondaryContainer = Color(0xFF6F3C2A),
    onSecondaryContainer = Color(0xFFE6AE99),
    tertiary = Color(0xFF9B821B),
    onTertiary = Color(0xFF1A1A1A),
    tertiaryContainer = Color(0xFF6F612A),
    onTertiaryContainer = Color(0xFFEBE0B0),
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
// endregion

// region Berry — magenta / gold / teal
private val berryLightScheme = lightColorScheme(
    primary = Color(0xFFD42895),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFF4D7E9),
    onPrimaryContainer = Color(0xFFB3207D),
    secondary = Color(0xFF9A6C1D),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFF4E9D7),
    onSecondaryContainer = Color(0xFF8A6018),
    tertiary = Color(0xFF198185),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFD7F3F4),
    onTertiaryContainer = Color(0xFF16767A),
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

private val berryDarkScheme = darkColorScheme(
    primary = Color(0xFFDC49A7),
    onPrimary = Color(0xFF1A1A1A),
    primaryContainer = Color(0xFF6F2A56),
    onPrimaryContainer = Color(0xFFE69BCA),
    secondary = Color(0xFFAD7A21),
    onSecondary = Color(0xFF1A1A1A),
    secondaryContainer = Color(0xFF6F562A),
    onSecondaryContainer = Color(0xFFE8D0A5),
    tertiary = Color(0xFF1C9296),
    onTertiary = Color(0xFF1A1A1A),
    tertiaryContainer = Color(0xFF2A6D6F),
    onTertiaryContainer = Color(0xFFB8ECED),
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
// endregion

// region Midnight Gold — indigo / gold / emerald
private val midnightGoldLightScheme = lightColorScheme(
    primary = Color(0xFF626BD1),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFD7DAF4),
    onPrimaryContainer = Color(0xFF3F4CDD),
    secondary = Color(0xFF8F712A),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFF4EBD7),
    onSecondaryContainer = Color(0xFF856418),
    tertiary = Color(0xFF268455),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFD7F4E6),
    onTertiaryContainer = Color(0xFF157A47),
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

private val midnightGoldDarkScheme = darkColorScheme(
    primary = Color(0xFF737CD6),
    onPrimary = Color(0xFF1A1A1A),
    primaryContainer = Color(0xFF2A306F),
    onPrimaryContainer = Color(0xFF949BE4),
    secondary = Color(0xFFA17F2F),
    onSecondary = Color(0xFF1A1A1A),
    secondaryContainer = Color(0xFF6F5A2A),
    onSecondaryContainer = Color(0xFFE9D6A8),
    tertiary = Color(0xFF2B9560),
    onTertiary = Color(0xFF1A1A1A),
    tertiaryContainer = Color(0xFF2A6F4D),
    onTertiaryContainer = Color(0xFFB6EDD1),
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
// endregion
