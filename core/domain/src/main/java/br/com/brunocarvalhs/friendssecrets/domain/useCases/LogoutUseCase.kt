package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import br.com.brunocarvalhs.friendssecrets.domain.services.PerformanceService
import br.com.brunocarvalhs.friendssecrets.domain.services.SessionService

class LogoutUseCase(
    private val session: SessionService<UserEntities>,
    private val performance: PerformanceService
) {
    suspend operator fun invoke(): Result<Unit> {
        performance.start(LogoutUseCase::class.java.simpleName)
        return try {
            runCatching {
                session.signOut()
            }
        } finally {
            performance.stop(LogoutUseCase::class.java.simpleName)
        }
    }
}
