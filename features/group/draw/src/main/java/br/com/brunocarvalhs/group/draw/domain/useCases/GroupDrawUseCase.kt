package br.com.brunocarvalhs.group.draw.domain.useCases

import br.com.brunocarvalhs.group.core.domain.entities.GroupEntities
import br.com.brunocarvalhs.group.core.domain.entities.UserEntities
import br.com.brunocarvalhs.group.core.domain.repositories.GroupRepository

internal class GroupDrawUseCase(
    private val repository: GroupRepository
) {
    suspend fun invoke(group: GroupEntities): Result<Unit> {
        return try {
            runCatching {
                validateMembers(group.members)
                validateDraw(group.draws)
                repository.drawMembers(group)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun validateMembers(members: List<UserEntities>) {
        require(members.size > 2)
    }

    private fun validateDraw(draw: Map<String, String>) {
        require(draw.isEmpty())
    }
}

