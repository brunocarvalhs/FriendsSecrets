package br.com.brunocarvalhs.friendssecrets.presentation.views.home.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import br.com.brunocarvalhs.friendssecrets.commons.crashlytics.report
import br.com.brunocarvalhs.friendssecrets.data.repository.GroupRepositoryImpl
import br.com.brunocarvalhs.friendssecrets.data.service.StorageService
import br.com.brunocarvalhs.friendssecrets.domain.useCases.GroupByTokenUseCase
import br.com.brunocarvalhs.friendssecrets.domain.useCases.GroupListUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val groupListUseCase: GroupListUseCase,
    private val groupByTokenUseCase: GroupByTokenUseCase,
) : ViewModel() {

    private val _uiState: MutableStateFlow<HomeUiState> =
        MutableStateFlow(HomeUiState.Loading)

    val uiState: StateFlow<HomeUiState> =
        _uiState.asStateFlow()

    fun event(intent: HomeIntent) {
        when (intent) {
            HomeIntent.FetchGroups -> fetchGroups()
            is HomeIntent.GroupToEnter -> groupToEnter(intent.token)
        }
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
            delay(timeMillis = 1500)
            groupListUseCase.invoke().onSuccess {
                _uiState.value = HomeUiState.Success(list = it)
            }.onFailure {
                _uiState.value = HomeUiState.Error(errorMessage = it.report()?.message.orEmpty())
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val repository = GroupRepositoryImpl()
                    val storage = StorageService()
                    val groupListUseCase = GroupListUseCase(
                        groupRepository = repository,
                        storage = storage
                    )
                    val groupByTokenUseCase = GroupByTokenUseCase(
                        groupRepository = repository,
                        storage = storage
                    )
                    HomeViewModel(
                        groupListUseCase = groupListUseCase,
                        groupByTokenUseCase = groupByTokenUseCase
                    )
                }
            }
    }
}