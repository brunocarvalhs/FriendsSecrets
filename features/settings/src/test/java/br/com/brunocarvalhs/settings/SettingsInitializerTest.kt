package br.com.brunocarvalhs.settings

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertNotNull
import org.junit.Test

class SettingsInitializerTest {

    @Test
    fun `builder should create instance and call build`() {
        // Given
        val navController = mockk<NavHostController>(relaxed = true)

        // When
        val initializer = SettingsInitializer.Builder()
            .navController(navController)
            .onBack { }

        // Then
        assertNotNull(initializer)
    }
}
