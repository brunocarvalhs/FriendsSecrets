package br.com.brunocarvalhs.group.create.app.data.repository

import br.com.brunocarvalhs.group.create.app.domain.entities.GroupModel
import br.com.brunocarvalhs.group.create.app.domain.repositories.GroupCreateRepository
import br.com.brunocarvalhs.group.create.commons.providers.GroupCreateCrypto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GroupCreateRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val crypto: GroupCreateCrypto
) : GroupCreateRepository {

    override suspend fun create(group: GroupModel) {
        val payload = group.toMap()
        val data = crypto.encrypt(input = payload)
        firestore.collection(COLLECTION_NAME)
            .document(group.id)
            .set(data)
            .await()
    }

    companion object {
        const val COLLECTION_NAME = "groups"
    }
}