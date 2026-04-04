package br.com.brunocarvalhs.group.create.app.presentation.forms

import br.com.brunocarvalhs.group.create.app.domain.entities.ContactModel

sealed interface FormsIntent {
    data class CreateGroup(val onFinish: (String) -> Unit) : FormsIntent
    data class UpdateName(val name: String): FormsIntent
    data class ToggleMember(val contact: ContactModel): FormsIntent
}