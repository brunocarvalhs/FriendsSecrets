package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import br.com.brunocarvalhs.friendssecrets.domain.repositories.UserRepository
import br.com.brunocarvalhs.friendssecrets.domain.services.ContactService
import br.com.brunocarvalhs.friendssecrets.domain.services.PerformanceService

class GetListUsersByContactUseCase(
    private val repository: UserRepository,
    private val contact: ContactService,
    private val performance: PerformanceService
) {
    suspend operator fun invoke(): Result<List<UserEntities>> {
        performance.start(GetListUsersByContactUseCase::class.java.simpleName)
        return try {
            runCatching {
                val phoneNumbers = getFormattedPhoneNumbers()
                val registeredUsers = fetchRegisteredUsers(phoneNumbers)
                val deviceContacts = fetchDeviceContacts()

                val mergedList = mergeContacts(registeredUsers, deviceContacts)
                mergedList.sortedByDescending { registeredUsers.contains(it) }
            }
        } finally {
            performance.stop(GetListUsersByContactUseCase::class.java.simpleName)
        }
    }

    private fun getFormattedPhoneNumbers(): List<String> {
        return contact.getPhoneNumbers()
            .map { it.replace("[^\\d+]".toRegex(), "") }
            .distinct()
    }

    private suspend fun fetchRegisteredUsers(phoneNumbers: List<String>): List<UserEntities> {
        return repository
            .listUsersByPhoneNumber(phoneNumbers)
    }

    private fun fetchDeviceContacts(): List<UserEntities> {
        return contact.getContacts()
    }

    private fun mergeContacts(
        registeredUsers: List<UserEntities>,
        deviceContacts: List<UserEntities>
    ): MutableList<UserEntities> {
        val mergedList = mutableListOf<UserEntities>()
        mergedList.addAll(registeredUsers)

        deviceContacts
            .filter { contact ->
                registeredUsers.none { it.phoneNumber == contact.phoneNumber } &&
                        !isNameJustPhoneNumber(contact.name)
            }
            .forEach { mergedList.add(it) }

        return mergedList
    }

    private fun isNameJustPhoneNumber(name: String?): Boolean {
        if (name.isNullOrBlank()) return true
        val normalized = name.replace("[\\s()+-]".toRegex(), "")
        return normalized.all { it.isDigit() }
    }
}
