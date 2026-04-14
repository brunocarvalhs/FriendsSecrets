package br.com.brunocarvalhs.chat.app.domain.usecase

import br.com.brunocarvalhs.chat.app.domain.repository.ChatRepository
import br.com.brunocarvalhs.friendssecrets.domain.model.MessageModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test

class SendMessageUseCaseTest {

    private val repository: ChatRepository = mockk()
    private val useCase = SendMessageUseCase(repository)

    @Test
    fun `invoke should return success from repository`() = runTest {
        // Given
        val groupId = "group123"
        val message = MessageModel(text = "Hello")
        coEvery { repository.sendMessage(groupId, message) } returns Result.success(Unit)

        // When
        val result = useCase(groupId, message)

        // Then
        assertTrue(result.isSuccess)
    }

    @Test
    fun `invoke should return failure from repository`() = runTest {
        // Given
        val groupId = "group123"
        val message = MessageModel(text = "Hello")
        val exception = Exception("Error")
        coEvery { repository.sendMessage(groupId, message) } returns Result.failure(exception)

        // When
        val result = useCase(groupId, message)

        // Then
        assertTrue(result.isFailure)
    }
}
