package br.com.brunocarvalhs.group.list.app.domain.useCases

import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel
import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel.Companion.COLLECTION_NAME
import br.com.brunocarvalhs.friendssecrets.domain.services.DeviceService
import br.com.brunocarvalhs.friendssecrets.domain.services.StorageService
import br.com.brunocarvalhs.group.list.app.data.exceptions.GroupAlreadyExistException
import br.com.brunocarvalhs.group.list.app.data.exceptions.GroupNotFoundException
import br.com.brunocarvalhs.group.list.app.domain.repository.GroupListRepository
import javax.inject.Inject

internal class GroupByTokenUseCase @Inject constructor(
    private val repository: GroupListRepository,
    private val storage: StorageService,
    private val device: DeviceService
) {
    suspend fun invoke(token: String): Result<GroupModel> = runCatching {
        validateToken(token)
        val group = fetchGroupByToken(token)
        storeToken(group)
        group
    }

    private fun validateToken(token: String) {
        require(token.isNotBlank()) { "Token cannot be blank" }
    }

    private suspend fun ensureTokenNotExists(token: String): List<String> {
        val groupList = storage.load(COLLECTION_NAME, Array<String>::class)?.toList().orEmpty()
        if (groupList.contains(token)) { throw GroupAlreadyExistException() }
        return groupList
    }

    private suspend fun fetchGroupByToken(token: String): GroupModel {
        val ownerId = device.getDeviceId()
        val data = repository.searchByToken(token) ?: throw GroupNotFoundException()
        return data.toDomain().copy(isOwner = ownerId == data.owner_id)
    }

    private suspend fun storeToken(group: GroupModel) {
        val list = ensureTokenNotExists(group.token)
        val newList = list.toMutableList().apply { add(group.token) }
        storage.save(key = COLLECTION_NAME, value = newList.toTypedArray())
    }
}
