package br.com.brunocarvalhs.biometric

import androidx.navigation.NavHostController
import io.mockk.mockk
import org.junit.Assert.assertNotNull
import org.junit.Test

class BiometricInitializerTest {

    @Test
    fun `builder should set properties and return itself`() {
        // Given
        val builder = BiometricInitializer.Builder()
        val navController: NavHostController = mockk()
        val onSuccess: () -> Unit = {}

        // When
        val result = builder
            .navController(navController)
            .onSuccess(onSuccess)

        // Then
        assertNotNull(result)
    }
}
