package br.com.brunocarvalhs.friendssecrets.data.repository

import br.com.brunocarvalhs.friendssecrets.commons.security.CryptoService
import br.com.brunocarvalhs.friendssecrets.data.exceptions.GroupNotFoundException
import br.com.brunocarvalhs.friendssecrets.data.model.GroupModel
import br.com.brunocarvalhs.friendssecrets.data.repository.dto.GroupDTO
import br.com.brunocarvalhs.friendssecrets.data.repository.dto.toDTO
import br.com.brunocarvalhs.friendssecrets.data.service.DrawService
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.repository.GroupRepository
import br.com.brunocarvalhs.friendssecrets.domain.repository.response.GroupResponse
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

internal class GroupRepositoryImpl(
    private val firestore: FirebaseFirestore = Firebase.firestore,
    private val cryptoService: CryptoService = CryptoService(),
    private val drawService: DrawService = DrawService(
        cryptoService = cryptoService
    )
) : GroupRepository {

    override suspend fun create(group: GroupEntities) {
        val payload = group.toDTO()

        val data =
            cryptoService.encryptMap(payload.toMap(), setOf(GroupEntities.TOKEN, GroupEntities.ID))

        firestore.collection(GroupEntities.COLLECTION_NAME)
            .document(payload.id)
            .set(data)
            .await()
    }

    override suspend fun read(groupId: String): GroupResponse {
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
        return GroupDTO.fromMap(decryptedData)
    }

    override suspend fun update(group: GroupEntities) {
        val payload = group.toDTO()

        val data =
            cryptoService.encryptMap(payload.toMap(), setOf(GroupEntities.TOKEN, GroupEntities.ID))

        firestore.collection(GroupEntities.COLLECTION_NAME)
            .document(payload.id)
            .set(data)
            .await()
    }

    override suspend fun delete(groupId: String) {
        firestore.collection(GroupEntities.COLLECTION_NAME).document(groupId).delete().await()
    }

    override suspend fun list(list: List<String>): List<GroupResponse> {
        val querySnapshot = firestore.collection(GroupEntities.COLLECTION_NAME)
            .whereIn(GroupEntities.TOKEN, list)
            .get()
            .await()

        return querySnapshot.documents.map { documentSnapshot ->
            val encryptedData = documentSnapshot.data ?: throw GroupNotFoundException()
            val decryptedData = cryptoService.decryptMap(
                encryptedData,
                setOf(GroupEntities.TOKEN, GroupEntities.ID)
            )
            GroupDTO.fromMap(decryptedData)
        }
    }

    override suspend fun searchByToken(token: String): GroupResponse? {
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
        return GroupDTO.fromMap(decryptedData)
    }

    override suspend fun drawMembers(group: GroupEntities) {
        firestore.runTransaction { transaction ->
            val groupDocRef = firestore.collection(GroupEntities.COLLECTION_NAME).document(group.id)
            val snapshot = transaction.get(groupDocRef)
            val currentGroupData = snapshot.data
            val currentGroup = GroupModel.fromMap(
                cryptoService.decryptMap(
                    currentGroupData ?: mapOf(),
                    setOf(GroupEntities.TOKEN, GroupEntities.ID)
                )
            )

            val secretSantaMap = drawService.drawMembers(currentGroup)

            transaction.update(groupDocRef, GroupEntities.DRAWS, secretSantaMap)
            null
        }.await()
    }

}
