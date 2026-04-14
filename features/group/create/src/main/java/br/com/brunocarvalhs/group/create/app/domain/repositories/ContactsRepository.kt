package br.com.brunocarvalhs.group.create.app.domain.repositories

import br.com.brunocarvalhs.group.create.app.domain.model.ContactModel

internal interface ContactsRepository {
    suspend fun getContacts(): List<ContactModel>
}