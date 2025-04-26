package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.commons.performance.PerformanceManager
import br.com.brunocarvalhs.friendssecrets.data.manager.SessionManager
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import br.com.brunocarvalhs.friendssecrets.domain.repository.UserRepository

class GetLikesProfileUseCase(
    private val sessionManager: SessionManager = SessionManager.getInstance(),
    private val userRepository: UserRepository,
    private val performanceManager: PerformanceManager
) {
    suspend operator fun invoke(): Result<UserEntities> = kotlin.runCatching {
        performanceManager.start(TAG)
        var user = sessionManager.getCurrentUserModel() ?: throw Exception("User not found")
        user = userRepository.getUserById(user.id) ?: throw Exception("User not found")
        performanceManager.stop(TAG)
        return Result.success(user)
    }

    companion object {
        private const val TAG = "GetLikesProfileUseCase"
    }
}