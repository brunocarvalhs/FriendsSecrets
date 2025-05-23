package br.com.brunocarvalhs.friendssecrets.data.manager

import android.net.Uri
import br.com.brunocarvalhs.friendssecrets.data.model.UserModel
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.perf.metrics.AddTrace
import kotlinx.coroutines.tasks.await

class SessionManager(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
) {

    init {
        instance = this
    }

    @AddTrace(name = "SessionManager.getCurrentUserModel")
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

    @AddTrace(name = "SessionManager.isUserLoggedIn")
    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null || auth.currentUser?.isAnonymous == true
    }

    @AddTrace(name = "SessionManager.isProfileComplete")
    fun isProfileComplete(): Boolean {
        val user = auth.currentUser ?: return false

        return user.displayName != null && user.photoUrl != null
    }

    @AddTrace(name = "SessionManager.isPhoneNumberVerified")
    fun isPhoneNumberVerified(): Boolean {
        val user = auth.currentUser ?: return false

        return user.phoneNumber != null
    }

    @AddTrace(name = "SessionManager.getUserName")
    fun getUserName(): String? {
        return auth.currentUser?.displayName
    }

    @AddTrace(name = "SessionManager.getUserPhotoUrl")
    fun getUserPhotoUrl(): String? {
        return auth.currentUser?.photoUrl?.toString()
    }

    @AddTrace(name = "SessionManager.getUserPhoneNumber")
    fun getUserPhoneNumber(): String? {
        return auth.currentUser?.phoneNumber
    }

    @AddTrace(name = "SessionManager.setUserAnonymous")
    fun setUserAnonymous() {
        auth.signInAnonymously()
    }

    @AddTrace(name = "SessionManager.updateUserProfile")
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

    @AddTrace(name = "SessionManager.signOut")
    fun signOut() {
        auth.signOut()
    }

    @AddTrace(name = "SessionManager.deleteAccount")
    fun deleteAccount() {
        val user = auth.currentUser ?: return

        user.delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    auth.signOut()
                }
            }
    }

    companion object {
        private lateinit var instance: SessionManager
            private set

        @JvmStatic
        @Synchronized
        @AddTrace(name = "SessionManager.getInstance")
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