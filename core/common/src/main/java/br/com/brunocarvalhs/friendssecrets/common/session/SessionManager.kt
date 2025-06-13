package br.com.brunocarvalhs.friendssecrets.common.session

import br.com.brunocarvalhs.friendssecrets.domain.services.SessionService

class SessionManager<T>(
    private val event: SessionEvent<T>
) : SessionService<T> {
    override suspend fun getCurrentUserModel(): T? = event.getCurrentUserModel()
    override suspend fun isUserLoggedIn(): Boolean = event.isUserLoggedIn()
    override suspend fun isProfileComplete(): Boolean = event.isProfileComplete()
    override suspend fun isPhoneNumberVerified(): Boolean = event.isPhoneNumberVerified()
    override fun getUserName(): String? = event.getUserName()
    override fun getUserPhotoUrl(): String? = event.getUserPhotoUrl()
    override fun getUserPhoneNumber(): String? = event.getUserPhoneNumber()
    override suspend fun setUserAnonymous() = event.setUserAnonymous()
    override suspend fun updateUserProfile(profile: T) = event.updateUserProfile(profile)
    override suspend fun signOut() = event.signOut()
    override suspend fun deleteAccount() = event.deleteAccount()

    interface SessionEvent<T> {
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
}