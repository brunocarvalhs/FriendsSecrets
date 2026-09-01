package br.com.brunocarvalhs.chat.app.data.model

data class AiChatMessage(
    val text: String,
    val isFromUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)
