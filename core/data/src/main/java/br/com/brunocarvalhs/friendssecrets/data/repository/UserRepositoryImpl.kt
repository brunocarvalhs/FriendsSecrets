package br.com.brunocarvalhs.friendssecrets.data.repository

import br.com.brunocarvalhs.friendssecrets.data.mappers.toEntities
import br.com.brunocarvalhs.friendssecrets.data.repository.dto.UserDTO
import br.com.brunocarvalhs.friendssecrets.domain.repositories.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.perf.metrics.AddTrace
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Repository implementation for anonymous user data
 * No personal data lookups - only ID-based access
 */
class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
) : UserRepository {

    @AddTrace(name = "UserRepositoryImpl.createUser", enabled = true)
    override suspend fun createUser(user: UserEntities): Unit = withContext(Dispatchers.IO) {
        val data = user.toMap()

        firestore.collection(UserEntities.COLLECTION_NAME)
            .document(user.id)
            .set(data)
            .await()
    }

    @AddTrace(name = "UserRepositoryImpl.updateUser", enabled = true)
    override suspend fun updateUser(user: UserEntities): Unit = withContext(Dispatchers.IO) {
        val data = user.toMap()

        firestore.collection(UserEntities.COLLECTION_NAME)
            .document(user.id)
            .set(data)
            .await()
    }

    @AddTrace(name = "UserRepositoryImpl.getUserById", enabled = true)
    override suspend fun getUserById(userId: String): UserEntities? = withContext(Dispatchers.IO) {
        val documentSnapshot = firestore.collection(UserEntities.COLLECTION_NAME)
            .document(userId)
            .get()
            .await()

        if (!documentSnapshot.exists()) return@withContext null

        val data = documentSnapshot.data ?: emptyMap()
        UserDTO.fromMap(data).toEntities()
    }

    @AddTrace(name = "UserRepositoryImpl.deleteUser", enabled = true)
    override suspend fun deleteUser(userId: String): Unit = withContext(Dispatchers.IO) {
        firestore.collection(UserEntities.COLLECTION_NAME)
            .document(userId)
            .delete()
            .await()
    }

    // ============================================================================
    // REMOVED METHODS - Phone-based lookups no longer available
    // ============================================================================
    // suspend fun listUsersByPhoneNumber(phoneNumber: String): List<UserEntities>
    // suspend fun listUsersByPhoneNumber(list: List<String>): List<UserEntities>
    // suspend fun getUserByPhoneNumber(phoneNumber: String): UserEntities?
    //
    // Reason: These methods enabled PII (Personally Identifiable Information) lookups
    // which contradicts the app's commitment to anonymity and privacy.
}