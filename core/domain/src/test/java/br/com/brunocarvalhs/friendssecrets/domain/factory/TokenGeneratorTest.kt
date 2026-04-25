package br.com.brunocarvalhs.friendssecrets.domain.factory

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TokenGeneratorTest {

    @Test
    fun `should generate token with default size`() {
        val token = TokenGenerator.generate()

        assertEquals(8, token.length)
    }

    @Test
    fun `should generate token with custom size`() {
        val token = TokenGenerator.generate(size = 12)

        assertEquals(12, token.length)
    }

    @Test
    fun `should generate only uppercase and numbers`() {
        val token = TokenGenerator.generate(
            uppercase = true,
            lowercase = false,
            numbers = true
        )

        assertTrue(token.all { it.isUpperCase() || it.isDigit() })
    }

    @Test
    fun `should generate only numbers`() {
        val token = TokenGenerator.generate(
            uppercase = false,
            lowercase = false,
            numbers = true
        )

        assertTrue(token.all { it.isDigit() })
    }

    @Test(expected = IllegalArgumentException::class)
    fun `should throw exception when no character type is enabled`() {
        TokenGenerator.generate(
            uppercase = false,
            lowercase = false,
            numbers = false
        )
    }
}
