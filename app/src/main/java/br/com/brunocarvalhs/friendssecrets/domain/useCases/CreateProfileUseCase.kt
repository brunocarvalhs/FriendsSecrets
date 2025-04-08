package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.data.manager.SessionManager

class CreateProfileUseCase(
    private val sessionManager: SessionManager,
) {
    suspend operator fun invoke(name: String, photoUrl: String): Result<Unit> {
        return try {
            sessionManager.updateUserProfile(name, photoUrl)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}