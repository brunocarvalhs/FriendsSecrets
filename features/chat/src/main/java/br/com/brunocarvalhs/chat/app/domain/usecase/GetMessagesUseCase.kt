package br.com.brunocarvalhs.chat.app.domain.usecase

import br.com.brunocarvalhs.chat.app.domain.repository.ChatRepository
import br.com.brunocarvalhs.core.domain.model.MessageModel
import com.google.firebase.perf.metrics.AddTrace
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMessagesUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    @AddTrace(name = "GetMessagesUseCase.invoke", enabled = true)
    suspend operator fun invoke(groupId: String): Flow<List<MessageModel>> {
        return repository.getMessages(groupId)
    }
}
