package br.com.brunocarvalhs.group.details.app.domain.useCases

import br.com.brunocarvalhs.core.domain.model.GroupModel
import br.com.brunocarvalhs.core.domain.model.UserModel
import br.com.brunocarvalhs.deviceid.DeviceService
import br.com.brunocarvalhs.group.details.app.domain.repository.GroupDetailsRepository
import javax.inject.Inject

internal class UpdateMemberLikesUseCase @Inject constructor(
    private val repository: GroupDetailsRepository,
    private val deviceService: DeviceService,
) {
    suspend operator fun invoke(group: GroupModel, likes: List<String>): Result<GroupModel> =
        runCatching {
            val deviceId = deviceService.getDeviceId()
            val sanitizedLikes = likes.map { it.trim() }.filter { it.isNotBlank() }

            require(group.members.any { it.isCurrentDevice(deviceId) }) {
                "Current device is not a member of this group"
            }

            val updatedMembers = group.members.map { member ->
                if (member.isCurrentDevice(deviceId)) {
                    member.copy(likes = sanitizedLikes)
                } else {
                    member
                }
            }

            repository.update(group.copy(members = updatedMembers))
        }

    private fun UserModel.isCurrentDevice(deviceId: String): Boolean {
        return id == deviceId || phoneNumber == deviceId
    }
}
