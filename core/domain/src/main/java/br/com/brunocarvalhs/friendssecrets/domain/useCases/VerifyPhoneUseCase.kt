package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.domain.services.PerformanceService
import br.com.brunocarvalhs.friendssecrets.domain.services.PhoneAuthService

class VerifyPhoneUseCase(
    private val phoneAuthService: PhoneAuthService,
    private val performance: PerformanceService
) {
    suspend fun invoke(code: String): Result<Unit> {
        performance.start(VerifyPhoneUseCase::class.java.simpleName)
        return try {
            runCatching {
                phoneAuthService.signInWithCode(code).getOrThrow()
            }
        } finally {
            performance.stop(VerifyPhoneUseCase::class.java.simpleName)
        }
    }
}

