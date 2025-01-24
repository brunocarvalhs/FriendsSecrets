package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.data.service.SessionService
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import br.com.brunocarvalhs.friendssecrets.domain.repository.UserRepository

class LoginUserUseCase(
    private val repository: UserRepository,
    private val service: SessionService<UserEntities>,
) {
    suspend fun invoke(): Result<UserEntities?> = runCatching {
        service.logIn()
        service.getCurrentUser()?.let { user ->
            repository.create(user)
            val data = repository.read(user.id) ?: throw Exception()
            data
        }
    }
}