package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.domain.repositories.UserRepository
import br.com.brunocarvalhs.friendssecrets.domain.services.PerformanceService
import br.com.brunocarvalhs.friendssecrets.domain.services.SessionService

class CreateProfileUseCase(
    private val session: SessionService,
    private val repository: UserRepository,
    private val performance: PerformanceService
) {
    suspend operator fun invoke(name: String, photoUrl: String, likes: List<String> = emptyList()): Result<Unit> {
        performance.start(CreateProfileUseCase::class.java.simpleName)
        return try {
            session.updateUserProfile(name, photoUrl)
            session.getCurrentUserModel()?.let { user ->
                repository.updateUser(
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
            performance.stop(CreateProfileUseCase::class.java.simpleName)
        }
    }
}