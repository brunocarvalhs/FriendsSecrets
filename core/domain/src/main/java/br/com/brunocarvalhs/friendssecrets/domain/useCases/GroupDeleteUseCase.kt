package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.repositories.GroupRepository
import br.com.brunocarvalhs.friendssecrets.domain.services.PerformanceService
import br.com.brunocarvalhs.friendssecrets.domain.services.StorageService

class GroupDeleteUseCase(
    private val repository: GroupRepository,
    private val storage: StorageService,
    private val performance: PerformanceService
) {
    suspend fun invoke(groupId: String): Result<Unit> {
        performance.start(GroupDeleteUseCase::class.java.simpleName)
        return try {
            runCatching {
                val group = repository.read(groupId)

                removeGroupFromStorage(group.token)
                removeAdminFromStorage(group.token)

                repository.delete(groupId)
            }
        } finally {
            performance.stop(GroupDeleteUseCase::class.java.simpleName)
        }
    }

    private fun removeGroupFromStorage(token: String) {
        val groupList =
            storage.load(GroupEntities.COLLECTION_NAME, Array<String>::class.java)?.toList()
                .orEmpty()
        if (token in groupList) {
            storage.save(
                GroupEntities.COLLECTION_NAME,
                groupList.toMutableList().apply { remove(token) }
            )
        }
    }

    private fun removeAdminFromStorage(token: String) {
        val adminList =
            storage.load(GroupEntities.COLLECTION_NAME_ADMINS, Array<String>::class.java)?.toList()
                .orEmpty()
        if (token in adminList) {
            storage.save(
                GroupEntities.COLLECTION_NAME_ADMINS,
                adminList.toMutableList().apply { remove(token) }
            )
        }
    }
}
