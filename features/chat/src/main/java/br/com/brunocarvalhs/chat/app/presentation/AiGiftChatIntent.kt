package br.com.brunocarvalhs.chat.app.presentation

internal sealed interface AiGiftChatIntent {
    data class UpdateInput(val text: String) : AiGiftChatIntent
    data object SendMessage : AiGiftChatIntent
}
