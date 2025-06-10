package br.com.brunocarvalhs.friendssecrets.data.initialization.providers

import android.net.Uri
import br.com.brunocarvalhs.friendssecrets.common.session.SessionManager
import br.com.brunocarvalhs.friendssecrets.data.model.create
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import br.com.brunocarvalhs.friendssecrets.domain.services.StorageService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class SessionEventImpl(
    private val auth: FirebaseAuth,
    private val storageManager: StorageService
) : SessionManager.SessionEvent<UserEntities> {

    override suspend fun getCurrentUserModel(): UserEntities? {
        val user = auth.currentUser?.let { user ->
            val userEntity = UserEntities.create(
                id = user.uid,
                name = user.displayName.orEmpty(),
                photoUrl = user.photoUrl?.toString(),
                phoneNumber = user.phoneNumber.orEmpty(),
                isPhoneNumberVerified = user.phoneNumber != null,
            )
            storageManager.save(STORAGE_USER_KEY, userEntity)
            return@let userEntity
        }
        return user
    }

    override suspend fun isUserLoggedIn(): Boolean {
        return true
    }

    override suspend fun isProfileComplete(): Boolean {
        return withContext(Dispatchers.IO) {
            val user = auth.currentUser ?: return@withContext false
            return@withContext user.displayName != null && user.photoUrl != null
        }
    }

    override suspend fun isPhoneNumberVerified(): Boolean {
        return withContext(Dispatchers.IO) {
            val user = auth.currentUser ?: return@withContext false
            return@withContext user.phoneNumber != null
        }
    }

    override fun getUserName(): String? = auth.currentUser?.displayName

    override fun getUserPhotoUrl(): String? = auth.currentUser?.photoUrl?.toString()

    override fun getUserPhoneNumber(): String? = auth.currentUser?.phoneNumber

    override suspend fun setUserAnonymous() {
        auth.signInAnonymously()
    }

    override suspend fun updateUserProfile(profile: UserEntities) {
        val user = auth.currentUser
        val profileUpdates = userProfileChangeRequest {
            displayName = profile.name
            photoUri = Uri.parse(profile.photoUrl)
        }

        user?.updateProfile(profileUpdates)?.await()

        storageManager.save(STORAGE_USER_KEY, profile)
    }

    override suspend fun signOut() {
        withContext(Dispatchers.IO) {
            auth.signOut()
            storageManager.remove(STORAGE_USER_KEY)
        }
    }

    override suspend fun deleteAccount() {
        return withContext(Dispatchers.IO) {
            val user = auth.currentUser ?: return@withContext

            user.delete()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        auth.signOut()
                    }
                }

            storageManager.remove(STORAGE_USER_KEY)
        }
    }

    companion object {
        private const val STORAGE_USER_KEY = "user_session_model"
    }
}