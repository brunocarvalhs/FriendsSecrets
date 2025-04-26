package br.com.brunocarvalhs.friendssecrets.domain.useCases

import android.app.Activity
import br.com.brunocarvalhs.friendssecrets.data.service.AuthService

class SendPhoneUseCase(
    private val activity: Activity,
    private val authService: AuthService
) {
    suspend fun invoke(phone: String, countryCode: String): Result<Unit> {
        return authService.sendVerificationCode(
            phoneNumber = "$countryCode$phone",
            activity = activity
        )
    }
}