package br.com.brunocarvalhs.chat.app.domain.usecase

import br.com.brunocarvalhs.friendssecrets.domain.services.StorageService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class IdentifyUserUseCaseTest {

    private val storageService: StorageService = mockk()
    private val useCase = IdentifyUserUseCase(storageService)

    @Test
    fun `saveNickname should call storageService save`() = runTest {
        // Given
        val name = "Bruno"
        coEvery { storageService.save("user_nickname_cache", name) } returns Unit

        // When
        useCase.saveNickname(name)

        // Then
        coVerify { storageService.save("user_nickname_cache", name) }
    }

    @Test
    fun `getNickname should return value from storageService`() = runTest {
        // Given
        val name = "Bruno"
        coEvery { storageService.load("user_nickname_cache", String::class) } returns name

        // When
        val result = useCase.getNickname()

        // Then
        assertEquals(name, result)
    }
}
