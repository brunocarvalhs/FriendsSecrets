package br.com.brunocarvalhs.group.create.app.domain.useCases

import br.com.brunocarvalhs.group.create.app.domain.entities.ContactModel
import br.com.brunocarvalhs.group.create.app.domain.repositories.ContactsRepository
import javax.inject.Inject

class GetContactsUseCase @Inject constructor(
    private val repository: ContactsRepository
) {
    suspend operator fun invoke(): Result<List<ContactModel>> = runCatching {
        repository.getContacts()
    }
}
