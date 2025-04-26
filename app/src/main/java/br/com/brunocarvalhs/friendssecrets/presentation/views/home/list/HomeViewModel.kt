package br.com.brunocarvalhs.friendssecrets.presentation.views.home.list

import br.com.brunocarvalhs.friendssecrets.commons.analytics.AnalyticsProvider
import br.com.brunocarvalhs.friendssecrets.commons.extensions.report
import br.com.brunocarvalhs.friendssecrets.commons.remote.toggle.ToggleManager
import br.com.brunocarvalhs.friendssecrets.domain.useCases.GroupByTokenUseCase
import br.com.brunocarvalhs.friendssecrets.domain.useCases.GroupListUseCase
import br.com.brunocarvalhs.friendssecrets.domain.useCases.LogoutUseCase
import br.com.brunocarvalhs.friendssecrets.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val groupListUseCase: GroupListUseCase,
    private val groupByTokenUseCase: GroupByTokenUseCase,
    private val logoutUseCase: LogoutUseCase,
    val toggleManager: ToggleManager,
    val analyticsProvider: AnalyticsProvider
) : BaseViewModel() {

    private val _uiState: MutableStateFlow<HomeUiState> =
        MutableStateFlow(HomeUiState.Loading)

    val uiState: StateFlow<HomeUiState> =
        _uiState.asStateFlow()

    init {
        fetchGroups()
    }

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
        launchIO(
            onError = { error ->
                _uiState.value = HomeUiState.Error(error.report()?.message.orEmpty())
            }
        ) {
            groupByTokenUseCase.invoke(token).onSuccess { 
                fetchGroups() 
            }.onFailure { error ->
                _uiState.value = HomeUiState.Error(error.report()?.message.orEmpty())
            }
        }
    }

    private fun fetchGroups() {
        _uiState.value = HomeUiState.Loading
        launchIO(
            onError = { error ->
                _uiState.value = HomeUiState.Error(error.report()?.message.orEmpty())
            }
        ) {
            delay(timeMillis = 1500)
            groupListUseCase.invoke().onSuccess { groups ->
                _uiState.value = HomeUiState.Success(list = groups)
            }.onFailure { error ->
                _uiState.value = HomeUiState.Error(errorMessage = error.report()?.message.orEmpty())
            }
        }
    }
}