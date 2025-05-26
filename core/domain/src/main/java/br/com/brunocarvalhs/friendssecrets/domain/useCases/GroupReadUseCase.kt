package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.repositories.GroupRepository
import br.com.brunocarvalhs.friendssecrets.domain.services.PerformanceService
import br.com.brunocarvalhs.friendssecrets.domain.services.StorageService

class GroupReadUseCase(
    private val repository: GroupRepository,
    private val storage: StorageService,
    private val performance: PerformanceService
) {
    suspend fun invoke(groupId: String): Result<GroupEntities> {
        performance.start(GroupReadUseCase::class.java.simpleName)
        return try {
            runCatching {
                validationGroupId(groupId)
                val group = repository.read(groupId)
                defineAdmin(group)
            }
        } finally {
            performance.stop(GroupReadUseCase::class.java.simpleName)
        }
    }

    private fun validationGroupId(groupId: String) {
        require(groupId.isNotBlank())
    }

    private fun defineAdmin(group: GroupEntities): GroupEntities {
        val adminList = storage.load(
            key = GroupEntities.COLLECTION_NAME_ADMINS,
            clazz = Array<String>::class.java
        )?.toList().orEmpty()
        return if (adminList.contains(group.token)) group.toCopy(isOwner = true) else group
    }
}
