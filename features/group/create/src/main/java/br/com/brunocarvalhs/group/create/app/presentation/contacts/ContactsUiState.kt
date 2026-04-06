package br.com.brunocarvalhs.group.create.app.presentation.contacts

import androidx.compose.runtime.Stable
import br.com.brunocarvalhs.group.create.app.domain.model.ContactModel

@Stable
data class ContactsUiState(
    val contacts: List<ContactModel> = emptyList(),
    val filteredContacts: List<ContactModel> = emptyList(),
    val searchQuery: String = "",
    val members: List<ContactModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
