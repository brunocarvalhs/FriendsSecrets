package br.com.brunocarvalhs.friendssecrets.domain.useCases

import android.app.Activity
import br.com.brunocarvalhs.friendssecrets.commons.performance.PerformanceManager
import br.com.brunocarvalhs.friendssecrets.data.service.AuthService

class SendPhoneUseCase(
    private val activity: Activity,
    private val authService: AuthService = AuthService(),
    private val performance: PerformanceManager = PerformanceManager()
) {
    suspend fun invoke(phone: String, countryCode: String): Result<Unit> {
        performance.start(SendPhoneUseCase::class.java.simpleName)
        return try {
            runCatching {
                authService.sendVerificationCode(
                    phoneNumber = "$countryCode$phone",
                    activity = activity
                ).getOrThrow()
            }
        } finally {
            performance.stop(SendPhoneUseCase::class.java.simpleName)
        }
    }
}
