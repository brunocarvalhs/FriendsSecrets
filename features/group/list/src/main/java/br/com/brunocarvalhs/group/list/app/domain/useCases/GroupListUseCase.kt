package br.com.brunocarvalhs.group.list.app.domain.useCases

import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel
import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel.Companion.COLLECTION_NAME
import br.com.brunocarvalhs.friendssecrets.domain.services.DeviceService
import br.com.brunocarvalhs.friendssecrets.domain.services.StorageService
import br.com.brunocarvalhs.group.list.app.domain.repository.GroupListRepository
import javax.inject.Inject

class GroupListUseCase @Inject constructor(
    private val repository: GroupListRepository,
    private val storage: StorageService,
    private val deviceService: DeviceService
) {
    suspend fun invoke(): Result<List<GroupModel>> {
        return try {
            runCatching {
                val groupTokens = loadGroupTokens()
                if (groupTokens.isNotEmpty()) {
                    val ownerId = deviceService.getDeviceId()
                    repository.list(groupTokens).map { group ->
                        group.toDomain()
                            .copy(isOwner = group.owner_id == ownerId)
                    }
                } else emptyList()
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun loadGroupTokens(): List<String> =
        storage.load(COLLECTION_NAME, Array<String>::class)?.toList().orEmpty()
}
