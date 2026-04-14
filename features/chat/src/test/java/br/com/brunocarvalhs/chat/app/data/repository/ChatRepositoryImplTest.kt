package br.com.brunocarvalhs.chat.app.data.repository

import br.com.brunocarvalhs.chat.app.data.local.ChatMessageDao
import br.com.brunocarvalhs.chat.app.data.model.ChatMessage
import br.com.brunocarvalhs.friendssecrets.domain.model.MessageModel
import br.com.brunocarvalhs.friendssecrets.domain.model.MessageStatus
import br.com.brunocarvalhs.friendssecrets.domain.services.ChatService
import br.com.brunocarvalhs.friendssecrets.domain.services.NetworkService
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ChatRepositoryImplTest {

    private val chatService: ChatService = mockk()
    private val networkService: NetworkService = mockk()
    private val chatMessageDao: ChatMessageDao = mockk()
    private lateinit var repository: ChatRepositoryImpl

    @Before
    fun setup() {
        repository = ChatRepositoryImpl(chatService, networkService, chatMessageDao)
    }

    @Test
    fun `getMessages should fetch from network and return from local dao`() = runTest {
        // Given
        val groupId = "group123"
        val networkMessages = arrayOf(
            MessageModel(id = "1", text = "Network", status = MessageStatus.SENT)
        )
        val domainMessages = listOf(
            MessageModel(id = "2", text = "Local", status = MessageStatus.SENT)
        )
        val entities = listOf(
            ChatMessage(id = "2", groupId = groupId, text = "Local", senderId = "", senderName = "", timestamp = 0L, status = MessageStatus.SENT)
        )

        coEvery { networkService.make<Array<MessageModel>>(
            endpoint = "messages",
            query = mapOf("groupId" to groupId),
            method = NetworkService.Method.GET,
            clazz = Array<MessageModel>::class
        ) } returns networkMessages

        coEvery { chatMessageDao.insertMessages(any()) } returns Unit
        coEvery { chatService.getMessages(groupId) } returns flowOf(emptyList())
        coEvery { chatMessageDao.getMessages(groupId) } returns flowOf(entities)

        // When
        val result = repository.getMessages(groupId)

        // Then
        result.collect {
            assertEquals(domainMessages.size, it.size)
            assertEquals(domainMessages[0].id, it[0].id)
        }
        coVerify { chatMessageDao.insertMessages(any()) }
    }

    @Test
    fun `sendMessage should call network and service`() = runTest {
        // Given
        val groupId = "group123"
        val message = MessageModel(id = "1", text = "Hello")
        
        coEvery { networkService.make<String>(
            endpoint = "messages",
            payload = any(),
            method = NetworkService.Method.POST,
            clazz = String::class
        ) } returns "OK"
        
        coEvery { chatService.sendMessage(groupId, message) } returns Result.success(Unit)

        // When
        val result = repository.sendMessage(groupId, message)

        // Then
        assertTrue(result.isSuccess)
        coVerify { networkService.make<String>(any(), any(), any(), any(), any(), any()) }
        coVerify { chatService.sendMessage(groupId, message) }
    }

    @Test
    fun `clearMessages should call network and service`() = runTest {
        // Given
        val groupId = "group123"
        
        coEvery { networkService.make<Boolean>(
            endpoint = "messages/$groupId",
            method = NetworkService.Method.DELETE,
            clazz = Boolean::class
        ) } returns true
        
        coEvery { chatService.clearMessages(groupId) } returns Result.success(Unit)

        // When
        val result = repository.clearMessages(groupId)

        // Then
        assertTrue(result.isSuccess)
        coVerify { networkService.make<Boolean>(any(), any(), any(), any(), any(), any()) }
        coVerify { chatService.clearMessages(groupId) }
    }
}
