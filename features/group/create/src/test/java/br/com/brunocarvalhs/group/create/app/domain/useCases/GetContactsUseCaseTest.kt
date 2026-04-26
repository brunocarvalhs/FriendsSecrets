package br.com.brunocarvalhs.group.create.app.domain.useCases

import br.com.brunocarvalhs.group.create.app.domain.model.ContactModel
import br.com.brunocarvalhs.group.create.app.domain.repositories.ContactsRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetContactsUseCaseTest {

    private val repository: ContactsRepository = mockk()
    private lateinit var useCase: GetContactsUseCase

    @Before
    fun setup() {
        useCase = GetContactsUseCase(repository)
    }

    @Test
    fun `invoke should return list of users from repository`() = runTest {
        // Given
        val contacts = listOf(
            ContactModel(id = "1", name = "Test", photoUrl = "photo", phoneNumber = "123")
        )
        coEvery { repository.getContacts() } returns contacts

        // When
        val result = useCase()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull()?.size)
        assertEquals("1", result.getOrNull()?.first()?.id)
    }

    @Test
    fun `invoke should return failure when repository throws exception`() = runTest {
        // Given
        coEvery { repository.getContacts() } throws RuntimeException("Network Error")

        // When
        val result = useCase()

        // Then
        assertTrue(result.isFailure)
    }
}
