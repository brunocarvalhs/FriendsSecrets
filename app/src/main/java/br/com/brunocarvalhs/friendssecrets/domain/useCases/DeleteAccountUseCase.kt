package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.data.manager.SessionManager
import br.com.brunocarvalhs.friendssecrets.domain.repository.UserRepository

class DeleteAccountUseCase(
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager = SessionManager.getInstance()
) {
    suspend operator fun invoke(): Result<Unit> = kotlin.runCatching {
        val user = sessionManager.getCurrentUserModel() ?: throw Exception("User not found")
        userRepository.deleteUser(user.id)
        sessionManager.deleteAccount()
    }
}