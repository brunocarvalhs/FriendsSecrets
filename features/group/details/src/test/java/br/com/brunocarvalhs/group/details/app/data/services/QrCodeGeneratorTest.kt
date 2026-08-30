package br.com.brunocarvalhs.group.details.app.data.services

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class QrCodeGeneratorTest {

    private lateinit var generator: QrCodeGenerator

    @Before
    fun setup() {
        generator = QrCodeGenerator()
    }

    @Test
    fun `generate should return a bitmap with the requested size`() {
        // When
        val bitmap = generator.generate("ABC12345", size = 128)

        // Then
        assertNotNull(bitmap)
        assertEquals(128, bitmap?.width)
        assertEquals(128, bitmap?.height)
    }

    @Test
    fun `generate should return null for blank content`() {
        // When
        val bitmap = generator.generate("")

        // Then
        assertEquals(null, bitmap)
    }
}
