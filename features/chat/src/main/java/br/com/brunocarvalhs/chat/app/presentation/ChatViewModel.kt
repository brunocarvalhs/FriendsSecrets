package br.com.brunocarvalhs.chat.app.presentation

import AnalyticsParam
import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import br.com.brunocarvalhs.chat.app.data.model.ChatMessage
import br.com.brunocarvalhs.chat.app.domain.usecase.ClearMessagesUseCase
import br.com.brunocarvalhs.chat.app.domain.usecase.GetMessagesUseCase
import br.com.brunocarvalhs.chat.app.domain.usecase.IdentifyUserUseCase
import br.com.brunocarvalhs.chat.app.domain.usecase.SendMessageUseCase
import br.com.brunocarvalhs.chat.app.domain.usecase.ToggleMessageReactionUseCase
import br.com.brunocarvalhs.core.analytics.AnalyticsService
import br.com.brunocarvalhs.core.analytics.commons.AnalyticsEvent
import br.com.brunocarvalhs.core.domain.model.MessageModel
import br.com.brunocarvalhs.core.domain.model.MessageModel.MessageStatus
import br.com.brunocarvalhs.core.navigation.routers.ChatGraph
import br.com.brunocarvalhs.deviceid.DeviceService
import com.google.firebase.perf.metrics.AddTrace
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import javax.inject.Inject

