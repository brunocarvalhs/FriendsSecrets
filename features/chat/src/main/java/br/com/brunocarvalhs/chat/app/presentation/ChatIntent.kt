package br.com.brunocarvalhs.chat.app.presentation

internal sealed class ChatIntent {
    data class UpdateInput(val text: String) : ChatIntent()
    object SendMessage : ChatIntent()
    object LoadMessages : ChatIntent()
    object ClearChat : ChatIntent()
    data class IdentifyUser(val name: String) : ChatIntent()
    object DismissIdentification : ChatIntent()
    data class ToggleReaction(val messageId: String, val emoji: String) : ChatIntent()
}
