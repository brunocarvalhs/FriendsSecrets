package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import br.com.brunocarvalhs.friendssecrets.domain.services.SessionService
import com.google.firebase.perf.metrics.AddTrace

class LoginAnonymousUseCase(
    private val session: SessionService<UserEntities>,
) {
    @AddTrace(name = "LoginAnonymousUseCase.invoke", enabled = true)
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