package br.com.brunocarvalhs.group.draw.app.presentation.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import kotlin.random.Random

private val CONFETTI_COLORS = listOf(
    Color(0xFFE53935),
    Color(0xFFFDD835),
    Color(0xFF43A047),
    Color(0xFF1E88E5),
    Color(0xFFFF9800),
    Color(0xFF8E24AA),
)

private data class ConfettiPiece(
    val startXFraction: Float,
    val fallDelayFraction: Float,
    val driftAmplitude: Float,
    val rotationSpeed: Float,
    val sizeDp: Float,
    val color: Color,
)

@Composable
internal fun ConfettiOverlay(
    visible: Boolean,
    modifier: Modifier = Modifier,
    particleCount: Int = 60,
) {
    if (!visible) return

    val pieces = remember {
        List(particleCount) {
            ConfettiPiece(
                startXFraction = Random.nextFloat(),
                fallDelayFraction = Random.nextFloat() * 0.3f,
                driftAmplitude = Random.nextFloat() * 60f - 30f,
                rotationSpeed = Random.nextFloat() * 720f - 360f,
                sizeDp = Random.nextFloat() * 6f + 6f,
                color = CONFETTI_COLORS.random(),
            )
        }
    }

    val progress by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 2200, easing = LinearEasing),
        label = "confettiProgress"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val canvasHeight = size.height
        val canvasWidth = size.width

        pieces.forEach { piece ->
            val localProgress =
                ((progress - piece.fallDelayFraction) / (1f - piece.fallDelayFraction))
                    .coerceIn(0f, 1f)
            if (localProgress <= 0f) return@forEach

            val y = canvasHeight * localProgress
            val x = canvasWidth * piece.startXFraction +
                piece.driftAmplitude * kotlin.math.sin(localProgress * Math.PI * 2).toFloat()
            val alpha = (1f - localProgress).coerceIn(0f, 1f)
            val pieceSizePx = piece.sizeDp.dp.toPx()

            rotate(degrees = piece.rotationSpeed * localProgress, pivot = Offset(x, y)) {
                drawRect(
                    color = piece.color.copy(alpha = alpha),
                    topLeft = Offset(x - pieceSizePx / 2, y - pieceSizePx / 2),
                    size = androidx.compose.ui.geometry.Size(pieceSizePx, pieceSizePx * 2)
                )
            }
        }
    }
}
