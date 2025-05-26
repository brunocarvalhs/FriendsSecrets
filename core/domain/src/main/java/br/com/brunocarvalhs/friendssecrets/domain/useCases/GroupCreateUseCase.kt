package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.repositories.GroupRepository
import br.com.brunocarvalhs.friendssecrets.domain.services.PerformanceService
import br.com.brunocarvalhs.friendssecrets.domain.services.StorageService

class GroupCreateUseCase(
    private val repository: GroupRepository,
    private val storage: StorageService,
    private val performance: PerformanceService,
) {

    suspend fun invoke(group: GroupEntities): Result<Unit> {
        performance.start(GroupCreateUseCase::class.java.simpleName)
        return try {
            runCatching {
                repository.create(group)
                persistGroupToken(group.token)
                persistAdminToken(group.token)
            }
        } finally {
            performance.stop(GroupCreateUseCase::class.java.simpleName)
        }
    }

    private fun persistGroupToken(token: String) {
        val groupList =
            storage.load(GroupEntities.COLLECTION_NAME, Array<String>::class.java)?.toList()
                .orEmpty()
        storage.save(
            GroupEntities.COLLECTION_NAME,
            groupList.toMutableList().apply { add(token) }
        )
    }

    private fun persistAdminToken(token: String) {
        val adminList =
            storage.load(GroupEntities.COLLECTION_NAME_ADMINS, Array<String>::class.java)?.toList()
                .orEmpty()
        storage.save(
            GroupEntities.COLLECTION_NAME_ADMINS,
            adminList.toMutableList().apply { add(token) }
        )
    }
}
