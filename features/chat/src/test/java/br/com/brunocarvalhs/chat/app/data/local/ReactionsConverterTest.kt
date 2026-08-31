package br.com.brunocarvalhs.chat.app.data.local

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ReactionsConverterTest {

    private lateinit var converter: ReactionsConverter

    @Before
    fun setup() {
        converter = ReactionsConverter()
    }

    @Test
    fun `fromReactions and toReactions should round trip`() {
        // Given
        val reactions = mapOf("device-1" to "👍", "device-2" to "❤️")

        // When
        val serialized = converter.fromReactions(reactions)
        val deserialized = converter.toReactions(serialized)

        // Then
        assertEquals(reactions, deserialized)
    }

    @Test
    fun `toReactions should return empty map for invalid input`() {
        // When
        val result = converter.toReactions("not-json")

        // Then
        assertTrue(result.isEmpty())
    }
}
