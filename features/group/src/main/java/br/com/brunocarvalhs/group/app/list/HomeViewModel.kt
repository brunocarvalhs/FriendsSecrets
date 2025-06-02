package br.com.brunocarvalhs.group.app.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.brunocarvalhs.friendssecrets.common.extensions.report
import br.com.brunocarvalhs.friendssecrets.domain.useCases.GroupByTokenUseCase
import br.com.brunocarvalhs.friendssecrets.domain.useCases.GroupListUseCase
import br.com.brunocarvalhs.friendssecrets.domain.useCases.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val groupListUseCase: GroupListUseCase,
    private val groupByTokenUseCase: GroupByTokenUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<HomeUiState> =
        MutableStateFlow(HomeUiState.Loading)

    val uiState: StateFlow<HomeUiState> =
        _uiState.asStateFlow()

    fun event(intent: HomeIntent) {
        when (intent) {
            HomeIntent.FetchGroups -> fetchGroups()
            is HomeIntent.GroupToEnter -> groupToEnter(intent.token)
            HomeIntent.Logout -> logout()
        }
    }

    private fun logout() {
        logoutUseCase.invoke()
    }

    private fun groupToEnter(token: String) {
        _uiState.value = HomeUiState.Loading
        viewModelScope.launch {
            groupByTokenUseCase.invoke(token).onSuccess { fetchGroups() }.onFailure {
                _uiState.value = HomeUiState.Error(it.report()?.message.orEmpty())
            }
        }
    }

    private fun fetchGroups() {
        _uiState.value = HomeUiState.Loading
        viewModelScope.launch {
            groupListUseCase.invoke().onSuccess {
                _uiState.value = HomeUiState.Success(list = it)
            }.onFailure {
                _uiState.value = HomeUiState.Error(errorMessage = it.report()?.message.orEmpty())
            }
        }
    }
}