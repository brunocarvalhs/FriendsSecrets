package br.com.brunocarvalhs.group.app.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.brunocarvalhs.friendssecrets.data.model.create
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import br.com.brunocarvalhs.friendssecrets.domain.useCases.GetListUsersByContactUseCase
import br.com.brunocarvalhs.friendssecrets.domain.useCases.GroupCreateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupCreateViewModel @Inject constructor(
    private val useCase: GroupCreateUseCase,
    private val getListUsersByContactUseCase: GetListUsersByContactUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(GroupCreateUiState())
    val uiState: StateFlow<GroupCreateUiState> = _uiState.asStateFlow()

    fun eventIntent(intent: GroupCreateIntent) {
        when (intent) {
            is GroupCreateIntent.CreateGroup -> createGroup()
            is GroupCreateIntent.FetchContacts -> fetchContacts()
            is GroupCreateIntent.UpdateName -> updateName(intent.name)
            is GroupCreateIntent.UpdateDescription -> updateDescription(intent.description)
            is GroupCreateIntent.ToggleMember -> toggleMember(intent.member)
            is GroupCreateIntent.NextStep -> nextStep()
            is GroupCreateIntent.Back -> resetState()
            is GroupCreateIntent.UpdateDrawDate -> updateDrawDate(intent.date)
            is GroupCreateIntent.UpdateMinValue -> updateMinValue(intent.value)
            is GroupCreateIntent.UpdateMaxValue -> updateMaxValue(intent.value)
            is GroupCreateIntent.UpdateDrawType -> updateDrawType(intent.type)
            is GroupCreateIntent.GoToStep -> goToStep(intent.step)
        }
    }

    private fun updateDrawDate(date: String) {
        _uiState.update { it.copy(drawDate = date) }
    }

    private fun updateMinValue(value: String) {
        _uiState.update { it.copy(minValue = value) }
    }

    private fun updateMaxValue(value: String) {
        _uiState.update { it.copy(maxValue = value) }
    }

    private fun updateDrawType(type: String) {
        _uiState.update { it.copy(drawType = type) }
    }

    private fun updateName(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    private fun updateDescription(description: String) {
        _uiState.update { it.copy(description = description) }
    }

    private fun toggleMember(member: UserEntities) {
        _uiState.update {
            val updated = if (member in it.members)
                it.members - member
            else
                it.members + member
            it.copy(members = updated)
        }
    }

    private fun fetchContacts() {
        viewModelScope.launch {
            val result = getListUsersByContactUseCase.invoke().getOrNull().orEmpty()
            _uiState.update {
                it.copy(
                    contacts = result,
                    isLoading = false
                )
            }
        }
    }

    private fun createGroup() {
        val state = _uiState.value
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val group = GroupEntities.create(
                name = state.name,
                description = state.description,
                members = state.members
            )
            useCase.invoke(group).onSuccess {
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            }.onFailure {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = it.errorMessage ?: "Erro ao criar grupo"
                    )
                }
            }
        }
    }

    private fun nextStep() {
        _uiState.update { current ->
            val next = (current.currentStep + 1).coerceAtMost(2)
            current.copy(currentStep = next)
        }
    }

    private fun resetState() {
        _uiState.value = GroupCreateUiState()
    }

    private fun goToStep(step: Int) {
        _uiState.update { it.copy(currentStep = step) }
    }
}
