package br.com.brunocarvalhs.group.details.app.data.repository

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GiftSuggestionRepositoryImplTest {

    @Test
    fun `parseSuggestions should map a well-formed callable response`() {
        // Given
        val data = mapOf(
            "suggestions" to listOf(
                mapOf("title" to "Livro de receitas", "reason" to "Combina com culinária"),
                mapOf("title" to "Fone de ouvido", "reason" to "Combina com música")
            )
        )

        // When
        val suggestions = GiftSuggestionRepositoryImpl.parseSuggestions(data)

        // Then
        assertEquals(2, suggestions.size)
        assertEquals("Livro de receitas", suggestions[0].title)
        assertEquals("Combina com música", suggestions[1].reason)
    }

    @Test
    fun `parseSuggestions should skip entries missing title or reason`() {
        // Given
        val data = mapOf(
            "suggestions" to listOf(
                mapOf("title" to "Livro"),
                mapOf("title" to "Fone", "reason" to "Combina com música")
            )
        )

        // When
        val suggestions = GiftSuggestionRepositoryImpl.parseSuggestions(data)

        // Then
        assertEquals(1, suggestions.size)
        assertEquals("Fone", suggestions[0].title)
    }

    @Test
    fun `parseSuggestions should return empty list when the suggestions field is missing`() {
        // Given
        val data = mapOf("unexpected" to "shape")

        // When
        val suggestions = GiftSuggestionRepositoryImpl.parseSuggestions(data)

        // Then
        assertTrue(suggestions.isEmpty())
    }

    @Test
    fun `parseSuggestions should return empty list when data is not a map`() {
        // When
        val suggestions = GiftSuggestionRepositoryImpl.parseSuggestions("unexpected")

        // Then
        assertTrue(suggestions.isEmpty())
    }
}
