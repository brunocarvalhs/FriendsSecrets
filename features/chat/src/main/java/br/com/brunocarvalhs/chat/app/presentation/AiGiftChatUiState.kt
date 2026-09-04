package br.com.brunocarvalhs.chat.app.presentation

import br.com.brunocarvalhs.chat.app.data.model.AiChatMessage

internal data class AiGiftChatUiState(
    val groupName: String = "",
    val messages: List<AiChatMessage> = emptyList(),
    val inputText: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)
