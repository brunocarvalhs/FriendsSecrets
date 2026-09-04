package br.com.brunocarvalhs.group.list.app.domain.useCases

import br.com.brunocarvalhs.deviceid.DeviceService
import br.com.brunocarvalhs.core.domain.model.GroupModel
import br.com.brunocarvalhs.core.domain.model.GroupModel.Companion.COLLECTION_NAME
import br.com.brunocarvalhs.group.list.app.data.exceptions.GroupNotFoundException
import br.com.brunocarvalhs.group.list.app.domain.repository.GroupListRepository
import br.com.brunocarvalhs.storage.domain.StorageService
import javax.inject.Inject

internal class GroupByTokenUseCase @Inject constructor(
    private val repository: GroupListRepository,
    private val storage: StorageService,
    private val device: DeviceService
) {
    suspend fun invoke(token: String): Result<GroupModel> = runCatching {
        validateToken(token)
        val group = fetchGroupByToken(token)
        rememberToken(group.token)
        group
    }

    private fun validateToken(token: String) {
        require(token.isNotBlank()) { "Token cannot be blank" }
    }

    private suspend fun fetchGroupByToken(token: String): GroupModel {
        val ownerId = device.getDeviceId()
        val data = repository.searchByToken(token) ?: throw GroupNotFoundException()
        return data.toDomain().copy(isOwner = ownerId == data.ownerId)
    }

    // Joining a group whose token is already known locally is an idempotent
    // no-op, not a failure: the user is simply re-opening an invite (link,
    // QR code) to a group they're already part of.
    private suspend fun rememberToken(token: String) {
        val knownTokens = storage.load(COLLECTION_NAME, Array<String>::class)?.toList().orEmpty()
        if (token in knownTokens) return
        storage.save(key = COLLECTION_NAME, value = (knownTokens + token).toTypedArray())
    }
}
