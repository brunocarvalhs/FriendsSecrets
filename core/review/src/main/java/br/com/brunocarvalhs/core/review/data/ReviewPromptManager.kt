package br.com.brunocarvalhs.core.review.data

import br.com.brunocarvalhs.core.review.domain.ReviewPromptService
import br.com.brunocarvalhs.storage.domain.StorageService
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ReviewPromptManager @Inject constructor(
    private val storageService: StorageService
) : ReviewPromptService {

    override suspend fun shouldPrompt(): Boolean {
        val lastPromptedAt = storageService.load(KEY_LAST_PROMPTED_AT, Long::class) ?: return true
        val elapsedDays = TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - lastPromptedAt)
        return elapsedDays >= MIN_INTERVAL_DAYS
    }

    override suspend fun recordPrompted() {
        storageService.save(KEY_LAST_PROMPTED_AT, System.currentTimeMillis())
    }

    private companion object {
        const val KEY_LAST_PROMPTED_AT = "review_prompt_last_shown_at"
        const val MIN_INTERVAL_DAYS = 30L
    }
}
