package br.com.brunocarvalhs.group.create.app.data.repository

import br.com.brunocarvalhs.group.create.app.domain.entities.GroupModel
import br.com.brunocarvalhs.group.create.app.domain.repositories.GroupCreateRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GroupCreateRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
) : GroupCreateRepository {

    override suspend fun create(group: GroupModel) {
        val payload = group.toMap()

        firestore.collection(COLLECTION_NAME)
            .document(group.id)
            .set(payload)
            .await()
    }

    companion object {
        const val COLLECTION_NAME = "groups"
    }
}