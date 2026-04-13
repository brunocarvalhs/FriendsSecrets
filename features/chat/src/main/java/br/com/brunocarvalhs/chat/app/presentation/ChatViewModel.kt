package br.com.brunocarvalhs.chat.app.presentation

import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import br.com.brunocarvalhs.chat.app.data.model.ChatMessage
import br.com.brunocarvalhs.chat.app.domain.repository.ChatRepository
import br.com.brunocarvalhs.chat.commons.navigation.ChatGraphRouter
import br.com.brunocarvalhs.friendssecrets.domain.services.DeviceService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@Stable
@HiltViewModel
class ChatViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: ChatRepository,
    private val deviceService: DeviceService
) : ViewModel() {
    private val args = savedStateHandle.toRoute<ChatGraphRouter>(ChatGraphRouter.typeMap)
    private val _uiState = MutableStateFlow(ChatUiState(
        groupModel = args.group
    ))
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    private var deviceId: String = ""

    init {
        viewModelScope.launch {
            deviceId = deviceService.getDeviceId()
            loadMessages()
        }
    }

    fun handleIntent(intent: ChatIntent) {
        when (intent) {
            is ChatIntent.UpdateInput -> updateInput(intent.text)
            is ChatIntent.SendMessage -> sendMessage()
            is ChatIntent.LoadMessages -> loadMessages()
            is ChatIntent.ClearChat -> {
                // Clear chat locally or in repo if needed
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
            groupId = _uiState.value.groupModel.id,
            text = messageText,
            isFromMe = true,
            senderId = deviceId,
            senderName = _uiState.value.currentUserNickname,
            timestamp = System.currentTimeMillis()
        )

        viewModelScope.launch {
            _uiState.update { it.copy(inputText = "") }
            repository.sendMessage(_uiState.value.groupModel.id, newMessage)
        }
    }

    private fun loadMessages() {
        repository.getMessages(_uiState.value.groupModel.id)
            .onEach { messages ->
                _uiState.update { state ->
                    state.copy(
                        messages = messages.map { it.copy(isFromMe = it.senderId == deviceId) },
                        isLoading = false
                    )
                }
            }
            .launchIn(viewModelScope)
    }
}
