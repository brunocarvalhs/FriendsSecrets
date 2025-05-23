package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.data.manager.SessionManager
import com.google.firebase.perf.metrics.AddTrace

class LogoutUseCase(
    private val sessionManager: SessionManager = SessionManager.getInstance()
) {

    /**
     * This function logs out the user by signing them out and setting them as anonymous.
     * It uses the SessionManager to perform the logout operation.
     */
    @Throws(Exception::class)
    @AddTrace(name = "LogoutUseCase.invoke")
    operator fun invoke() {
        sessionManager.signOut()
        sessionManager.setUserAnonymous()
    }
}