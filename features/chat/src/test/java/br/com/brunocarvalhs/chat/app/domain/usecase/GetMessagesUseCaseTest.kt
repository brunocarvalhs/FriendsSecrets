package br.com.brunocarvalhs.chat.app.domain.usecase

import br.com.brunocarvalhs.chat.app.domain.repository.ChatRepository
import br.com.brunocarvalhs.core.domain.model.MessageModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetMessagesUseCaseTest {

    private val repository: ChatRepository = mockk()
    private lateinit var useCase: GetMessagesUseCase

    @Before
    fun setup() {
        useCase = GetMessagesUseCase(repository)
    }

    @Test
    fun `invoke should return flow from repository`() = kotlinx.coroutines.test.runTest {
        // Given
        val groupId = "group1"
        val expectedFlow = mockk<Flow<List<MessageModel>>>()
        coEvery { repository.getMessages(groupId) } returns expectedFlow

        // When
        val result = useCase(groupId)

        // Then
        assertEquals(expectedFlow, result)
    }
}
