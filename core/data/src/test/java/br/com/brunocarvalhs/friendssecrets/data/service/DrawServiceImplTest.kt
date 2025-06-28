package br.com.brunocarvalhs.friendssecrets.data.service

import br.com.brunocarvalhs.friendssecrets.domain.exceptions.MinimumsMembersOfDrawException
import br.com.brunocarvalhs.friendssecrets.domain.services.CryptoService
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

class DrawServiceImplTest {
    private lateinit var cryptoService: CryptoService
    private lateinit var service: DrawServiceImpl

    @Before
    fun setUp() {
        cryptoService = mockk()
        service = DrawServiceImpl(cryptoService)
    }

    @Test
    fun `drawMembers throws exception when less than 3 participants`() {
        val participants = mutableListOf("A", "B")
        assertThrows(MinimumsMembersOfDrawException::class.java) {
            service.drawMembers(participants)
        }
    }

    @Test
    fun `drawMembers returns map with encrypted values`() {
        val participants = mutableListOf("A", "B", "C")
        every { cryptoService.encrypt(any()) } answers { "enc_${firstArg<String>()}" }
        val result = service.drawMembers(participants)
        assertEquals(3, result.size)
        result.forEach { (_, value) ->
            assert(value.startsWith("enc_"))
        }
    }

    @Test
    fun `revelation returns decrypted value`() {
        every { cryptoService.decrypt("enc_X") } returns "X"
        val result = service.revelation("enc_X")
        assertEquals("X", result)
    }
}

