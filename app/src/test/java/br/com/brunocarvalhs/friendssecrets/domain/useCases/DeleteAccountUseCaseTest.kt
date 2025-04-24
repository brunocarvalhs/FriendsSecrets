package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.data.manager.SessionManager
import br.com.brunocarvalhs.friendssecrets.domain.repository.UserRepository
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class DeleteAccountUseCaseTest {

    private lateinit var userRepository: UserRepository
    private lateinit var sessionManager: SessionManager
    private lateinit var deleteAccountUseCase: DeleteAccountUseCase

    @Before
    fun setUp() {
        userRepository = mockk()
        sessionManager = mockk()
        deleteAccountUseCase = DeleteAccountUseCase(userRepository, sessionManager)
    }

    @Test
    fun `invoke should delete user account successfully`() = runBlocking {
        // Arrange
        val userId = "123"
        every { sessionManager.getCurrentUserModel() } returns mockk {
            every { id } returns userId
        }
        coEvery { userRepository.deleteUser(userId) } just Runs
        every { sessionManager.deleteAccount() } just Runs

        // Act
        val result = deleteAccountUseCase()

        // Assert
        assertTrue(result.isSuccess)
        coVerify { userRepository.deleteUser(userId) }
        verify { sessionManager.deleteAccount() }
    }

    @Test
    fun `invoke should throw exception when user not found`() = runBlocking {
        // Arrange
        every { sessionManager.getCurrentUserModel() } returns null

        // Act
        val result = deleteAccountUseCase()

        // Assert
        assertTrue(result.isFailure)
        coVerify(exactly = 0) { userRepository.deleteUser(any()) }
        verify(exactly = 0) { sessionManager.deleteAccount() }
    }
}