package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.data.manager.SessionManager
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class LogoutUseCaseTest {

    @Test
    fun `invoke should call signOut and setUserAnonymous`() {
        // Arrange
        val sessionManager = mockk<SessionManager>(relaxed = true)
        val logoutUseCase = LogoutUseCase(sessionManager)

        // Act
        logoutUseCase.invoke()

        // Assert
        verify(exactly = 1) { sessionManager.signOut() }
        verify(exactly = 1) { sessionManager.setUserAnonymous() }
    }
}