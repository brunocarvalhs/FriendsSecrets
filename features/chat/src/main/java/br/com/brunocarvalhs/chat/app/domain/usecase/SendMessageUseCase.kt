package br.com.brunocarvalhs.chat.app.domain.usecase

import br.com.brunocarvalhs.core.domain.model.MessageModel
import br.com.brunocarvalhs.chat.app.domain.repository.ChatRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(groupId: String, message: MessageModel): Result<Unit> {
        return repository.sendMessage(groupId, message)
    }
}
