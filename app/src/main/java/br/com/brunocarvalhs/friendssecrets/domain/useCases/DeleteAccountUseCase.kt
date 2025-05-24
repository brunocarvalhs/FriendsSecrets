package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.commons.performance.PerformanceManager
import br.com.brunocarvalhs.friendssecrets.data.manager.SessionManager
import br.com.brunocarvalhs.friendssecrets.domain.repository.UserRepository

class DeleteAccountUseCase(
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager = SessionManager.getInstance(),
    private val performanceManager: PerformanceManager = PerformanceManager()
) {
    suspend operator fun invoke(): Result<Unit> {
        performanceManager.start(DeleteAccountUseCase::class.java.simpleName)
        return try {
            runCatching {
                val user = sessionManager.getCurrentUserModel() ?: throw Exception("User not found")
                userRepository.deleteUser(user.id)
                sessionManager.deleteAccount()
            }
        } finally {
            performanceManager.stop(DeleteAccountUseCase::class.java.simpleName)
        }
    }
}