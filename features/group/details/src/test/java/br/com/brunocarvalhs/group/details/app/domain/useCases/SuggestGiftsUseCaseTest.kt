package br.com.brunocarvalhs.group.details.app.domain.useCases

import br.com.brunocarvalhs.group.details.app.domain.model.GiftSuggestion
import br.com.brunocarvalhs.group.details.app.domain.repository.GiftSuggestionRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SuggestGiftsUseCaseTest {

    private val repository: GiftSuggestionRepository = mockk()
    private lateinit var useCase: SuggestGiftsUseCase

    @Before
    fun setup() {
        useCase = SuggestGiftsUseCase(repository)
    }

    @Test
    fun `invoke should trim blank interests before calling the repository`() = runTest {
        // Given
        val suggestions = listOf(GiftSuggestion(title = "Livro", reason = "Combina com leitura"))
        coEvery {
            repository.suggest(listOf("Livros", "Games"), 50.0, 100.0, "Qualquer coisa")
        } returns Result.success(suggestions)

        // When
        val result = useCase(
            interests = listOf(" Livros ", "", "Games", "   "),
            minPrice = 50.0,
            maxPrice = 100.0,
            giftType = "Qualquer coisa"
        )

        // Then
        assertTrue(result.isSuccess)
        assertTrue(result.getOrThrow() == suggestions)
        coVerify { repository.suggest(listOf("Livros", "Games"), 50.0, 100.0, "Qualquer coisa") }
    }

    @Test
    fun `invoke should fail without calling the repository when there are no interests`() = runTest {
        // When
        val result = useCase(interests = listOf("", "   "), minPrice = null, maxPrice = null, giftType = null)

        // Then
        assertTrue(result.isFailure)
        coVerify(inverse = true) { repository.suggest(any(), any(), any(), any()) }
    }
}
