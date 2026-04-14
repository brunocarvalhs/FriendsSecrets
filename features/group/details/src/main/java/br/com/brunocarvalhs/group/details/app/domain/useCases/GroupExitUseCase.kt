package br.com.brunocarvalhs.group.details.app.domain.useCases

import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel
import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel.Companion.COLLECTION_NAME
import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel.Companion.COLLECTION_NAME_ADMIN
import br.com.brunocarvalhs.friendssecrets.domain.services.StorageService
import br.com.brunocarvalhs.group.details.app.domain.repository.GroupDetailsRepository
import javax.inject.Inject

internal class GroupExitUseCase @Inject constructor(
    private val repository: GroupDetailsRepository,
    private val storage: StorageService
) {
    suspend fun invoke(group: GroupModel): Result<Unit> = runCatching {
        val group = repository.read(group.id)
        val token = group.token

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
