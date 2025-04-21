package br.com.brunocarvalhs.friendssecrets.presentation.views.home.list

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import br.com.brunocarvalhs.friendssecrets.commons.extensions.report
import br.com.brunocarvalhs.friendssecrets.di.ServiceLocator
import br.com.brunocarvalhs.friendssecrets.domain.useCases.GroupByTokenUseCase
import br.com.brunocarvalhs.friendssecrets.domain.useCases.GroupListUseCase
import br.com.brunocarvalhs.friendssecrets.domain.useCases.LogoutUseCase
import br.com.brunocarvalhs.friendssecrets.presentation.base.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ViewModel para a tela de listagem de grupos.
 */
class HomeViewModel(
    private val groupListUseCase: GroupListUseCase,
    private val groupByTokenUseCase: GroupByTokenUseCase,
    private val logoutUseCase: LogoutUseCase
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

    companion object {
        val Factory: ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    HomeViewModel(
                        groupListUseCase = ServiceLocator.groupListUseCase,
                        groupByTokenUseCase = ServiceLocator.groupByTokenUseCase,
                        logoutUseCase = ServiceLocator.logoutUseCase
                    )
                }
            }
    }
}
