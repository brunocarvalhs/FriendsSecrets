package br.com.brunocarvalhs.friendssecrets.domain.useCases

import android.content.Context
import br.com.brunocarvalhs.friendssecrets.CustomApplication
import br.com.brunocarvalhs.friendssecrets.R
import br.com.brunocarvalhs.friendssecrets.commons.performance.PerformanceManager
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import br.com.brunocarvalhs.friendssecrets.domain.repository.GroupRepository

class GroupDrawUseCase(
    private val context: Context = CustomApplication.getInstance(),
    private val groupRepository: GroupRepository,
    private val performance: PerformanceManager
) {
    suspend fun invoke(group: GroupEntities): Result<Unit> {
        performance.start(GroupDrawUseCase::class.java.simpleName)
        return try {
            runCatching {
                validateMembers(group.members)
                validateDraw(group.draws)
                groupRepository.drawMembers(group)
            }
        } finally {
            performance.stop(GroupDrawUseCase::class.java.simpleName)
        }
    }

    private fun validateMembers(members: List<UserEntities>) {
        require(members.size > 2) { context.getString(R.string.require_group_cannot_have_more_than_2_members) }
    }

    private fun validateDraw(draw: Map<String, String>) {
        require(draw.isEmpty()) { context.getString(R.string.require_draw_already_held) }
    }
}
