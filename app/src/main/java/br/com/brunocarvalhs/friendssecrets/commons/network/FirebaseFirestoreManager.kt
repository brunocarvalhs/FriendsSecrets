package br.com.brunocarvalhs.friendssecrets.commons.network

import br.com.brunocarvalhs.friendssecrets.domain.services.NetworkService
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import java.lang.reflect.Array
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
        
        val isArray = clazz.java.isArray
        val targetClass = if (isArray) clazz.java.componentType!! else clazz.java

        if (parts.size == 2) {
            val (collection, documentId) = parts

            val snapshot = firebaseFirestore
                .collection(collection)
                .whereEqualTo("id", documentId)
                .get()
                .await()

            val doc = snapshot.documents.firstOrNull()
                ?: return null

            return doc.toObject(targetClass)
        }

        var ref: Query = firebaseFirestore.collection(parts[0])

        query?.forEach { (key, value) ->
            ref = when (value) {
                is List<*> -> ref.whereIn(key, value)
                else -> ref.whereEqualTo(key, value)
            }
        }

        val snapshot = ref.get().await()
        val list = snapshot.documents.mapNotNull { it.toObject(targetClass) }

        return if (isArray) {
            val array = Array.newInstance(targetClass, list.size)
            list.forEachIndexed { index, item ->
                Array.set(array, index, item)
            }
            array
        } else {
            list
        }
    }

    private suspend fun post(endpoint: String, data: Map<String, Any?>?): String {
        requireNotNull(data)
        val collection = endpoint.split("/")[0]
        val docRef = firebaseFirestore.collection(collection).add(data).await()
        return docRef.id
    }

    private suspend fun put(endpoint: String, data: Map<String, Any?>?): Boolean {
        requireNotNull(data)

        val parts = endpoint.split("/")
        val collection = parts[0]
        val id = parts.getOrNull(1)
            ?: throw IllegalArgumentException("PUT precisa de endpoint com ID")

        val snapshot = firebaseFirestore
            .collection(collection)
            .whereEqualTo("id", id)
            .get()
            .await()

        val document = snapshot.documents.firstOrNull()
            ?: throw IllegalArgumentException("Documento não encontrado")

        firebaseFirestore
            .collection(collection)
            .document(document.id)
            .set(data)
            .await()

        return true
    }

    private suspend fun delete(endpoint: String): Boolean {
        val parts = endpoint.split("/")
        val collection = parts[0]
        val id = parts.getOrNull(1)
            ?: throw IllegalArgumentException("DELETE precisa de endpoint com ID")

        val snapshot = firebaseFirestore
            .collection(collection)
            .whereEqualTo("id", id)
            .get()
            .await()

        val document = snapshot.documents.firstOrNull()
            ?: throw IllegalArgumentException("Documento não encontrado")

        firebaseFirestore
            .collection(collection)
            .document(document.id)
            .delete()
            .await()

        return true
    }
}
