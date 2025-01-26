package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import br.com.brunocarvalhs.friendssecrets.domain.repository.UserRepository

class UserByNameOrPhoneUseCase(
    private val repository: UserRepository
) {
    suspend fun invoke(value: String): Result<UserEntities?> = runCatching {
        when {
            isPhone(value) -> repository.findByPhone(value)
            isEmail(value) -> repository.findByEmail(value)
            else -> null
        }
    }

    private fun isPhone(value: String): Boolean {
        val phoneRegex = "^\\+?[1-9]\\d{1,14}\$".toRegex()
        return phoneRegex.matches(value)
    }

    private fun isEmail(value: String): Boolean {
        val emailRegex = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}\$".toRegex()
        return emailRegex.matches(value)
    }
}
