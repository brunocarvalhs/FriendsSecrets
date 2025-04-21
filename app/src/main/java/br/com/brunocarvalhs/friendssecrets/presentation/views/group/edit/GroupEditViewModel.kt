package br.com.brunocarvalhs.friendssecrets.presentation.views.group.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.brunocarvalhs.friendssecrets.commons.extensions.report
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.useCases.GroupEditUseCase
import br.com.brunocarvalhs.friendssecrets.domain.useCases.GroupReadUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupEditViewModel @Inject constructor(
    private val groupReadUseCase: GroupReadUseCase,
    private val groupEditUseCase: GroupEditUseCase,
) : ViewModel() {

    private val _uiState: MutableStateFlow<GroupEditUiState> =
        MutableStateFlow(GroupEditUiState.Loading)

    val uiState: StateFlow<GroupEditUiState> =
        _uiState.asStateFlow()

    fun eventIntent(intent: GroupEditIntent) {
        when (intent) {
            is GroupEditIntent.EditGroup -> editGroup(intent.group)
            is GroupEditIntent.FetchGroup -> fetchGroup(intent.id)
        }
    }

    private fun fetchGroup(groupId: String) {
        _uiState.value = GroupEditUiState.Loading
        viewModelScope.launch {
            delay(timeMillis = 1000)
            groupReadUseCase.invoke(groupId)
                .onSuccess { group ->
                    _uiState.value = GroupEditUiState.Idle(group)
                }.onFailure {
                    _uiState.value = GroupEditUiState.Error(it.report()?.message.orEmpty())
                }
        }
    }

    private fun editGroup(group: GroupEntities) {
        _uiState.value = GroupEditUiState.Loading
        viewModelScope.launch {
            groupEditUseCase.invoke(group)
                .onSuccess {
                    _uiState.value = GroupEditUiState.Success
                }.onFailure {
                    _uiState.value = GroupEditUiState.Error(it.report()?.message.orEmpty())
                }

        }
    }
}