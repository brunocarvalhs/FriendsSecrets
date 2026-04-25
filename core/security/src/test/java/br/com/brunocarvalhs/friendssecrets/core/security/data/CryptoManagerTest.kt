package br.com.brunocarvalhs.friendssecrets.core.security.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CryptoManagerTest {

    private lateinit var cryptoManager: CryptoManager

    @Before
    fun setup() {
        cryptoManager = CryptoManager()
    }

    @Test
    fun `should encrypt and decrypt string correctly`() {
        val input = "hello world"

        val encrypted = cryptoManager.encrypt(input)
        val decrypted = cryptoManager.decrypt(encrypted)

        assertEquals(input, decrypted)
    }

    @Test
    fun `should return same value when decrypt fails`() {
        val invalid = "not_base64_valid_string"

        val result = cryptoManager.decrypt(invalid)

        assertEquals(invalid, result)
    }

    @Test
    fun `should encrypt map values except excluded keys`() {
        val input = mapOf(
            "name" to "Bruno",
            "age" to 30
        )

        val result = cryptoManager.encryptMap(
            input,
            excludedKeys = setOf("age")
        )

        assertEquals(30, result["age"])
        assertTrue(result["name"] is String)
        assertNotEquals("Bruno", result["name"])
    }

    @Test
    fun `should decrypt map values correctly`() {
        val original = "secret"

        val encrypted = cryptoManager.encrypt(original)

        val input = mapOf(
            "data" to encrypted
        )

        val result = cryptoManager.decryptMap(
            input,
            excludedKeys = emptySet()
        )

        assertEquals(original, result["data"])
    }

    @Test
    fun `should keep excluded keys untouched on decrypt`() {
        val input = mapOf(
            "token" to "do_not_touch"
        )

        val result = cryptoManager.decryptMap(
            input,
            excludedKeys = setOf("token")
        )

        assertEquals("do_not_touch", result["token"])
    }

    @Test
    fun `should encrypt and decrypt list inside map`() {
        val list = listOf("a", "b", "c")

        val input = mapOf(
            "list" to list
        )

        val encrypted = cryptoManager.encryptMap(input, emptySet())
        val decrypted = cryptoManager.decryptMap(encrypted, emptySet())

        val result = decrypted["list"] as List<*>

        assertEquals(list, result)
    }

    @Test
    fun `should handle null values in map`() {
        val input = mapOf(
            "a" to null,
            "b" to "value"
        )

        val result = cryptoManager.encryptMap(input, emptySet())

        assertFalse(result.containsKey("a"))
        assertTrue(result.containsKey("b"))
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun data(): Collection<Array<Any>> {
            return listOf(
                arrayOf("Bruno"),
                arrayOf("José da Silva"),
                arrayOf("Ana Maria"),
                arrayOf("Joãozinho 123"),
                arrayOf("💀 Bruno 💀"),
                arrayOf("🔥 Fire User 🔥"),
                arrayOf("C@rl0s_#1"),
                arrayOf("李小龙"),
                arrayOf("山田太郎"),
                arrayOf("👨‍👩‍👧 Família"),
                arrayOf("Élise"),
                arrayOf("François"),
                arrayOf("O'Connor"),
                arrayOf("NÃO TOQUE!"),
                arrayOf("user.name+test@gmail.com")
            )
        }
    }
}