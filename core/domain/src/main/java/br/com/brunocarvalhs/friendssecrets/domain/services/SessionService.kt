package br.com.brunocarvalhs.friendssecrets.domain.services

interface SessionService<T> {
    fun getCurrentUserModel(): T?
    fun isUserLoggedIn(): Boolean
    fun isProfileComplete(): Boolean
    fun isPhoneNumberVerified(): Boolean
    fun getUserName(): String?
    fun getUserPhotoUrl(): String?
    fun getUserPhoneNumber(): String?
    fun setUserAnonymous()
    suspend fun updateUserProfile(profile: T)
    fun signOut()
    fun deleteAccount()
}