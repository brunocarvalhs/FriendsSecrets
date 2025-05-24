package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.commons.performance.PerformanceManager
import br.com.brunocarvalhs.friendssecrets.data.manager.SessionManager

class LogoutUseCase(
    private val sessionManager: SessionManager = SessionManager.getInstance(),
    private val performance: PerformanceManager = PerformanceManager()
) {
    operator fun invoke(): Result<Unit> {
        performance.start(LogoutUseCase::class.java.simpleName)
        return try {
            runCatching {
                sessionManager.signOut()
                sessionManager.setUserAnonymous()
            }
        } finally {
            performance.stop(LogoutUseCase::class.java.simpleName)
        }
    }
}
