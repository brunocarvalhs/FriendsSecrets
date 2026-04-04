package br.com.brunocarvalhs.group.list.app.domain.useCases

import br.com.brunocarvalhs.group.list.app.domain.entities.GroupModel.Companion.COLLECTION_NAME
import br.com.brunocarvalhs.group.list.app.domain.entities.GroupModel.Companion.COLLECTION_NAME_ADMINS
import br.com.brunocarvalhs.group.list.app.domain.repository.GroupListRepository
import br.com.brunocarvalhs.group.list.app.domain.services.StorageService
import javax.inject.Inject

class GroupDeleteUseCase @Inject constructor(
    private val repository: GroupListRepository,
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
        val groupList = storage.load(COLLECTION_NAME, Array<String>::class)?.toList().orEmpty()
        if (token in groupList) {
            storage.save(
                COLLECTION_NAME,
                groupList.toMutableList().apply { remove(token) }
            )
        }
    }

    private suspend fun removeAdminFromStorage(token: String) {
        val adminList = storage.load(COLLECTION_NAME_ADMINS, Array<String>::class)?.toList().orEmpty()
        if (token in adminList) {
            storage.save(
                COLLECTION_NAME_ADMINS,
                adminList.toMutableList().apply { remove(token) }
            )
        }
    }
}

