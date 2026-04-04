package br.com.brunocarvalhs.group.create.app.presentation.forms

import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import br.com.brunocarvalhs.group.create.app.domain.entities.ContactModel
import br.com.brunocarvalhs.group.create.app.domain.entities.GroupModel
import br.com.brunocarvalhs.group.create.app.domain.useCases.GroupCreateUseCase
import br.com.brunocarvalhs.group.create.commons.navigation.FormsRouter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@Stable
@HiltViewModel
class FormsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val groupCreateUseCase: GroupCreateUseCase
) : ViewModel() {

    private val args = savedStateHandle.toRoute<FormsRouter>(FormsRouter.typeMap)
    private val _uiState = MutableStateFlow(FormsUiState(
        members = args.members
    ))
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