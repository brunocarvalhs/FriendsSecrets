package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import br.com.brunocarvalhs.friendssecrets.domain.repositories.UserRepository
import br.com.brunocarvalhs.friendssecrets.domain.services.PerformanceService
import br.com.brunocarvalhs.friendssecrets.domain.services.SessionService

class DeleteAccountUseCase(
    private val repository: UserRepository,
    private val session: SessionService<UserEntities>,
    private val performance: PerformanceService
) {
    suspend operator fun invoke(): Result<Unit> {
        performance.start(DeleteAccountUseCase::class.java.simpleName)
        return try {
            runCatching {
                val user = session.getCurrentUserModel() ?: throw Exception("User not found")
                repository.deleteUser(user.id)
                session.deleteAccount()
            }
        } finally {
            performance.stop(DeleteAccountUseCase::class.java.simpleName)
        }
    }
}