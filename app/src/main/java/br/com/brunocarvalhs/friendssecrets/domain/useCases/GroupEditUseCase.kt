package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.commons.performance.PerformanceManager
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.repository.GroupRepository
import com.google.firebase.perf.metrics.AddTrace

class GroupEditUseCase(
    private val groupRepository: GroupRepository,
    private val performance: PerformanceManager
) {

    /**
     * This function updates a group. It validates the group and then updates it using the group repository.
     *
     * @param group The group to be updated.
     * @return A Result containing the updated GroupEntities object.
     */
    @Throws(Exception::class)
    @AddTrace(name = "GroupEditUseCase.invoke")
    suspend fun invoke(group: GroupEntities): Result<GroupEntities> = runCatching {
        performance.start(GroupEditUseCase::class.java.simpleName)
        groupRepository.update(group)
        group
    }.also {
        performance.stop(GroupEditUseCase::class.java.simpleName)
    }
}