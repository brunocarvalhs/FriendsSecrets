package br.com.brunocarvalhs.chat.app.data.repository

import br.com.brunocarvalhs.chat.app.domain.services.ChatService
import br.com.brunocarvalhs.friendssecrets.core.network.domain.NetworkRequest
import br.com.brunocarvalhs.friendssecrets.core.network.domain.NetworkService
import br.com.brunocarvalhs.friendssecrets.domain.model.MessageModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ChatRepositoryImplTest {

    private val chatService: ChatService = mockk()
    private val networkService: NetworkService = mockk()
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
        coEvery {
            networkService.make(
                request = NetworkRequest(
                    endpoint = "messages",
                    payload = any(),
                    method = NetworkService.Method.POST
                ),
                response = String::class
            )
        } returns "OK"
        coEvery { chatService.sendMessage(groupId, message) } returns Result.success(Unit)

        // When
        val result = repository.sendMessage(groupId, message)

        // Then
        assertTrue(result.isSuccess)
        coVerify { chatService.sendMessage(groupId, message) }
        coVerify {
            networkService.make<String>(
                request = NetworkRequest(
                    endpoint = any(),
                    payload = any(),
                    headers = any(),
                    query = any(),
                    method = any()
                ),
                response = any()
            )
        }
    }

    @Test
    fun `clearMessages should call network and service`() = runTest {
        // Given
        val groupId = "group1"
        coEvery {
            networkService.make(
                request = NetworkRequest(
                    endpoint = "messages/$groupId",
                    method = NetworkService.Method.DELETE
                ),
                response = Boolean::class
            )
        } returns true
        coEvery { chatService.clearMessages(groupId) } returns Result.success(Unit)

        // When
        val result = repository.clearMessages(groupId)

        // Then
        assertTrue(result.isSuccess)
        coVerify { chatService.clearMessages(groupId) }
    }
}
