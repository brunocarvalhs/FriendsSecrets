package br.com.brunocarvalhs.friendssecrets.commons.network

import br.com.brunocarvalhs.friendssecrets.domain.services.NetworkService
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.reflect.KClass

class FirebaseFirestoreManager @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore
) {

    suspend fun <T : Any> execute(
        endpoint: String,
        method: NetworkService.Method,
        data: Map<String, Any?>? = null,
        query: Map<String, Any?>? = null,
        clazz: KClass<T>
    ): Any? = when (method) {
        NetworkService.Method.GET -> get(endpoint, query, clazz)
        NetworkService.Method.POST -> post(endpoint, data)
        NetworkService.Method.PUT -> put(endpoint, data)
        NetworkService.Method.DELETE -> delete(endpoint)
    }

    private suspend fun <T : Any> get(
        endpoint: String,
        query: Map<String, Any?>?,
        clazz: KClass<T>
    ): Any? {

        val parts = endpoint.split("/")

        if (parts.size == 2) {
            val (collection, documentId) = parts

            val snapshot = firebaseFirestore
                .collection(collection)
                .document(documentId)
                .get()
                .await()

            return snapshot.toObject(clazz.java)
        }

        var ref: Query = firebaseFirestore.collection(parts[0])

        query?.forEach { (key, value) ->
            ref = when (value) {
                is List<*> -> {
                    ref.whereIn(key, value)
                }

                else -> {
                    ref.whereEqualTo(key, value)
                }
            }
        }

        val snapshot = ref.get().await()

        return snapshot.documents.mapNotNull {
            it.toObject(clazz.java)
        }
    }

    private suspend fun post(
        endpoint: String, data: Map<String, Any?>?
    ): String {
        requireNotNull(data)

        val collection = endpoint.split("/")[0]

        val docRef = firebaseFirestore.collection(collection).add(data).await()

        return docRef.id
    }

    private suspend fun put(
        endpoint: String, data: Map<String, Any?>?
    ): Boolean {
        requireNotNull(data)

        val (collection, documentId) = endpoint.split("/").takeIf { it.size == 2 }
            ?: throw IllegalArgumentException("PUT precisa de endpoint com ID")

        firebaseFirestore.collection(collection)
            .document(documentId).set(data).await()

        return true
    }

    private suspend fun delete(
        endpoint: String
    ): Boolean {

        val (collection, documentId) = endpoint.split("/").takeIf { it.size == 2 }
            ?: throw IllegalArgumentException("DELETE precisa de endpoint com ID")

        firebaseFirestore.collection(collection)
            .document(documentId).delete().await()

        return true
    }
}
