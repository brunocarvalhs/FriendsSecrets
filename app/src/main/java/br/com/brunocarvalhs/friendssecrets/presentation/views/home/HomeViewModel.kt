package br.com.brunocarvalhs.friendssecrets.presentation.views.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import br.com.brunocarvalhs.friendssecrets.commons.crashlytics.report
import br.com.brunocarvalhs.friendssecrets.data.repository.GroupRepositoryImpl
import br.com.brunocarvalhs.friendssecrets.domain.useCases.GroupListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val groupListUseCase: GroupListUseCase,
) : ViewModel() {

    private val _uiState: MutableStateFlow<HomeUiState> =
        MutableStateFlow(HomeUiState.Loading)

    val uiState: StateFlow<HomeUiState> =
        _uiState.asStateFlow()

    fun event(intent: HomeIntent) {
        when (intent) {
            HomeIntent.FetchGroups -> fetchGroups()
            HomeIntent.ReadFriendSecret -> TODO()
        }
    }

    private fun fetchGroups() {
        _uiState.value = HomeUiState.Loading
        viewModelScope.launch {
            groupListUseCase.invoke().onSuccess {
                _uiState.value = HomeUiState.Success(list = it)
            }.onFailure {
                _uiState.value = HomeUiState.Error(it.report(
                    params = mapOf(

                    )
                )?.message.orEmpty())
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val repository = GroupRepositoryImpl()
                    val groupListUseCase = GroupListUseCase(repository)
                    HomeViewModel(groupListUseCase)
                }
            }
    }
}