@Stable
@HiltViewModel
@Suppress("LongParameterList")
internal class ChatViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getMessagesUseCase: GetMessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val clearMessagesUseCase: ClearMessagesUseCase,
    private val identifyUserUseCase: IdentifyUserUseCase,
    private val toggleMessageReactionUseCase: ToggleMessageReactionUseCase,
    private val deviceService: DeviceService,
    private val analyticsService: AnalyticsService
) : ViewModel() {
    private val args = savedStateHandle.toRoute<ChatGraph>(ChatGraph.typeMap)
    private val _uiState = MutableStateFlow(
        ChatUiState(
            groupModel = args.group
        )
    )
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    private var deviceId: String = ""

    init {
        initializer()
    }

    @AddTrace(name = "ChatViewModel.initializer", enabled = true)
    private fun initializer() {
        analyticsService.logEvent(
            name = AnalyticsEvent.VIEW,
            params = mapOf(
                AnalyticsParam.ACTION to "initializer"
            )
        )
        analyticsService.logEvent(name = AnalyticsEvent.CHAT_OPENED)
        viewModelScope.launch {
            deviceId = deviceService.getDeviceId()

            checkAndClearExpiredChat()

            val cachedName = identifyUserUseCase.getNickname()
            val member =
                _uiState.value.groupModel.members.find { it.phoneNumber == deviceId || it.id == deviceId }
            val finalName = cachedName ?: member?.name ?: ""

            _uiState.update { it.copy(currentUserNickname = finalName) }

            if (finalName.isBlank()) {
                _uiState.update { it.copy(showIdentificationModal = true) }
            }

            observeMessages()
        }
    }

    @AddTrace(name = "ChatViewModel.checkAndClearExpiredChat", enabled = true)
    private suspend fun checkAndClearExpiredChat() {
        analyticsService.logEvent(
            name = AnalyticsEvent.VIEW,
            params = mapOf(
                AnalyticsParam.ACTION to "check_and_clear_expired_chat"
            )
        )
        val groupDateString = _uiState.value.groupModel.date ?: return
        runCatching {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val groupDate = sdf.parse(groupDateString) ?: return
            val currentDate = Date()

            if (currentDate.after(groupDate)) {
                Timber.d("Chat expirado para o grupo ${_uiState.value.groupModel.id}. Limpando...")
                clearMessagesUseCase(_uiState.value.groupModel.id)
            }
        }.onFailure { Timber.e(it) }
    }

    @AddTrace(name = "ChatViewModel.handleIntent", enabled = true)
    fun handleIntent(intent: ChatIntent) {
        when (intent) {
            is ChatIntent.UpdateInput -> updateInput(intent.text)
            is ChatIntent.SendMessage -> sendMessage()
            is ChatIntent.LoadMessages -> observeMessages()
            is ChatIntent.IdentifyUser -> identifyUser(intent.name)
            is ChatIntent.DismissIdentification -> _uiState.update { it.copy(showIdentificationModal = false) }
            is ChatIntent.ClearChat -> {}
            is ChatIntent.ToggleReaction -> toggleReaction(intent.messageId, intent.emoji)
        }
    }

    @AddTrace(name = "ChatViewModel.toggleReaction", enabled = true)
    private fun toggleReaction(messageId: String, emoji: String) {
        analyticsService.logEvent(
            name = AnalyticsEvent.SUBMIT,
            params = mapOf(
                AnalyticsParam.ACTION to "toggle_message_reaction",
                AnalyticsParam.PARAM to emoji
            )
        )
        val message = _uiState.value.messages.find { it.id == messageId } ?: return
        viewModelScope.launch {
            toggleMessageReactionUseCase(
                groupId = _uiState.value.groupModel.id,
                messageId = messageId,
                deviceId = deviceId,
                currentReactions = message.reactions,
                emoji = emoji
            )
        }
    }

    @AddTrace(name = "ChatViewModel.updateInput", enabled = true)
    private fun updateInput(text: String) {
        analyticsService.logEvent(
            name = AnalyticsEvent.SUBMIT,
            params = mapOf(
                AnalyticsParam.ACTION to "update_input",
                AnalyticsParam.PARAM to text
            )
        )
        _uiState.update { it.copy(inputText = text) }
    }

    @AddTrace(name = "ChatViewModel.identifyUser", enabled = true)
    private fun identifyUser(name: String) {
        analyticsService.logEvent(
            name = AnalyticsEvent.SUBMIT,
            params = mapOf(
                AnalyticsParam.ACTION to "identify_user",
                AnalyticsParam.PARAM to name
            )
        )
        if (name.isBlank()) return
        viewModelScope.launch {
            identifyUserUseCase.saveNickname(name)
            _uiState.update { it.copy(currentUserNickname = name, showIdentificationModal = false) }

            val joinMessage = MessageModel(
                id = UUID.randomUUID().toString(),
                groupId = _uiState.value.groupModel.id,
                text = "$name acessou o chat",
                senderId = "system",
                senderName = "Sistema",
                timestamp = System.currentTimeMillis(),
                status = MessageStatus.SENT
            )
            sendMessageUseCase(_uiState.value.groupModel.id, joinMessage)
        }
    }

    @AddTrace(name = "ChatViewModel.sendMessage", enabled = true)
    private fun sendMessage() {
        analyticsService.logEvent(
            name = AnalyticsEvent.SUBMIT,
            params = mapOf(
                AnalyticsParam.ACTION to "send_message"
            )
        )
        val messageText = _uiState.value.inputText
        if (messageText.isBlank()) return

        if (_uiState.value.currentUserNickname.isBlank()) {
            _uiState.update { it.copy(showIdentificationModal = true) }
            return
        }

        val tempId = UUID.randomUUID().toString()
        val newMessage = MessageModel(
            id = tempId,
            groupId = _uiState.value.groupModel.id,
            text = messageText,
            senderId = deviceId,
            senderName = _uiState.value.currentUserNickname,
            timestamp = System.currentTimeMillis(),
            status = MessageStatus.SENDING
        )

        _uiState.update { state ->
            state.copy(
                messages = state.messages + newMessage.toChatMessage(deviceId),
                inputText = ""
            )
        }

        viewModelScope.launch {
            val result = sendMessageUseCase(_uiState.value.groupModel.id, newMessage)

            if (result.isSuccess) {
                analyticsService.logEvent(name = AnalyticsEvent.CHAT_MESSAGE_SENT)
            } else {
                _uiState.update { state ->
                    state.copy(
                        messages = state.messages.map {
                            if (it.id == tempId) it.copy(status = MessageStatus.ERROR) else it
                        }
                    )
                }
            }
        }
    }

    @AddTrace(name = "ChatViewModel.observeMessages", enabled = true)
    private fun observeMessages() {
        analyticsService.logEvent(
            name = AnalyticsEvent.VIEW,
            params = mapOf(
                AnalyticsParam.ACTION to "observe_messages"
            )
        )
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            getMessagesUseCase(_uiState.value.groupModel.id)
                .onEach { messages ->
                    _uiState.update { state ->
                        val remoteIds = messages.map { it.id }.toSet()
                        val pendingMessages = state.messages.filter {
                            it.status == MessageStatus.SENDING && it.id !in remoteIds
                        }

                        state.copy(
                            messages = (messages.map {
                                it.toChatMessage(deviceId)
                            } + pendingMessages)
                                .sortedBy { it.timestamp },
                            isLoading = false
                        )
                    }
                }.launchIn(viewModelScope)
        }
    }

    @AddTrace(name = "ChatViewModel.MessageModel.toChatMessage", enabled = true)
    private fun MessageModel.toChatMessage(currentDeviceId: String): ChatMessage {
        analyticsService.logEvent(
            name = AnalyticsEvent.VIEW,
            params = mapOf(
                AnalyticsParam.ACTION to "to_chat_message",
                AnalyticsParam.PARAM to this.toString()
            )
        )
        val groupMembers = _uiState.value.groupModel.members
        val member = groupMembers.find { it.id == senderId || it.phoneNumber == senderId }

        val displayName = when {
            senderId == currentDeviceId -> "Você"
            senderId == "system" -> "Sistema"
            member != null -> member.name
            else -> senderName.ifBlank { "Membro" }
        }

        return ChatMessage(
            id = id,
            groupId = groupId,
            text = text,
            isFromMe = senderId == currentDeviceId,
            senderId = senderId,
            senderName = displayName,
            timestamp = timestamp,
            status = status,
            reactions = reactions
        )
    }
}
