package br.com.brunocarvalhs.biometric.app.domain.useCases

import br.com.brunocarvalhs.friendssecrets.domain.services.BiometricService
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class IsBiometricEnabledUseCaseTest {

    private val service: BiometricService = mockk()
    private lateinit var useCase: IsBiometricEnabledUseCase

    @Before
    fun setup() {
        useCase = IsBiometricEnabledUseCase(service)
    }

    @Test
    fun `invoke should return state flow from service`() {
        // Given
        val expectedFlow = MutableStateFlow(true)
        every { service.isBiometricPromptEnabled } returns expectedFlow

        // When
        val result = useCase()

        // Then
        assertEquals(expectedFlow, result)
        assertEquals(true, result.value)
    }
}
