package br.com.brunocarvalhs.friendssecrets.domain.services

interface SessionService<T> {
    suspend fun getCurrentUserModel(): T?
    suspend fun isUserLoggedIn(): Boolean
    suspend fun isProfileComplete(): Boolean
    suspend fun isPhoneNumberVerified(): Boolean
    fun getUserName(): String?
    fun getUserPhotoUrl(): String?
    fun getUserPhoneNumber(): String?
    suspend fun setUserAnonymous()
    suspend fun updateUserProfile(profile: T)
    suspend fun signOut()
    suspend fun deleteAccount()
}