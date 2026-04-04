package br.com.brunocarvalhs.group.details.app.domain.useCases

import br.com.brunocarvalhs.friendssecrets.domain.services.StorageService
import br.com.brunocarvalhs.group.core.domain.entities.GroupEntities
import br.com.brunocarvalhs.group.core.domain.repositories.GroupRepository

internal class GroupReadUseCase(
    private val repository: GroupRepository,
    private val storage: StorageService
) {
    suspend fun invoke(groupId: String): Result<GroupEntities> {
        return try {
            runCatching {
                validationGroupId(groupId)
                val group = repository.read(groupId)
                defineAdmin(group)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun validationGroupId(groupId: String) {
        require(groupId.isNotBlank())
    }

    private suspend fun defineAdmin(group: GroupEntities): GroupEntities {
        val adminList = storage.load(
            key = GroupEntities.COLLECTION_NAME_ADMINS,
            clazz = Array<String>::class.java
        )?.toList().orEmpty()
        return if (adminList.contains(group.token)) group.toCopy(isOwner = true) else group
    }
}

