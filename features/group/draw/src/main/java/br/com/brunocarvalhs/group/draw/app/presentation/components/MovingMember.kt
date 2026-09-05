package br.com.brunocarvalhs.group.draw.app.presentation.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.setValue
import br.com.brunocarvalhs.core.domain.model.UserModel
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

private const val BOUNDARY_DIVIDER = 2.2f
private const val GRAVITY_FORCE = 5f
private const val ORBIT_MIN_RADIUS_FRACTION = 0.3f
private const val ORBIT_MAX_RADIUS_FRACTION = 0.9f
private const val ORBIT_BASE_ANGULAR_SPEED = 0.12f
private const val ORBIT_ANGULAR_SPEED_VARIANCE = 0.08f
private const val SELF_SPIN_SPEED = 22f
private const val FULL_CIRCLE_RADIANS = (2f * PI).toFloat()

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

    var orbitRadius = 0f
    var orbitAngle = 0f
    var orbitAngularSpeed = 0f
    var orbitInitialized = false
}

internal fun updateMemberPosition(
    m: MovingMember,
    phase: DrawPhase,
    width: Float,
    height: Float
) {
    when (phase) {
        DrawPhase.IDLE -> bounce(m, width, height)
        DrawPhase.SPINNING -> spinInDrum(m, width, height)
        DrawPhase.FALLING -> {
            m.vy += GRAVITY_FORCE
            m.y += m.vy
        }
    }
}

private fun bounce(m: MovingMember, width: Float, height: Float) {
    m.x += m.vx
    m.y += m.vy

    if (m.x > width / BOUNDARY_DIVIDER || m.x < -width / BOUNDARY_DIVIDER) {
        m.vx *= -1f
    }
    if (m.y > height / BOUNDARY_DIVIDER || m.y < -height / BOUNDARY_DIVIDER) {
        m.vy *= -1f
    }
}

private fun spinInDrum(m: MovingMember, width: Float, height: Float) {
    if (!m.orbitInitialized) {
        val maxRadius = minOf(width, height) / 2f
        m.orbitRadius = maxRadius * (
            ORBIT_MIN_RADIUS_FRACTION +
                Random.nextFloat() * (ORBIT_MAX_RADIUS_FRACTION - ORBIT_MIN_RADIUS_FRACTION)
            )
        m.orbitAngle = Random.nextFloat() * FULL_CIRCLE_RADIANS
        val direction = if (Random.nextBoolean()) 1f else -1f
        m.orbitAngularSpeed = direction * (
            ORBIT_BASE_ANGULAR_SPEED + Random.nextFloat() * ORBIT_ANGULAR_SPEED_VARIANCE
            )
        m.orbitInitialized = true
    }

    m.orbitAngle += m.orbitAngularSpeed
    m.x = m.orbitRadius * cos(m.orbitAngle)
    m.y = m.orbitRadius * sin(m.orbitAngle)
    m.rotation += SELF_SPIN_SPEED
}
