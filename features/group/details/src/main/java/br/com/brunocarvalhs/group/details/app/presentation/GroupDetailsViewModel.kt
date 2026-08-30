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
import br.com.brunocarvalhs.group.details.app.domain.useCases.GroupDeleteUseCase
import br.com.brunocarvalhs.group.details.app.domain.useCases.GroupExitUseCase
import br.com.brunocarvalhs.group.details.app.domain.useCases.GroupReadUseCase
import br.com.brunocarvalhs.group.details.app.domain.useCases.GroupShareUseCase
import br.com.brunocarvalhs.group.details.app.domain.useCases.ShareWishlistUseCase
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
internal class GroupDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val readUseCase: GroupReadUseCase,
    private val deleteUseCase: GroupDeleteUseCase,
    private val exitUseCase: GroupExitUseCase,
    private val shareUseCase: GroupShareUseCase,
    private val shareWishlistUseCase: ShareWishlistUseCase,
    private val analyticsService: AnalyticsService
) : ViewModel() {
    private val args = savedStateHandle.toRoute<GroupDetailsGraph>(GroupDetailsGraph.typeMap)
    private val _uiState = MutableStateFlow(GroupDetailsUiState(group = args.group))
    val uiState: StateFlow<GroupDetailsUiState> = _uiState.asStateFlow()

    @AddTrace(name = "GroupDetailsViewModel.handleIntent", enabled = true)
    fun handleIntent(intent: GroupDetailsIntent) = when (intent) {
        is GroupDetailsIntent.Refresh -> readGroup()
        is GroupDetailsIntent.Delete -> deleteGroup(intent.callback)
        is GroupDetailsIntent.Exit -> exitGroup(intent.callback)
        GroupDetailsIntent.Share -> shareGroup()
        GroupDetailsIntent.ShareWishlist -> shareWishlist()
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
        viewModelScope.launch {
            shareUseCase(group = _uiState.value.group)
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
