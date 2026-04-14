package br.com.brunocarvalhs.group.list.commons.options

import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class OptionsMoreTest {

    @Test
    fun `OptionsMore should store properties and invoke lambda`() {
        // Given
        val lambda: () -> Unit = mockk(relaxed = true)
        val options = OptionsMore(
            name = { "Name" },
            icon = mockk(),
            contentDescription = { "Description" },
            lambda = lambda
        )

        // When
        options.lambda()

        // Then
        verify { lambda() }
    }
}
