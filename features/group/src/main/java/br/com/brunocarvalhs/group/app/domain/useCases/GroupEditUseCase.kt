package br.com.brunocarvalhs.group.app.domain.useCases

import br.com.brunocarvalhs.group.app.domain.entities.GroupEntities
import br.com.brunocarvalhs.group.app.domain.repositories.GroupRepository
import br.com.brunocarvalhs.group.commons.providers.performance.PerformanceService


internal class GroupEditUseCase(
    private val repository: GroupRepository,
    private val performance: PerformanceService
) {
    suspend fun invoke(group: GroupEntities): Result<GroupEntities> {
        performance.start(GroupEditUseCase::class.java.simpleName)
        return try {
            runCatching {
                repository.update(group)
                group
            }
        } finally {
            performance.stop(GroupEditUseCase::class.java.simpleName)
        }
    }
}
