package br.com.brunocarvalhs.friendssecrets.presentation.views.generative.chat

sealed interface ChatGenerativeIntent {
    data class SendMessage(val message: String) : ChatGenerativeIntent
}