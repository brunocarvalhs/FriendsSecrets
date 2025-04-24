package br.com.brunocarvalhs.friendssecrets.domain.useCases

import android.content.Context
import br.com.brunocarvalhs.friendssecrets.data.model.UserModel
import br.com.brunocarvalhs.friendssecrets.data.service.ContactService
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import br.com.brunocarvalhs.friendssecrets.domain.repository.UserRepository
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetListUsersByContactUseCaseTest {

    private lateinit var userRepository: UserRepository
    private lateinit var contactService: ContactService
    private lateinit var context: Context
    private lateinit var useCase: GetListUsersByContactUseCase

    @Before
    fun setUp() {
        userRepository = mockk()
        contactService = mockk()
        context = mockk()
        useCase = GetListUsersByContactUseCase(userRepository, contactService)
    }

    @Test
    fun `should return merged list of users and contacts`() = runBlocking {
        // Mock data
        val phoneNumbers = listOf("123456789", "987654321")
        val users = listOf(
            UserModel("1", "User1", "123456789"),
            UserModel("2", "User2", "987654321")
        )

        val contacts = listOf(
            UserModel("3", "Contact1", "111111111"),
            UserModel("4", "Contact2", "123456789") // Duplicate phone number
        )

        val mergedList = users.toMutableList()
        // Adiciona os contatos que não estão na lista de usuários
        contacts.forEach { contact ->
            if (users.none { it.phoneNumber == contact.phoneNumber }) {
                mergedList.add(contact)
            }
        }


        // Mock behaviors
        coEvery { contactService.getPhoneNumbers(context) } returns phoneNumbers
        coEvery { userRepository.listUsersByPhoneNumber(phoneNumbers) } returns users
        coEvery { contactService.getContacts(context) } returns contacts

        // Execute use case
        val result = useCase(context).getOrThrow()

        // Verify the result
        val expected = listOf(
            UserModel("1", "User1", "123456789"),
            UserModel("2", "User2", "987654321"),
        )
        assertEquals(expected, result)

        // Verify interactions
        coVerify { contactService.getPhoneNumbers(context) }
        coVerify { userRepository.listUsersByPhoneNumber(phoneNumbers) }
        coVerify { contactService.getContacts(context) }
    }

    @Test
    fun `should handle empty phone numbers and contacts`() = runBlocking {
        // Mock behaviors
        coEvery { contactService.getPhoneNumbers(context) } returns emptyList()
        coEvery { userRepository.listUsersByPhoneNumber(any<List<String>>()) } returns emptyList()
        coEvery { contactService.getContacts(context) } returns emptyList()

        // Execute use case
        val result = useCase(context).getOrThrow()

        // Verify the result
        assertEquals(emptyList<UserEntities>(), result)

        // Verify interactions
        coVerify { contactService.getPhoneNumbers(context) }
        coVerify { userRepository.listUsersByPhoneNumber(emptyList()) }
        coVerify { contactService.getContacts(context) }
    }
}