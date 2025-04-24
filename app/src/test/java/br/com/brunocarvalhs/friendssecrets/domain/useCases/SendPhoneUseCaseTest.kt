package br.com.brunocarvalhs.friendssecrets.domain.useCases

import android.app.Activity
import br.com.brunocarvalhs.friendssecrets.data.service.AuthService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class SendPhoneUseCaseTest {

    private val mockActivity: Activity = mockk(relaxed = true)
    private val mockAuthService: AuthService = mockk()
    private val useCase = SendPhoneUseCase(activity = mockActivity, authService = mockAuthService)

    @Test
    fun `should return success when sendVerificationCode succeeds`() = runBlocking {
        // Arrange
        val phone = "123456789"
        val countryCode = "+55"
        coEvery { mockAuthService.sendVerificationCode(any(), any()) } returns Result.success(Unit)

        // Act
        val result = useCase.invoke(phone, countryCode)

        // Assert
        assertEquals(Result.success(Unit), result)
        coVerify { mockAuthService.sendVerificationCode("$countryCode$phone", mockActivity) }
    }

    @Test
    fun `should return failure when sendVerificationCode fails`() = runBlocking {
        // Arrange
        val phone = "123456789"
        val countryCode = "+55"
        val exception = Exception("Error sending verification code")
        coEvery { mockAuthService.sendVerificationCode(any(), any()) } returns Result.failure(exception)

        // Act
        val result = useCase.invoke(phone, countryCode)

        // Assert
        assertEquals(Result.failure<Unit>(exception), result)
        coVerify { mockAuthService.sendVerificationCode("$countryCode$phone", mockActivity) }
    }
}