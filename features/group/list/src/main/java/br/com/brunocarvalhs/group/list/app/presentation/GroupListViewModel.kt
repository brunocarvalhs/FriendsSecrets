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
internal class GroupListViewModel @Inject constructor(
    private val groupListUseCase: GroupListUseCase,
    private val groupByTokenUseCase: GroupByTokenUseCase,
    private val analytics: GroupListAnalytics,
) : ViewModel() {

    private val _uiState = MutableStateFlow(GroupListUiState())
    val uiState: StateFlow<GroupListUiState> = _uiState.asStateFlow()

    init {
        analytics.trackScreenView()
    }

    @AddTrace(name = "GroupListViewModel.handleEvent", enabled = true)
    fun handleEvent(intent: GroupListIntent) {
        when (intent) {
            GroupListIntent.FetchGroups -> fetchGroups()
            is GroupListIntent.GroupToEnter -> groupToEnter(intent.token)
            is GroupListIntent.OnSearchQueryChange -> {
                analytics.trackSearch(intent.query)
                _uiState.update { it.copy(searchQuery = intent.query) }
            }
            is GroupListIntent.OnTagSelected -> {
                _uiState.update { it.copy(selectedTag = intent.tag) }
            }
        }
    }

    @AddTrace(name = "GroupListViewModel.groupToEnter", enabled = true)
    private fun groupToEnter(token: String) {
        analytics.trackGroupToEnter(token)
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

    @AddTrace(name = "GroupListViewModel.fetchGroups", enabled = true)
    private fun fetchGroups() {
        analytics.trackFetchGroups()
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