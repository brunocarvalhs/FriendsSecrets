package br.com.brunocarvalhs.friendssecrets.presentation.views.generative.chat

sealed interface ChatGenerativeUiState {
    data class Chat(
        val messages: List<Pair<ChatGenerativeType, String>>,
    ) : ChatGenerativeUiState
}