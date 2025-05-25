package br.com.brunocarvalhs.friendssecrets.domain.services

interface AuthService {
    suspend fun sendVerificationCode(phoneNumber: String): Result<Unit>
    suspend fun signInWithCode(code: String): Result<Unit>
    fun signOut()
    fun isUserLoggedIn(): Boolean
}