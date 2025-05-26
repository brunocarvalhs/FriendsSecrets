package br.com.brunocarvalhs.friendssecrets.initialization.providers

import android.net.Uri
import br.com.brunocarvalhs.friendssecrets.common.session.SessionManager
import br.com.brunocarvalhs.friendssecrets.data.model.create
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.tasks.await

class SessionEventImpl(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
) : SessionManager.SessionEvent<UserEntities> {

    override fun getCurrentUserModel(): UserEntities? {
        val user = auth.currentUser ?: return null

        return UserEntities.create(
            id = user.uid,
            name = user.displayName.orEmpty(),
            photoUrl = user.photoUrl?.toString(),
            phoneNumber = user.phoneNumber.orEmpty(),
            isPhoneNumberVerified = user.phoneNumber != null,
        )
    }

    override fun isUserLoggedIn(): Boolean =
        auth.currentUser != null || auth.currentUser?.isAnonymous == true

    override fun isProfileComplete(): Boolean {
        val user = auth.currentUser ?: return false

        return user.displayName != null && user.photoUrl != null
    }

    override fun isPhoneNumberVerified(): Boolean {
        val user = auth.currentUser ?: return false

        return user.phoneNumber != null
    }

    override fun getUserName(): String? = auth.currentUser?.displayName

    override fun getUserPhotoUrl(): String? = auth.currentUser?.photoUrl?.toString()

    override fun getUserPhoneNumber(): String? = auth.currentUser?.phoneNumber

    override fun setUserAnonymous() {
        auth.signInAnonymously()
    }

    override suspend fun updateUserProfile(profile: UserEntities) {
        val user = auth.currentUser

        val profileUpdates = userProfileChangeRequest {
            displayName = profile.name
            photoUri = Uri.parse(profile.photoUrl)
        }

        user?.updateProfile(profileUpdates)?.await()
    }

    override fun signOut() {
        auth.signOut()
    }

    override fun deleteAccount() {
        val user = auth.currentUser ?: return

        user.delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    auth.signOut()
                }
            }
    }
}