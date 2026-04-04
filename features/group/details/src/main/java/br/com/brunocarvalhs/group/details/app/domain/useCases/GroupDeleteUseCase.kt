package br.com.brunocarvalhs.group.details.app.domain.useCases

import br.com.brunocarvalhs.friendssecrets.domain.services.StorageService
import br.com.brunocarvalhs.group.core.domain.entities.GroupEntities
import br.com.brunocarvalhs.group.core.domain.repositories.GroupRepository

internal class GroupDeleteUseCase(
    private val repository: GroupRepository,
    private val storage: StorageService
) {
    suspend fun invoke(groupId: String): Result<Unit> {
        return try {
            runCatching {
                val group = repository.read(groupId)

                removeGroupFromStorage(group.token)
                removeAdminFromStorage(group.token)

                repository.delete(groupId)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun removeGroupFromStorage(token: String) {
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

    private suspend fun removeAdminFromStorage(token: String) {
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

