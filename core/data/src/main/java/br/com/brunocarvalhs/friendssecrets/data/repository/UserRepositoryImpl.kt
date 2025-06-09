package br.com.brunocarvalhs.friendssecrets.data.repository

import br.com.brunocarvalhs.friendssecrets.data.mappers.toEntities
import br.com.brunocarvalhs.friendssecrets.data.repository.dto.UserDTO
import br.com.brunocarvalhs.friendssecrets.domain.entities.UserEntities
import br.com.brunocarvalhs.friendssecrets.domain.repositories.UserRepository
import br.com.brunocarvalhs.friendssecrets.domain.services.CryptoService
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val cryptoService: CryptoService,
) : UserRepository {
    override suspend fun listUsersByPhoneNumber(phoneNumber: String): List<UserEntities> =
        withContext(Dispatchers.IO) {
            val phoneNumberEncrypted = cryptoService.encrypt(phoneNumber)

            val querySnapshot = firestore.collection(UserEntities.COLLECTION_NAME)
                .whereEqualTo(UserEntities.PHONE_NUMBER, phoneNumberEncrypted)
                .get()
                .await()

            querySnapshot.documents.map { document ->
                val encryptedData = document.data ?: emptyMap()
                val decryptedData = cryptoService.decryptMap(encryptedData, setOf(UserEntities.ID))
                UserDTO.fromMap(decryptedData).toEntities()
            }
        }

    override suspend fun listUsersByPhoneNumber(list: List<String>): List<UserEntities> =
        withContext(Dispatchers.IO) {
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
                    val decryptedData =
                        cryptoService.decryptMap(encryptedData, setOf(UserEntities.ID))
                    UserDTO.fromMap(decryptedData).toEntities()
                }

                allResults.addAll(users)
            }

            return@withContext allResults
        }

    override suspend fun createUser(user: UserEntities): Unit = withContext(Dispatchers.IO) {
        val data = cryptoService.encryptMap(user.toMap(), setOf(UserEntities.ID))

        firestore.collection(UserEntities.COLLECTION_NAME)
            .document(user.id)
            .set(data)
            .await()
    }

    override suspend fun updateUser(user: UserEntities): Unit = withContext(Dispatchers.IO) {
        val data = cryptoService.encryptMap(user.toMap(), setOf(UserEntities.ID))

        firestore.collection(UserEntities.COLLECTION_NAME)
            .document(user.id)
            .set(data)
            .await()
    }

    override suspend fun getUserById(userId: String): UserEntities? = withContext(Dispatchers.IO) {
        val documentSnapshot = firestore.collection(UserEntities.COLLECTION_NAME)
            .document(userId)
            .get()
            .await()

        if (!documentSnapshot.exists()) return@withContext null

        val encryptedData = documentSnapshot.data ?: emptyMap()
        val decryptedData = cryptoService.decryptMap(encryptedData, setOf(UserEntities.ID))
        UserDTO.fromMap(decryptedData).toEntities()
    }

    override suspend fun getUserByPhoneNumber(phoneNumber: String): UserEntities? =
        withContext(Dispatchers.IO) {
            val querySnapshot = firestore.collection(UserEntities.COLLECTION_NAME)
                .whereEqualTo(UserEntities.PHONE_NUMBER, phoneNumber)
                .get()
                .await()

            if (querySnapshot.isEmpty) return@withContext null

            val document = querySnapshot.documents.first()
            val encryptedData = document.data ?: emptyMap()
            val decryptedData = cryptoService.decryptMap(encryptedData, setOf(UserEntities.ID))
            return@withContext UserDTO.fromMap(decryptedData).toEntities()
        }

    override suspend fun deleteUser(userId: String): Unit = withContext(Dispatchers.IO) {
        firestore.collection(UserEntities.COLLECTION_NAME)
            .document(userId)
            .delete()
            .await()
    }
}