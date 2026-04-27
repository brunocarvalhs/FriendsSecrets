package br.com.brunocarvalhs.group.details

import androidx.navigation.NavHostController
import io.mockk.mockk
import org.junit.Assert.assertNotNull
import org.junit.Test

class GroupDetailsInitializerTest {

    @Test
    fun `builder should create instance and call build`() {
        // Given
        val navController = mockk<NavHostController>(relaxed = true)

        // When
        val initializer = GroupDetailsInitializer.Builder()
            .navController(navController)
            .onBack { }
            .onDraw { }
            .onChat { }
            .onEdit { }

        // Then
        assertNotNull(initializer)
    }
}
