package br.com.brunocarvalhs.friendssecrets.data.repository

import br.com.brunocarvalhs.friendssecrets.data.mappers.toDTO
import br.com.brunocarvalhs.friendssecrets.data.mappers.toEntities
import br.com.brunocarvalhs.friendssecrets.data.model.GroupModel
import br.com.brunocarvalhs.friendssecrets.data.repository.dto.GroupDTO
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.exceptions.GroupNotFoundException
import br.com.brunocarvalhs.friendssecrets.domain.repositories.GroupRepository
import br.com.brunocarvalhs.friendssecrets.domain.services.CryptoService
import br.com.brunocarvalhs.friendssecrets.domain.services.DrawService
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GroupRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val cryptoService: CryptoService,
    private val drawService: DrawService,
) : GroupRepository {

    override suspend fun create(group: GroupEntities): Unit = withContext(Dispatchers.IO) {
        val payload = group.toDTO()

        val data =
            cryptoService.encryptMap(payload.toMap(), setOf(GroupEntities.TOKEN, GroupEntities.ID))

        firestore.collection(GroupEntities.COLLECTION_NAME)
            .document(payload.id)
            .set(data)
            .await()
    }

    override suspend fun read(groupId: String): GroupEntities = withContext(Dispatchers.IO) {
        val documentSnapshot = firestore.collection(GroupEntities.COLLECTION_NAME)
            .document(groupId)
            .get()
            .await()
        if (!documentSnapshot.exists()) {
            throw GroupNotFoundException()
        }
        val encryptedData = documentSnapshot.data ?: throw GroupNotFoundException()
        val decryptedData =
            cryptoService.decryptMap(encryptedData, setOf(GroupEntities.TOKEN, GroupEntities.ID))
        GroupDTO.fromMap(decryptedData).toEntities()
    }

    override suspend fun update(group: GroupEntities): Unit = withContext(Dispatchers.IO) {
        val payload = group.toDTO()

        val data =
            cryptoService.encryptMap(payload.toMap(), setOf(GroupEntities.TOKEN, GroupEntities.ID))

        firestore.collection(GroupEntities.COLLECTION_NAME)
            .document(payload.id)
            .set(data)
            .await()
    }

    override suspend fun delete(groupId: String): Unit = withContext(Dispatchers.IO) {
        firestore.collection(GroupEntities.COLLECTION_NAME).document(groupId).delete().await()
    }

    override suspend fun list(list: List<String>): List<GroupEntities> =
        withContext(Dispatchers.IO) {
            val querySnapshot = firestore.collection(GroupEntities.COLLECTION_NAME)
                .whereIn(GroupEntities.TOKEN, list)
                .get()
                .await()

            querySnapshot.documents.map { documentSnapshot ->
                val encryptedData = documentSnapshot.data ?: throw GroupNotFoundException()
                val decryptedData = cryptoService.decryptMap(
                    encryptedData,
                    setOf(GroupEntities.TOKEN, GroupEntities.ID)
                )
                GroupDTO.fromMap(decryptedData).toEntities()
            }
        }

    override suspend fun searchByToken(token: String): GroupEntities? {
        val querySnapshot = firestore.collection(GroupEntities.COLLECTION_NAME)
            .whereEqualTo(GroupEntities.TOKEN, token)
            .get()
            .await()

        if (querySnapshot.isEmpty) {
            return null
        }
        val documentSnapshot = querySnapshot.documents.first()
        val encryptedData = documentSnapshot.data ?: throw GroupNotFoundException()

        val decryptedData =
            cryptoService.decryptMap(encryptedData, setOf(GroupEntities.TOKEN, GroupEntities.ID))
        return GroupDTO.fromMap(decryptedData).toEntities()
    }

    override suspend fun drawMembers(group: GroupEntities) {
        firestore.runTransaction { transaction ->
            val groupDocRef = firestore.collection(GroupEntities.COLLECTION_NAME).document(group.id)
            val snapshot = transaction.get(groupDocRef)
            val currentGroupData = cryptoService.decryptMap(
                snapshot.data ?: mapOf(),
                setOf(GroupEntities.TOKEN, GroupEntities.ID)
            )
            val currentGroup = GroupModel.fromMap(currentGroupData)

            val secretSantaMap =
                drawService.drawMembers(currentGroup.members.map { it.name }.toMutableList())

            transaction.update(groupDocRef, GroupEntities.DRAWS, secretSantaMap)
            null
        }.await()
    }
}
