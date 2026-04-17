package br.com.brunocarvalhs.group.draw.app.presentation

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.brunocarvalhs.friendssecrets.domain.model.UserModel
import br.com.brunocarvalhs.group.draw.R
import br.com.brunocarvalhs.group.draw.app.presentation.components.ContactAvatar
import br.com.brunocarvalhs.group.draw.app.presentation.components.DrawItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
internal fun DrawScreen(
    viewModel: DrawViewModel,
    onBack: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DrawContent(
        isDrawn = uiState.isDrawn,
        results = uiState.results,
        members = uiState.members,
        onShare = { viewModel.handleIntent(DrawIntent.Share(it)) },
        onDraw = { viewModel.handleIntent(DrawIntent.Draw) },
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DrawContent(
    isDrawn: Boolean = false,
    members: List<UserModel> = emptyList(),
    results: Map<String, String> = emptyMap(),
    onShare: (String) -> Unit = {},
    onDraw: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isDrawn) {
                            stringResource(R.string.draw_results)
                        } else {
                            stringResource(R.string.secret_santa_draw)
                        },
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
            )
        }) { innerPadding ->
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
                    onDraw = onDraw
                )
            }

            AnimatedVisibility(
                visible = isDrawn,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Text(
                            text = stringResource(R.string.the_draw_has_been_completed_share_the_results_with_group_members),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                    items(results.toList()) { (member, secretFriend) ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = MaterialTheme.shapes.medium,
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            DrawItem(members = members, member = member) {
                                onShare(secretFriend)
                            }
                        }
                    }
                }
            }
        }
    }
}

class MovingMember(
    val user: UserModel,
    x: Float,
    y: Float,
    var vx: Float,
    var vy: Float
) {
    var x by mutableStateOf(x)
    var y by mutableStateOf(y)
}

@Composable
fun AnimatedDraw(
    members: List<UserModel>,
    onDraw: () -> Unit
) {
    var isFalling by remember { mutableStateOf(false) }
    var selectedMember by remember { mutableStateOf<UserModel?>(null) }
    val scope = rememberCoroutineScope()

    val movingMembers = remember(members) {
        members.map {
            MovingMember(
                user = it,
                x = 0f,
                y = 0f,
                vx = Random.nextFloat() * 30f - 15f,
                vy = Random.nextFloat() * 30f - 15f
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.ready_to_draw),
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            ),
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.tap_the_button_below_to_start_the_draw),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        BoxWithConstraints(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            val width = constraints.maxWidth.toFloat()
            val height = constraints.maxHeight.toFloat()

            LaunchedEffect(Unit) {
                while (true) {
                    withFrameMillis {
                        movingMembers.forEach { m ->
                            if (!isFalling) {
                                m.x += m.vx
                                m.y += m.vy

                                if (m.x > width / 2.2f || m.x < -width / 2.2f) {
                                    m.vx *= -1
                                }
                                if (m.y > height / 2.2f || m.y < -height / 2.2f) {
                                    m.vy *= -1
                                }
                            } else {
                                m.vy += 5f
                                m.y += m.vy
                            }
                        }
                    }
                }
            }

            movingMembers.forEach { member ->
                val scale by animateFloatAsState(
                    targetValue = if (member.user == selectedMember) 1.8f else 1f,
                    label = stringResource(R.string.scale)
                )

                ContactAvatar(
                    name = member.user.name,
                    photoUrl = member.user.photoUrl,
                    modifier = Modifier
                        .size(80.dp)
                        .graphicsLayer {
                            translationX = member.x
                            translationY = member.y
                            scaleX = scale
                            scaleY = scale
                        }
                        .align(Alignment.Center)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if (!isFalling) {
                    selectedMember = movingMembers.random().user
                    movingMembers.forEach { it.vy = -30f }
                    isFalling = true
                    scope.launch {
                        delay(2000)
                        onDraw()
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = MaterialTheme.shapes.extraLarge,
            enabled = !isFalling
        ) {
            Text(
                text = if (isFalling) {
                    stringResource(R.string.drawing)
                } else {
                    stringResource(R.string.just_drawn)
                },
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Preview(uiMode = UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun DrawNewLayoutPreview() {
    MaterialTheme {
        DrawContent(
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
    }
}

@Preview(uiMode = UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun DrawContentPreview() {
    MaterialTheme {
        DrawContent(
            isDrawn = false,
            members = listOf(
                UserModel(name = "Bruno"),
                UserModel(name = "Carlos")
            )
        )
    }
}