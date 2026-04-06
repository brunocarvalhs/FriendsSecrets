package br.com.brunocarvalhs.group.create.app.presentation.contacts

import androidx.compose.runtime.Stable
import br.com.brunocarvalhs.friendssecrets.domain.model.UserModel
import br.com.brunocarvalhs.group.create.app.domain.model.ContactModel

@Stable
data class ContactsUiState(
    val contacts: List<UserModel> = emptyList(),
    val filteredContacts: List<UserModel> = emptyList(),
    val searchQuery: String = "",
    val members: List<UserModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
