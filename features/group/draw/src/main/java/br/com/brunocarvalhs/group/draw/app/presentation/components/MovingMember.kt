package br.com.brunocarvalhs.group.draw.app.presentation.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import br.com.brunocarvalhs.core.domain.model.UserModel

private const val BOUNDARY_DIVIDER = 2.2f
private const val GRAVITY_FORCE = 5f
private const val TILT_ACCELERATION = 4f
private const val VELOCITY_DAMPING = 0.98f
private const val MAX_IDLE_SPEED = 18f
private const val SPIN_ROTATION_SPEED = 22f

internal enum class DrawPhase { IDLE, SPINNING, FALLING }

internal class MovingMember(
    val user: UserModel,
    x: Float,
    y: Float,
    var vx: Float,
    var vy: Float
) {
    var x by mutableFloatStateOf(x)
    var y by mutableFloatStateOf(y)
    var rotation by mutableFloatStateOf(0f)
}

internal fun updateMemberPosition(
    m: MovingMember,
    phase: DrawPhase,
    tilt: Offset,
    width: Float,
    height: Float
) {
    when (phase) {
        DrawPhase.IDLE -> moveWithTilt(m, tilt, width, height)
        DrawPhase.SPINNING -> m.rotation += SPIN_ROTATION_SPEED
        DrawPhase.FALLING -> {
            m.vy += GRAVITY_FORCE
            m.y += m.vy
        }
    }
}

private fun moveWithTilt(m: MovingMember, tilt: Offset, width: Float, height: Float) {
    m.vx = ((m.vx + tilt.x * TILT_ACCELERATION) * VELOCITY_DAMPING)
        .coerceIn(-MAX_IDLE_SPEED, MAX_IDLE_SPEED)
    m.vy = ((m.vy + tilt.y * TILT_ACCELERATION) * VELOCITY_DAMPING)
        .coerceIn(-MAX_IDLE_SPEED, MAX_IDLE_SPEED)

    m.x += m.vx
    m.y += m.vy

    val boundX = width / BOUNDARY_DIVIDER
    val boundY = height / BOUNDARY_DIVIDER

    if (m.x > boundX || m.x < -boundX) {
        m.vx *= -1f
        m.x = m.x.coerceIn(-boundX, boundX)
    }
    if (m.y > boundY || m.y < -boundY) {
        m.vy *= -1f
        m.y = m.y.coerceIn(-boundY, boundY)
    }
}
