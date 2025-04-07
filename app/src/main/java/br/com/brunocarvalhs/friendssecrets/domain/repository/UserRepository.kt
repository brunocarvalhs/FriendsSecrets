package br.com.brunocarvalhs.friendssecrets.domain.repository

import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities

interface UserRepository {
    suspend fun getUserById(userId: String): UserEntities?

    suspend fun getUserByPhoneNumber(phoneNumber: String): UserEntities?

    suspend fun createUser(user: UserEntities): Boolean

    suspend fun updateUser(user: UserEntities): Boolean

    suspend fun deleteUser(userId: String): Boolean
}