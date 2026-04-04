package br.com.brunocarvalhs.group.create.app.presentation.forms

import br.com.brunocarvalhs.group.create.app.domain.entities.ContactModel

data class FormsUiState(
    val name: String = "",
    val members: List<ContactModel> = emptyList(),
    val contacts: List<ContactModel> = emptyList()
)