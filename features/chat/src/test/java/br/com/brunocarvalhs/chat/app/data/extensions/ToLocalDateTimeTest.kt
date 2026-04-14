package br.com.brunocarvalhs.chat.app.data.extensions

import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Locale

class ToLocalDateTimeTest {

    @Test
    fun `toLocalDateTime should format long timestamp to HH mm string`() {
        // Given
        val timestamp = 1715692800000L // Exemplo de timestamp
        val expected = SimpleDateFormat("HH:mm", Locale.getDefault()).format(java.util.Date(timestamp))

        // When
        val result = timestamp.toLocalDateTime()

        // Then
        assertEquals(expected, result)
    }

    @Test
    fun `toLocalDateTime should format zero timestamp correctly`() {
        // Given
        val timestamp = 0L
        val expected = SimpleDateFormat("HH:mm", Locale.getDefault()).format(java.util.Date(timestamp))

        // When
        val result = timestamp.toLocalDateTime()

        // Then
        assertEquals(expected, result)
    }
}
