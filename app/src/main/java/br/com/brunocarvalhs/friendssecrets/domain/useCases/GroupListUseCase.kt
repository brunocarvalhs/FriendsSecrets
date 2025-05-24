package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.commons.performance.PerformanceManager
import br.com.brunocarvalhs.friendssecrets.data.service.StorageService
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.repository.GroupRepository
import br.com.brunocarvalhs.friendssecrets.domain.repository.response.toModel

class GroupListUseCase(
    private val groupRepository: GroupRepository,
    private val storage: StorageService,
    private val performance: PerformanceManager
) {
    suspend fun invoke(): Result<List<GroupEntities>> {
        performance.start(GroupListUseCase::class.java.simpleName)
        return try {
            runCatching {
                val groupTokens = loadGroupTokens()
                if (groupTokens.isEmpty()) emptyList() else groupRepository.list(groupTokens)
                    .map { it.toModel() }
            }
        } finally {
            performance.stop(GroupListUseCase::class.java.simpleName)
        }
    }

    private suspend fun loadGroupTokens(): List<String> =
        storage.load<List<String>>(GroupEntities.COLLECTION_NAME) ?: emptyList()
}
