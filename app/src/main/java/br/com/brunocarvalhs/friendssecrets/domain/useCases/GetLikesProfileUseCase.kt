package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.commons.performance.PerformanceManager
import br.com.brunocarvalhs.friendssecrets.data.manager.SessionManager
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import br.com.brunocarvalhs.friendssecrets.domain.repository.UserRepository
import br.com.brunocarvalhs.friendssecrets.domain.repository.response.toModel

class GetLikesProfileUseCase(
    private val sessionManager: SessionManager = SessionManager.getInstance(),
    private val userRepository: UserRepository,
    private val performanceManager: PerformanceManager = PerformanceManager()
) {
    suspend operator fun invoke(): Result<UserEntities> {
        performanceManager.start(GetLikesProfileUseCase::class.java.simpleName)
        return try {
            runCatching {
                val user = sessionManager.getCurrentUserModel() ?: throw Exception("User not found")
                userRepository.getUserById(user.id)?.toModel() ?: throw Exception("User not found")
            }
        } finally {
            performanceManager.stop(GetLikesProfileUseCase::class.java.simpleName)
        }
    }
}