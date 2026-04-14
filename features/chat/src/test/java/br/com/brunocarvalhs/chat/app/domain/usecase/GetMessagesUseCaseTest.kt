package br.com.brunocarvalhs.chat.app.domain.usecase

import br.com.brunocarvalhs.chat.app.domain.repository.ChatRepository
import br.com.brunocarvalhs.friendssecrets.domain.model.MessageModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetMessagesUseCaseTest {

    private val repository: ChatRepository = mockk()
    private val useCase = GetMessagesUseCase(repository)

    @Test
    fun `invoke should return flow of messages from repository`() = runTest {
        // Given
        val groupId = "group123"
        val messages = listOf(
            MessageModel(id = "1", text = "Hello"),
            MessageModel(id = "2", text = "World")
        )
        coEvery { repository.getMessages(groupId) } returns flowOf(messages)

        // When
        val result = useCase(groupId)

        // Then
        result.collect {
            assertEquals(messages, it)
        }
    }
}
