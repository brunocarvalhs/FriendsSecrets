package br.com.brunocarvalhs.core.domain.extensions

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Calendar

class ToFormattedDateTest {

    @Test
    fun `should format timestamp to default date pattern`() {
        val calendar = Calendar.getInstance().apply {
            set(2023, Calendar.JANUARY, 15, 0, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val timestamp = calendar.timeInMillis

        val result = timestamp.toFormattedDate()

        assertEquals("15/01/2023", result)
    }

    @Test
    fun `should return empty string when timestamp is zero`() {
        val timestamp = 0L

        val result = timestamp.toFormattedDate()

        assertEquals("", result)
    }

    @Test
    fun `should format timestamp with custom pattern`() {
        val calendar = Calendar.getInstance().apply {
            set(2023, Calendar.DECEMBER, 25, 14, 30, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val timestamp = calendar.timeInMillis

        val result = timestamp.toFormattedDate("yyyy-MM-dd")

        assertEquals("2023-12-25", result)
    }

    @Test
    fun `should return empty string for invalid pattern`() {
        val timestamp = System.currentTimeMillis()

        val result = timestamp.toFormattedDate("invalid-pattern-%%%")

        assertEquals("", result)
    }

    @Test
    fun `should format datetime correctly`() {
        val calendar = Calendar.getInstance().apply {
            set(2023, Calendar.JUNE, 10, 16, 45, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val timestamp = calendar.timeInMillis

        val result = timestamp.toFormattedDateTime()

        assertEquals("10/06/2023 16:45", result)
    }
}
