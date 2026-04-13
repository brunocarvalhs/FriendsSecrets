package br.com.brunocarvalhs.chat.app.presentation

import br.com.brunocarvalhs.chat.app.data.model.ChatMessage
import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel

data class ChatUiState(
    val groupModel: GroupModel,
    val messages: List<ChatMessage> = emptyList(),
    val inputText: String = "", 
    val isLoading: Boolean = false,
    val chatTitle: String = "Chat Secreto",
    val currentUserNickname: String = "",
    val showIdentificationModal: Boolean = false
)