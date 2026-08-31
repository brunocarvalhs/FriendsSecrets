package br.com.brunocarvalhs.chat.app.domain.usecase

import br.com.brunocarvalhs.chat.app.domain.repository.ChatRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ToggleMessageReactionUseCaseTest {

    private val repository: ChatRepository = mockk()
    private lateinit var useCase: ToggleMessageReactionUseCase

    @Before
    fun setup() {
        useCase = ToggleMessageReactionUseCase(repository)
    }

    @Test
    fun `invoke should set reaction when device has not reacted with this emoji`() = runTest {
        // Given
        coEvery { repository.setReaction("group1", "msg1", "device1", "👍") } returns Result.success(Unit)

        // When
        val result = useCase("group1", "msg1", "device1", emptyMap(), "👍")

        // Then
        assertTrue(result.isSuccess)
        coVerify { repository.setReaction("group1", "msg1", "device1", "👍") }
    }

    @Test
    fun `invoke should clear reaction when device already reacted with this emoji`() = runTest {
        // Given
        val currentReactions = mapOf("device1" to "👍")
        coEvery { repository.setReaction("group1", "msg1", "device1", null) } returns Result.success(Unit)

        // When
        val result = useCase("group1", "msg1", "device1", currentReactions, "👍")

        // Then
        assertTrue(result.isSuccess)
        coVerify { repository.setReaction("group1", "msg1", "device1", null) }
    }

    @Test
    fun `invoke should replace reaction when device reacted with a different emoji`() = runTest {
        // Given
        val currentReactions = mapOf("device1" to "😂")
        coEvery { repository.setReaction("group1", "msg1", "device1", "👍") } returns Result.success(Unit)

        // When
        val result = useCase("group1", "msg1", "device1", currentReactions, "👍")

        // Then
        assertTrue(result.isSuccess)
        coVerify { repository.setReaction("group1", "msg1", "device1", "👍") }
    }
}
