package br.com.brunocarvalhs.friendssecrets.presentation.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Dimensões padronizadas para uso em toda a aplicação.
 * Isso facilita a manutenção e consistência do design.
 */
data class Dimensions(
    // Espaçamentos
    val spacingNone: Dp = 0.dp,
    val spacingExtraSmall: Dp = 4.dp,
    val spacingSmall: Dp = 8.dp,
    val spacingMedium: Dp = 16.dp,
    val spacingLarge: Dp = 24.dp,
    val spacingExtraLarge: Dp = 32.dp,
    val spacingXXL: Dp = 48.dp,
    val spacingXXXL: Dp = 64.dp,
    
    // Elevações
    val elevationSmall: Dp = 2.dp,
    val elevationMedium: Dp = 4.dp,
    val elevationLarge: Dp = 8.dp,
    
    // Tamanhos de componentes
    val buttonHeight: Dp = 48.dp,
    val iconSize: Dp = 24.dp,
    val iconSizeSmall: Dp = 16.dp,
    val iconSizeLarge: Dp = 32.dp,
    
    // Bordas
    val borderSmall: Dp = 1.dp,
    val borderMedium: Dp = 2.dp,
    val borderLarge: Dp = 4.dp,
    
    // Raios de cantos
    val cornerSmall: Dp = 4.dp,
    val cornerMedium: Dp = 8.dp,
    val cornerLarge: Dp = 16.dp,
    val cornerRound: Dp = 24.dp
)

/**
 * CompositionLocal para acessar as dimensões em qualquer lugar da UI.
 */
val LocalDimensions = staticCompositionLocalOf { Dimensions() }