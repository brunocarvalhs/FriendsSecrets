package br.com.brunocarvalhs.chat.app.domain.usecase

import br.com.brunocarvalhs.friendssecrets.domain.model.MessageModel
import br.com.brunocarvalhs.chat.app.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMessagesUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    operator fun invoke(groupId: String): Flow<List<MessageModel>> {
        return repository.getMessages(groupId)
    }
}
