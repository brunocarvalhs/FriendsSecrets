package br.com.brunocarvalhs.chat.app.domain.usecase

import br.com.brunocarvalhs.chat.app.domain.services.AiChatSession
import br.com.brunocarvalhs.chat.app.domain.services.AiGiftAssistantService
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class StartAiGiftChatUseCaseTest {

    private val service: AiGiftAssistantService = mockk()
    private lateinit var useCase: StartAiGiftChatUseCase

    @Before
    fun setup() {
        useCase = StartAiGiftChatUseCase(service)
    }

    @Test
    fun `invoke should start a chat session from the service using the group name`() {
        // Given
        val groupName = "Amigo Secreto da Família"
        val expectedSession = mockk<AiChatSession>()
        every { service.startChat(groupName) } returns expectedSession

        // When
        val result = useCase(groupName)

        // Then
        assertEquals(expectedSession, result)
    }
}
