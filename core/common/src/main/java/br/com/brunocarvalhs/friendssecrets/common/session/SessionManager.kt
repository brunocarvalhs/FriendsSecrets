package br.com.brunocarvalhs.friendssecrets.common.session

import br.com.brunocarvalhs.friendssecrets.domain.services.SessionService

class SessionManager<T>(
    private val event: SessionEvent<T>
) : SessionService<T> {
    override fun getCurrentUserModel(): T? = event.getCurrentUserModel()
    override fun isUserLoggedIn(): Boolean = event.isUserLoggedIn()
    override fun isProfileComplete(): Boolean = event.isProfileComplete()
    override fun isPhoneNumberVerified(): Boolean = event.isPhoneNumberVerified()
    override fun getUserName(): String? = event.getUserName()
    override fun getUserPhotoUrl(): String? = event.getUserPhotoUrl()
    override fun getUserPhoneNumber(): String? = event.getUserPhoneNumber()
    override fun setUserAnonymous() = event.setUserAnonymous()
    override suspend fun updateUserProfile(profile: T) = event.updateUserProfile(profile)
    override fun signOut() = event.signOut()
    override fun deleteAccount() = event.deleteAccount()

    interface SessionEvent<T> {
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
}