package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.domain.services.AuthService
import br.com.brunocarvalhs.friendssecrets.domain.services.PerformanceService

class VerifyPhoneUseCase(
    private val authService: AuthService,
    private val performance: PerformanceService
) {
    suspend fun invoke(code: String): Result<Unit> {
        performance.start(VerifyPhoneUseCase::class.java.simpleName)
        return try {
            runCatching {
                authService.signInWithCode(code).getOrThrow()
            }
        } finally {
            performance.stop(VerifyPhoneUseCase::class.java.simpleName)
        }
    }
}

