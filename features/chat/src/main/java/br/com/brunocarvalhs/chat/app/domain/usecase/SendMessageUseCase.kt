package br.com.brunocarvalhs.chat.app.domain.usecase

import br.com.brunocarvalhs.chat.app.domain.repository.ChatRepository
import br.com.brunocarvalhs.core.domain.model.MessageModel
import com.google.firebase.perf.metrics.AddTrace
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    @AddTrace(name = "SendMessageUseCase.invoke", enabled = true)
    suspend operator fun invoke(groupId: String, message: MessageModel): Result<Unit> {
        return repository.sendMessage(groupId, message)
    }
}
