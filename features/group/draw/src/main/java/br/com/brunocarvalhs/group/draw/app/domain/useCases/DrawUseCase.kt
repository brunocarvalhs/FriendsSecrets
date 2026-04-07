package br.com.brunocarvalhs.group.draw.app.domain.useCases

import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel
import br.com.brunocarvalhs.friendssecrets.domain.model.UserModel
import br.com.brunocarvalhs.group.draw.app.domain.repository.DrawRepository
import javax.inject.Inject

class DrawUseCase @Inject constructor(
    private val repository: DrawRepository
) {
    suspend operator fun invoke(group: GroupModel): Result<Unit> = runCatching {
        validateMembers(group.members)
        validateDraw(group.draws)
        repository.drawMembers(group)
    }

    private fun validateMembers(members: List<UserModel>) {
        require(members.size > 2)
    }

    private fun validateDraw(draw: Map<String, String>) {
        require(draw.isEmpty())
    }
}
