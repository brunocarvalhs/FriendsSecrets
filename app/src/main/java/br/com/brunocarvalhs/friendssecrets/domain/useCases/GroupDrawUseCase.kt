package br.com.brunocarvalhs.friendssecrets.domain.useCases

import android.content.Context
import br.com.brunocarvalhs.friendssecrets.CustomApplication
import br.com.brunocarvalhs.friendssecrets.R
import br.com.brunocarvalhs.friendssecrets.commons.performance.PerformanceManager
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.repository.GroupRepository

class GroupDrawUseCase(
    private val context: Context,
    private val groupRepository: GroupRepository,
    private val performance: PerformanceManager
) {
    suspend fun invoke(group: GroupEntities) = runCatching {
        performance.start(GroupDrawUseCase::class.java.simpleName)
        validationMembers(group.members)
        validationDraw(group.draws)
        groupRepository.drawMembers(group)
    }.also {
        performance.stop(GroupDrawUseCase::class.java.simpleName)
    }

    private fun validationMembers(members: Map<String, String>) {
        require(value = members.size > 2) { context.getString(R.string.require_group_cannot_have_more_than_2_members) }
    }

    private fun validationDraw(draw: Map<String, String>) {
        require(value = draw.isEmpty()) { context.getString(R.string.require_draw_already_held) }
    }
}