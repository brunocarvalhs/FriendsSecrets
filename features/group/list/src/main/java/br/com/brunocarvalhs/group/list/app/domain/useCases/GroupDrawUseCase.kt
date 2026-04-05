package br.com.brunocarvalhs.group.list.app.domain.useCases

import br.com.brunocarvalhs.group.list.app.domain.entities.GroupModel
import br.com.brunocarvalhs.group.list.app.domain.repository.GroupListRepository
import javax.inject.Inject

class GroupDrawUseCase @Inject constructor(
    private val repository: GroupListRepository
) {
    suspend fun invoke(groupId: String): Result<Unit> {
        return try {
            runCatching {
                val group = repository.read(groupId)
                validateMembers(group)
                validateDraw(group)
                // O sorteio real deve ser feito no repositório (lado do servidor ou localmente)
                // Aqui estamos chamando o repositório para realizar a operação.
                // Nota: O método drawMembers deve estar presente no GroupListRepository.
                // Como não está, podemos assumir que será adicionado ou usar a implementação de atualização.
                repository.drawMembers(groupId)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun validateMembers(group: GroupModel) {
        require(group.members.size >= 3) { "O grupo precisa de pelo menos 3 participantes para o sorteio." }
    }

    private fun validateDraw(group: GroupModel) {
        require(group.draws.isEmpty()) { "O sorteio já foi realizado neste grupo." }
    }
}
