package br.com.brunocarvalhs.friendssecrets.common.extensions

import org.junit.Assert.assertEquals
import org.junit.Test

class StringExtensionsTest {

    @Test
    fun `should mask phone number with more than 8 digits`() {
        val input = "+55 (11) 91234-5678"
        val expected = "+551191******78"

        val result = input.toMaskedPhoneNumber()

        assertEquals(expected, result)
    }

    @Test
    fun `should mask phone number exactly 8 digits`() {
        val input = "12345678"
        val expected = "+12345678"

        val result = input.toMaskedPhoneNumber()

        assertEquals(expected, result)
    }

    @Test
    fun `should mask phone number less than 8 digits`() {
        val input = "1234"
        val expected = "+1234"

        val result = input.toMaskedPhoneNumber()

        assertEquals(expected, result)
    }

    @Test
    fun `should remove non-digit characters`() {
        val input = "(+55) 11-91234-5678"
        val expected = "+551191******78"

        val result = input.toMaskedPhoneNumber()

        assertEquals(expected, result)
    }
}
