package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.data.manager.SessionManager
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import br.com.brunocarvalhs.friendssecrets.domain.repository.UserRepository

class LoginUserUseCase(
    private val repository: UserRepository,
    private val service: SessionManager,
) {
    suspend fun invoke(): Result<UserEntities> = runCatching {
        val data = service.getCurrentUserModel() ?: throw Exception("User not found")
        repository.createUser(data)
        data
    }
}