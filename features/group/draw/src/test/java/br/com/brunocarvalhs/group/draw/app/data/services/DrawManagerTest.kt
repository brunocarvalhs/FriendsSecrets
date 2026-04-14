package br.com.brunocarvalhs.group.draw.app.data.services

import br.com.brunocarvalhs.friendssecrets.domain.services.CryptoService
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class DrawManagerTest {

    private val crypto: CryptoService = mockk()
    private lateinit var drawManager: DrawManager

    @Before
    fun setup() {
        drawManager = DrawManager(crypto)
        // Mock simple encryption (return original string for easy testing)
        every { crypto.encrypt(any()) } answers { it.invocation.args[0] as String }
    }

    @Test
    fun `draw should assign each person to a different secret santa`() {
        // Given
        val participants = mutableListOf("Bruno", "Alice", "Bob")

        // When
        val result = drawManager.draw(participants)

        // Then
        assertEquals(3, result.size)
        participants.forEach { participant ->
            assertTrue(result.containsKey(participant))
            assertNotEquals(participant, result[participant]) // No one should draw themselves
        }
    }

    @Test(expected = IllegalArgumentException::class)
    fun `draw should throw exception when participants less than 3`() {
        drawManager.draw(mutableListOf("Bruno", "Alice"))
    }
}
