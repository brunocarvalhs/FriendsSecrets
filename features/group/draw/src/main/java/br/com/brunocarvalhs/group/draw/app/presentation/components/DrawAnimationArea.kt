package br.com.brunocarvalhs.group.draw.app.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.com.brunocarvalhs.core.domain.model.UserModel
import br.com.brunocarvalhs.group.draw.R

@Composable
internal fun DrawAnimationArea(
    movingMembers: List<MovingMember>,
    phase: DrawPhase,
    selectedMember: UserModel?,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
    ) {
        val width = constraints.maxWidth.toFloat()
        val height = constraints.maxHeight.toFloat()

        LaunchedEffect(phase) {
            while (true) {
                withFrameMillis {
                    movingMembers.forEach { m ->
                        updateMemberPosition(m, phase, width, height)
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
                        rotationZ = member.rotation
                        scaleX = scale
                        scaleY = scale
                    }
                    .align(Alignment.Center)
            )
        }
    }
}
