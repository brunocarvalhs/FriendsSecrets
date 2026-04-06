package br.com.brunocarvalhs.group.create.app.presentation.contacts

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.brunocarvalhs.group.create.app.domain.model.ContactModel
import br.com.brunocarvalhs.group.create.app.domain.useCases.GetContactsUseCase
import br.com.brunocarvalhs.group.create.commons.navigation.FormsRouter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@Stable
@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val getContactsUseCase: GetContactsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ContactsUiState())
    val uiState: StateFlow<ContactsUiState> = _uiState.asStateFlow()

    init {
        handleIntent(ContactsIntent.LoadContacts)
    }

    internal fun handleIntent(intent: ContactsIntent) {
        when (intent) {
            is ContactsIntent.LoadContacts -> loadContacts()
            is ContactsIntent.SearchContacts -> searchContacts(intent.query)
            is ContactsIntent.AddMember -> addMember(intent.contact)
            is ContactsIntent.RemoveMember -> removeMember(intent.contact)
            is ContactsIntent.Next -> next(intent.callback)
        }
    }

    private fun loadContacts() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            getContactsUseCase().onSuccess { contacts ->
                _uiState.update { currentState ->
                    currentState.copy(
                        contacts = contacts,
                        filteredContacts = filterContacts(contacts, currentState.searchQuery),
                        isLoading = false
                    )
                }
            }.onFailure { exception ->
                _uiState.update { it.copy(isLoading = false, error = exception.message) }
            }
        }
    }

    private fun searchContacts(query: String) {
        _uiState.update { currentState ->
            currentState.copy(
                searchQuery = query,
                filteredContacts = filterContacts(currentState.contacts, query)
            )
        }
    }

    private fun addMember(contact: ContactModel) {
        _uiState.update { currentState ->
            if (currentState.members.any { it.phoneNumber == contact.phoneNumber }) {
                currentState
            } else {
                currentState.copy(members = currentState.members + contact)
            }
        }
    }

    private fun removeMember(contact: ContactModel) {
        _uiState.update { currentState ->
            currentState.copy(
                members = currentState.members.filterNot { it.phoneNumber == contact.phoneNumber }
            )
        }
    }

    private fun filterContacts(contacts: List<ContactModel>, query: String): List<ContactModel> {
        if (query.isBlank()) return contacts
        return contacts.filter {
            it.name.contains(query, ignoreCase = true) ||
                    it.phoneNumber.contains(query)
        }
    }

    private fun next(callback: (FormsRouter) -> Unit) {
        val members = _uiState.value.members
        
        if (members.isEmpty()) {
            _uiState.update { it.copy(error = "Selecione ao menos um membro") }
            return
        }
        
        if (members.size < 3) {
            _uiState.update { it.copy(error = "O grupo precisa de no mínimo 3 membros") }
            return
        }

        callback(
            FormsRouter(
                members = members,
                contacts = _uiState.value.contacts.size
            )
        )
    }
}
