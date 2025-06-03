package br.com.brunocarvalhs.friendssecrets.domain.repositories

import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities

interface UserRepository {
    suspend fun listUsersByPhoneNumber(phoneNumber: String): List<UserEntities>
    suspend fun listUsersByPhoneNumber(list: List<String>): List<UserEntities>
    suspend fun createUser(user: UserEntities)
    suspend fun updateUser(user: UserEntities)
    suspend fun getUserById(userId: String): UserEntities?
    suspend fun getUserByPhoneNumber(phoneNumber: String): UserEntities?
    suspend fun deleteUser(userId: String)
}