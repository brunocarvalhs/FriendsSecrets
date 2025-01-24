package br.com.brunocarvalhs.friendssecrets.data.repository

import br.com.brunocarvalhs.friendssecrets.data.model.UserModel
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import br.com.brunocarvalhs.friendssecrets.domain.repository.UserRepository
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.getField
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class UserRepositoryImpl(
    private val database: FirebaseFirestore = Firebase.firestore
) : UserRepository {

    override suspend fun create(user: UserEntities) = withContext(Dispatchers.IO) {
        database.collection(UserEntities.COLLECTION).document(user.id).set(user.toMap()).await()
        user
    }

    override suspend fun read(id: String): UserEntities? = withContext(Dispatchers.IO) {
        val documentSnapshot =
            database.collection(UserEntities.COLLECTION).document(id).get().await()
        documentSnapshot.toUserModel()
    }

    private fun DocumentSnapshot.toUserModel(): UserEntities? {
        val name = getString(UserEntities.NAME).orEmpty()
        val phone = getString(UserEntities.PHONE)
        val email = getString(UserEntities.EMAIL)
        val photo = getString(UserEntities.PHOTO)
        val likes = getField<List<String>>(UserEntities.LIKES).orEmpty()

        return UserModel(
            id = id,
            name = name,
            phone = phone,
            email = email,
            photo = photo,
            likes = likes
        )
    }
}