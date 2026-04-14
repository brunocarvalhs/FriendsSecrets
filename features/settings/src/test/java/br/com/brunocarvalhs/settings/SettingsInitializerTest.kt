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
        val navGraphBuilder = mockk<NavGraphBuilder>(relaxed = true)

        // When
        val initializer = SettingsInitializer.Builder()
            .navController(navController)
            .onBack { }
            .build(navGraphBuilder)

        // Then
        assertNotNull(initializer)
        // Verify that navigation was configured (build calls navGraphBuilder.navigation)
        verify { navGraphBuilder.addDestination(any()) }
    }
}
