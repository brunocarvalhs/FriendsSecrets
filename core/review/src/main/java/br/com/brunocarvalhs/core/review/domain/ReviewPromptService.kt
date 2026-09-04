package br.com.brunocarvalhs.core.review.domain

interface ReviewPromptService {
    /**
     * Whether a review prompt should be shown now, based on how long it has
     * been since the last time one was shown. Callers are expected to only
     * check this right after a positive, natural moment in the app (e.g. a
     * completed draw), never on a timer or on every screen.
     */
    suspend fun shouldPrompt(): Boolean

    /** Records that a review prompt was just shown, resetting the cooldown. */
    suspend fun recordPrompted()
}
