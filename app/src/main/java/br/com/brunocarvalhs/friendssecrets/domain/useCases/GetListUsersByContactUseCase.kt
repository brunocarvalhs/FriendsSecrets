package br.com.brunocarvalhs.friendssecrets.domain.useCases

import android.content.Context
import br.com.brunocarvalhs.friendssecrets.data.service.ContactService
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import br.com.brunocarvalhs.friendssecrets.domain.repository.UserRepository
import com.google.firebase.perf.metrics.AddTrace

class GetListUsersByContactUseCase(
    private val userRepository: UserRepository,
    private val contactService: ContactService
) {

    /**
     * This function retrieves a list of users from the repository based on the phone numbers
     * obtained from the contacts in the device. It merges the users and contacts into a single list,
     * prioritizing the users over the contacts.
     *
     * @param context The context of the application.
     * @return A Result containing a list of UserEntities.
     */
    @Throws(Exception::class)
    @AddTrace(name = "GetListUsersByContactUseCase.invoke")
    suspend operator fun invoke(context: Context): Result<List<UserEntities>> = runCatching {
        // Get the phone numbers from the contacts
        val phoneNumbers = contactService.getPhoneNumbers(context).map { it.replace("[^\\d+]".toRegex(), "") }.distinct()
        // Fetch the users from the repository using the phone numbers
        val users = userRepository.listUsersByPhoneNumber(phoneNumbers)

        // Get the contacts from the phone numbers
        val contacts = contactService.getContacts(context)

        // Merge the users and contacts into a single list
        val mergedList = mutableListOf<UserEntities>()
        mergedList.addAll(users)
        contacts.forEach { contact ->
            if (!mergedList.any { it.phoneNumber == contact.phoneNumber }) {
                mergedList.add(contact)
            }
        }
        // prioritize the users over the contacts
        mergedList.sortByDescending { users.contains(it) }
        // Return the merged list
        mergedList
    }
}