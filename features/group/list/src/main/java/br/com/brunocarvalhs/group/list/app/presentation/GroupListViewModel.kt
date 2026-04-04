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
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@Stable
@HiltViewModel
class GroupListViewModel @Inject constructor(
    private val groupListUseCase: GroupListUseCase,
    private val groupByTokenUseCase: GroupByTokenUseCase,
) : ViewModel() {

    private val _uiState: MutableStateFlow<GroupListUiState> =
        MutableStateFlow(GroupListUiState.Loading)
    val uiState: StateFlow<GroupListUiState> = _uiState.asStateFlow()

    @AddTrace(name = "HomeViewModel.event", enabled = true)
    fun event(intent: GroupListIntent) {
        when (intent) {
            GroupListIntent.FetchGroups -> fetchGroups()
            is GroupListIntent.GroupToEnter -> groupToEnter(intent.token)
        }
    }

    @AddTrace(name = "HomeViewModel.groupToEnter", enabled = true)
    private fun groupToEnter(token: String) {
        _uiState.value = GroupListUiState.Loading
        viewModelScope.launch {
            groupByTokenUseCase.invoke(token).onSuccess { fetchGroups() }.onFailure {
                Timber.e(it)
                _uiState.value = GroupListUiState.Error(it.message.orEmpty())
            }
        }
    }

    @AddTrace(name = "HomeViewModel.fetchGroups", enabled = true)
    private fun fetchGroups() {
        _uiState.value = GroupListUiState.Loading
        viewModelScope.launch {
            groupListUseCase.invoke().onSuccess {
                _uiState.value = GroupListUiState.Success(list = it)
            }.onFailure {
                Timber.e(it)
                _uiState.value =
                    GroupListUiState.Error(errorMessage = it.message.orEmpty())
            }
        }
    }
}