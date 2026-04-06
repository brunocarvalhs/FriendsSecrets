package br.com.brunocarvalhs.group.details.app.data.repository

import br.com.brunocarvalhs.group.list.app.data.exceptions.GroupNotFoundException
import br.com.brunocarvalhs.group.details.app.domain.entities.GroupModel
import br.com.brunocarvalhs.group.details.app.domain.repository.GroupDetailsRepository
import br.com.brunocarvalhs.group.list.commons.providers.GroupListCrypto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GroupDetailsRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val cryptoService: GroupListCrypto,
) : GroupDetailsRepository {

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
        const val DRAWS = "draws"
    }
}
