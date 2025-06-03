package br.com.brunocarvalhs.group.app.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.brunocarvalhs.friendssecrets.common.extensions.report
import br.com.brunocarvalhs.friendssecrets.data.model.create
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import br.com.brunocarvalhs.friendssecrets.domain.useCases.GetListUsersByContactUseCase
import br.com.brunocarvalhs.friendssecrets.domain.useCases.GroupCreateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupCreateViewModel @Inject constructor(
    private val useCase: GroupCreateUseCase,
    private val getListUsersByContactUseCase: GetListUsersByContactUseCase,
) : ViewModel() {

    private var contacts: List<UserEntities> = emptyList()

    private val _uiState: MutableStateFlow<GroupCreateUiState> =
        MutableStateFlow(GroupCreateUiState.Idle(
            contacts = contacts,
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
                    contacts = contacts,
                )
            }

            is GroupCreateIntent.FetchContacts -> fetchContacts()
        }
    }

    private fun fetchContacts() {
        viewModelScope.launch {
            contacts = getListUsersByContactUseCase.invoke().getOrNull().orEmpty()
            _uiState.value = GroupCreateUiState.Idle(
                contacts = contacts,
            )
        }
    }

    private fun createGroup(name: String, description: String, members: List<UserEntities>) {
        _uiState.value = GroupCreateUiState.Loading
        viewModelScope.launch {
            val group = GroupEntities.create(
                name = name,
                description = description,
                members = members
            )
            useCase.invoke(group).onSuccess {
                _uiState.value = GroupCreateUiState.Success
            }.onFailure {
                _uiState.value = GroupCreateUiState.Error(it.report()?.message.orEmpty())
            }

        }
    }
}