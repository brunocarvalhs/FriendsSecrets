package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.commons.performance.PerformanceManager
import br.com.brunocarvalhs.friendssecrets.data.manager.SessionManager
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import br.com.brunocarvalhs.friendssecrets.domain.repository.UserRepository

class CreateProfileUseCase(
    private val sessionManager: SessionManager,
    private val userRepository: UserRepository,
    private val performanceManager: PerformanceManager
) {
    suspend operator fun invoke(
        data: UserEntities
    ): Result<Unit> {
        return try {
            performanceManager.start(name = TAG)
            sessionManager.updateUserProfile(data.name, data.photoUrl.orEmpty())
            userRepository.updateUser(data)
            performanceManager.stop(name = TAG)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    companion object {
        private const val TAG = "CreateProfileUseCase"
    }
}