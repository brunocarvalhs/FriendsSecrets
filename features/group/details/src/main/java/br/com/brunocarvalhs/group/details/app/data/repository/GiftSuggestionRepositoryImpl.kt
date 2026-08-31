package br.com.brunocarvalhs.group.details.app.data.repository

import br.com.brunocarvalhs.group.details.app.domain.model.GiftSuggestion
import br.com.brunocarvalhs.group.details.app.domain.repository.GiftSuggestionRepository
import com.google.firebase.functions.FirebaseFunctions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

internal class GiftSuggestionRepositoryImpl @Inject constructor(
    private val functions: FirebaseFunctions,
) : GiftSuggestionRepository {

    override suspend fun suggest(
        interests: List<String>,
        minPrice: Double?,
        maxPrice: Double?,
        giftType: String?,
    ): Result<List<GiftSuggestion>> = runCatching {
        val payload = mutableMapOf<String, Any?>("interests" to interests)
        minPrice?.let { payload["minPrice"] = it }
        maxPrice?.let { payload["maxPrice"] = it }
        giftType?.let { payload["giftType"] = it }

        val result = functions
            .getHttpsCallable(CALLABLE_NAME)
            .call(payload)
            .await()

        parseSuggestions(result.data)
    }

    internal companion object {
        const val CALLABLE_NAME = "suggestGifts"

        internal fun parseSuggestions(data: Any?): List<GiftSuggestion> {
            val suggestions = (data as? Map<*, *>)?.get("suggestions") as? List<*>
                ?: return emptyList()

            return suggestions.mapNotNull { entry ->
                val fields = entry as? Map<*, *> ?: return@mapNotNull null
                val title = fields["title"] as? String ?: return@mapNotNull null
                val reason = fields["reason"] as? String ?: return@mapNotNull null
                GiftSuggestion(title = title, reason = reason)
            }
        }
    }
}
