package br.com.brunocarvalhs.chat.app.data.repository

import br.com.brunocarvalhs.chat.app.data.local.ChatMessageDao
import br.com.brunocarvalhs.chat.app.data.local.toEntity
import br.com.brunocarvalhs.chat.app.domain.services.ChatService
import br.com.brunocarvalhs.core.domain.model.MessageModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ChatRepositoryImplTest {

    private val chatService: ChatService = mockk()
    private val chatMessageDao: ChatMessageDao = mockk(relaxed = true)
    private lateinit var repository: ChatRepositoryImpl

    @Before
    fun setup() {
        repository = ChatRepositoryImpl(chatService, chatMessageDao)
    }

    @Test
    fun `sendMessage should save locally and call the service`() = runTest {
        // Given
        val groupId = "group1"
        val message = MessageModel(id = "msg1", text = "Hello", groupId = groupId)
        coEvery { chatService.sendMessage(groupId, message) } returns Result.success(Unit)

        // When
        val result = repository.sendMessage(groupId, message)

        // Then
        assertTrue(result.isSuccess)
        coVerify { chatMessageDao.insertMessage(message.toEntity()) }
        coVerify { chatService.sendMessage(groupId, message) }
    }

    @Test
    fun `clearMessages should only clear the local cache and not touch the remote service`() =
        runTest {
            // Given
            val groupId = "group1"

            // When
            val result = repository.clearMessages(groupId)

            // Then
            assertTrue(result.isSuccess)
            coVerify { chatMessageDao.clearMessages(groupId) }
        }

    @Test
    fun `getMessages should fetch only messages newer than the local cache`() = runTest {
        // Given
        val groupId = "group1"
        coEvery { chatMessageDao.getLastTimestamp(groupId) } returns 1000L
        coEvery { chatService.getMessages(groupId, 1000L) } returns flowOf(emptyList())
        coEvery { chatMessageDao.getMessages(groupId) } returns flowOf(emptyList())

        // When
        repository.getMessages(groupId).toList()

        // Then
        coVerify { chatService.getMessages(groupId, 1000L) }
    }

    @Test
    fun `getMessages should fetch from scratch when there is no local cache`() = runTest {
        // Given
        val groupId = "group1"
        coEvery { chatMessageDao.getLastTimestamp(groupId) } returns null
        coEvery { chatService.getMessages(groupId, 0L) } returns flowOf(emptyList())
        coEvery { chatMessageDao.getMessages(groupId) } returns flowOf(emptyList())

        // When
        repository.getMessages(groupId).toList()

        // Then
        coVerify { chatService.getMessages(groupId, 0L) }
    }

    @Test
    fun `getMessages should return the local cache mapped to the domain model`() = runTest {
        // Given
        val groupId = "group1"
        val message = MessageModel(id = "msg1", text = "Hello", groupId = groupId, timestamp = 500L)
        coEvery { chatMessageDao.getLastTimestamp(groupId) } returns null
        coEvery { chatService.getMessages(groupId, 0L) } returns flowOf(emptyList())
        coEvery { chatMessageDao.getMessages(groupId) } returns flowOf(listOf(message.toEntity()))

        // When
        val result = repository.getMessages(groupId).toList()

        // Then
        assertEquals(listOf(message), result.first())
    }

    @Test
    fun `setReaction should call service and return success`() = runTest {
        // Given
        val groupId = "group1"
        val messageId = "msg1"
        val deviceId = "device1"
        coEvery { chatService.setReaction(groupId, messageId, deviceId, "👍") } returns Result.success(Unit)

        // When
        val result = repository.setReaction(groupId, messageId, deviceId, "👍")

        // Then
        assertTrue(result.isSuccess)
        coVerify { chatService.setReaction(groupId, messageId, deviceId, "👍") }
    }
}
