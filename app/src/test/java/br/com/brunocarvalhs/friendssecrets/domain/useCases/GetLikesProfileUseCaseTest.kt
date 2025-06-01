package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.data.manager.SessionManager
import br.com.brunocarvalhs.friendssecrets.data.model.UserModel
import br.com.brunocarvalhs.friendssecrets.domain.repository.UserRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetLikesProfileUseCaseTest {

    private lateinit var sessionManager: SessionManager
    private lateinit var userRepository: UserRepository
    private lateinit var getLikesProfileUseCase: GetLikesProfileUseCase

    @Before
    fun setUp() {
        sessionManager = mockk()
        userRepository = mockk()
        getLikesProfileUseCase = GetLikesProfileUseCase(
            sessionManager,
            userRepository,
            performanceManager
        )
    }

    @Test
    fun `invoke should return user when user exists`() = runBlocking {
        // Arrange
        val mockUser = UserModel(id = "123", name = "Test User")
        every { sessionManager.getCurrentUserModel() } returns mockUser
        coEvery { userRepository.getUserById("123") } returns mockUser

        // Act
        val result = getLikesProfileUseCase()

        // Assert
        assertTrue(result.isSuccess)
        assertEquals(mockUser, result.getOrNull())
        verify { sessionManager.getCurrentUserModel() }
        coVerify { userRepository.getUserById("123") }
    }

    @Test
    fun `invoke should throw exception when user is not found in session`() = runBlocking {
        // Arrange
        every { sessionManager.getCurrentUserModel() } returns null

        // Act
        val result = getLikesProfileUseCase()

        // Assert
        assertTrue(result.isFailure)
        assertEquals("User not found", result.exceptionOrNull()?.message)
        verify { sessionManager.getCurrentUserModel() }
        coVerify(exactly = 0) { userRepository.getUserById(any()) }
    }

    @Test
    fun `invoke should throw exception when user is not found in repository`() = runBlocking {
        // Arrange
        val mockUser = UserModel(id = "123", name = "Test User")
        every { sessionManager.getCurrentUserModel() } returns mockUser
        coEvery { userRepository.getUserById("123") } returns null

        // Act
        val result = getLikesProfileUseCase()

        // Assert
        assertTrue(result.isFailure)
        assertEquals("User not found", result.exceptionOrNull()?.message)
        verify { sessionManager.getCurrentUserModel() }
        coVerify { userRepository.getUserById("123") }
    }
}