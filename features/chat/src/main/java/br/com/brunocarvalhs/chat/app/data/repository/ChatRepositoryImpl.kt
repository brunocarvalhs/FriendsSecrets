package br.com.brunocarvalhs.chat.app.data.repository

import br.com.brunocarvalhs.chat.app.data.local.ChatMessageDao
import br.com.brunocarvalhs.chat.app.data.model.ChatMessage
import br.com.brunocarvalhs.chat.app.domain.repository.ChatRepository
import br.com.brunocarvalhs.friendssecrets.domain.model.MessageModel
import br.com.brunocarvalhs.friendssecrets.domain.services.ChatService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val chatService: ChatService,
    private val chatMessageDao: ChatMessageDao
) : ChatRepository {

    private val repositoryScope = CoroutineScope(Dispatchers.IO)

    override fun getMessages(groupId: String): Flow<List<MessageModel>> {
        chatService.getMessages(groupId)
            .onEach { messages ->
                chatMessageDao.insertMessages(messages.map { it.toEntity() })
            }
            .launchIn(repositoryScope)

        return chatMessageDao.getMessages(groupId).map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun sendMessage(groupId: String, message: MessageModel): Result<Unit> {
        return chatService.sendMessage(groupId, message)
    }

    private fun MessageModel.toEntity() = ChatMessage(
        id = id,
        groupId = groupId,
        text = text,
        senderId = senderId,
        senderName = senderName,
        timestamp = timestamp
    )

    private fun ChatMessage.toDomain() = MessageModel(
        id = id,
        groupId = groupId,
        text = text,
        senderId = senderId,
        senderName = senderName,
        timestamp = timestamp
    )
}
