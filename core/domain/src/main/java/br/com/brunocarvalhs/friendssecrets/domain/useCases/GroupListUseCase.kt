package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.repositories.GroupRepository
import br.com.brunocarvalhs.friendssecrets.domain.services.PerformanceService
import br.com.brunocarvalhs.friendssecrets.domain.services.StorageService

class GroupListUseCase(
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

    private fun loadGroupTokens(): List<String> =
        storage.load(GroupEntities.COLLECTION_NAME, Array<String>::class.java)?.toList().orEmpty()
}
