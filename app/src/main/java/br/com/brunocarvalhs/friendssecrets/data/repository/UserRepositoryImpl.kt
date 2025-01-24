package br.com.brunocarvalhs.friendssecrets.data.repository

import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import br.com.brunocarvalhs.friendssecrets.domain.repository.UserRepository

class UserRepositoryImpl : UserRepository {
    override suspend fun create(entities: UserEntities) {
        TODO("Not yet implemented")
    }

    override fun read(string: String): UserEntities? {
        TODO("Not yet implemented")
    }
}