package br.com.brunocarvalhs.friendssecrets.core.security.data

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class CryptoManagerNameTest(
    private val input: String
) {

    private lateinit var cryptoManager: CryptoManager

    @Before
    fun setup() {
        cryptoManager = CryptoManager()
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

    @Test
    fun `should encrypt and decrypt any contact name correctly`() {
        val encrypted = cryptoManager.encrypt(input)
        val decrypted = cryptoManager.decrypt(encrypted)

        assertEquals(input, decrypted)
    }
}