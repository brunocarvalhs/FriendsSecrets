package br.com.brunocarvalhs.group.edit.app.domain.useCases

import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel
import br.com.brunocarvalhs.group.edit.app.domain.repository.GroupEditRepository

internal class GroupEditUseCase(
    private val repository: GroupEditRepository
) {
    suspend fun invoke(group: GroupModel): Result<GroupModel> = runCatching {
        repository.update(group)
    }
}