package br.com.brunocarvalhs.chat.app.domain.services

import br.com.brunocarvalhs.core.domain.model.MessageModel
import kotlinx.coroutines.flow.Flow

interface ChatService {
    fun getMessages(groupId: String, sinceTimestamp: Long = 0L): Flow<List<MessageModel>>
    suspend fun sendMessage(groupId: String, message: MessageModel): Result<Unit>
    suspend fun clearMessages(groupId: String): Result<Unit>
    suspend fun setReaction(
        groupId: String,
        messageId: String,
        deviceId: String,
        emoji: String?
    ): Result<Unit>
}
