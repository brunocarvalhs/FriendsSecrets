package br.com.brunocarvalhs.friendssecrets.domain.repository

import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import br.com.brunocarvalhs.friendssecrets.domain.repository.response.UserResponse

interface UserRepository {
    suspend fun listUsersByPhoneNumber(phoneNumber: String): List<UserResponse>
    suspend fun listUsersByPhoneNumber(list: List<String>): List<UserResponse>
    suspend fun createUser(user: UserEntities)
    suspend fun updateUser(user: UserEntities)
    suspend fun getUserById(userId: String): UserResponse?
    suspend fun getUserByPhoneNumber(phoneNumber: String): UserResponse?
    suspend fun deleteUser(userId: String)
}