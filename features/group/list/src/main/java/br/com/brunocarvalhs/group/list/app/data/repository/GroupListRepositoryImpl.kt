package br.com.brunocarvalhs.group.list.app.data.repository

import br.com.brunocarvalhs.group.list.app.data.exceptions.GroupNotFoundException
import br.com.brunocarvalhs.group.list.app.domain.entities.GroupModel
import br.com.brunocarvalhs.group.list.app.domain.repository.GroupListRepository
import br.com.brunocarvalhs.group.list.commons.providers.GroupListCrypto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GroupListRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val cryptoService: GroupListCrypto,
) : GroupListRepository {

    override suspend fun list(groupTokens: List<String>): List<GroupModel> {
        return withContext(Dispatchers.IO) {
            val querySnapshot = firestore.collection(COLLECTION_NAME)
                .whereIn(TOKEN, groupTokens)
                .get()
                .await()

            querySnapshot.documents.map { documentSnapshot ->
                val encryptedData = documentSnapshot.data ?: throw GroupNotFoundException()
                val decryptedData = cryptoService.decryptMap(
                    encryptedData,
                    setOf(TOKEN, ID)
                )
                GroupModel.fromMap(decryptedData)
            }
        }
    }

    override suspend fun read(groupId: String): GroupModel = withContext(Dispatchers.IO) {
        val documentSnapshot = firestore.collection(COLLECTION_NAME)
            .document(groupId)
            .get()
            .await()
        if (!documentSnapshot.exists()) {
            throw GroupNotFoundException()
        }
        val encryptedData = documentSnapshot.data ?: throw GroupNotFoundException()
        val decryptedData =
            cryptoService.decryptMap(encryptedData, setOf(TOKEN, ID))
        GroupModel.fromMap(decryptedData)
    }

    override suspend fun searchByToken(token: String): GroupModel? {
        val querySnapshot = firestore.collection(COLLECTION_NAME)
            .whereEqualTo(TOKEN, token)
            .get()
            .await()

        if (querySnapshot.isEmpty) {
            return null
        }
        val documentSnapshot = querySnapshot.documents.first()
        val encryptedData = documentSnapshot.data ?: throw GroupNotFoundException()

        val decryptedData = cryptoService.decryptMap(encryptedData, setOf(TOKEN, ID))
        return GroupModel.fromMap(decryptedData)
    }

    override suspend fun delete(groupId: String): Unit = withContext(Dispatchers.IO) {
        firestore.collection(COLLECTION_NAME)
            .document(groupId)
            .delete()
            .await()
    }

    companion object {
        const val COLLECTION_NAME = "groups"
        const val TOKEN = "token"
        const val ID = "id"
    }
}