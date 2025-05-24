package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.commons.performance.PerformanceManager
import br.com.brunocarvalhs.friendssecrets.data.service.AuthService

class VerifyPhoneUseCase(
    private val authService: AuthService = AuthService(),
    private val performance: PerformanceManager = PerformanceManager()
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

