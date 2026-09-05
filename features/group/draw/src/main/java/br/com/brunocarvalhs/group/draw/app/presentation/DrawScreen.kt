package br.com.brunocarvalhs.group.draw.app.presentation

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.brunocarvalhs.core.domain.model.UserModel
import br.com.brunocarvalhs.group.draw.app.presentation.components.AnimatedDraw
import br.com.brunocarvalhs.group.draw.app.presentation.components.ConfettiOverlay
import br.com.brunocarvalhs.group.draw.app.presentation.components.DrawResultsList
import br.com.brunocarvalhs.group.draw.app.presentation.components.DrawTopBar
import kotlinx.coroutines.delay

@Composable
internal fun DrawScreen(
    viewModel: DrawViewModel,
    onBack: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val activity = LocalActivity.current

    LaunchedEffect(uiState.shouldRequestReview) {
        if (uiState.shouldRequestReview && activity != null) {
            delay(REVIEW_PROMPT_DELAY_MS)
            viewModel.handleIntent(DrawIntent.RequestReview(activity))
        }
    }

    DrawContent(
        uiState = uiState,
        onShare = { viewModel.handleIntent(DrawIntent.Share(it)) },
        onDraw = { viewModel.handleIntent(DrawIntent.Draw) },
        onBack = onBack
    )
}

private const val REVIEW_PROMPT_DELAY_MS = 2500L

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DrawContent(
    uiState: DrawUiState,
    onShare: (String) -> Unit = {},
    onDraw: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val isDrawn = uiState.isDrawn
    val members = uiState.members
    val results = uiState.results

    var previousIsDrawn by remember { mutableStateOf(isDrawn) }
    var showCelebration by remember { mutableStateOf(false) }

    LaunchedEffect(isDrawn) {
        if (isDrawn && !previousIsDrawn) {
            showCelebration = true
            delay(CELEBRATION_DURATION_MS)
            showCelebration = false
        }
        previousIsDrawn = isDrawn
    }

    Scaffold(
        topBar = {
            DrawTopBar(isDrawn = isDrawn, onBack = onBack)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedVisibility(
                visible = !isDrawn,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                AnimatedDraw(
                    members = members,
                    isDrawn = isDrawn,
                    onDraw = onDraw
                )
            }

            AnimatedVisibility(
                visible = isDrawn,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                DrawResultsList(
                    results = results,
                    members = members,
                    onShare = onShare
                )
            }

            ConfettiOverlay(visible = showCelebration)
        }
    }
}

private const val CELEBRATION_DURATION_MS = 2200L

@Preview(uiMode = UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = UI_MODE_NIGHT_YES, showBackground = true)
@Composable
internal fun DrawNewLayoutPreview() {
    MaterialTheme {
        DrawContent(
            uiState = DrawUiState(
                isDrawn = true,
                results = mapOf(
                    "Bruno" to "Carlos", "Carlos" to "Alice", "Alice" to "Bruno"
                ),
                members = listOf(
                    UserModel(name = "Bruno"),
                    UserModel(name = "Carlos"),
                    UserModel(name = "Alice")
                )
            )
        )
    }
}

@Preview(uiMode = UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = UI_MODE_NIGHT_YES, showBackground = true)
@Composable
internal fun DrawContentPreview() {
    MaterialTheme {
        DrawContent(
            uiState = DrawUiState(
                isDrawn = false,
                members = listOf(
                    UserModel(name = "Bruno"),
                    UserModel(name = "Carlos")
                )
            )
        )
    }
}
