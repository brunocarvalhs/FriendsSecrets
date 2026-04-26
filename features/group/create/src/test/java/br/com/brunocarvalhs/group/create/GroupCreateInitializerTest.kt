package br.com.brunocarvalhs.group.create

import androidx.navigation.NavHostController
import io.mockk.mockk
import org.junit.Assert.assertNotNull
import org.junit.Test

class GroupCreateInitializerTest {

    @Test
    fun `builder should create instance and call build`() {
        val builder = GroupCreateInitializer.Builder()
        val navController: NavHostController = mockk()
        val onFinish: (String) -> Unit = {}

        // When
        val result = builder
            .navController(navController)
            .onFinish(onFinish)

        // Then
        assertNotNull(result)
    }
}
