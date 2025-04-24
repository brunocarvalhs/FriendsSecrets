package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.data.manager.SessionManager
import br.com.brunocarvalhs.friendssecrets.data.model.UserModel
import br.com.brunocarvalhs.friendssecrets.domain.repository.UserRepository
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CreateProfileUseCaseTest {

    private lateinit var sessionManager: SessionManager
    private lateinit var userRepository: UserRepository
    private lateinit var createProfileUseCase: CreateProfileUseCase

    @Before
    fun setUp() {
        sessionManager = mockk(relaxed = true)
        userRepository = mockk(relaxed = true)
        createProfileUseCase = CreateProfileUseCase(sessionManager, userRepository)
    }

    @Test
    fun `should update user profile successfully`() = runBlocking {
        // Arrange
        val name = "John Doe"
        val photoUrl = "http://example.com/photo.jpg"
        val likes = listOf("Music", "Movies")
        val mockUser = mockk<UserModel>(relaxed = true)

        coEvery { sessionManager.updateUserProfile(name, photoUrl) } just Runs
        coEvery { sessionManager.getCurrentUserModel() } returns mockUser
        coEvery { userRepository.updateUser(any()) } just Runs

        // Act
        val result = createProfileUseCase(name, photoUrl, likes)

        // Assert
        assertTrue(result.isSuccess)
        coVerify { sessionManager.updateUserProfile(name, photoUrl) }
        coVerify { sessionManager.getCurrentUserModel() }
        coVerify { userRepository.updateUser(any()) }
    }

    @Test
    fun `should return failure when an exception occurs`() = runBlocking {
        // Arrange
        val name = "John Doe"
        val photoUrl = "http://example.com/photo.jpg"
        val likes = listOf("Music", "Movies")

        coEvery { sessionManager.updateUserProfile(name, photoUrl) } throws Exception("Error")

        // Act
        val result = createProfileUseCase(name, photoUrl, likes)

        // Assert
        assertTrue(result.isFailure)
        coVerify { sessionManager.updateUserProfile(name, photoUrl) }
        coVerify(exactly = 0) { userRepository.updateUser(any()) }
    }
}