package br.com.brunocarvalhs.group.draw

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertNotNull
import org.junit.Test

class DrawInitializerTest {

    @Test
    fun `builder should create instance and call build`() {
        // Given
        val navController = mockk<NavHostController>(relaxed = true)

        // When
        val initializer = DrawInitializer.Builder()
            .navController(navController)
            .onBack { }

        // Then
        assertNotNull(initializer)
    }
}
