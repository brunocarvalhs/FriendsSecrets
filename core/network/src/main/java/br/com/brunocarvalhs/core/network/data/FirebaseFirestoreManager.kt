package br.com.brunocarvalhs.core.network.data

import br.com.brunocarvalhs.core.network.domain.NetworkService
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseFirestoreManager @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore
) {

    suspend fun execute(
        endpoint: String,
        method: NetworkService.Method,
        data: Map<String, Any?>? = null,
        query: Map<String, Any?>? = null,
    ): Any? = when (method) {
        NetworkService.Method.GET -> get(endpoint, query)
        NetworkService.Method.POST -> post(endpoint, data)
        NetworkService.Method.PUT -> put(endpoint, data)
        NetworkService.Method.DELETE -> delete(endpoint)
    }

    private suspend fun get(
        endpoint: String,
        query: Map<String, Any?>?
    ): Any? {
        val parts = endpoint.split("/")
        
        if (parts.size == 2) {
            val (collection, documentId) = parts
            val snapshot = firebaseFirestore
                .collection(collection)
                .document(documentId)
                .get()
                .await()

            return snapshot.data?.toMutableMap()?.apply {
                put("id", snapshot.id)
            }
        }

        val collection = parts[0]
        val listEntry = query?.entries?.firstOrNull { it.value is List<*> }
        val normalFilters = query?.filterValues { it !is List<*> } ?: emptyMap()

        val results = mutableListOf<Map<String, Any>>()

        if (listEntry != null) {
            val key = listEntry.key
            val values = listEntry.value as List<*>
            val chunks = values.chunked(FIRESTORE_WHERE_IN_BATCH_SIZE)

            for (chunk in chunks) {
                var ref: Query = firebaseFirestore.collection(collection).whereIn(key, chunk)
                normalFilters.forEach { (k, v) -> ref = ref.whereEqualTo(k, v) }
                val snapshot = ref.get().await()
                results.addAll(snapshot.documents.mapNotNull { doc ->
                    doc.data?.toMutableMap()?.apply { put("id", doc.id) }
                })
            }
        } else {
            var ref: Query = firebaseFirestore.collection(collection)
            normalFilters.forEach { (k, v) -> ref = ref.whereEqualTo(k, v) }
            val snapshot = ref.get().await()
            results.addAll(snapshot.documents.mapNotNull { doc ->
                doc.data?.toMutableMap()?.apply { put("id", doc.id) }
            })
        }

        return results.distinctBy { it["id"] }
    }

    private suspend fun post(endpoint: String, data: Map<String, Any?>?): String {
        requireNotNull(data)
        val collection = endpoint.split("/")[0]
        val docRef = firebaseFirestore.collection(collection).add(data).await()
        return docRef.id
    }

    private suspend fun put(endpoint: String, data: Map<String, Any?>?): Boolean {
        requireNotNull(data)
        val (collection, id) = endpoint.split("/")
        
        val updateData = data.filterKeys { it != "id" }.filterValues { it != null }
        
        if (updateData.isNotEmpty()) {
            firebaseFirestore.collection(collection)
                .document(id)
                .set(updateData, SetOptions.merge())
                .await()
        }
        return true
    }

    private suspend fun delete(endpoint: String): Boolean {
        val (collection, id) = endpoint.split("/")
        firebaseFirestore.collection(collection).document(id).delete().await()
        return true
    }

    companion object {
        private const val FIRESTORE_WHERE_IN_BATCH_SIZE = 10
    }
}
