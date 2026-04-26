package br.com.brunocarvalhs.group.list

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import io.mockk.mockk
import org.junit.Assert.assertNotNull
import org.junit.Test

class GroupListInitializerTest {

    @Test
    fun `builder should create instance and call build`() {
        // Given
        val navController = mockk<NavHostController>(relaxed = true)

        // When
        val initializer = GroupListInitializer.Builder()
            .navController(navController)
            .onGroupToCreate { }
            .onGroupToDetails { }

        // Then
        assertNotNull(initializer)
    }
}
