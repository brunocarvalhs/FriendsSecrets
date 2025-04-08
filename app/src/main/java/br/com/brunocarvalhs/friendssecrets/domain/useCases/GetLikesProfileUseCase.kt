package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.data.manager.SessionManager
import br.com.brunocarvalhs.friendssecrets.domain.repository.UserRepository

class GetLikesProfileUseCase(
    private val sessionManager: SessionManager = SessionManager.getInstance(),
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<List<String>> = kotlin.runCatching {
        val user = sessionManager.getCurrentUserModel() ?: throw Exception("User not found")
        userRepository.getUserById(user.id)?.likes ?: emptyList()
    }
}