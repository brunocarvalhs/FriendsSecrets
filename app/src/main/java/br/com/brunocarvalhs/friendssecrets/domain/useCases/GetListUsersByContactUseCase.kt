package br.com.brunocarvalhs.friendssecrets.domain.useCases

import android.content.Context
import br.com.brunocarvalhs.friendssecrets.data.service.ContactService
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import br.com.brunocarvalhs.friendssecrets.domain.repository.UserRepository

class GetListUsersByContactUseCase(
    private val userRepository: UserRepository,
    private val contactService: ContactService
) {
    suspend operator fun invoke(context: Context): Result<List<UserEntities>> = runCatching {
        val contacts = contactService.getPhoneNumbers(context)
        val phoneNumbers = contacts.map { it.replace("[^\\d+]".toRegex(), "") }.distinct()
        userRepository.listUsersByPhoneNumber(phoneNumbers)
    }
}