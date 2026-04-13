package br.com.brunocarvalhs.group.create.app.presentation.contacts

import br.com.brunocarvalhs.friendssecrets.domain.model.UserModel
import br.com.brunocarvalhs.group.create.commons.navigation.FormsRouter


internal sealed class ContactsIntent {
    object LoadContacts : ContactsIntent()
    data class SearchContacts(val query: String) : ContactsIntent()
    data class ToggleMember(val contact: UserModel) : ContactsIntent()
    data class RemoveMember(val contact: UserModel) : ContactsIntent()
    data class Next(val callback: (FormsRouter) -> Unit) : ContactsIntent()
}