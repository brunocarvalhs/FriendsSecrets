package br.com.brunocarvalhs.group.details.app.domain.useCases

import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel
import br.com.brunocarvalhs.group.details.app.domain.repository.GroupDetailsRepository
import javax.inject.Inject

class GroupReadUseCase @Inject constructor(
    private val repository: GroupDetailsRepository
) {
    suspend operator fun invoke(groupId: String): Result<GroupModel> = runCatching {
        repository.read(groupId)
    }
}