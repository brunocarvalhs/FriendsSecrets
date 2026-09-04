package br.com.brunocarvalhs.group.details.app.presentation

import AnalyticsParam
import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import br.com.brunocarvalhs.core.analytics.AnalyticsService
import br.com.brunocarvalhs.core.analytics.commons.AnalyticsEvent
import br.com.brunocarvalhs.core.navigation.routers.GroupDetailsGraph
import br.com.brunocarvalhs.deviceid.DeviceService
import br.com.brunocarvalhs.group.details.app.domain.useCases.AddMemberLikeUseCase
import br.com.brunocarvalhs.group.details.app.domain.useCases.GroupDeleteUseCase
import br.com.brunocarvalhs.group.details.app.domain.useCases.GroupExitUseCase
import br.com.brunocarvalhs.group.details.app.domain.useCases.GroupReadUseCase
import br.com.brunocarvalhs.group.details.app.domain.useCases.GroupShareUseCase
import br.com.brunocarvalhs.group.details.app.domain.useCases.IsGroupReminderEnabledUseCase
import br.com.brunocarvalhs.group.details.app.domain.useCases.RemoveMemberUseCase
import br.com.brunocarvalhs.group.details.app.domain.useCases.ShareGroupInviteCardUseCase
import br.com.brunocarvalhs.group.details.app.domain.useCases.ShareGroupQrCodeUseCase
import br.com.brunocarvalhs.group.details.app.domain.useCases.ShareWishlistUseCase
import br.com.brunocarvalhs.group.details.app.domain.useCases.ToggleGroupReminderUseCase
import br.com.brunocarvalhs.group.details.app.domain.useCases.UpdateMemberLikesUseCase
import com.google.firebase.perf.metrics.AddTrace
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@Stable
@HiltViewModel
@Suppress("TooManyFunctions")
internal class GroupDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val readUseCase: GroupReadUseCase,
    private val deleteUseCase: GroupDeleteUseCase,
    private val exitUseCase: GroupExitUseCase,
    private val shareUseCase: GroupShareUseCase,
    private val shareInviteCardUseCase: ShareGroupInviteCardUseCase,
    private val shareQrCodeUseCase: ShareGroupQrCodeUseCase,
    private val toggleReminderUseCase: ToggleGroupReminderUseCase,
    private val isReminderEnabledUseCase: IsGroupReminderEnabledUseCase,
    private val removeMemberUseCase: RemoveMemberUseCase,
    private val shareWishlistUseCase: ShareWishlistUseCase,
    private val updateMemberLikesUseCase: UpdateMemberLikesUseCase,
    private val addMemberLikeUseCase: AddMemberLikeUseCase,
    private val deviceService: DeviceService,
    private val analyticsService: AnalyticsService
) : ViewModel() {
    private val args = savedStateHandle.toRoute<GroupDetailsGraph>(GroupDetailsGraph.typeMap)
    private val _uiState = MutableStateFlow(GroupDetailsUiState(group = args.group))
    val uiState: StateFlow<GroupDetailsUiState> = _uiState.asStateFlow()

    init {
        loadReminderState()
        loadCurrentDeviceId()
    }

    @AddTrace(name = "GroupDetailsViewModel.handleIntent", enabled = true)
    fun handleIntent(intent: GroupDetailsIntent) = when (intent) {
        is GroupDetailsIntent.Refresh -> readGroup()
        is GroupDetailsIntent.Delete -> deleteGroup(intent.callback)
        is GroupDetailsIntent.Exit -> exitGroup(intent.callback)
        GroupDetailsIntent.Share -> shareGroup()
        GroupDetailsIntent.ShareInviteCard -> shareInviteCard()
        GroupDetailsIntent.ShareQr -> shareQrCode()
        is GroupDetailsIntent.ToggleReminder -> toggleReminder(intent.enabled)
        is GroupDetailsIntent.RemoveMember -> removeMember(intent.memberId)
        GroupDetailsIntent.ShareWishlist -> shareWishlist()
        is GroupDetailsIntent.UpdateLikes -> updateLikes(intent.likes)
        is GroupDetailsIntent.AddLike -> addLike(intent.memberId, intent.like)
        is GroupDetailsIntent.DismissError -> _uiState.update { it.copy(error = null) }
    }

    private fun loadReminderState() {
        viewModelScope.launch {
            val enabled = isReminderEnabledUseCase(_uiState.value.group.id)
            _uiState.update { it.copy(isReminderEnabled = enabled) }
        }
    }

    @AddTrace(name = "GroupDetailsViewModel.toggleReminder", enabled = true)
    private fun toggleReminder(enabled: Boolean) {
        analyticsService.logEvent(
            name = AnalyticsEvent.SUBMIT,
            params = mapOf(
                AnalyticsParam.ACTION to "toggle_group_reminder"
            )
        )
        viewModelScope.launch {
            toggleReminderUseCase(_uiState.value.group, enabled)
                .onSuccess { isEnabled ->
                    _uiState.update { it.copy(isReminderEnabled = isEnabled) }
                }
                .onFailure { error ->
                    Timber.e(error, "Error toggling group reminder")
                    _uiState.update { it.copy(error = "Erro ao configurar o lembrete") }
                }
        }
    }

    private fun loadCurrentDeviceId() {
        viewModelScope.launch {
            _uiState.update { it.copy(currentDeviceId = deviceService.getDeviceId()) }
        }
    }

    @AddTrace(name = "GroupDetailsViewModel.updateLikes", enabled = true)
    private fun updateLikes(likes: List<String>) {
        analyticsService.logEvent(
            name = AnalyticsEvent.SUBMIT,
            params = mapOf(
                AnalyticsParam.ACTION to "update_member_likes"
            )
        )
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            updateMemberLikesUseCase(_uiState.value.group, likes)
                .onSuccess { group ->
                    _uiState.update { it.copy(isLoading = false, group = group) }
                }
                .onFailure { error ->
                    Timber.e(error, "Error updating member likes")
                    _uiState.update { it.copy(isLoading = false, error = "Erro ao salvar interesses") }
                }
        }
    }

    @AddTrace(name = "GroupDetailsViewModel.addLike", enabled = true)
    private fun addLike(memberId: String, like: String) {
        analyticsService.logEvent(
            name = AnalyticsEvent.SUBMIT,
            params = mapOf(
                AnalyticsParam.ACTION to "add_member_like"
            )
        )
        viewModelScope.launch {
            addMemberLikeUseCase(_uiState.value.group, memberId, like)
                .onSuccess { group ->
                    _uiState.update { it.copy(group = group) }
                }
                .onFailure { error ->
                    Timber.e(error, "Error adding member like")
                    _uiState.update { it.copy(error = "Erro ao adicionar item à lista") }
                }
        }
    }

    @AddTrace(name = "GroupDetailsViewModel.deleteGroup", enabled = true)
    private fun deleteGroup(callback: () -> Unit) {
        analyticsService.logEvent(
            name = AnalyticsEvent.SUBMIT,
            params = mapOf(
                AnalyticsParam.ACTION to "delete_group"
            )
        )
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            deleteUseCase.invoke(_uiState.value.group)
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false) }
                    callback()
                }
                .onFailure { error ->
                    Timber.e(error, "Error deleting group")
                    _uiState.update { it.copy(isLoading = false, error = "Erro ao excluir grupo") }
                }
        }
    }

    @AddTrace(name = "GroupDetailsViewModel.exitGroup", enabled = true)
    private fun exitGroup(callback: () -> Unit) {
        analyticsService.logEvent(
            name = AnalyticsEvent.SUBMIT,
            params = mapOf(
                AnalyticsParam.ACTION to "exit_group"
            )
        )
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            exitUseCase.invoke(_uiState.value.group)
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false) }
                    callback()
                }
                .onFailure { error ->
                    Timber.e(error, "Error exiting group")
                    _uiState.update { it.copy(isLoading = false, error = "Erro ao sair do grupo") }
                }
        }
    }

    @AddTrace(name = "GroupDetailsViewModel.shareGroup", enabled = true)
    private fun shareGroup() {
        analyticsService.logEvent(
            name = AnalyticsEvent.SUBMIT,
            params = mapOf(
                AnalyticsParam.ACTION to "share_group"
            )
        )
        analyticsService.logEvent(name = AnalyticsEvent.INVITE_SHARE_CODE)
        viewModelScope.launch {
            shareUseCase(group = _uiState.value.group)
        }
    }

    @AddTrace(name = "GroupDetailsViewModel.shareInviteCard", enabled = true)
    private fun shareInviteCard() {
        analyticsService.logEvent(
            name = AnalyticsEvent.SUBMIT,
            params = mapOf(
                AnalyticsParam.ACTION to "share_group_invite_card"
            )
        )
        analyticsService.logEvent(name = AnalyticsEvent.INVITE_SHARE_CARD)
        viewModelScope.launch {
            shareInviteCardUseCase(_uiState.value.group)
                .onFailure { error ->
                    Timber.e(error, "Error sharing group invite card")
                    _uiState.update { it.copy(error = "Erro ao gerar o cartão de convite") }
                }
        }
    }

    @AddTrace(name = "GroupDetailsViewModel.shareQrCode", enabled = true)
    private fun shareQrCode() {
        analyticsService.logEvent(
            name = AnalyticsEvent.SUBMIT,
            params = mapOf(
                AnalyticsParam.ACTION to "share_group_qr_code"
            )
        )
        analyticsService.logEvent(name = AnalyticsEvent.INVITE_SHARE_QR)
        viewModelScope.launch {
            shareQrCodeUseCase(_uiState.value.group)
                .onFailure { error ->
                    Timber.e(error, "Error sharing group QR code")
                    _uiState.update { it.copy(error = "Erro ao gerar QR Code") }
                }
        }
    }

    @AddTrace(name = "GroupDetailsViewModel.removeMember", enabled = true)
    private fun removeMember(memberId: String) {
        analyticsService.logEvent(
            name = AnalyticsEvent.SUBMIT,
            params = mapOf(
                AnalyticsParam.ACTION to "remove_member"
            )
        )
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            removeMemberUseCase(_uiState.value.group, memberId)
                .onSuccess { group ->
                    _uiState.update { it.copy(isLoading = false, group = group) }
                }
                .onFailure { error ->
                    Timber.e(error, "Error removing member")
                    _uiState.update { it.copy(isLoading = false, error = "Erro ao remover participante") }
                }
        }
    }

    @AddTrace(name = "GroupDetailsViewModel.shareWishlist", enabled = true)
    private fun shareWishlist() {
        analyticsService.logEvent(
            name = AnalyticsEvent.SUBMIT,
            params = mapOf(
                AnalyticsParam.ACTION to "share_wishlist"
            )
        )
        viewModelScope.launch {
            shareWishlistUseCase(group = _uiState.value.group)
                .onFailure { error ->
                    Timber.e(error, "Error sharing wishlist")
                    _uiState.update { it.copy(error = "Adicione itens à sua lista antes de compartilhar") }
                }
        }
    }

    @AddTrace(name = "GroupDetailsViewModel.readGroup", enabled = true)
    private fun readGroup() {
        analyticsService.logEvent(
            name = AnalyticsEvent.SUBMIT,
            params = mapOf(
                AnalyticsParam.ACTION to "read_group"
            )
        )
        viewModelScope.launch {
            readUseCase(_uiState.value.group.id)
                .onSuccess { group ->
                    if (group != _uiState.value.group) {
                        _uiState.update { it.copy(group = group) }
                        Timber.d("Dados atualizados: O grupo foi modificado em outra tela.")
                    } else {
                        Timber.d("Sem alterações detectadas no grupo.")
                    }
                }
        }
    }
}
