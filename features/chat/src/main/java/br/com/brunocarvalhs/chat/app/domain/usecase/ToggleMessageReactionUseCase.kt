package br.com.brunocarvalhs.chat.app.domain.usecase

import br.com.brunocarvalhs.chat.app.domain.repository.ChatRepository
import com.google.firebase.perf.metrics.AddTrace
import javax.inject.Inject

class ToggleMessageReactionUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    @AddTrace(name = "ToggleMessageReactionUseCase.invoke", enabled = true)
    suspend operator fun invoke(
        groupId: String,
        messageId: String,
        deviceId: String,
        currentReactions: Map<String, String>,
        emoji: String
    ): Result<Unit> {
        val newValue = if (currentReactions[deviceId] == emoji) null else emoji
        return repository.setReaction(groupId, messageId, deviceId, newValue)
    }
}
