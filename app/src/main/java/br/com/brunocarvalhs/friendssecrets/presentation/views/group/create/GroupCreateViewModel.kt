package br.com.brunocarvalhs.friendssecrets.presentation.views.group.create

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import br.com.brunocarvalhs.friendssecrets.commons.extensions.report
import br.com.brunocarvalhs.friendssecrets.commons.performance.PerformanceManager
import br.com.brunocarvalhs.friendssecrets.data.repository.GroupRepositoryImpl
import br.com.brunocarvalhs.friendssecrets.data.repository.UserRepositoryImpl
import br.com.brunocarvalhs.friendssecrets.data.service.ContactService
import br.com.brunocarvalhs.friendssecrets.data.service.StorageService
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import br.com.brunocarvalhs.friendssecrets.domain.useCases.GetListUsersByContactUseCase
import br.com.brunocarvalhs.friendssecrets.domain.useCases.GroupCreateUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GroupCreateViewModel(
    private val useCase: GroupCreateUseCase,
    private val getListUsersByContactUseCase: GetListUsersByContactUseCase
) : ViewModel() {

    private var contacts: List<UserEntities> = emptyList()

    private val _uiState: MutableStateFlow<GroupCreateUiState> =
        MutableStateFlow(GroupCreateUiState.Idle(
            contacts = contacts
        ))

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
                _uiState.value = GroupCreateUiState.Idle(
                    contacts = contacts
                )
            }

            is GroupCreateIntent.FetchContacts -> fetchContacts(
                intent.context
            )
        }
    }

    private fun fetchContacts(context: Context) {
        viewModelScope.launch {
            getListUsersByContactUseCase.invoke(context).onSuccess {
                contacts = it
                _uiState.value = GroupCreateUiState.Idle(contacts = it)
            }.onFailure {
                _uiState.value = GroupCreateUiState.Error(it.report()?.message.orEmpty())
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

    companion object {
        val Factory: ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val repository = GroupRepositoryImpl()
                    val storage = StorageService()
                    val performance = PerformanceManager()
                    val useCase = GroupCreateUseCase(
                        groupRepository = repository,
                        storage = storage,
                        performance = performance
                    )
                    val contactService = ContactService()
                    val userRepository = UserRepositoryImpl()
                    val getListUsersByContactUseCase = GetListUsersByContactUseCase(
                        userRepository = userRepository,
                        contactService = contactService
                    )
                    GroupCreateViewModel(
                        useCase = useCase,
                        getListUsersByContactUseCase = getListUsersByContactUseCase
                    )
                }
            }
    }
}