package br.com.brunocarvalhs.group.app.domain.useCases

import br.com.brunocarvalhs.friendssecrets.domain.services.StorageService
import br.com.brunocarvalhs.group.app.domain.entities.GroupEntities
import br.com.brunocarvalhs.group.app.domain.repositories.GroupRepository
import br.com.brunocarvalhs.group.commons.providers.performance.PerformanceService

internal class GroupListUseCase(
    private val repository: GroupRepository,
    private val storage: StorageService,
    private val performance: PerformanceService
) {
    suspend fun invoke(): Result<List<GroupEntities>> {
        performance.start(GroupListUseCase::class.java.simpleName)
        return try {
            runCatching {
                val groupTokens = loadGroupTokens()
                if (groupTokens.isEmpty()) emptyList() else repository.list(groupTokens)
            }
        } finally {
            performance.stop(GroupListUseCase::class.java.simpleName)
        }
    }

    private suspend fun loadGroupTokens(): List<String> =
        storage.load(GroupEntities.COLLECTION_NAME, Array<String>::class.java)?.toList().orEmpty()
}
