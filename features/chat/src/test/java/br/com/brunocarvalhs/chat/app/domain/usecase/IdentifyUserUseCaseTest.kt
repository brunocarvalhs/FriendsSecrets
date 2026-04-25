package br.com.brunocarvalhs.chat.app.domain.usecase

import br.com.brunocarvalhs.storage.domain.StorageService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class IdentifyUserUseCaseTest {

    private val storageService: StorageService = mockk()
    private lateinit var useCase: IdentifyUserUseCase

    @Before
    fun setup() {
        useCase = IdentifyUserUseCase(storageService)
    }

    @Test
    fun `saveNickname should call storage service`() = runTest {
        // Given
        val nickname = "Bruno"
        coEvery { storageService.save("user_nickname_cache", nickname) } returns Unit

        // When
        useCase.saveNickname(nickname)

        // Then
        coVerify { storageService.save("user_nickname_cache", nickname) }
    }

    @Test
    fun `getNickname should return value from storage service`() = runTest {
        // Given
        val nickname = "Bruno"
        coEvery { storageService.load("user_nickname_cache", String::class) } returns nickname

        // When
        val result = useCase.getNickname()

        // Then
        assertEquals(nickname, result)
    }

    @Test
    fun `getNickname should return null when not found`() = runTest {
        // Given
        coEvery { storageService.load("user_nickname_cache", String::class) } returns null

        // When
        val result = useCase.getNickname()

        // Then
        assertEquals(null, result)
    }
}
