package br.com.brunocarvalhs.friendssecrets.domain.useCases

import android.content.Context
import br.com.brunocarvalhs.friendssecrets.commons.performance.PerformanceManager
import br.com.brunocarvalhs.friendssecrets.data.service.ContactService
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import br.com.brunocarvalhs.friendssecrets.domain.repository.UserRepository
import br.com.brunocarvalhs.friendssecrets.domain.repository.response.toModel

class GetListUsersByContactUseCase(
    private val userRepository: UserRepository,
    private val contactService: ContactService,
    private val performanceManager: PerformanceManager = PerformanceManager()
) {
    suspend operator fun invoke(context: Context): Result<List<UserEntities>> {
        performanceManager.start(GetListUsersByContactUseCase::class.java.simpleName)
        return try {
            runCatching {
                val phoneNumbers = getFormattedPhoneNumbers(context)
                val registeredUsers = fetchRegisteredUsers(phoneNumbers)
                val deviceContacts = fetchDeviceContacts(context)

                val mergedList = mergeContacts(registeredUsers, deviceContacts)
                mergedList.sortedByDescending { registeredUsers.contains(it) }
            }
        } finally {
            performanceManager.stop(GetListUsersByContactUseCase::class.java.simpleName)
        }
    }

    private fun getFormattedPhoneNumbers(context: Context): List<String> {
        return contactService.getPhoneNumbers(context)
            .map { it.replace("[^\\d+]".toRegex(), "") }
            .distinct()
    }

    private suspend fun fetchRegisteredUsers(phoneNumbers: List<String>): List<UserEntities> {
        return userRepository
            .listUsersByPhoneNumber(phoneNumbers)
            .map { it.toModel() }
    }

    private fun fetchDeviceContacts(context: Context): List<UserEntities> {
        return contactService.getContacts(context)
    }

    private fun mergeContacts(
        registeredUsers: List<UserEntities>,
        deviceContacts: List<UserEntities>
    ): MutableList<UserEntities> {
        val mergedList = mutableListOf<UserEntities>()
        mergedList.addAll(registeredUsers)

        deviceContacts.forEach { contact ->
            if (registeredUsers.none { it.phoneNumber == contact.phoneNumber }) {
                mergedList.add(contact)
            }
        }

        return mergedList
    }
}
