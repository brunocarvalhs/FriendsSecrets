package br.com.brunocarvalhs.group.list.app.presentation

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.brunocarvalhs.group.list.app.domain.useCases.GroupByTokenUseCase
import br.com.brunocarvalhs.group.list.app.domain.useCases.GroupListUseCase
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
class GroupListViewModel @Inject constructor(
    private val groupListUseCase: GroupListUseCase,
    private val groupByTokenUseCase: GroupByTokenUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(GroupListUiState())
    val uiState: StateFlow<GroupListUiState> = _uiState.asStateFlow()

    @AddTrace(name = "HomeViewModel.event", enabled = true)
    fun handleEvent(intent: GroupListIntent) {
        when (intent) {
            GroupListIntent.FetchGroups -> fetchGroups()
            is GroupListIntent.GroupToEnter -> groupToEnter(intent.token)
            is GroupListIntent.OnSearchQueryChange -> {
                _uiState.update { it.copy(searchQuery = intent.query) }
            }
            is GroupListIntent.OnTagSelected -> {
                _uiState.update { it.copy(selectedTag = intent.tag) }
            }
        }
    }

    @AddTrace(name = "HomeViewModel.groupToEnter", enabled = true)
    private fun groupToEnter(token: String) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            groupByTokenUseCase.invoke(token)
                .onSuccess { fetchGroups() }
                .onFailure { t ->
                    Timber.e(t)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = t.message.orEmpty()
                        )
                    }
                }
        }
    }

    @AddTrace(name = "HomeViewModel.fetchGroups", enabled = true)
    private fun fetchGroups() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            groupListUseCase.invoke().onSuccess { result ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        list = result,
                        errorMessage = null
                    )
                }
            }.onFailure { t ->
                Timber.e(t)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = t.message.orEmpty()
                    )
                }
            }
        }
    }
}