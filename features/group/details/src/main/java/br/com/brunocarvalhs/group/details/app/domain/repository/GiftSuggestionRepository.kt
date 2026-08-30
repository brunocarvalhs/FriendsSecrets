package br.com.brunocarvalhs.group.details.app.domain.repository

import br.com.brunocarvalhs.group.details.app.domain.model.GiftSuggestion

internal interface GiftSuggestionRepository {
    suspend fun suggest(
        interests: List<String>,
        minPrice: Double?,
        maxPrice: Double?,
        giftType: String?,
    ): Result<List<GiftSuggestion>>
}
