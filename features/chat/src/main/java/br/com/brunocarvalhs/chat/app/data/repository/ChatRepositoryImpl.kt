package br.com.brunocarvalhs.chat.app.data.repository

import br.com.brunocarvalhs.chat.app.data.local.ChatMessageDao
import br.com.brunocarvalhs.chat.app.data.model.ChatMessage
import br.com.brunocarvalhs.chat.app.domain.repository.ChatRepository
import br.com.brunocarvalhs.friendssecrets.domain.model.MessageModel
import br.com.brunocarvalhs.friendssecrets.domain.services.ChatService
import br.com.brunocarvalhs.friendssecrets.domain.services.NetworkService
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val chatService: ChatService,
    private val networkService: NetworkService,
    private val chatMessageDao: ChatMessageDao
) : ChatRepository {

    override suspend fun getMessages(groupId: String): Flow<List<MessageModel>> = coroutineScope {
        launch {
            val history = networkService.make(
                endpoint = "messages",
                query = mapOf("groupId" to groupId),
                method = NetworkService.Method.GET,
                clazz = Array<MessageModel>::class
            )
            history?.toList()?.let { list ->
                chatMessageDao.insertMessages(list.map { it.toEntity() })
            }
        }

        chatService.getMessages(groupId)
            .onEach { messages -> chatMessageDao.insertMessages(messages.map { it.toEntity() }) }
            .launchIn(this@coroutineScope)

        return@coroutineScope chatMessageDao.getMessages(groupId).map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun sendMessage(groupId: String, message: MessageModel): Result<Unit> =
        coroutineScope {
            launch {
                networkService.make(
                    endpoint = "messages",
                    payload = message.toMap(),
                    method = NetworkService.Method.POST,
                    clazz = String::class
                )
            }

            return@coroutineScope chatService.sendMessage(groupId, message)
        }

    override suspend fun clearMessages(groupId: String): Result<Unit> = coroutineScope {
        launch {
            networkService.make(
                endpoint = "messages/$groupId",
                method = NetworkService.Method.DELETE,
                clazz = Boolean::class
            )
        }
        return@coroutineScope chatService.clearMessages(groupId)
    }

    private fun MessageModel.toMap(): Map<String, Any?> = mapOf(
        "id" to id,
        "groupId" to groupId,
        "text" to text,
        "senderId" to senderId,
        "senderName" to senderName,
        "timestamp" to timestamp,
        "status" to status.name
    )

    private fun MessageModel.toEntity() = ChatMessage(
        id = id,
        groupId = groupId,
        text = text,
        senderId = senderId,
        senderName = senderName,
        timestamp = timestamp,
        status = status
    )

    private fun ChatMessage.toDomain() = MessageModel(
        id = id,
        groupId = groupId,
        text = text,
        senderId = senderId,
        senderName = senderName,
        timestamp = timestamp,
        status = status
    )
}
