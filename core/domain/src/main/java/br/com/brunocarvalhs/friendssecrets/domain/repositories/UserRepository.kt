package br.com.brunocarvalhs.friendssecrets.domain.repositories

import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities

/**
 * Repository for anonymous user data
 * Note: No personal data like phone numbers, emails, or names are used for lookups
 */
interface UserRepository {
    // Phone lookup removed - no personal data is stored
    
    suspend fun createUser(user: UserEntities)
    suspend fun updateUser(user: UserEntities)
    suspend fun getUserById(userId: String): UserEntities?
    suspend fun deleteUser(userId: String)
}