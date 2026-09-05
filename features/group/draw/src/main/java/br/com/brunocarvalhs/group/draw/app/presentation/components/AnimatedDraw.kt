package br.com.brunocarvalhs.group.draw.app.presentation.components

import androidx.compose.foundation.layout.Arrangement
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
private const val SPIN_DURATION_MS = 1800L
private const val FALL_DURATION_MS = 600L

@Composable
internal fun AnimatedDraw(
    members: List<UserModel>,
    onDraw: () -> Unit,
    modifier: Modifier = Modifier.Companion
) {
    var phase by remember { mutableStateOf(DrawPhase.IDLE) }
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

        DrawAnimationArea(
            movingMembers = movingMembers,
            phase = phase,
            selectedMember = selectedMember,
            modifier = Modifier.Companion.weight(1f)
        )

        Spacer(modifier = Modifier.Companion.height(32.dp))

        DrawActionButton(
            isFalling = phase != DrawPhase.IDLE,
            onClick = {
                if (phase == DrawPhase.IDLE) {
                    selectedMember = movingMembers.random().user
                    phase = DrawPhase.SPINNING
                    scope.launch {
                        delay(SPIN_DURATION_MS)
                        movingMembers.forEach { it.vy = DROP_VELOCITY }
                        phase = DrawPhase.FALLING
                        delay(FALL_DURATION_MS)
                        onDraw()
                    }
                }
            }
        )
    }
}
