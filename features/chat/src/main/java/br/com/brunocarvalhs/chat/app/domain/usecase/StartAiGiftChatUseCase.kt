package br.com.brunocarvalhs.chat.app.domain.usecase

import br.com.brunocarvalhs.chat.app.domain.services.AiChatSession
import br.com.brunocarvalhs.chat.app.domain.services.AiGiftAssistantService
import javax.inject.Inject

class StartAiGiftChatUseCase @Inject constructor(
    private val service: AiGiftAssistantService
) {
    operator fun invoke(groupName: String): AiChatSession = service.startChat(groupName)
}
