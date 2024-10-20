package br.com.brunocarvalhs.friendssecrets.commons.remote.theme

import androidx.compose.runtime.Immutable
import com.google.gson.annotations.SerializedName

@Immutable
data class Theme(
    @SerializedName("primary") val primary: String = "",
    @SerializedName("onPrimary") val onPrimary: String = "",
    @SerializedName("primaryContainer") val primaryContainer: String = "",
    @SerializedName("onPrimaryContainer") val onPrimaryContainer: String = "",
    @SerializedName("secondary") val secondary: String = "",
    @SerializedName("onSecondary") val onSecondary: String = "",
    @SerializedName("secondaryContainer") val secondaryContainer: String = "",
    @SerializedName("onSecondaryContainer") val onSecondaryContainer: String = "",
    @SerializedName("tertiary") val tertiary: String = "",
    @SerializedName("onTertiary") val onTertiary: String = "",
    @SerializedName("tertiaryContainer") val tertiaryContainer: String = "",
    @SerializedName("onTertiaryContainer") val onTertiaryContainer: String = "",
    @SerializedName("error") val error: String = "",
    @SerializedName("onError") val onError: String = "",
    @SerializedName("errorContainer") val errorContainer: String = "",
    @SerializedName("onErrorContainer") val onErrorContainer: String = "",
    @SerializedName("background") val background: String = "",
    @SerializedName("onBackground") val onBackground: String = "",
    @SerializedName("surface") val surface: String = "",
    @SerializedName("onSurface") val onSurface: String = "",
    @SerializedName("surfaceVariant") val surfaceVariant: String = "",
    @SerializedName("onSurfaceVariant") val onSurfaceVariant: String = "",
    @SerializedName("outline") val outline: String = "",
    @SerializedName("outlineVariant") val outlineVariant: String = "",
    @SerializedName("scrim") val scrim: String = "",
    @SerializedName("inverseSurface") val inverseSurface: String = "",
    @SerializedName("inverseOnSurface") val inverseOnSurface: String = "",
    @SerializedName("inversePrimary") val inversePrimary: String = "",
    @SerializedName("surfaceDim") val surfaceDim: String = "",
    @SerializedName("surfaceBright") val surfaceBright: String = "",
    @SerializedName("surfaceContainerLowest") val surfaceContainerLowest: String = "",
    @SerializedName("surfaceContainerLow") val surfaceContainerLow: String = "",
    @SerializedName("surfaceContainer") val surfaceContainer: String = "",
    @SerializedName("surfaceContainerHigh") val surfaceContainerHigh: String = "",
    @SerializedName("surfaceContainerHighest") val surfaceContainerHighest: String = ""
)
