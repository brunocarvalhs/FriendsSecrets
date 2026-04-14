package br.com.brunocarvalhs.friendssecrets.commons.security

import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.Base64

class CryptoManagerTest {

    private val base64Encoder: CryptoManager.Base64Encoder = mockk()
    private lateinit var cryptoManager: CryptoManager

    @Before
    fun setup() {
        cryptoManager = CryptoManager(base64Encoder)
        
        // Mocking Base64 for tests
        every { base64Encoder.encodeToString(any(), any()) } answers {
            Base64.getUrlEncoder().withoutPadding().encodeToString(it.invocation.args[0] as ByteArray)
        }
        every { base64Encoder.decode(any(), any()) } answers {
            Base64.getUrlDecoder().decode(it.invocation.args[0] as String)
        }
    }

    @Test
    fun `encrypt should encode string to base64`() {
        // Given
        val input = "hello"
        val expected = Base64.getUrlEncoder().withoutPadding().encodeToString(input.toByteArray())

        // When
        val result = cryptoManager.encrypt(input)

        // Then
        assertEquals(expected, result)
    }

    @Test
    fun `decrypt should decode base64 to string`() {
        // Given
        val input = "hello"
        val encoded = Base64.getUrlEncoder().withoutPadding().encodeToString(input.toByteArray())

        // When
        val result = cryptoManager.decrypt(encoded)

        // Then
        assertEquals(input, result)
    }

    @Test
    fun `encryptMap should encrypt non-excluded keys`() {
        // Given
        val inputMap = mapOf("secret" to "value", "public" to "visible")
        val excludedKeys = setOf("public")
        val expectedSecret = Base64.getUrlEncoder().withoutPadding().encodeToString("\"value\"".toByteArray())

        // When
        val result = cryptoManager.encryptMap(inputMap, excludedKeys)

        // Then
        assertEquals("visible", result["public"])
        assertEquals(expectedSecret, result["secret"])
    }

    @Test
    fun `decryptMap should decrypt non-excluded keys`() {
        // Given
        val secretValue = "value"
        val encodedSecret = Base64.getUrlEncoder().withoutPadding().encodeToString("\"$secretValue\"".toByteArray())
        val inputMap = mapOf("secret" to encodedSecret, "public" to "visible")
        val excludedKeys = setOf("public")

        // When
        val result = cryptoManager.decryptMap(inputMap, excludedKeys)

        // Then
        assertEquals("visible", result["public"])
        assertEquals(secretValue, result["secret"])
    }
}
