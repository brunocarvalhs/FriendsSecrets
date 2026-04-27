package br.com.brunocarvalhs.group.create.app.data.repository

import br.com.brunocarvalhs.group.create.app.domain.model.ContactModel
import br.com.brunocarvalhs.group.create.app.domain.services.ContactService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ContactsRepositoryImplTest {

    private val service: ContactService = mockk()
    private lateinit var repository: ContactsRepositoryImpl

    @Before
    fun setup() {
        repository = ContactsRepositoryImpl(service)
    }

    @Test
    fun `getContacts should return list of contacts from service`() = runTest {
        // Given
        val expectedContacts = listOf(
            ContactModel(id = "1", name = "Test")
        )
        every { service.getContacts() } returns expectedContacts

        // When
        val result = repository.getContacts()

        // Then
        assertEquals(expectedContacts, result)
        verify(exactly = 1) { service.getContacts() }
    }
}
