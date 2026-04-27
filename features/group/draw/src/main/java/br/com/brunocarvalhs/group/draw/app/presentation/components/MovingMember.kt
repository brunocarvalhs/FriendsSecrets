package br.com.brunocarvalhs.group.draw.app.presentation.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.setValue
import br.com.brunocarvalhs.core.domain.model.UserModel

private const val BOUNDARY_DIVIDER = 2.2f
private const val GRAVITY_FORCE = 5f

internal class MovingMember(
    val user: UserModel,
    x: Float,
    y: Float,
    var vx: Float,
    var vy: Float
) {
    var x by mutableFloatStateOf(x)
    var y by mutableFloatStateOf(y)
}

internal fun updateMemberPosition(m: MovingMember, isFalling: Boolean, width: Float, height: Float) {
    if (!isFalling) {
        m.x += m.vx
        m.y += m.vy

        if (m.x > width / BOUNDARY_DIVIDER || m.x < -width / BOUNDARY_DIVIDER) {
            m.vx *= -1f
        }
        if (m.y > height / BOUNDARY_DIVIDER || m.y < -height / BOUNDARY_DIVIDER) {
            m.vy *= -1f
        }
    } else {
        m.vy += GRAVITY_FORCE
        m.y += m.vy
    }
}
