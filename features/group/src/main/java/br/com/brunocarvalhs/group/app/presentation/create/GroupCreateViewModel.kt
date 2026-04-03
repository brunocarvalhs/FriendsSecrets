package br.com.brunocarvalhs.group.app.presentation.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.brunocarvalhs.friendssecrets.data.model.create
import br.com.brunocarvalhs.friendssecrets.domain.useCases.GetListUsersByContactUseCase
import br.com.brunocarvalhs.group.app.domain.useCases.GroupCreateUseCase
import br.com.brunocarvalhs.group.app.create.GroupCreateIntent.CreateGroup
import br.com.brunocarvalhs.group.app.create.GroupCreateIntent.FetchContacts
import br.com.brunocarvalhs.group.app.create.GroupCreateIntent.ToggleMember
import br.com.brunocarvalhs.group.app.create.GroupCreateIntent.UpdateDescription
import br.com.brunocarvalhs.group.app.create.GroupCreateIntent.UpdateDrawDate
import br.com.brunocarvalhs.group.app.create.GroupCreateIntent.UpdateDrawType
import br.com.brunocarvalhs.group.app.create.GroupCreateIntent.UpdateMaxValue
import br.com.brunocarvalhs.group.app.create.GroupCreateIntent.UpdateMinValue
import br.com.brunocarvalhs.group.app.create.GroupCreateIntent.UpdateName
import br.com.brunocarvalhs.group.app.create.GroupCreateIntent.UpdateSearch
import br.com.brunocarvalhs.group.commons.validation.GroupValidator
import com.google.firebase.perf.metrics.AddTrace
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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

    @AddTrace(name = "GroupCreateViewModel.eventIntent", enabled = true)
    fun eventIntent(intent: GroupCreateIntent) {
        when (intent) {
            is CreateGroup -> createGroup()
            is FetchContacts -> fetchContacts()
            is ToggleMember -> toggleMember(member = intent.member)
            is UpdateSearch -> updateSearch(query = intent.value)
            is UpdateName -> updateField { it.copy(name = intent.value) }
            is UpdateDescription -> updateField { it.copy(description = intent.value) }
            is UpdateDrawDate -> updateField { it.copy(drawDate = intent.value) }
            is UpdateMinValue -> updateField { it.copy(minValue = intent.value) }
            is UpdateMaxValue -> updateField { it.copy(maxValue = intent.value) }
            is UpdateDrawType -> updateField { it.copy(drawType = intent.value) }
            is GroupCreateIntent.ClearError -> { updateField { it.copy(errorMessage = null) } }
        }
    }

    @AddTrace(name = "GroupCr_uiState.asStateFlowSearch", enabled = true)
    private fun updateSearch(query: String) = updateField { state ->
        val filtered = if (query.isBlank()) {
            state.contacts
        } else {
            state.contacts.filter { it.name.contains(query, ignoreCase = true) }
        }
        state.copy(searchQuery = query, filteredContacts = filtered)
    }

    @AddTrace(name = "GroupCreateViewModel.updateField", enabled = true)
    private fun updateField(update: (GroupCreateUiState) -> GroupCreateUiState) {
        _uiState.update(update)
    }

    @AddTrace(name = "GroupCreateViewModel.toggleMember", enabled = true)
    private fun toggleMember(member: UserEntities) = updateField { state ->
        val updated = if (member in state.members) state.members - member else state.members + member
        state.copy(members = updated)
    }

    @AddTrace(name = "GroupCreateViewModel.fetchContacts", enabled = true)
    private fun fetchContacts() {
        setLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            val result = getListUsersByContactUseCase()
                .getOrNull()
                .orEmpty()

            updateField { it.copy(contacts = result, isLoading = false) }
        }
    }

    @AddTrace(name = "GroupCreateViewModel.createGroup", enabled = true)
    private fun createGroup() {
        val state = uiState.value

        val errors = GroupValidator.validate(state)
        if (errors.isNotEmpty()) {
            updateField { it.copy(fieldErrors = errors) }
            return
        }

        updateField { it.copy(fieldErrors = emptyMap()) }
        setLoading(true)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val group = GroupEntities.create(
                    name = state.name,
                    description = state.description,
                    members = state.members,
                    date = state.drawDate,
                    minPrice = state.minValue.toDoubleOrNull(),
                    maxPrice = state.maxValue.toDoubleOrNull(),
                    type = state.drawType
                )

                useCase.invoke(group)
                    .onSuccess {
                        updateField { it.copy(isLoading = false, isSuccess = true) }
                    }
                    .onFailure { throwable ->
                        updateField { it.copy(isLoading = false) }
                        setError(throwable.message ?: "Erro ao criar grupo")
                    }
            } catch (e: Exception) {
                updateField { it.copy(isLoading = false) }
                setError(e.message ?: "Erro inesperado ao criar grupo")
            }
        }
    }

    private fun setLoading(isLoading: Boolean) = updateField { it.copy(isLoading = isLoading) }
    private fun setError(message: String) = updateField { it.copy(isLoading = false, errorMessage = message) }
}
