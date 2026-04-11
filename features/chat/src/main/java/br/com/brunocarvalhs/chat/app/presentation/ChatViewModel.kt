package br.com.brunocarvalhs.chat.app.presentation

import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import br.com.brunocarvalhs.chat.app.data.model.ChatMessage
import br.com.brunocarvalhs.chat.commons.navigation.ChatGraphRouter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@Stable
@HiltViewModel
class ChatViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val args = savedStateHandle.toRoute<ChatGraphRouter>(ChatGraphRouter.typeMap)
    private val _uiState = MutableStateFlow(ChatUiState(
        groupModel = args.group
    ))
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    fun handleIntent(intent: ChatIntent) {
        when (intent) {
            is ChatIntent.UpdateInput -> updateInput(intent.text)
            is ChatIntent.SendMessage -> sendMessage()
            is ChatIntent.LoadMessages -> loadInitialMessages()
            is ChatIntent.ClearChat -> _uiState.update {
                it.copy(messages = emptyList())
            }
        }
    }

    private fun updateInput(text: String) {
        _uiState.update { it.copy(inputText = text) }
    }

    private fun sendMessage() {
        val messageText = _uiState.value.inputText
        if (messageText.isBlank()) return

        val newMessage = ChatMessage(
            text = messageText,
            isFromMe = true,
            senderName = _uiState.value.currentUserNickname
        )

        _uiState.update { state ->
            state.copy(
                messages = state.messages + newMessage,
                inputText = ""
            )
        }

        simulateResponse()
    }

    private fun loadInitialMessages() {
        _uiState.update { it.copy(isLoading = true) }
        _uiState.update { it.copy(isLoading = false) }
    }

    private fun simulateResponse() {
        val response = ChatMessage(
            text = "Esta é uma resposta automática do segredo! 🤫",
            isFromMe = false,
            senderName = "Amigo Secreto"
        )
        _uiState.update { it.copy(messages = it.messages + response) }
    }
}