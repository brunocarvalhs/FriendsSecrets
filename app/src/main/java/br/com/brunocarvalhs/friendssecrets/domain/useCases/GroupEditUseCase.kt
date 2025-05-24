package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.commons.performance.PerformanceManager
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.repository.GroupRepository

class GroupEditUseCase(
    private val groupRepository: GroupRepository,
    private val performance: PerformanceManager
) {
    suspend fun invoke(group: GroupEntities): Result<GroupEntities> {
        performance.start(GroupEditUseCase::class.java.simpleName)
        return try {
            runCatching {
                groupRepository.update(group)
                group
            }
        } finally {
            performance.stop(GroupEditUseCase::class.java.simpleName)
        }
    }
}
