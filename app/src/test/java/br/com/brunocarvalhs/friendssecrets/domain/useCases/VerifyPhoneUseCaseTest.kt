package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.data.service.AuthService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class VerifyPhoneUseCaseTest {

    private val authService: AuthService = mockk()
    private val verifyPhoneUseCase = VerifyPhoneUseCase(authService)

    @Test
    fun `invoke should return success when authService returns success`() = runBlocking {
        // Arrange
        val code = "123456"
        coEvery { authService.signInWithCode(code) } returns Result.success(Unit)

        // Act
        val result = verifyPhoneUseCase.invoke(code)

        // Assert
        assertEquals(Result.success(Unit), result)
    }

    @Test
    fun `invoke should return failure when authService returns failure`() = runBlocking {
        // Arrange
        val code = "123456"
        val exception = Exception("Invalid code")
        coEvery { authService.signInWithCode(code) } returns Result.failure(exception)

        // Act
        val result = verifyPhoneUseCase.invoke(code)

        // Assert
        assertEquals(Result.failure<Unit>(exception), result)
    }
}