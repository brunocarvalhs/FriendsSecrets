package br.com.brunocarvalhs.friendssecrets.data.repository

import br.com.brunocarvalhs.friendssecrets.commons.security.CryptoService
import br.com.brunocarvalhs.friendssecrets.data.model.UserModel
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import br.com.brunocarvalhs.friendssecrets.domain.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl(
    private val firestore: FirebaseFirestore = Firebase.firestore,
    private val cryptoService: CryptoService = CryptoService(),
) : UserRepository {
    override suspend fun listUsersByPhoneNumber(phoneNumber: String): List<UserEntities> {
        val phoneNumberEncrypted = cryptoService.encrypt(phoneNumber)

        val querySnapshot = firestore.collection(UserEntities.COLLECTION_NAME)
            .whereEqualTo(UserEntities.PHONE_NUMBER, phoneNumberEncrypted)
            .get()
            .await()

        return querySnapshot.documents.map { document ->
            val encryptedData = document.data ?: emptyMap()
            val decryptedData = cryptoService.decryptMap(encryptedData, setOf(UserEntities.ID))
            UserModel.fromMap(decryptedData)
        }
    }

    override suspend fun listUsersByPhoneNumber(list: List<String>): List<UserEntities> {
        val phoneNumbersEncrypted = list.map { cryptoService.encrypt(it) }

        val chunks = phoneNumbersEncrypted.chunked(10)
        val allResults = mutableListOf<UserEntities>()

        for (chunk in chunks) {
            val querySnapshot = firestore.collection(UserEntities.COLLECTION_NAME)
                .whereIn(UserEntities.PHONE_NUMBER, chunk)
                .get()
                .await()

            val users = querySnapshot.documents.map { document ->
                val encryptedData = document.data ?: emptyMap()
                val decryptedData = cryptoService.decryptMap(encryptedData, setOf(UserEntities.ID))
                UserModel.fromMap(decryptedData)
            }

            allResults.addAll(users)
        }

        return allResults
    }

    override suspend fun createUser(user: UserEntities) {
        val data = cryptoService.encryptMap(user.toMap(), setOf(UserEntities.ID))

        firestore.collection(UserEntities.COLLECTION_NAME)
            .document(user.id)
            .set(data)
            .await()
    }

    override suspend fun updateUser(user: UserEntities) {
        val data = cryptoService.encryptMap(user.toMap(), setOf(UserEntities.ID))

        firestore.collection(UserEntities.COLLECTION_NAME)
            .document(user.id)
            .set(data)
            .await()
    }

    override suspend fun getUserById(userId: String): UserEntities? {
        val documentSnapshot = firestore.collection(UserEntities.COLLECTION_NAME)
            .document(userId)
            .get()
            .await()

        if (!documentSnapshot.exists()) return null

        val encryptedData = documentSnapshot.data ?: emptyMap()
        val decryptedData = cryptoService.decryptMap(encryptedData, setOf(UserEntities.ID))
        return UserModel.fromMap(decryptedData)
    }

    override suspend fun getUserByPhoneNumber(phoneNumber: String): UserEntities? {
        val querySnapshot = firestore.collection(UserEntities.COLLECTION_NAME)
            .whereEqualTo(UserEntities.PHONE_NUMBER, phoneNumber)
            .get()
            .await()

        if (querySnapshot.isEmpty) return null

        val document = querySnapshot.documents.first()
        val encryptedData = document.data ?: emptyMap()
        val decryptedData = cryptoService.decryptMap(encryptedData, setOf(UserEntities.ID))
        return UserModel.fromMap(decryptedData)
    }

    override suspend fun deleteUser(userId: String) {
        firestore.collection(UserEntities.COLLECTION_NAME)
            .document(userId)
            .delete()
            .await()
    }
}