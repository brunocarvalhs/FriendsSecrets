package br.com.brunocarvalhs.group.list.app.domain.useCases

import br.com.brunocarvalhs.deviceid.DeviceService
import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel
import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel.Companion.COLLECTION_NAME
import br.com.brunocarvalhs.group.list.app.domain.repository.GroupListRepository
import br.com.brunocarvalhs.storage.domain.StorageService
import javax.inject.Inject

internal class GroupListUseCase @Inject constructor(
    private val repository: GroupListRepository,
    private val storage: StorageService,
    private val deviceService: DeviceService
) {
    suspend fun invoke(): Result<List<GroupModel>> = runCatching {
        val groupTokens = loadGroupTokens()
        
        if (groupTokens.isEmpty()) {
            return@runCatching emptyList()
        }

        val ownerId = deviceService.getDeviceId()
        repository.list(groupTokens).map { group ->
            group.toDomain().copy(isOwner = group.owner_id == ownerId)
        }
    }

    private suspend fun loadGroupTokens(): List<String> {
        return storage.load(COLLECTION_NAME, Array<String>::class)?.toList().orEmpty()
    }
}
