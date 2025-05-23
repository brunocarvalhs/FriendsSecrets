package br.com.brunocarvalhs.friendssecrets.domain.useCases

import android.app.Activity
import br.com.brunocarvalhs.friendssecrets.data.service.AuthService
import com.google.firebase.perf.metrics.AddTrace

class SendPhoneUseCase(
    private val activity: Activity,
    private val authService: AuthService = AuthService()
) {

    /**
     * This function sends a verification code to the provided phone number.
     * It uses the AuthService to perform the operation.
     *
     * @param phone The phone number to which the verification code will be sent.
     * @param countryCode The country code for the phone number.
     * @return A Result containing Unit if successful, or an exception if an error occurs.
     */
    @Throws(Exception::class)
    @AddTrace(name = "SendPhoneUseCase.invoke")
    suspend fun invoke(phone: String, countryCode: String): Result<Unit> {
        return authService.sendVerificationCode(
            phoneNumber = "$countryCode$phone",
            activity = activity
        )
    }
}