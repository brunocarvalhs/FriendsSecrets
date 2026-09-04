package br.com.brunocarvalhs.chat.app.presentation

import AnalyticsParam
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import br.com.brunocarvalhs.chat.app.data.model.AiChatMessage
import br.com.brunocarvalhs.chat.app.domain.services.AiChatSession
import br.com.brunocarvalhs.chat.app.domain.usecase.StartAiGiftChatUseCase
import br.com.brunocarvalhs.core.analytics.AnalyticsService
import br.com.brunocarvalhs.core.analytics.commons.AnalyticsEvent
import br.com.brunocarvalhs.core.domain.model.UserModel
import br.com.brunocarvalhs.core.navigation.routers.AiGiftChatGraph
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class AiGiftChatViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    startAiGiftChatUseCase: StartAiGiftChatUseCase,
    private val analyticsService: AnalyticsService
) : ViewModel() {
    private val args = savedStateHandle.toRoute<AiGiftChatGraph>(AiGiftChatGraph.typeMap)
    private val session: AiChatSession = startAiGiftChatUseCase(
        args.group.name,
        buildMembersContext(args.group.members)
    )

    private val _uiState = MutableStateFlow(
        AiGiftChatUiState(
            groupName = args.group.name,
            messages = listOf(
                AiChatMessage(
                    text = INITIAL_GREETING,
                    isFromUser = false
                )
            )
        )
    )
    val uiState: StateFlow<AiGiftChatUiState> = _uiState.asStateFlow()

    init {
        analyticsService.logEvent(
            name = AnalyticsEvent.VIEW,
            params = mapOf(AnalyticsParam.ACTION to "ai_gift_chat_opened")
        )
    }

    fun handleIntent(intent: AiGiftChatIntent) {
        when (intent) {
            is AiGiftChatIntent.UpdateInput -> _uiState.update { it.copy(inputText = intent.text) }
            is AiGiftChatIntent.SendMessage -> sendMessage()
            is AiGiftChatIntent.DismissError -> _uiState.update { it.copy(error = null) }
        }
    }

    private fun sendMessage() {
        val text = _uiState.value.inputText.trim()
        if (text.isBlank() || _uiState.value.isLoading) return

        analyticsService.logEvent(
            name = AnalyticsEvent.SUBMIT,
            params = mapOf(AnalyticsParam.ACTION to "ai_gift_chat_send_message")
        )

        _uiState.update {
            it.copy(
                messages = it.messages + AiChatMessage(text = text, isFromUser = true),
                inputText = "",
                isLoading = true,
                error = null
            )
        }

        viewModelScope.launch {
            session.sendMessage(text)
                .onSuccess { reply ->
                    _uiState.update {
                        it.copy(
                            messages = it.messages + AiChatMessage(text = reply, isFromUser = false),
                            isLoading = false
                        )
                    }
                }
                .onFailure { error ->
                    Timber.e(error, "Error sending message to AI gift assistant")
                    _uiState.update {
                        it.copy(isLoading = false, error = "Não foi possível falar com a IA agora")
                    }
                }
        }
    }

    private fun buildMembersContext(members: List<UserModel>): String {
        if (members.isEmpty()) return NO_MEMBERS_CONTEXT

        return members.joinToString(separator = "\n") { member ->
            val likes = member.likes.filter { it.isNotBlank() }
            if (likes.isEmpty()) {
                "- ${member.name}: sem gostos registrados ainda"
            } else {
                "- ${member.name}: gosta de ${likes.joinToString(", ")}"
            }
        }
    }

    private companion object {
        const val INITIAL_GREETING =
            "Oi! 👋 Me conta um pouco sobre a pessoa que você quer presentear: " +
                "hobbies, gostos, uma faixa de preço... e eu te ajudo com ideias de presente!"
        const val NO_MEMBERS_CONTEXT = "Nenhum membro com gostos registrados ainda."
    }
}
