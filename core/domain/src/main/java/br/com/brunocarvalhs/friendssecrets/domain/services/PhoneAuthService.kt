package br.com.brunocarvalhs.friendssecrets.domain.services

interface PhoneAuthService {
    suspend fun <T> sendVerificationCode(phoneNumber: String, activity: T): Result<Unit>
    suspend fun signInWithCode(code: String): Result<Unit>
}