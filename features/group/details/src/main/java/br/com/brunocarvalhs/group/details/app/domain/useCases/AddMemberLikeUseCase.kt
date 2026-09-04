package br.com.brunocarvalhs.group.details.app.domain.useCases

import br.com.brunocarvalhs.core.domain.model.GroupModel
import br.com.brunocarvalhs.group.details.app.domain.repository.GroupDetailsRepository
import javax.inject.Inject

internal class AddMemberLikeUseCase @Inject constructor(
    private val repository: GroupDetailsRepository,
) {
    suspend operator fun invoke(
        group: GroupModel,
        memberId: String,
        like: String
    ): Result<GroupModel> = runCatching {
        val sanitizedLike = like.trim()
        require(sanitizedLike.isNotBlank()) { "Like must not be blank" }

        val updatedMembers = group.members.map { member ->
            if (member.id == memberId || member.phoneNumber == memberId) {
                if (member.likes.any { it.equals(sanitizedLike, ignoreCase = true) }) {
                    member
                } else {
                    member.copy(likes = member.likes + sanitizedLike)
                }
            } else {
                member
            }
        }

        repository.update(group.copy(members = updatedMembers))
    }
}
