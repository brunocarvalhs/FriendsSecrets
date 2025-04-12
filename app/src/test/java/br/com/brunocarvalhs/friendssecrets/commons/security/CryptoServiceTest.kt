package br.com.brunocarvalhs.friendssecrets.commons.security

import android.util.Base64
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CryptoServiceTest {

    private lateinit var base64Encoder: Base64Encoder
    private lateinit var cryptoService: CryptoService

    @Before
    fun setUp() {
        base64Encoder = mockk(relaxed = true)
        cryptoService = spyk(CryptoService(base64Encoder))
    }

    @Test
    fun `encryptMap should encrypt all non-excluded keys`() {
        every { base64Encoder.encodeToString(any(), any()) } returns "encodedValue"
        every { base64Encoder.decode(any(), any()) } returns "decodedValue".toByteArray()

        val inputMap = mapOf("key1" to "value1", "key2" to "value2")
        val excludedKeys = setOf("key2")

        val result = cryptoService.encryptMap(inputMap, excludedKeys)

        assertEquals(cryptoService.encrypt("value1"), result["key1"])
        assertEquals("value2", result["key2"])
    }

    @Test
    fun `decryptMap should decrypt all non-excluded keys`() {
        every { base64Encoder.encodeToString(any(), any()) } returns "encodedValue"
        every { base64Encoder.decode(any(), any()) } returns "value1".toByteArray()

        val encodedMap = mapOf("key1" to cryptoService.encrypt("value1"), "key2" to "value2")
        val excludedKeys = setOf("key2")

        val result = cryptoService.decryptMap(encodedMap, excludedKeys)

        assertEquals("value1", result["key1"])
        assertEquals("value2", result["key2"])
    }

    @Test
    fun `encrypt should encode a string to Base64`() {
        val input = "testString"
        val expected = base64Encoder.encodeToString(
            input = input.toByteArray(),
            flags = Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP
        )

        val result = cryptoService.encrypt(input)

        assertEquals(expected, result)
    }

    @Test
    fun `decrypt should decode a Base64 string`() {
        every { base64Encoder.encodeToString(any(), any()) } returns "encodedValue"
        every { base64Encoder.decode(any(), any()) } returns "testString".toByteArray()
        val input = "testString"
        val encoded = cryptoService.encrypt(input)

        val result = cryptoService.decrypt(encoded)

        assertEquals(input, result)
    }
}