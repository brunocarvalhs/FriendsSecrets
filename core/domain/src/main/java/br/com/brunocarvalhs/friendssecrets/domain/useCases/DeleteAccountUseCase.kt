package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.domain.repositories.UserRepository
import br.com.brunocarvalhs.friendssecrets.domain.services.PerformanceService
import br.com.brunocarvalhs.friendssecrets.domain.services.SessionService

class DeleteAccountUseCase(
    private val repository: UserRepository,
    private val session: SessionService,
    private val performanceManager: PerformanceService
) {
    suspend operator fun invoke(): Result<Unit> {
        performanceManager.start(DeleteAccountUseCase::class.java.simpleName)
        return try {
            runCatching {
                val user = session.getCurrentUserModel() ?: throw Exception("User not found")
                repository.deleteUser(user.id)
                session.deleteAccount()
            }
        } finally {
            performanceManager.stop(DeleteAccountUseCase::class.java.simpleName)
        }
    }
}