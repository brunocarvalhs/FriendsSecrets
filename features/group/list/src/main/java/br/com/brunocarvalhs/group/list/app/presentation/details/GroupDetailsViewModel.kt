package br.com.brunocarvalhs.group.list.app.presentation.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import br.com.brunocarvalhs.group.list.commons.navigation.DetailRouter
import com.google.firebase.perf.metrics.AddTrace
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val args = savedStateHandle.toRoute<DetailRouter>(DetailRouter.typeMap)

    private val _uiState = MutableStateFlow(GroupDetailsUiState(group = args.groupModel))
    val uiState: StateFlow<GroupDetailsUiState> = _uiState.asStateFlow()

    init {
        fetchGroup(args.groupModel.id)
    }

    @AddTrace(name = "GroupDetailsViewModel.fetchGroup", enabled = true)
    fun fetchGroup(groupId: String) {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
//            groupReadUseCase.invoke(groupId)
//                .onSuccess { group ->
//                    _uiState.update { it.copy(group = group, isLoading = false) }
//                }
//                .onFailure { error ->
//                    _uiState.update {
//                        it.copy(
//                            isLoading = false,
//                            error = error.report()?.message ?: "Erro desconhecido"
//                        )
//                    }
//                }
        }
    }
}
