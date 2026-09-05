package br.com.brunocarvalhs.group.draw.app.presentation.components

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.brunocarvalhs.core.domain.model.UserModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random


private const val INITIAL_VELOCITY_RANGE = 30f
private const val INITIAL_VELOCITY_OFFSET = 15f
private const val DROP_VELOCITY = -30f
private const val AVATARS_VISIBLE_WHILE_FALLING_DURATION = 500L
private const val LOADING_INDICATOR_DURATION = 2000L
private const val DRAW_DELAY = AVATARS_VISIBLE_WHILE_FALLING_DURATION + LOADING_INDICATOR_DURATION

@Composable
internal fun AnimatedDraw(
    members: List<UserModel>,
    onDraw: () -> Unit,
    modifier: Modifier = Modifier.Companion
) {
    var isFalling by remember { mutableStateOf(false) }
    var showLoading by remember { mutableStateOf(false) }
    var selectedMember by remember { mutableStateOf<UserModel?>(null) }
    val scope = rememberCoroutineScope()

    val movingMembers = remember(members) {
        members.map {
            MovingMember(
                user = it,
                x = 0f,
                y = 0f,
                vx = Random.Default.nextFloat() * INITIAL_VELOCITY_RANGE - INITIAL_VELOCITY_OFFSET,
                vy = Random.Default.nextFloat() * INITIAL_VELOCITY_RANGE - INITIAL_VELOCITY_OFFSET
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.Companion.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        DrawHeader()

        Spacer(modifier = Modifier.Companion.height(32.dp))

        Box(
            modifier = Modifier.Companion.weight(1f),
            contentAlignment = Alignment.Companion.Center
        ) {
            DrawAnimationArea(
                movingMembers = movingMembers,
                isFalling = isFalling,
                selectedMember = selectedMember,
                modifier = Modifier.Companion.fillMaxSize()
            )

            androidx.compose.animation.AnimatedVisibility(
                visible = showLoading,
                enter = fadeIn(animationSpec = tween(durationMillis = 300)),
                exit = fadeOut(animationSpec = tween(durationMillis = 200))
            ) {
                DrawShufflingIndicator()
            }
        }

        Spacer(modifier = Modifier.Companion.height(32.dp))

        DrawActionButton(
            isFalling = isFalling,
            onClick = {
                if (!isFalling) {
                    selectedMember = movingMembers.random().user
                    movingMembers.forEach { it.vy = DROP_VELOCITY }
                    isFalling = true
                    scope.launch {
                        delay(AVATARS_VISIBLE_WHILE_FALLING_DURATION)
                        showLoading = true
                    }
                    scope.launch {
                        delay(DRAW_DELAY)
                        onDraw()
                    }
                }
            }
        )
    }
}
