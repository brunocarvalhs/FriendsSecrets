package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.repositories.GroupRepository
import br.com.brunocarvalhs.friendssecrets.domain.services.PerformanceService
import br.com.brunocarvalhs.friendssecrets.domain.services.StorageService

class GroupExitUseCase(
    private val repository: GroupRepository,
    private val storage: StorageService,
    private val performance: PerformanceService
) {
    suspend fun invoke(groupId: String): Result<Unit> {
        performance.start(GroupExitUseCase::class.java.simpleName)
        return try {
            runCatching {
                validationGroupId(groupId)
                val group = repository.read(groupId)
                clearGroupToken(group.token)
                clearAdminToken(group.token)
            }
        } finally {
            performance.stop(GroupExitUseCase::class.java.simpleName)
        }
    }

    private fun validationGroupId(groupId: String) {
        require(groupId.isNotBlank())
    }

    private fun clearGroupToken(token: String) {
        val groupList = storage.load<List<String>>(GroupEntities.COLLECTION_NAME) ?: emptyList()
        if (groupList.contains(token)) {
            storage.save(
                GroupEntities.COLLECTION_NAME,
                groupList.toMutableList().apply { remove(token) }
            )
        }
    }

    private fun clearAdminToken(token: String) {
        val adminList = storage.load<List<String>>(GroupEntities.COLLECTION_NAME_ADMINS) ?: emptyList()
        if (adminList.contains(token)) {
            storage.save(
                GroupEntities.COLLECTION_NAME_ADMINS,
                adminList.toMutableList().apply { remove(token) }
            )
        }
    }
}
