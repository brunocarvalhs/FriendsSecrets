package br.com.brunocarvalhs.chat.app.domain.usecase

import br.com.brunocarvalhs.chat.app.domain.repository.ChatRepository
import com.google.firebase.perf.metrics.AddTrace
import javax.inject.Inject

class ClearMessagesUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    @AddTrace(name = "ClearMessagesUseCase.invoke", enabled = true)
    suspend operator fun invoke(groupId: String): Result<Unit> {
        return repository.clearMessages(groupId)
    }
}
