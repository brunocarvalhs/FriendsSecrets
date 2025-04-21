package br.com.brunocarvalhs.friendssecrets.commons.remote.theme

import android.content.Context
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import br.com.brunocarvalhs.friendssecrets.commons.remote.RemoteProvider
import com.google.gson.Gson
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ThemeRemoteProvider @Inject constructor(
    private val context: Context,
    private val remoteProvider: RemoteProvider,
    private val gson: Gson
) {

    init {
        remoteProvider.fetchAndActivate()
    }

    fun getDarkColorScheme(): ColorScheme? {
        try {
            val data = remoteProvider.getString(THEME_DARK)

            val theme = gson.fromJson(data, Theme::class.java)

            return darkColorScheme(
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
        } catch (e: Exception) {
            Timber.e(e)
            return null
        }
    }

    fun getLightColorScheme(): ColorScheme? {
        try {
            val data = remoteProvider.getString(THEME_LIGHT)

            val theme = gson.fromJson(data, Theme::class.java)

            return lightColorScheme(
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
        } catch (e: Exception) {
            Timber.e(e)
            return null
        }
    }

    private fun hexToComposeColor(hex: String): Color {
        val cleanedHex = hex.removePrefix("#")

        return when (cleanedHex.length) {
            6 -> {
                val r = Integer.parseInt(cleanedHex.substring(0, 2), 16)
                val g = Integer.parseInt(cleanedHex.substring(2, 4), 16)
                val b = Integer.parseInt(cleanedHex.substring(4, 6), 16)
                Color(red = r, green = g, blue = b)
            }

            8 -> {
                val a = Integer.parseInt(cleanedHex.substring(0, 2), 16)
                val r = Integer.parseInt(cleanedHex.substring(2, 4), 16)
                val g = Integer.parseInt(cleanedHex.substring(4, 6), 16)
                val b = Integer.parseInt(cleanedHex.substring(6, 8), 16)
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
    }
}