package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.data.service.AuthService
import com.google.firebase.perf.metrics.AddTrace

class VerifyPhoneUseCase(
    private val authService: AuthService = AuthService()
) {

    /**
     * This function verifies the phone number by signing in with the provided code.
     * It uses the AuthService to perform the operation.
     *
     * @param code The verification code sent to the phone number.
     * @return A Result containing Unit if successful, or an exception if an error occurs.
     */
    @Throws(Exception::class)
    @AddTrace(name = "VerifyPhoneUseCase.invoke")
    suspend fun invoke(code: String): Result<Unit> {
        return authService.signInWithCode(code)
    }
}
