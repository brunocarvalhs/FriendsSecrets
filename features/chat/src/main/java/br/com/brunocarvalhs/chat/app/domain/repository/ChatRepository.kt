package br.com.brunocarvalhs.chat.app.domain.repository

import br.com.brunocarvalhs.friendssecrets.domain.model.MessageModel
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun getMessages(groupId: String): Flow<List<MessageModel>>
    suspend fun sendMessage(groupId: String, message: MessageModel): Result<Unit>
    suspend fun clearMessages(groupId: String): Result<Unit>
}
