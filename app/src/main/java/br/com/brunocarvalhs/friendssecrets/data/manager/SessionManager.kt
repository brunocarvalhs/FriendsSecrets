package br.com.brunocarvalhs.friendssecrets.data.manager

import android.net.Uri
import br.com.brunocarvalhs.friendssecrets.data.model.UserModel
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.tasks.await

class SessionManager(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
) {

    init {
        instance = this
    }

    fun getCurrentUserModel(): UserEntities? {
        val user = auth.currentUser ?: return null

        return UserModel(
            id = user.uid,
            name = user.displayName.orEmpty(),
            photoUrl = user.photoUrl?.toString(),
            phoneNumber = user.phoneNumber.orEmpty(),
            isPhoneNumberVerified = user.phoneNumber != null,
        )
    }

    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    fun isProfileComplete(): Boolean {
        val user = auth.currentUser ?: return false

        return user.displayName != null && user.photoUrl != null
    }

    fun isPhoneNumberVerified(): Boolean {
        val user = auth.currentUser ?: return false

        return user.phoneNumber != null
    }

    fun getUserId(): String? {
        return auth.currentUser?.uid
    }

    fun getUserName(): String? {
        return auth.currentUser?.displayName
    }

    fun getUserPhotoUrl(): String? {
        return auth.currentUser?.photoUrl?.toString()
    }

    suspend fun updateUserProfile(
        name: String,
        photoUrl: String
    ) {
        val user = auth.currentUser

        val profileUpdates = userProfileChangeRequest {
            displayName = name
            photoUri = Uri.parse(photoUrl)
        }

        user?.updateProfile(profileUpdates)?.await()
    }

    companion object {
        private lateinit var instance: SessionManager
            private set

        fun getInstance(): SessionManager {
            synchronized(this) {
                if (!::instance.isInitialized) {
                    instance = SessionManager()
                }
                return instance
            }
        }
    }
}