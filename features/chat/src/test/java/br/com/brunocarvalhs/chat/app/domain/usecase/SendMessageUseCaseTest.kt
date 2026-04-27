package br.com.brunocarvalhs.chat.app.domain.usecase

import br.com.brunocarvalhs.chat.app.domain.repository.ChatRepository
import br.com.brunocarvalhs.core.domain.model.MessageModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SendMessageUseCaseTest {

    private val repository: ChatRepository = mockk()
    private lateinit var useCase: SendMessageUseCase

    @Before
    fun setup() {
        useCase = SendMessageUseCase(repository)
    }

    @Test
    fun `invoke should call repository and return success`() = runTest {
        // Given
        val groupId = "group1"
        val message = mockk<MessageModel>()
        coEvery { repository.sendMessage(groupId, message) } returns Result.success(Unit)

        // When
        val result = useCase(groupId, message)

        // Then
        assertTrue(result.isSuccess)
    }

    @Test
    fun `invoke should return failure when repository fails`() = runTest {
        // Given
        val groupId = "group1"
        val message = mockk<MessageModel>()
        coEvery { repository.sendMessage(groupId, message) } returns Result.failure(Exception("Error"))

        // When
        val result = useCase(groupId, message)

        // Then
        assertTrue(result.isFailure)
    }
}
