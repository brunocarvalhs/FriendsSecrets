package br.com.brunocarvalhs.group.details.app.domain.useCases

import br.com.brunocarvalhs.core.domain.model.GroupModel
import br.com.brunocarvalhs.group.details.app.domain.repository.GroupDetailsRepository
import javax.inject.Inject

internal class RemoveMemberUseCase @Inject constructor(
    private val repository: GroupDetailsRepository
) {
    suspend operator fun invoke(group: GroupModel, memberId: String): Result<GroupModel> =
        runCatching {
            check(group.draws.isEmpty()) {
                "Cannot remove a member after the draw has already been made"
            }

            val updatedMembers = group.members.filterNot { it.id == memberId }
            require(updatedMembers.size != group.members.size) {
                "Member $memberId is not part of this group"
            }

            repository.update(group.copy(members = updatedMembers))
        }
}
