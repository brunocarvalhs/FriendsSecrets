package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import br.com.brunocarvalhs.friendssecrets.domain.repositories.GroupRepository
import br.com.brunocarvalhs.friendssecrets.domain.services.PerformanceService

class GroupDrawUseCase(
    private val repository: GroupRepository,
    private val performance: PerformanceService
) {
    suspend fun invoke(group: GroupEntities): Result<Unit> {
        performance.start(GroupDrawUseCase::class.java.simpleName)
        return try {
            runCatching {
                validateMembers(group.members)
                validateDraw(group.draws)
                repository.drawMembers(group)
            }
        } finally {
            performance.stop(GroupDrawUseCase::class.java.simpleName)
        }
    }

    private fun validateMembers(members: List<UserEntities>) {
        require(members.size > 2)
    }

    private fun validateDraw(draw: Map<String, String>) {
        require(draw.isEmpty())
    }
}
