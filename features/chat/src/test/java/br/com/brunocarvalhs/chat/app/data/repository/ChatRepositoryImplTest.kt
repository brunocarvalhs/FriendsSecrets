package br.com.brunocarvalhs.chat.app.data.repository

import br.com.brunocarvalhs.chat.app.domain.services.ChatService
import br.com.brunocarvalhs.core.domain.model.MessageModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ChatRepositoryImplTest {

    private val chatService: ChatService = mockk()
    private lateinit var repository: ChatRepositoryImpl

    @Before
    fun setup() {
        repository = ChatRepositoryImpl(chatService)
    }

    @Test
    fun `sendMessage should call network and service`() = runTest {
        // Given
        val groupId = "group1"
        val message = MessageModel(id = "msg1", text = "Hello", groupId = groupId)
        coEvery { chatService.sendMessage(groupId, message) } returns Result.success(Unit)

        // When
        val result = repository.sendMessage(groupId, message)

        // Then
        assertTrue(result.isSuccess)
        coVerify { chatService.sendMessage(groupId, message) }
    }

    @Test
    fun `clearMessages should call network and service`() = runTest {
        // Given
        val groupId = "group1"
        coEvery { chatService.clearMessages(groupId) } returns Result.success(Unit)

        // When
        val result = repository.clearMessages(groupId)

        // Then
        assertTrue(result.isSuccess)
        coVerify { chatService.clearMessages(groupId) }
    }

    @Test
    fun `getMessages should return flow from service`() = runTest {
        val groupId = "group1"
        val expectedFlow = flowOf(emptyList<MessageModel>())

        coEvery { chatService.getMessages(groupId) } returns expectedFlow

        val result = repository.getMessages(groupId)

        assertTrue(result === expectedFlow)
        coVerify { chatService.getMessages(groupId) }
    }
}
