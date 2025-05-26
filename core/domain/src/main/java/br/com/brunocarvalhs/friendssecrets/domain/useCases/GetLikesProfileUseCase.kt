package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import br.com.brunocarvalhs.friendssecrets.domain.repositories.UserRepository
import br.com.brunocarvalhs.friendssecrets.domain.services.PerformanceService
import br.com.brunocarvalhs.friendssecrets.domain.services.SessionService

class GetLikesProfileUseCase(
    private val session: SessionService<UserEntities>,
    private val repository: UserRepository,
    private val performance: PerformanceService
) {
    suspend operator fun invoke(): Result<UserEntities> {
        performance.start(GetLikesProfileUseCase::class.java.simpleName)
        return try {
            runCatching {
                val user = session.getCurrentUserModel() ?: throw Exception("User not found")
                repository.getUserById(user.id) ?: throw Exception("User not found")
            }
        } finally {
            performance.stop(GetLikesProfileUseCase::class.java.simpleName)
        }
    }
}