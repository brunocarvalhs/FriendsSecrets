package br.com.brunocarvalhs.group.list

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertNotNull
import org.junit.Test

class GroupListInitializerTest {

    @Test
    fun `builder should create instance and call build`() {
        // Given
        val navController = mockk<NavHostController>(relaxed = true)
        val navGraphBuilder = mockk<NavGraphBuilder>(relaxed = true)

        // When
        val initializer = GroupListInitializer.Builder()
            .navController(navController)
            .onGroupToCreate { }
            .onGroupToDetails { }
            .build(navGraphBuilder)

        // Then
        assertNotNull(initializer)
        // Verify that navigation was configured (build calls navGraphBuilder.navigation)
        verify { navGraphBuilder.addDestination(any()) }
    }
}
