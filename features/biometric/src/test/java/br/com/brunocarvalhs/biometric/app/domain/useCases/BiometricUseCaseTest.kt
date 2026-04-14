package br.com.brunocarvalhs.biometric.app.domain.useCases

import br.com.brunocarvalhs.friendssecrets.domain.services.BiometricService
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BiometricUseCaseTest {

    private val biometricService: BiometricService = mockk()
    private val useCase = BiometricUseCase(biometricService)

    @Test
    fun `canAuthenticate should return true when service returns true`() {
        // Given
        every { biometricService.canAuthenticate() } returns true

        // When
        val result = useCase.canAuthenticate()

        // Then
        assertTrue(result)
    }

    @Test
    fun `canAuthenticate should return false when service returns false`() {
        // Given
        every { biometricService.canAuthenticate() } returns false

        // When
        val result = useCase.canAuthenticate()

        // Then
        assertFalse(result)
    }
}
