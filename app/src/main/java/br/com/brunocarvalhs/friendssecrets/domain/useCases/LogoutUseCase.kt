package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.data.manager.SessionManager

class LogoutUseCase(
    private val sessionManager: SessionManager
) {
    operator fun invoke() {
        sessionManager.signOut()
        sessionManager.setUserAnonymous()
    }
}