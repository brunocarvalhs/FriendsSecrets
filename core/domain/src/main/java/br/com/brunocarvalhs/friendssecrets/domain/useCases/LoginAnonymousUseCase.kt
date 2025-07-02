package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import br.com.brunocarvalhs.friendssecrets.domain.services.SessionService

class LoginAnonymousUseCase(
    private val session: SessionService<UserEntities>,
) {
    suspend operator fun invoke(): Result<Unit> {
        return try {
            runCatching {
                session.setUserAnonymous()
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}