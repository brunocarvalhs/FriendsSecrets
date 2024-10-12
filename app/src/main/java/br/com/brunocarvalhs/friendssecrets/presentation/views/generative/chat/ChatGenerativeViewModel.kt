package br.com.brunocarvalhs.friendssecrets.presentation.views.generative.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import br.com.brunocarvalhs.friendssecrets.data.service.GenerativeService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatGenerativeViewModel(
    private val service: GenerativeService = GenerativeService()
) : ViewModel() {

    private val _uiState: MutableStateFlow<ChatGenerativeUiState> =
        MutableStateFlow(ChatGenerativeUiState.Chat(emptyList())) // Estado inicial com lista vazia de mensagens

    val uiState: StateFlow<ChatGenerativeUiState> =
        _uiState.asStateFlow()

    private fun sendMessageHuman(message: String) {
        _uiState.value = addMessageToChat(ChatGenerativeType.HUMAN, message)
    }

    private fun sendMessageAI(message: String) {
        _uiState.value = addMessageToChat(ChatGenerativeType.AI, message)
    }

    private fun sendMessage(message: String) {
        viewModelScope.launch {
            // Adiciona mensagem do humano
            sendMessageHuman(message)

            // Obtém a resposta da IA
            val response = service.invoke(message).orEmpty()

            // Adiciona mensagem da IA
            sendMessageAI(response)
        }
    }

    // Função que adiciona a nova mensagem à lista de mensagens no estado
    private fun addMessageToChat(type: ChatGenerativeType, message: String): ChatGenerativeUiState.Chat {
        val currentState = _uiState.value
        if (currentState is ChatGenerativeUiState.Chat) {
            val updatedMessages = currentState.messages + (type to message) // Adiciona a nova mensagem à lista
            return ChatGenerativeUiState.Chat(updatedMessages)
        }
        return ChatGenerativeUiState.Chat(listOf(type to message))
    }

    // Processa os eventos
    fun eventIntent(event: ChatGenerativeIntent) {
        when (event) {
            is ChatGenerativeIntent.SendMessage -> sendMessage(event.message)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                ChatGenerativeViewModel()
            }
        }
    }
}
