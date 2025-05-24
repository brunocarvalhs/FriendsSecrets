package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.commons.performance.PerformanceManager
import br.com.brunocarvalhs.friendssecrets.data.manager.SessionManager
import br.com.brunocarvalhs.friendssecrets.domain.repository.UserRepository

class CreateProfileUseCase(
    private val sessionManager: SessionManager,
    private val userRepository: UserRepository,
    private val performanceManager: PerformanceManager
) {
    suspend operator fun invoke(name: String, photoUrl: String, likes: List<String> = emptyList()): Result<Unit> {
        performanceManager.start(CreateProfileUseCase::class.java.simpleName)
        return try {
            sessionManager.updateUserProfile(name, photoUrl)
            sessionManager.getCurrentUserModel()?.let { user ->
                userRepository.updateUser(
                    user.toCopy(
                        name = name,
                        photoUrl = photoUrl,
                        likes = likes.isNotEmpty().let { likes }
                    )
                )
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        } finally {
            performanceManager.start(CreateProfileUseCase::class.java.simpleName)
        }
    }
}