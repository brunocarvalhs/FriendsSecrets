package br.com.brunocarvalhs.friendssecrets.data.initialization.providers

import android.net.Uri
import br.com.brunocarvalhs.friendssecrets.common.session.SessionManager
import br.com.brunocarvalhs.friendssecrets.data.model.UserModel
import br.com.brunocarvalhs.friendssecrets.data.model.create
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import br.com.brunocarvalhs.friendssecrets.domain.services.StorageService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class SessionEventImpl(
    private val auth: FirebaseAuth,
    private val storageManager: StorageService
) : SessionManager.SessionEvent<UserEntities> {

    private val _currentUser = MutableStateFlow<UserEntities?>(null)
    private val mutex = Mutex()
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    init {
        observeAuthState()
        loadCachedUser()
    }

    override suspend fun getCurrentUserModel(): UserEntities? {
        return auth.currentUser?.toUserEntity()?.also {
            storageManager.save(STORAGE_USER_KEY, it)
        }
    }

    override suspend fun isUserLoggedIn(): Boolean = withContext(Dispatchers.IO) {
        mutex.withLock {
            auth.currentUser != null || hasLocalSession()
        }
    }

    override suspend fun isProfileComplete(): Boolean = withContext(Dispatchers.IO) {
        auth.currentUser?.run { displayName != null && photoUrl != null } ?: false
    }

    override suspend fun isPhoneNumberVerified(): Boolean = withContext(Dispatchers.IO) {
        auth.currentUser?.phoneNumber != null
    }

    override fun getUserName(): String? = auth.currentUser?.displayName

    override fun getUserPhotoUrl(): String? = auth.currentUser?.photoUrl?.toString()

    override fun getUserPhoneNumber(): String? = auth.currentUser?.phoneNumber

    override suspend fun setUserAnonymous() {
        auth.signInAnonymously().await()
    }

    override suspend fun updateUserProfile(profile: UserEntities) {
        auth.currentUser?.apply {
            val updates = userProfileChangeRequest {
                displayName = profile.name
                photoUri = Uri.parse(profile.photoUrl)
            }
            updateProfile(updates).await()
            storageManager.save(STORAGE_USER_KEY, profile)
        }
    }

    override suspend fun signOut() = withContext(Dispatchers.IO) {
        auth.signOut()
        storageManager.remove(STORAGE_USER_KEY)
    }

    override suspend fun deleteAccount() = withContext(Dispatchers.IO) {
        auth.currentUser?.run {
            delete().await()
            auth.signOut()
        }
        storageManager.remove(STORAGE_USER_KEY)
    }

    private suspend fun hasLocalSession(): Boolean = withContext(Dispatchers.IO) {
        storageManager.load(USER_SESSION_KEY, UserModel::class.java) != null
    }

    private fun observeAuthState() {
        auth.addAuthStateListener { firebaseAuth ->
            scope.launch {
                firebaseAuth.currentUser?.let { syncFirebaseUserToCache(it) }
                    ?: loadCachedUser()
            }
        }
    }

    private fun loadCachedUser() {
        scope.launch {
            _currentUser.value = loadUserFromStorage()
        }
    }

    private suspend fun loadUserFromStorage(): UserEntities? = withContext(Dispatchers.IO) {
        return@withContext try {
            storageManager.load(USER_SESSION_KEY, UserModel::class.java)
        } catch (e: Exception) {
            Timber.e(e, "Erro ao carregar usuário do cache")
            null
        }
    }

    private suspend fun syncFirebaseUserToCache(user: FirebaseUser) = withContext(Dispatchers.IO) {
        try {
            val lastSync = storageManager.load(LAST_SYNC_KEY, Long::class.java) ?: 0L
            if (System.currentTimeMillis() - lastSync < SYNC_INTERVAL) return@withContext

            saveUserSession(user.toUserEntity())
        } catch (e: Exception) {
            Timber.e(e, "Erro ao sincronizar usuário com cache")
        }
    }

    private suspend fun saveUserSession(user: UserEntities) = withContext(Dispatchers.IO) {
        mutex.withLock {
            try {
                storageManager.save(USER_SESSION_KEY, user.toMap())
                storageManager.save(LAST_SYNC_KEY, System.currentTimeMillis())
                _currentUser.value = user
            } catch (e: Exception) {
                Timber.e(e, "Erro ao salvar sessão do usuário")
                throw e
            }
        }
    }

    private fun FirebaseUser.toUserEntity(): UserEntities = UserEntities.create(
        id = uid,
        name = displayName.orEmpty(),
        photoUrl = photoUrl?.toString(),
        phoneNumber = phoneNumber.orEmpty(),
        isAnonymous = isAnonymous,
        lastLogin = System.currentTimeMillis(),
        isActive = true,
        isPhoneNumberVerified = phoneNumber != null,
    )

    companion object {
        private const val STORAGE_USER_KEY = "user_session_model"
        private const val USER_SESSION_KEY = "user_session"
        private const val LAST_SYNC_KEY = "last_sync_timestamp"
        private const val SYNC_INTERVAL = 5 * 60 * 1000L
    }
}
