package br.com.brunocarvalhs.group.create.app.presentation.contacts

import androidx.compose.runtime.Stable
import br.com.brunocarvalhs.core.domain.model.UserModel

@Stable
internal data class ContactsUiState(
    val isEditing: Boolean = false,
    val contacts: List<UserModel> = emptyList(),
    val filteredContacts: List<UserModel> = emptyList(),
    val searchQuery: String = "",
    val members: List<UserModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
