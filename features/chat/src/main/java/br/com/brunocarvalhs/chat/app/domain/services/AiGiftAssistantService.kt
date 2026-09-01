package br.com.brunocarvalhs.chat.app.domain.services

interface AiGiftAssistantService {
    fun startChat(groupName: String): AiChatSession
}

interface AiChatSession {
    suspend fun sendMessage(text: String): Result<String>
}
