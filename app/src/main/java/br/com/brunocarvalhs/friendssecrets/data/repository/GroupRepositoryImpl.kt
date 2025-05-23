package br.com.brunocarvalhs.friendssecrets.data.repository

import br.com.brunocarvalhs.friendssecrets.commons.security.CryptoService
import br.com.brunocarvalhs.friendssecrets.data.exceptions.GroupNotFoundException
import br.com.brunocarvalhs.friendssecrets.data.model.GroupModel
import br.com.brunocarvalhs.friendssecrets.data.service.DrawService
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.repository.GroupRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.perf.metrics.AddTrace
import kotlinx.coroutines.tasks.await

internal class GroupRepositoryImpl(
    private val firestore: FirebaseFirestore = Firebase.firestore,
    private val cryptoService: CryptoService = CryptoService(),
    private val drawService: DrawService = DrawService(
        cryptoService = cryptoService
    )
) : GroupRepository {

    @AddTrace(name = "GroupRepository.create")
    override suspend fun create(group: GroupEntities) {
        val data = cryptoService.encryptMap(group.toMap(), setOf(GroupEntities.TOKEN, GroupEntities.ID))

        firestore.collection(GroupEntities.COLLECTION_NAME)
            .document(group.id)
            .set(data)
            .await()
    }

    @AddTrace(name = "GroupRepository.read")
    override suspend fun read(groupId: String): GroupEntities {
        val documentSnapshot = firestore.collection(GroupEntities.COLLECTION_NAME)
            .document(groupId)
            .get()
            .await()
        if (!documentSnapshot.exists()) { throw GroupNotFoundException() }
        val encryptedData = documentSnapshot.data ?: throw GroupNotFoundException()
        val decryptedData = cryptoService.decryptMap(encryptedData, setOf(GroupEntities.TOKEN, GroupEntities.ID))
        return GroupModel.fromMap(decryptedData)
    }

    @AddTrace(name = "GroupRepository.update")
    override suspend fun update(group: GroupEntities) {
        val data = cryptoService.encryptMap(group.toMap(), setOf(GroupEntities.TOKEN, GroupEntities.ID))

        firestore.collection(GroupEntities.COLLECTION_NAME)
            .document(group.id)
            .set(data)
            .await()
    }

    @AddTrace(name = "GroupRepository.delete")
    override suspend fun delete(groupId: String) {
        firestore.collection(GroupEntities.COLLECTION_NAME).document(groupId).delete().await()
    }

    @AddTrace(name = "GroupRepository.list")
    override suspend fun list(list: List<String>): List<GroupEntities> {
        val querySnapshot = firestore.collection(GroupEntities.COLLECTION_NAME)
            .whereIn(GroupEntities.TOKEN, list)
            .get()
            .await()

        return querySnapshot.documents.map { documentSnapshot ->
            val encryptedData = documentSnapshot.data ?: throw GroupNotFoundException()
            val decryptedData = cryptoService.decryptMap(encryptedData, setOf(GroupEntities.TOKEN, GroupEntities.ID))
            GroupModel.fromMap(decryptedData)
        }
    }

    @AddTrace(name = "GroupRepository.searchByToken")
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

        val decryptedData = cryptoService.decryptMap(encryptedData, setOf(GroupEntities.TOKEN, GroupEntities.ID))
        return GroupModel.fromMap(decryptedData)
    }

    @AddTrace(name = "GroupRepository.drawMembers")
    override suspend fun drawMembers(group: GroupEntities) {
        val secretSantaMap = drawService.drawMembers(group)

        firestore.collection(GroupEntities.COLLECTION_NAME)
            .document(group.id)
            .update(mapOf(GroupEntities.DRAWS to secretSantaMap))
            .await()
    }
}
