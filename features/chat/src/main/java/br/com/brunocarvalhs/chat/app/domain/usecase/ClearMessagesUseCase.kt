package br.com.brunocarvalhs.chat.app.domain.usecase

import br.com.brunocarvalhs.chat.app.domain.repository.ChatRepository
import javax.inject.Inject

class ClearMessagesUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(groupId: String): Result<Unit> {
        return repository.clearMessages(groupId)
    }
}
