package br.com.brunocarvalhs.friendssecrets.data.repository

import br.com.brunocarvalhs.friendssecrets.data.exceptions.GroupNotFoundException
import br.com.brunocarvalhs.friendssecrets.data.model.GroupModel
import br.com.brunocarvalhs.friendssecrets.data.service.DrawService
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.repository.GroupRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

internal class GroupRepositoryImpl(
    private val firestore: FirebaseFirestore = Firebase.firestore,
    private val drawService: DrawService = DrawService()
) : GroupRepository {

    override suspend fun create(group: GroupEntities) {
        firestore.collection(GroupEntities.COLLECTION_NAME)
            .document(group.id)
            .set(group.toMap())
            .await()
    }

    override suspend fun read(groupId: String): GroupEntities {
        return firestore.collection(GroupEntities.COLLECTION_NAME).document(groupId).get().await()
            .toObject(GroupModel::class.java) ?: throw GroupNotFoundException()
    }

    override suspend fun update(group: GroupEntities) {
        firestore.collection(GroupEntities.COLLECTION_NAME)
            .document(group.id)
            .set(group.toMap())
            .await()
    }

    override suspend fun delete(groupId: String) {
        firestore.collection(GroupEntities.COLLECTION_NAME).document(groupId).delete().await()
    }

    override suspend fun list(list: List<String>): List<GroupEntities> {
        return firestore.collection(GroupEntities.COLLECTION_NAME)
            .whereIn(GroupEntities.TOKEN, list).get().await()
            .toObjects(GroupModel::class.java)
    }

    override suspend fun search(query: String): List<GroupEntities> {
        return firestore.collection(GroupEntities.COLLECTION_NAME)
            .whereEqualTo(GroupEntities.NAME, query)
            .get().await()
            .toObjects(GroupModel::class.java)
    }

    override suspend fun searchByMember(memberId: String): List<GroupEntities> {
        return firestore.collection(GroupEntities.COLLECTION_NAME)
            .whereArrayContains(GroupEntities.MEMBERS, memberId).get().await()
            .toObjects(GroupModel::class.java)
    }

    override suspend fun searchByToken(token: String): GroupEntities? {
        return firestore.collection(GroupEntities.COLLECTION_NAME)
            .whereEqualTo(GroupEntities.TOKEN, token)
            .get().await()
            .toObjects(GroupModel::class.java).firstOrNull()
    }

    override suspend fun drawMembers(group: GroupEntities) {
        val secretSantaMap = drawService.drawMembers(group)

        firestore.collection(GroupEntities.COLLECTION_NAME)
            .document(group.id)
            .update(
                mapOf(
                    GroupEntities.DRAWS to secretSantaMap,
                    GroupEntities.IS_DRAW to true
                )
            ).await()
    }
}
