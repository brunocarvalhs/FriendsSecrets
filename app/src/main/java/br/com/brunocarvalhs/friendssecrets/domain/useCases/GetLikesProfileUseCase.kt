package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.data.manager.SessionManager
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import br.com.brunocarvalhs.friendssecrets.domain.repository.UserRepository

class GetLikesProfileUseCase(
    private val sessionManager: SessionManager = SessionManager.getInstance(),
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<UserEntities> = kotlin.runCatching {
        val user = sessionManager.getCurrentUserModel() ?: throw Exception("User not found")
        userRepository.getUserById(user.id) ?: throw Exception("User not found")
    }
}