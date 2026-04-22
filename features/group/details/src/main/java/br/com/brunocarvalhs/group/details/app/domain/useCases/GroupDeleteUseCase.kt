package br.com.brunocarvalhs.group.details.app.domain.useCases

import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel
import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel.Companion.COLLECTION_NAME
import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel.Companion.COLLECTION_NAME_ADMIN
import br.com.brunocarvalhs.group.details.app.domain.repository.GroupDetailsRepository
import br.com.brunocarvalhs.storage.domain.StorageService
import javax.inject.Inject

internal class GroupDeleteUseCase @Inject constructor(
    private val repository: GroupDetailsRepository,
    private val storage: StorageService
) {
    suspend fun invoke(group: GroupModel): Result<Unit> = runCatching {
        val group = repository.read(group.id)
        val token = group.token

        repository.delete(group)

        removeToken(COLLECTION_NAME, token)
        removeToken(COLLECTION_NAME_ADMIN, token)
    }

    private suspend fun removeToken(key: String, token: String) {
        val currentList = storage.load(key, Array<String>::class)?.toMutableList() ?: mutableListOf()
        if (currentList.remove(token)) {
            storage.save(key, currentList.toTypedArray())
        }
    }
}
