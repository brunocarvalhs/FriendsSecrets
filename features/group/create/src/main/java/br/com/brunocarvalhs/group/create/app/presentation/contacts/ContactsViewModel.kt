package br.com.brunocarvalhs.group.create.app.presentation.contacts

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.brunocarvalhs.group.create.app.domain.entities.ContactModel
import br.com.brunocarvalhs.group.create.app.domain.useCases.GetContactsUseCase
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

    fun handleIntent(intent: ContactsIntent) {
        when (intent) {
            is ContactsIntent.LoadContacts -> loadContacts()
            is ContactsIntent.SearchContacts -> searchContacts(intent.query)
            is ContactsIntent.AddMember -> addMember(intent.contact)
            is ContactsIntent.RemoveMember -> removeMember(intent.contact)
        }
    }

    private fun loadContacts() {
        viewModelScope.launch {
            val contacts: List<ContactModel> = getContactsUseCase().getOrThrow()
            _uiState.update { currentState ->
                currentState.copy(
                    contacts = contacts,
                    filteredContacts = filterContacts(contacts, currentState.searchQuery)
                )
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
}
