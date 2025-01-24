package br.com.brunocarvalhs.friendssecrets.domain.repository

import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities

interface UserRepository {
    suspend fun create(entities: UserEntities)
    fun read(string: String): UserEntities?
}