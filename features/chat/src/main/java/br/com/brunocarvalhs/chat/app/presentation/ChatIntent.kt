package br.com.brunocarvalhs.chat.app.presentation

sealed class ChatIntent {
    data class UpdateInput(val text: String) : ChatIntent()
    object SendMessage : ChatIntent()
    object LoadMessages : ChatIntent()
    object ClearChat : ChatIntent()
}