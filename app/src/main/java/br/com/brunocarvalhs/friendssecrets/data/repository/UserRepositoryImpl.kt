package br.com.brunocarvalhs.friendssecrets.data.repository

import br.com.brunocarvalhs.friendssecrets.data.model.UserModel
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import br.com.brunocarvalhs.friendssecrets.domain.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class UserRepositoryImpl(
    private val database: FirebaseFirestore = Firebase.firestore
) : UserRepository {

    override suspend fun create(user: UserEntities) = withContext(Dispatchers.IO) {
        try {
            database.collection(UserEntities.COLLECTION).document(user.id).set(user.toMap()).await()
            return@withContext user
        } catch (error: Exception) {
            throw error
        }
    }

    override suspend fun read(id: String): UserEntities? = withContext(Dispatchers.IO) {
        try {
            val result = database.collection(UserEntities.COLLECTION).document(id).get().await()
            return@withContext result.toObject(UserModel::class.java)
        } catch (error: Exception) {
            throw error
        }
    }

    override suspend fun findByEmail(email: String): UserEntities? = withContext(Dispatchers.IO) {
        try {
            val querySnapshot = database.collection(GroupEntities.COLLECTION_NAME)
                .whereEqualTo(UserEntities.EMAIL, email)
                .get()
                .await()

            if (querySnapshot.isEmpty) {
                return@withContext null
            }
            val result = querySnapshot.documents.first()

            return@withContext result.toObject(UserModel::class.java)
        } catch (error: Exception) {
            throw error
        }
    }

    override suspend fun findByPhone(phone: String): UserEntities? = withContext(Dispatchers.IO) {
        try {
            val querySnapshot = database.collection(GroupEntities.COLLECTION_NAME)
                .whereEqualTo(UserEntities.PHONE, phone)
                .get()
                .await()

            if (querySnapshot.isEmpty) {
                return@withContext null
            }
            val result = querySnapshot.documents.first()

            return@withContext result.toObject(UserModel::class.java)
        } catch (error: Exception) {
            throw error
        }
    }
}