package br.com.brunocarvalhs.chat.app.domain.usecase

import br.com.brunocarvalhs.chat.app.domain.repository.ChatRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ClearMessagesUseCaseTest {

    private val repository: ChatRepository = mockk()
    private lateinit var useCase: ClearMessagesUseCase

    @Before
    fun setup() {
        useCase = ClearMessagesUseCase(repository)
    }

    @Test
    fun `invoke should call repository clearMessages and return success`() = runTest {
        // Given
        val groupId = "group1"
        coEvery { repository.clearMessages(groupId) } returns Result.success(Unit)

        // When
        val result = useCase(groupId)

        // Then
        assertTrue(result.isSuccess)
    }

    @Test
    fun `invoke should return failure when repository fails`() = runTest {
        // Given
        val groupId = "group1"
        coEvery { repository.clearMessages(groupId) } returns Result.failure(Exception("Error"))

        // When
        val result = useCase(groupId)

        // Then
        assertTrue(result.isFailure)
    }
}
