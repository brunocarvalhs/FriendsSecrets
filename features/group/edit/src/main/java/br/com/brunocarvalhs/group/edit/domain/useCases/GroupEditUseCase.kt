package br.com.brunocarvalhs.group.edit.domain.useCases

import br.com.brunocarvalhs.group.core.domain.entities.GroupEntities
import br.com.brunocarvalhs.group.core.domain.repositories.GroupRepository

internal class GroupEditUseCase(
    private val repository: GroupRepository
) {
    suspend fun invoke(group: GroupEntities): Result<GroupEntities> {
        return try {
            runCatching {
                repository.update(group)
                group
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

