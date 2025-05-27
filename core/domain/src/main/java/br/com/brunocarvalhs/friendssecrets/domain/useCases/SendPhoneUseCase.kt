package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.domain.services.PerformanceService
import br.com.brunocarvalhs.friendssecrets.domain.services.PhoneAuthService

class SendPhoneUseCase(
    private val phoneAuthService: PhoneAuthService,
    private val performance: PerformanceService,
) {
    suspend fun <T> invoke(
        activity: T,
        phone: String,
        countryCode: String
    ): Result<Unit> {
        performance.start(SendPhoneUseCase::class.java.simpleName)
        return try {
            runCatching {
                phoneAuthService.sendVerificationCode(
                    phoneNumber = "$countryCode$phone",
                    activity = activity,
                ).getOrThrow()
            }
        } finally {
            performance.stop(SendPhoneUseCase::class.java.simpleName)
        }
    }
}
