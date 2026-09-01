package br.com.brunocarvalhs.group.details.app.domain.useCases

import br.com.brunocarvalhs.core.domain.model.GroupModel
import br.com.brunocarvalhs.deviceid.DeviceService
import br.com.brunocarvalhs.group.details.app.domain.repository.GroupDetailsRepository
import javax.inject.Inject

internal class AddMemberAdjectiveUseCase @Inject constructor(
    private val repository: GroupDetailsRepository,
    private val deviceService: DeviceService,
) {
    suspend operator fun invoke(
        group: GroupModel,
        memberId: String,
        adjective: String
    ): Result<GroupModel> = runCatching {
        val deviceId = deviceService.getDeviceId()
        val sanitizedAdjective = adjective.trim()
        require(sanitizedAdjective.isNotBlank()) { "Adjective must not be blank" }

        val updatedMembers = group.members.map { member ->
            if (member.id == memberId || member.phoneNumber == memberId) {
                val contributions = member.adjectives[deviceId].orEmpty()
                if (contributions.any { it.equals(sanitizedAdjective, ignoreCase = true) }) {
                    member
                } else {
                    member.copy(
                        adjectives = member.adjectives +
                            (deviceId to contributions + sanitizedAdjective)
                    )
                }
            } else {
                member
            }
        }

        repository.update(group.copy(members = updatedMembers))
    }
}
