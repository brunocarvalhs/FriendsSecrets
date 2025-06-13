package br.com.brunocarvalhs.group.app.draw

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.util.Base64
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.ui.components.ErrorComponent
import br.com.brunocarvalhs.friendssecrets.ui.components.LoadingProgress
import br.com.brunocarvalhs.friendssecrets.ui.components.NavigationBackIconButton
import br.com.brunocarvalhs.friendssecrets.ui.fake.toFake
import br.com.brunocarvalhs.friendssecrets.ui.theme.FriendsSecretsTheme
import br.com.brunocarvalhs.group.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawScreen(
    groupId: String,
    navController: NavController,
    viewModel: DrawViewModel,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.eventIntent(DrawIntent.FetchDraw(group = groupId))
    }

    DrawNewLayout(
        groupId = groupId,
        navController = navController,
        uiState = uiState,
        eventIntent = viewModel::eventIntent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawNewLayout(
    groupId: String,
    navController: NavController,
    uiState: DrawUiState,
    eventIntent: (DrawIntent) -> Unit,
) {
    var inputCode by remember { mutableStateOf(TextFieldValue("")) }
    var isFlipped by remember { mutableStateOf(false) }

    val decodedSecret = remember(uiState) {
        uiState.takeIf { it is DrawUiState.Success }?.let {
            val drawSecret = (it as DrawUiState.Success).draw.keys.first()
            return@let drawSecret
        }
    }

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text(stringResource(R.string.draw_screen_title)) },
                navigationIcon = { NavigationBackIconButton(navController) }
            )
        },
        content = { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                when (uiState) {
                    is DrawUiState.Idle -> {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .padding(32.dp)
                                .fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = inputCode,
                                onValueChange = { inputCode = it },
                                label = { Text(stringResource(R.string.draw_screen_code_secret)) },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            Button(
                                onClick = {
                                    eventIntent(
                                        DrawIntent.FetchDraw(
                                            group = groupId,
                                            code = inputCode.text
                                        )
                                    )
                                    isFlipped = false
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(stringResource(R.string.draw_screen_action_title))
                            }
                        }
                    }

                    is DrawUiState.Loading -> {
                        LoadingProgress()
                    }

                    is DrawUiState.Error -> {
                        ErrorComponent(
                            message = uiState.error,
                            onRefresh = { eventIntent(DrawIntent.Refresh) }
                        )
                    }

                    is DrawUiState.Success -> {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(32.dp)
                        ) {
                            FlipCard(
                                frontContent = {
                                    Box(
                                        modifier = Modifier
                                            .size(250.dp)
                                            .clip(RoundedCornerShape(16.dp))
                                            .background(MaterialTheme.colorScheme.primaryContainer),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = stringResource(R.string.draw_card_front_hint),
                                            style = MaterialTheme.typography.titleMedium,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.padding(16.dp)
                                        )
                                    }
                                },
                                backContent = {
                                    Box(
                                        modifier = Modifier
                                            .size(250.dp)
                                            .clip(RoundedCornerShape(16.dp))
                                            .background(MaterialTheme.colorScheme.primary),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = decodedSecret
                                                ?: stringResource(R.string.draw_error_decode),
                                            style = MaterialTheme.typography.headlineMedium.copy(
                                                fontWeight = FontWeight.ExtraBold
                                            ),
                                            color = MaterialTheme.colorScheme.onPrimary,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.padding(24.dp)
                                        )
                                    }
                                },
                                isFlipped = isFlipped,
                                onClick = { isFlipped = !isFlipped }
                            )
                            Spacer(modifier = Modifier.height(32.dp))

                            Text(
                                text = stringResource(R.string.draw_screen_title_like),
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            val likes = uiState.draw.values.first().split("|")
                                .map { it.trim() }
                                .filter { it.isNotBlank() }

                            if (likes.isNotEmpty()) {
                                Text(
                                    text = stringResource(R.string.draw_screen_title_like),
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Spacer(modifier = Modifier.height(12.dp))

                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    items(likes) { like ->
                                        AssistChip(
                                            onClick = { /* opcional */ },
                                            label = { Text(like) },
                                            modifier = Modifier.height(36.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(48.dp))
                            } else {
                                Spacer(modifier = Modifier.height(48.dp)) // mantém espaço antes do botão
                            }


                            Spacer(modifier = Modifier.height(48.dp))

                            Button(
                                onClick = {
                                    eventIntent(DrawIntent.Refresh)
                                    isFlipped = false
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(stringResource(R.string.draw_screen_action_reset))
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun FlipCard(
    frontContent: @Composable () -> Unit,
    backContent: @Composable () -> Unit,
    isFlipped: Boolean,
    onClick: () -> Unit,
) {
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 600)
    )

    Box(
        modifier = Modifier
            .clickable { onClick() }
            .graphicsLayer {
                cameraDistance = 12 * density
                rotationY = rotation
            },
        contentAlignment = Alignment.Center
    ) {
        if (rotation <= 90f) {
            frontContent()
        } else {
            Box(
                modifier = Modifier.graphicsLayer {
                    rotationY = 180f
                    rotationZ = 0f
                }
            ) {
                backContent()
            }
        }
    }
}


private class DrawPreviewProvider : PreviewParameterProvider<DrawUiState> {
    override val values = sequenceOf(
        DrawUiState.Idle,
        DrawUiState.Loading,
        DrawUiState.Success(
            draw = mapOf(
                Base64.encodeToString(
                    "Maria".toByteArray(),
                    Base64.NO_WRAP
                ) to "Chocolate | Viagem | Café"
            ),
            group = GroupEntities.toFake("Grupo Amigo Secreto")
        ),
        DrawUiState.Error("Não foi possível carregar o amigo secreto")
    )
}

@Preview(uiMode = UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun DrawNewLayoutPreview(@PreviewParameter(DrawPreviewProvider::class) state: DrawUiState) {
    FriendsSecretsTheme {
        DrawNewLayout(
            groupId = "fake_id",
            navController = rememberNavController(),
            uiState = state,
            eventIntent = {}
        )
    }
}
