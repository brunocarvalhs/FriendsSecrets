package br.com.brunocarvalhs.group.create.app.domain.useCases

import br.com.brunocarvalhs.friendssecrets.domain.model.UserModel
import br.com.brunocarvalhs.group.create.app.domain.repositories.ContactsRepository
import javax.inject.Inject

class GetContactsUseCase @Inject constructor(
    private val repository: ContactsRepository
) {
    suspend operator fun invoke(): Result<List<UserModel>> = runCatching {
        repository.getContacts().map { it.toUserModel() }
    }
}
