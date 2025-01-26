package br.com.brunocarvalhs.friendssecrets.presentation.views.group.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import br.com.brunocarvalhs.friendssecrets.commons.extensions.report
import br.com.brunocarvalhs.friendssecrets.commons.performance.PerformanceManager
import br.com.brunocarvalhs.friendssecrets.data.repository.GroupRepositoryImpl
import br.com.brunocarvalhs.friendssecrets.data.repository.UserRepositoryImpl
import br.com.brunocarvalhs.friendssecrets.data.service.StorageService
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import br.com.brunocarvalhs.friendssecrets.domain.useCases.GroupCreateUseCase
import br.com.brunocarvalhs.friendssecrets.domain.useCases.UserByNameOrPhoneUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GroupCreateViewModel(
    private val useCase: GroupCreateUseCase,
    private val readUseCase: UserByNameOrPhoneUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<GroupCreateUiState> =
        MutableStateFlow(GroupCreateUiState.Idle)

    val uiState: StateFlow<GroupCreateUiState> =
        _uiState.asStateFlow()

    fun eventIntent(intent: GroupCreateIntent) {
        when (intent) {
            is GroupCreateIntent.CreateGroup -> createGroup(
                intent.name,
                intent.description,
                intent.members
            )

            GroupCreateIntent.Back -> {
                _uiState.value = GroupCreateUiState.Idle
            }
        }
    }

    private fun createGroup(name: String, description: String, members: Map<String, String>) {
        _uiState.value = GroupCreateUiState.Loading
        viewModelScope.launch {
            useCase.invoke(
                name = name,
                description = description,
                members = members
            ).onSuccess {
                _uiState.value = GroupCreateUiState.Success
            }.onFailure {
                _uiState.value = GroupCreateUiState.Error(it.report()?.message.orEmpty())
            }

        }
    }

    fun search(string: String): UserEntities? {
        return null
    }

    companion object {
        val Factory: ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val useCase = GroupCreateUseCase(
                        groupRepository = GroupRepositoryImpl(),
                        storage = StorageService(),
                        performance = PerformanceManager()
                    )
                    val readUseCase = UserByNameOrPhoneUseCase(
                        repository = UserRepositoryImpl()
                    )
                    GroupCreateViewModel(
                        useCase = useCase,
                        readUseCase = readUseCase
                    )
                }
            }
    }
}