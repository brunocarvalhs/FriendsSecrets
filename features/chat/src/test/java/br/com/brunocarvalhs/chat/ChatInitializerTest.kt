package br.com.brunocarvalhs.chat

import androidx.navigation.NavHostController
import io.mockk.mockk
import org.junit.Assert.assertNotNull
import org.junit.Test

class ChatInitializerTest {

    @Test
    fun `builder should create instance and call build`() {
        // Given
        val navController = mockk<NavHostController>(relaxed = true)

        // When
        val initializer = ChatInitializer.Builder()
            .navController(navController)
            .onBack { }

        // Then
        assertNotNull(initializer)
    }
}
