package br.com.brunocarvalhs.group.details.app.domain.useCases

import br.com.brunocarvalhs.group.details.app.domain.model.GiftSuggestion
import br.com.brunocarvalhs.group.details.app.domain.repository.GiftSuggestionRepository
import javax.inject.Inject

internal class SuggestGiftsUseCase @Inject constructor(
    private val repository: GiftSuggestionRepository,
) {
    suspend operator fun invoke(
        interests: List<String>,
        minPrice: Double?,
        maxPrice: Double?,
        giftType: String?,
    ): Result<List<GiftSuggestion>> {
        val sanitizedInterests = interests.map { it.trim() }.filter { it.isNotBlank() }
        if (sanitizedInterests.isEmpty()) {
            return Result.failure(IllegalArgumentException("No wishlist items to suggest gifts from"))
        }
        return repository.suggest(sanitizedInterests, minPrice, maxPrice, giftType)
    }
}
