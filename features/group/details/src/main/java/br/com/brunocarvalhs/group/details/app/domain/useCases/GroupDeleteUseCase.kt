package br.com.brunocarvalhs.group.list.app.domain.useCases

import br.com.brunocarvalhs.group.details.app.domain.entities.GroupModel.Companion.COLLECTION_NAME_ADMINS
import br.com.brunocarvalhs.group.details.app.domain.entities.GroupModel.Companion.COLLECTION_NAME
import br.com.brunocarvalhs.group.list.app.domain.repository.GroupListRepository
import br.com.brunocarvalhs.group.details.app.domain.services.StorageService
import javax.inject.Inject

class GroupDeleteUseCase @Inject constructor(
    private val repository: GroupListRepository,
    private val storage: StorageService
) {
    suspend fun invoke(groupId: String): Result<Unit> {
        return try {
            val group = repository.read(groupId)
            val token = group.token

            removeToken(COLLECTION_NAME, token)
            removeToken(COLLECTION_NAME_ADMINS, token)

            repository.delete(groupId)
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun removeToken(key: String, token: String) {
        val currentList = storage.load(key, Array<String>::class)?.toMutableList() ?: mutableListOf()
        if (currentList.remove(token)) {
            storage.save(key, currentList.toTypedArray())
        }
    }
}
