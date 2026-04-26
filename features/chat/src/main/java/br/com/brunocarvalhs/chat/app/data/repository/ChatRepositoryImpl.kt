package br.com.brunocarvalhs.chat.app.data.repository

import br.com.brunocarvalhs.chat.app.domain.repository.ChatRepository
import br.com.brunocarvalhs.chat.app.domain.services.ChatService
import br.com.brunocarvalhs.core.domain.model.MessageModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val chatService: ChatService
) : ChatRepository {

    override suspend fun getMessages(groupId: String): Flow<List<MessageModel>> {
        return chatService.getMessages(groupId)
    }

    override suspend fun sendMessage(groupId: String, message: MessageModel): Result<Unit> {
        return chatService.sendMessage(groupId, message)
    }

    override suspend fun clearMessages(groupId: String): Result<Unit> {
        return chatService.clearMessages(groupId)
    }
}
