package br.com.brunocarvalhs.group.create.app.domain.useCases

import br.com.brunocarvalhs.deviceid.DeviceService
import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel
import br.com.brunocarvalhs.group.create.app.domain.repositories.GroupCreateRepository
import br.com.brunocarvalhs.storage.domain.StorageService
import javax.inject.Inject

internal class GroupCreateUseCase @Inject constructor(
    private val repository: GroupCreateRepository,
    private val storage: StorageService,
    private val deviceService: DeviceService
) {

    suspend operator fun invoke(group: GroupModel): Result<Unit> = runCatching {
        val owner = deviceService.getDeviceId()
        val groupWithId = group.copy(ownerId = owner)
        repository.create(groupWithId)
        persistGroupToken(group.token)
        persistAdminToken(group.token)
    }

    private suspend fun persistGroupToken(token: String) {
        val groupList = storage.load(
            key = GroupModel.COLLECTION_NAME,
            value = Array<String>::class
        ) ?: emptyArray()

        storage.save(
            GroupModel.COLLECTION_NAME,
            groupList + token
        )
    }

    private suspend fun persistAdminToken(token: String) {
        val adminList = storage.load(
            key = GroupModel.COLLECTION_NAME_ADMIN,
            value = Array<String>::class
        ) ?: emptyArray()

        storage.save(
            key = GroupModel.COLLECTION_NAME_ADMIN,
            value = adminList + token
        )
    }
}
