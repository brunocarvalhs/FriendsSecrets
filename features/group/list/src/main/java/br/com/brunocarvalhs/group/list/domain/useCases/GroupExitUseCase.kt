package br.com.brunocarvalhs.group.list.domain.useCases

import br.com.brunocarvalhs.friendssecrets.domain.services.StorageService
import br.com.brunocarvalhs.group.core.domain.entities.GroupEntities
import br.com.brunocarvalhs.group.core.domain.repositories.GroupRepository

internal class GroupExitUseCase(
    private val repository: GroupRepository,
    private val storage: StorageService
) {
    suspend fun invoke(groupId: String): Result<Unit> {
        return try {
            runCatching {
                validationGroupId(groupId)
                val group = repository.read(groupId)
                clearGroupToken(group.token)
                clearAdminToken(group.token)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun validationGroupId(groupId: String) {
        require(groupId.isNotBlank())
    }

    private suspend fun clearGroupToken(token: String) {
        val groupList =
            storage.load(GroupEntities.COLLECTION_NAME, Array<String>::class.java)?.toList()
                .orEmpty()
        if (groupList.contains(token)) {
            storage.save(
                GroupEntities.COLLECTION_NAME,
                groupList.toMutableList().apply { remove(token) }
            )
        }
    }

    private suspend fun clearAdminToken(token: String) {
        val adminList =
            storage.load(GroupEntities.COLLECTION_NAME_ADMINS, Array<String>::class.java)?.toList()
                .orEmpty()
        if (adminList.contains(token)) {
            storage.save(
                GroupEntities.COLLECTION_NAME_ADMINS,
                adminList.toMutableList().apply { remove(token) }
            )
        }
    }
}

