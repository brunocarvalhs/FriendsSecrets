package br.com.brunocarvalhs.chat.app.data.repository

import br.com.brunocarvalhs.chat.app.data.local.ChatMessageDao
import br.com.brunocarvalhs.chat.app.data.local.toDomain
import br.com.brunocarvalhs.chat.app.data.local.toEntity
import br.com.brunocarvalhs.chat.app.domain.repository.ChatRepository
import br.com.brunocarvalhs.chat.app.domain.services.ChatService
import br.com.brunocarvalhs.core.domain.model.MessageModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val chatService: ChatService,
    private val chatMessageDao: ChatMessageDao
) : ChatRepository {

    override suspend fun getMessages(groupId: String): Flow<List<MessageModel>> = flow {
        coroutineScope {
            launch {
                // Local Room cache is the source of truth for the UI; only fetch
                // messages newer than what we already have to avoid re-downloading
                // (and re-billing) the whole history on every chat open.
                val sinceTimestamp = chatMessageDao.getLastTimestamp(groupId) ?: 0L
                chatService.getMessages(groupId, sinceTimestamp).collect { newMessages ->
                    chatMessageDao.insertMessages(newMessages.map { it.toEntity() })
                }
            }
            emitAll(
                chatMessageDao.getMessages(groupId).map { entities ->
                    entities.map { it.toDomain() }
                }
            )
        }
    }

    override suspend fun sendMessage(groupId: String, message: MessageModel): Result<Unit> {
        // Save locally first so the message survives even if the remote write fails or the app closes.
        chatMessageDao.insertMessage(message.toEntity())
        return chatService.sendMessage(groupId, message)
    }

    override suspend fun clearMessages(groupId: String): Result<Unit> = runCatching {
        // Clearing is a local, per-device action (like WhatsApp's "Clear Chat"):
        // it never deletes data from Firebase, so other members keep their history.
        chatMessageDao.clearMessages(groupId)
    }

    override suspend fun setReaction(
        groupId: String,
        messageId: String,
        deviceId: String,
        emoji: String?
    ): Result<Unit> {
        return chatService.setReaction(groupId, messageId, deviceId, emoji)
    }
}
