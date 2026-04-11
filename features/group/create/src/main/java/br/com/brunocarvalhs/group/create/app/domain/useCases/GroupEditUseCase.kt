package br.com.brunocarvalhs.group.create.app.domain.useCases

import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel
import br.com.brunocarvalhs.group.create.app.domain.repositories.GroupCreateRepository
import javax.inject.Inject

class GroupEditUseCase @Inject constructor(
    private val repository: GroupCreateRepository
) {
    suspend operator fun invoke(group: GroupModel): Result<Unit> = runCatching {
        repository.update(group)
    }
}