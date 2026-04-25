package br.com.brunocarvalhs.friendssecrets.core.infrastructure.theme.remote

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import br.com.brunocarvalhs.friendssecrets.core.infrastructure.domain.ThemeRemote
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject

internal class ThemeRemoteProvider @Inject constructor(
    private val remoteProvider: FirebaseRemoteConfig
) : ThemeRemote {

    private val json = Json { ignoreUnknownKeys = true }

    init {
        remoteProvider.fetchAndActivate()
    }

    override fun getDarkColorScheme(): ColorScheme? {
        val data = remoteProvider.getString(THEME_DARK)
        if (data.isBlank()) return null

        var result: ColorScheme? = null
        try {
            val theme = json.decodeFromString<Theme>(data)

            result = darkColorScheme(
                primary = hexToComposeColor(theme.primary),
                onPrimary = hexToComposeColor(theme.onPrimary),
                primaryContainer = hexToComposeColor(theme.primaryContainer),
                onPrimaryContainer = hexToComposeColor(theme.onPrimaryContainer),
                secondary = hexToComposeColor(theme.secondary),
                onSecondary = hexToComposeColor(theme.onSecondary),
                secondaryContainer = hexToComposeColor(theme.secondaryContainer),
                onSecondaryContainer = hexToComposeColor(theme.onSecondaryContainer),
                tertiary = hexToComposeColor(theme.tertiary),
                onTertiary = hexToComposeColor(theme.onTertiary),
                tertiaryContainer = hexToComposeColor(theme.tertiaryContainer),
                onTertiaryContainer = hexToComposeColor(theme.onTertiaryContainer),
                error = hexToComposeColor(theme.error),
                onError = hexToComposeColor(theme.onError),
                errorContainer = hexToComposeColor(theme.errorContainer),
                onErrorContainer = hexToComposeColor(theme.onErrorContainer),
                background = hexToComposeColor(theme.background),
                onBackground = hexToComposeColor(theme.onBackground),
                surface = hexToComposeColor(theme.surface),
                onSurface = hexToComposeColor(theme.onSurface),
                surfaceVariant = hexToComposeColor(theme.surfaceVariant),
                onSurfaceVariant = hexToComposeColor(theme.onSurfaceVariant),
                outline = hexToComposeColor(theme.outline),
                outlineVariant = hexToComposeColor(theme.outlineVariant),
                scrim = hexToComposeColor(theme.scrim),
                inverseSurface = hexToComposeColor(theme.inverseSurface),
                inverseOnSurface = hexToComposeColor(theme.inverseOnSurface),
                inversePrimary = hexToComposeColor(theme.inversePrimary),
                surfaceDim = hexToComposeColor(theme.surfaceDim),
                surfaceBright = hexToComposeColor(theme.surfaceBright),
                surfaceContainerLowest = hexToComposeColor(theme.surfaceContainerLowest),
                surfaceContainerLow = hexToComposeColor(theme.surfaceContainerLow),
                surfaceContainer = hexToComposeColor(theme.surfaceContainer),
                surfaceContainerHigh = hexToComposeColor(theme.surfaceContainerHigh),
                surfaceContainerHighest = hexToComposeColor(theme.surfaceContainerHighest)
            )
        } catch (e: SerializationException) {
            Timber.e(e)
        } catch (e: IllegalArgumentException) {
            Timber.e(e)
        }

        return result
    }

    override fun getLightColorScheme(): ColorScheme? {
        val data = remoteProvider.getString(THEME_LIGHT)
        if (data.isBlank()) return null

        var result: ColorScheme? = null
        try {
            val theme = json.decodeFromString<Theme>(data)

            result = lightColorScheme(
                primary = hexToComposeColor(theme.primary),
                onPrimary = hexToComposeColor(theme.onPrimary),
                primaryContainer = hexToComposeColor(theme.primaryContainer),
                onPrimaryContainer = hexToComposeColor(theme.onPrimaryContainer),
                secondary = hexToComposeColor(theme.secondary),
                onSecondary = hexToComposeColor(theme.onSecondary),
                secondaryContainer = hexToComposeColor(theme.secondaryContainer),
                onSecondaryContainer = hexToComposeColor(theme.onSecondaryContainer),
                tertiary = hexToComposeColor(theme.tertiary),
                onTertiary = hexToComposeColor(theme.onTertiary),
                tertiaryContainer = hexToComposeColor(theme.tertiaryContainer),
                onTertiaryContainer = hexToComposeColor(theme.onTertiaryContainer),
                error = hexToComposeColor(theme.error),
                onError = hexToComposeColor(theme.onError),
                errorContainer = hexToComposeColor(theme.errorContainer),
                onErrorContainer = hexToComposeColor(theme.onErrorContainer),
                background = hexToComposeColor(theme.background),
                onBackground = hexToComposeColor(theme.onBackground),
                surface = hexToComposeColor(theme.surface),
                onSurface = hexToComposeColor(theme.onSurface),
                surfaceVariant = hexToComposeColor(theme.surfaceVariant),
                onSurfaceVariant = hexToComposeColor(theme.onSurfaceVariant),
                outline = hexToComposeColor(theme.outline),
                outlineVariant = hexToComposeColor(theme.outlineVariant),
                scrim = hexToComposeColor(theme.scrim),
                inverseSurface = hexToComposeColor(theme.inverseSurface),
                inverseOnSurface = hexToComposeColor(theme.inverseOnSurface),
                inversePrimary = hexToComposeColor(theme.inversePrimary),
                surfaceDim = hexToComposeColor(theme.surfaceDim),
                surfaceBright = hexToComposeColor(theme.surfaceBright),
                surfaceContainerLowest = hexToComposeColor(theme.surfaceContainerLowest),
                surfaceContainerLow = hexToComposeColor(theme.surfaceContainerLow),
                surfaceContainer = hexToComposeColor(theme.surfaceContainer),
                surfaceContainerHigh = hexToComposeColor(theme.surfaceContainerHigh),
                surfaceContainerHighest = hexToComposeColor(theme.surfaceContainerHighest)
            )
        } catch (e: SerializationException) {
            Timber.e(e)
        } catch (e: IllegalArgumentException) {
            Timber.e(e)
        }

        return result
    }

    private fun hexToComposeColor(hex: String): Color {
        val cleanedHex = hex.removePrefix("#")

        return when (cleanedHex.length) {
            HEX_LENGTH_RGB -> {
                val r = Integer.parseInt(cleanedHex.substring(OFFSET_0, OFFSET_2), RADIX_HEX)
                val g = Integer.parseInt(cleanedHex.substring(OFFSET_2, OFFSET_4), RADIX_HEX)
                val b = Integer.parseInt(cleanedHex.substring(OFFSET_4, OFFSET_6), RADIX_HEX)
                Color(red = r, green = g, blue = b)
            }

            HEX_LENGTH_ARGB -> {
                val a = Integer.parseInt(cleanedHex.substring(OFFSET_0, OFFSET_2), RADIX_HEX)
                val r = Integer.parseInt(cleanedHex.substring(OFFSET_2, OFFSET_4), RADIX_HEX)
                val g = Integer.parseInt(cleanedHex.substring(OFFSET_4, OFFSET_6), RADIX_HEX)
                val b = Integer.parseInt(cleanedHex.substring(OFFSET_6, OFFSET_8), RADIX_HEX)
                Color(red = r, green = g, blue = b, alpha = a)
            }

            else -> {
                throw IllegalArgumentException("Formato de cor inválido. Use #RRGGBB ou #AARRGGBB.")
            }
        }
    }

    companion object {
        const val THEME_DARK = "theme_dark"
        const val THEME_LIGHT = "theme_light"
        private const val HEX_LENGTH_RGB = 6
        private const val HEX_LENGTH_ARGB = 8
        private const val RADIX_HEX = 16
        private const val OFFSET_0 = 0
        private const val OFFSET_2 = 2
        private const val OFFSET_4 = 4
        private const val OFFSET_6 = 6
        private const val OFFSET_8 = 8
    }
}
