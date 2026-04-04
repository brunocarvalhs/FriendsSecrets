package br.com.brunocarvalhs.group.create.app.presentation.forms

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import br.com.brunocarvalhs.group.create.app.domain.entities.ContactModel
import br.com.brunocarvalhs.group.create.app.domain.entities.GroupModel
import br.com.brunocarvalhs.group.create.app.domain.useCases.GroupCreateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@Stable
@HiltViewModel
class FormsViewModel @Inject constructor(
    private val groupCreateUseCase: GroupCreateUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(FormsUiState())
    val uiState: StateFlow<FormsUiState> = _uiState.asStateFlow()
    
    fun handleIntent(intent: FormsIntent) = when (intent) {
        FormsIntent.CreateGroup -> createGroup()
        is FormsIntent.UpdateName -> updateName(intent.name)
        is FormsIntent.ToggleMember -> toggleMember(intent.contact)
    }

    private fun createGroup() {
        // TODO: Implement group creation logic using groupCreateUseCase
    }

    private fun updateName(name: String) {
        _uiState.value = _uiState.value.copy(name = name)
    }

    private fun toggleMember(contact: ContactModel) {
        val currentMembers = _uiState.value.members.toMutableList()
        if (currentMembers.contains(contact)) {
            currentMembers.remove(contact)
        } else {
            currentMembers.add(contact)
        }
        _uiState.value = _uiState.value.copy(members = currentMembers)
    }
}