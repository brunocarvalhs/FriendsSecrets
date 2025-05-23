package br.com.brunocarvalhs.friendssecrets.domain.useCases

import android.content.Context
import br.com.brunocarvalhs.friendssecrets.CustomApplication
import br.com.brunocarvalhs.friendssecrets.R
import br.com.brunocarvalhs.friendssecrets.commons.performance.PerformanceManager
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.repository.GroupRepository
import com.google.firebase.perf.metrics.AddTrace

class GroupDrawUseCase(
    private val context: Context = CustomApplication.getInstance(),
    private val groupRepository: GroupRepository,
    private val performance: PerformanceManager
) {

    /**
     * This function draws members for a group. It validates the members and draws,
     * and then draws the members using the group repository.
     *
     * @param group The group for which to draw members.
     * @return A Result containing Unit if successful, or an exception if an error occurs.
     */
    @Throws(Exception::class)
    @AddTrace(name = "GroupDrawUseCase.invoke")
    suspend fun invoke(group: GroupEntities) = runCatching {
        performance.start(GroupDrawUseCase::class.java.simpleName)
        validationMembers(group.members)
        validationDraw(group.draws)
        groupRepository.drawMembers(group)
    }.also {
        performance.stop(GroupDrawUseCase::class.java.simpleName)
    }

    /**
     * This function validates the members of the group.
     * It checks if the number of members is greater than 2.
     *
     * @param members A map of members in the group.
     */
    @Throws(Exception::class)
    @AddTrace(name = "GroupDrawUseCase.validationMembers")
    private fun validationMembers(members: Map<String, String>) {
        require(value = members.size > 2) { context.getString(R.string.require_group_cannot_have_more_than_2_members) }
    }

    /**
     * This function validates the draw of the group.
     * It checks if the draw is empty.
     *
     * @param draw A map representing the draw of the group.
     */
    @Throws(Exception::class)
    @AddTrace(name = "GroupDrawUseCase.validationDraw")
    private fun validationDraw(draw: Map<String, String>) {
        require(value = draw.isEmpty()) { context.getString(R.string.require_draw_already_held) }
    }
}