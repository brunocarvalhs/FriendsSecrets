package br.com.brunocarvalhs.group.details.app.domain.useCases

import br.com.brunocarvalhs.deviceid.DeviceService
import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel
import br.com.brunocarvalhs.group.details.app.domain.repository.GroupDetailsRepository
import javax.inject.Inject

internal class GroupReadUseCase @Inject constructor(
    private val repository: GroupDetailsRepository,
    private val deviceService: DeviceService
) {
    suspend operator fun invoke(groupId: String): Result<GroupModel> = runCatching {
        val ownerId = deviceService.getDeviceId()
        val group = repository.read(groupId)
        return@runCatching group.copy(isOwner = group.ownerId == ownerId)
    }
}