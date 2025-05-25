package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.domain.services.AuthService
import br.com.brunocarvalhs.friendssecrets.domain.services.PerformanceService

class SendPhoneUseCase(
    private val authService: AuthService,
    private val performance: PerformanceService
) {
    suspend fun invoke(phone: String, countryCode: String): Result<Unit> {
        performance.start(SendPhoneUseCase::class.java.simpleName)
        return try {
            runCatching {
                authService.sendVerificationCode(
                    phoneNumber = "$countryCode$phone",
                ).getOrThrow()
            }
        } finally {
            performance.stop(SendPhoneUseCase::class.java.simpleName)
        }
    }
}
