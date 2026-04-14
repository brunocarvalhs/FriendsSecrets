package br.com.brunocarvalhs.chat

import androidx.navigation.NavHostController
import io.mockk.mockk
import org.junit.Assert.assertNotNull
import org.junit.Test

class ChatInitializerTest {

    @Test
    fun `builder should set properties and return itself`() {
        // Given
        val builder = ChatInitializer.Builder()
        val navController: NavHostController = mockk()
        val onBack: () -> Unit = {}

        // When
        val result = builder
            .navController(navController)
            .onBack(onBack)

        // Then
        assertNotNull(result)
    }
}
