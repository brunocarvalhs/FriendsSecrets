package br.com.brunocarvalhs.group.list.app.presentation.details

import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import br.com.brunocarvalhs.group.list.app.domain.useCases.GroupDeleteUseCase
import br.com.brunocarvalhs.group.list.app.domain.useCases.GroupDrawUseCase
import br.com.brunocarvalhs.group.list.app.domain.useCases.GroupExitUseCase
import br.com.brunocarvalhs.group.list.app.domain.useCases.GroupReadUseCase
import br.com.brunocarvalhs.group.list.app.domain.useCases.GroupShareUseCase
import br.com.brunocarvalhs.group.list.commons.navigation.DetailRouter
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
class GroupDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val readUseCase: GroupReadUseCase,
    private val deleteUseCase: GroupDeleteUseCase,
    private val exitUseCase: GroupExitUseCase,
    private val drawUseCase: GroupDrawUseCase,
    private val shareUseCase: GroupShareUseCase,
) : ViewModel() {

    private val args = savedStateHandle.toRoute<DetailRouter>(DetailRouter.typeMap)

    private val _uiState = MutableStateFlow(GroupDetailsUiState(group = args.groupModel))
    val uiState: StateFlow<GroupDetailsUiState> = _uiState.asStateFlow()

    init {
        fetchGroup(args.groupModel.id)
    }

    fun handleIntent(intent: GroupDetailsIntent) = when (intent) {
        is GroupDetailsIntent.Delete -> deleteGroup(intent.callback)
        GroupDetailsIntent.Draw -> drawMembers()
        is GroupDetailsIntent.Exit -> exitGroup(intent.callback)
        GroupDetailsIntent.Share -> shareGroup()
        GroupDetailsIntent.Reveal -> showRevealModal()
        is GroupDetailsIntent.SelectMember -> onMemberSelected(intent.name)
        is GroupDetailsIntent.ChangeCode -> onCodeChange(intent.code)
        is GroupDetailsIntent.ConfirmReveal -> confirmIdentity(intent.name)
        GroupDetailsIntent.DismissReveal -> dismissRevealModal()
    }

    @AddTrace(name = "GroupDetailsViewModel.fetchGroup", enabled = true)
    fun fetchGroup(groupId: String) {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            readUseCase.invoke(groupId)
                .onSuccess { group ->
                    _uiState.update { it.copy(group = group, isLoading = false) }
                }
                .onFailure { error ->
                    Timber.e(error, "Error fetching group details")
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "Erro ao carregar detalhes do grupo"
                        )
                    }
                }
        }
    }

    @AddTrace(name = "GroupDetailsViewModel.deleteGroup", enabled = true)
    private fun deleteGroup(callback: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            deleteUseCase.invoke(_uiState.value.group.id)
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
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            exitUseCase.invoke(_uiState.value.group.id)
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

    @AddTrace(name = "GroupDetailsViewModel.drawMembers", enabled = true)
    private fun drawMembers() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            drawUseCase.invoke(_uiState.value.group.id)
                .onSuccess { fetchGroup(_uiState.value.group.id) }
                .onFailure { error ->
                    Timber.e(error, "Error drawing members")
                    _uiState.update { it.copy(isLoading = false, error = error.message ?: "Erro ao realizar sorteio") }
                }
        }
    }

    @AddTrace(name = "GroupDetailsViewModel.shareGroup", enabled = true)
    private fun shareGroup() {
        viewModelScope.launch {
            shareUseCase(group = _uiState.value.group)
        }
    }

    private fun onMemberSelected(name: String) {
        _uiState.update { it.copy(selectedMember = name) }
    }

    private fun onCodeChange(code: String) {
        _uiState.update { it.copy(revelationCode = code) }
    }

    private fun showRevealModal() {
        _uiState.update { it.copy(showRevealModal = true) }
    }

    private fun dismissRevealModal() {
        _uiState.update { it.copy(showRevealModal = false, revealedFriend = null, selectedMember = null, revelationCode = "") }
    }

    private fun confirmIdentity(memberName: String) {
        val secretFriend = _uiState.value.group.draws[memberName]
        _uiState.update { it.copy(revealedFriend = secretFriend) }
    }
}
