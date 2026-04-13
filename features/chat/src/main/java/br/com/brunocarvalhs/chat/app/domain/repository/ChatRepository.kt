package br.com.brunocarvalhs.chat.app.domain.repository

import br.com.brunocarvalhs.chat.app.data.model.ChatMessage
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getMessages(groupId: String): Flow<List<ChatMessage>>
    suspend fun sendMessage(groupId: String, message: ChatMessage): Result<Unit>
    suspend fun syncMessages(groupId: String)
}
