package br.com.brunocarvalhs.group.create.app.data.repository

import br.com.brunocarvalhs.group.create.app.domain.model.ContactModel
import br.com.brunocarvalhs.group.create.app.domain.repositories.ContactsRepository
import br.com.brunocarvalhs.group.create.app.domain.services.ContactService
import javax.inject.Inject

internal class ContactsRepositoryImpl @Inject constructor(
    private val service: ContactService
) : ContactsRepository {
    override suspend fun getContacts(): List<ContactModel> {
        val contacts = service.getContacts()
        return contacts
    }
}
