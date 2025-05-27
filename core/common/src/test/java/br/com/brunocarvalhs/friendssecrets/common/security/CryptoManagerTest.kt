package br.com.brunocarvalhs.friendssecrets.common.security

import com.google.gson.Gson
import io.mockk.spyk
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CryptoManagerTest {

    private lateinit var base64Encoder: Base64Encoder
    private lateinit var gson: Gson
    private lateinit var cryptoService: CryptoManager

    @Before
    fun setUp() {
        base64Encoder = spyk(object : Base64Encoder {
            override fun encodeToString(input: ByteArray, flags: Int): String {
                return java.util.Base64.getUrlEncoder().withoutPadding().encodeToString(input)
            }

            override fun decode(input: String, flags: Int): ByteArray {
                return java.util.Base64.getUrlDecoder().decode(input)
            }
        })

        gson = Gson()
        cryptoService = CryptoManager(base64Encoder, gson)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `should encrypt and decrypt simple map`() {
        val original = mapOf(
            "name" to "John",
            "age" to 30,
            "active" to true
        )

        val encrypted = cryptoService.encryptMap(original)
        assertTrue(encrypted.values.all { it is String })

        val decrypted = cryptoService.decryptMap(encrypted)
        assertEquals(original, decrypted)
    }

    @Test
    fun `should encrypt and decrypt map with nested structure`() {
        val original = mapOf(
            "user" to mapOf("id" to 1, "name" to "Alice"),
            "tags" to listOf("a", "b", "c")
        )

        val encrypted = cryptoService.encryptMap(original)
        assertTrue(encrypted["user"] is String)
        assertTrue(encrypted["tags"] is String)

        val decrypted = cryptoService.decryptMap(encrypted)
        assertEquals(original, decrypted)
    }

    @Test
    fun `should exclude specified keys from encryption`() {
        val original = mapOf(
            "id" to 123,
            "token" to "secret-token"
        )

        val encrypted = cryptoService.encryptMap(original, excludedKeys = setOf("token"))
        assertTrue(encrypted["token"] == original["token"])

        val decrypted = cryptoService.decryptMap(encrypted, excludedKeys = setOf("token"))
        assertEquals(original, decrypted)
    }

    @Test
    fun `should handle invalid encrypted string during decryption`() {
        val encrypted = mapOf(
            "bad_data" to "not_base64!!",
            "normal" to cryptoService.encrypt("true")
        )

        val decrypted = cryptoService.decryptMap(encrypted)
        assertEquals("not_base64!!", decrypted["bad_data"])
        assertEquals(true, decrypted["normal"])
    }

    @Test
    fun `should convert numeric strings back to proper types`() {
        val original = mapOf(
            "int" to 42,
            "double" to 3.14,
            "boolean" to false
        )

        val encrypted = cryptoService.encryptMap(original)
        val decrypted = cryptoService.decryptMap(encrypted)

        assertEquals(42, decrypted["int"])
        assertEquals(3.14, decrypted["double"])
        assertEquals(false, decrypted["boolean"])
    }
}
