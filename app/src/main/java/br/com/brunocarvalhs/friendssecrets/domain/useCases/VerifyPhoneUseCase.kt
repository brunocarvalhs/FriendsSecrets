package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.data.service.AuthService

class VerifyPhoneUseCase(
    private val authService: AuthService = AuthService()
) {
    suspend fun invoke(code: String): Result<Unit> {
        return authService.signInWithCode(code)
    }
}
