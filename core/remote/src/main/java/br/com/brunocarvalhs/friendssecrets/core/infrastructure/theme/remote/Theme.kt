package br.com.brunocarvalhs.friendssecrets.core.infrastructure.theme.remote

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class Theme(
    @SerialName("primary") val primary: String = "",
    @SerialName("onPrimary") val onPrimary: String = "",
    @SerialName("primaryContainer") val primaryContainer: String = "",
    @SerialName("onPrimaryContainer") val onPrimaryContainer: String = "",
    @SerialName("secondary") val secondary: String = "",
    @SerialName("onSecondary") val onSecondary: String = "",
    @SerialName("secondaryContainer") val secondaryContainer: String = "",
    @SerialName("onSecondaryContainer") val onSecondaryContainer: String = "",
    @SerialName("tertiary") val tertiary: String = "",
    @SerialName("onTertiary") val onTertiary: String = "",
    @SerialName("tertiaryContainer") val tertiaryContainer: String = "",
    @SerialName("onTertiaryContainer") val onTertiaryContainer: String = "",
    @SerialName("error") val error: String = "",
    @SerialName("onError") val onError: String = "",
    @SerialName("errorContainer") val errorContainer: String = "",
    @SerialName("onErrorContainer") val onErrorContainer: String = "",
    @SerialName("background") val background: String = "",
    @SerialName("onBackground") val onBackground: String = "",
    @SerialName("surface") val surface: String = "",
    @SerialName("onSurface") val onSurface: String = "",
    @SerialName("surfaceVariant") val surfaceVariant: String = "",
    @SerialName("onSurfaceVariant") val onSurfaceVariant: String = "",
    @SerialName("outline") val outline: String = "",
    @SerialName("outlineVariant") val outlineVariant: String = "",
    @SerialName("scrim") val scrim: String = "",
    @SerialName("inverseSurface") val inverseSurface: String = "",
    @SerialName("inverseOnSurface") val inverseOnSurface: String = "",
    @SerialName("inversePrimary") val inversePrimary: String = "",
    @SerialName("surfaceDim") val surfaceDim: String = "",
    @SerialName("surfaceBright") val surfaceBright: String = "",
    @SerialName("surfaceContainerLowest") val surfaceContainerLowest: String = "",
    @SerialName("surfaceContainerLow") val surfaceContainerLow: String = "",
    @SerialName("surfaceContainer") val surfaceContainer: String = "",
    @SerialName("surfaceContainerHigh") val surfaceContainerHigh: String = "",
    @SerialName("surfaceContainerHighest") val surfaceContainerHighest: String = ""
)
