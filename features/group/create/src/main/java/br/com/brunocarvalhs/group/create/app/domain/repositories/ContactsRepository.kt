package br.com.brunocarvalhs.group.create.app.domain.repositories

import br.com.brunocarvalhs.group.create.app.domain.entities.ContactModel

interface ContactsRepository {
    suspend fun getContacts(): List<ContactModel>
}