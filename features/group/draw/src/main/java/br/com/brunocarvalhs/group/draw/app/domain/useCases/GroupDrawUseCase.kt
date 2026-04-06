package br.com.brunocarvalhs.group.draw.app.domain.useCases

import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel
import br.com.brunocarvalhs.friendssecrets.domain.model.UserModel
import br.com.brunocarvalhs.group.draw.app.domain.repository.DrawRepository

class GroupDrawUseCase(
    private val repository: DrawRepository
) {
    suspend fun invoke(group: GroupModel): Result<Unit> {
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

    private fun validateMembers(members: List<UserModel>) {
        require(members.size > 2)
    }

    private fun validateDraw(draw: Map<String, String>) {
        require(draw.isEmpty())
    }
}
