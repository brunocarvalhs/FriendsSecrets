package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.commons.performance.PerformanceManager
import br.com.brunocarvalhs.friendssecrets.data.manager.SessionManager
import br.com.brunocarvalhs.friendssecrets.domain.repository.UserRepository

class DeleteAccountUseCase(
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager,
    private val performanceManager: PerformanceManager
) {
    suspend operator fun invoke(): Result<Unit> = kotlin.runCatching {
        performanceManager.start(name = TAG)
        val user = sessionManager.getCurrentUserModel() ?: throw Exception("User not found")
        userRepository.deleteUser(user.id)
        sessionManager.deleteAccount()
        performanceManager.stop(name = TAG)
    }

    companion object {
        private const val TAG = "DeleteAccountUseCase"
    }
}