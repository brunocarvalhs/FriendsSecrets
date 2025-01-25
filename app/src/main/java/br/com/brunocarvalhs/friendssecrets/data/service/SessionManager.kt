package br.com.brunocarvalhs.friendssecrets.data.service

import android.net.Uri
import br.com.brunocarvalhs.friendssecrets.data.model.UserModel
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import kotlin.let
import kotlin.text.isEmpty

interface SessionService<T> {
    fun isLoggedIn(): Boolean
    suspend fun logOut()
    suspend fun logIn()
    fun getCurrentUser(): T?
    fun setCurrentUser(session: T)
    fun isProfileCompleted(): Boolean

    fun delete()
}

class SessionManager(
    private val auth: FirebaseAuth = Firebase.auth
) : SessionService<UserEntities> {

    override fun isLoggedIn(): Boolean {
        return session != null
    }

    override suspend fun logOut() {
        auth.signOut()
        session = null
    }

    override suspend fun logIn() {
        auth.currentUser?.let { data -> session = data.toUserModel() }
    }

    override fun getCurrentUser(): UserEntities? {
        return session ?: run {
            session = auth.currentUser?.toUserModel()
            return session
        }
    }

    override fun setCurrentUser(user: UserEntities) {
        val profileUpdates = userProfileChangeRequest {
            if (user.name.isEmpty() == true) displayName = user.name
            if (photoUri != null) photoUri = Uri.parse(user.photo)
        }
        auth.currentUser?.updateProfile(profileUpdates)
        session = user
    }

    override fun isProfileCompleted(): Boolean {
        return session?.name?.isNotEmpty() == true
    }

    override fun delete() {
        FirebaseAuth.getInstance().currentUser?.delete()
    }

    private companion object {
        var session: UserEntities? = Firebase.auth.currentUser?.toUserModel()
    }
}

private fun FirebaseUser.toUserModel(): UserEntities {
    return UserModel(
        id = uid,
        name = displayName.orEmpty(),
        email = email,
        phone = phoneNumber,
        photo = photoUrl.toString(),
    )
}