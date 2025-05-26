package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import br.com.brunocarvalhs.friendssecrets.domain.repositories.UserRepository
import br.com.brunocarvalhs.friendssecrets.domain.services.PerformanceService
import br.com.brunocarvalhs.friendssecrets.domain.services.SessionService

class CreateProfileUseCase(
    private val session: SessionService<UserEntities>,
    private val repository: UserRepository,
    private val performance: PerformanceService
) {
    suspend operator fun invoke(
        user: UserEntities,
    ): Result<Unit> {
        performance.start(CreateProfileUseCase::class.java.simpleName)
        return try {
            session.updateUserProfile(user)
            session.getCurrentUserModel()?.let {
                repository.updateUser(
                    it.toCopy(
                        name = user.name,
                        photoUrl = user.photoUrl,
                        likes = user.likes
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