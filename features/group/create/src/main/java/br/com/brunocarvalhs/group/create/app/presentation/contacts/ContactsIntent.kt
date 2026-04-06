package br.com.brunocarvalhs.group.create.app.presentation.contacts

import br.com.brunocarvalhs.group.create.app.domain.model.ContactModel
import br.com.brunocarvalhs.group.create.commons.navigation.FormsRouter


internal sealed class ContactsIntent {
    object LoadContacts : ContactsIntent()
    data class SearchContacts(val query: String) : ContactsIntent()
    data class AddMember(val contact: ContactModel) : ContactsIntent()
    data class RemoveMember(val contact: ContactModel) : ContactsIntent()
    data class Next(val callback: (FormsRouter) -> Unit) : ContactsIntent()
}
