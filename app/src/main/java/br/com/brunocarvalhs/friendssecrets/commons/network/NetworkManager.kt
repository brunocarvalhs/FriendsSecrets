package br.com.brunocarvalhs.friendssecrets.commons.network

import br.com.brunocarvalhs.friendssecrets.domain.services.NetworkService
import kotlin.reflect.KClass

class NetworkManager(
    private val firebaseFirestoreManager: FirebaseFirestoreManager
) : NetworkService {

    override suspend fun <T : Any> make(
        endpoint: String,
        payload: Map<String, Any?>?,
        headers: Map<String, String>?,
        query: Map<String, Any>?,
        method: NetworkService.Method,
        clazz: KClass<T>
    ): T? {
        val finalEndpoint = getEndpoint(endpoint)
        val finalPayload = getPayload(payload)

        val result = firebaseFirestoreManager.execute(
            endpoint = finalEndpoint,
            method = method,
            data = finalPayload,
            query = query,
            clazz = clazz
        )

        @Suppress("UNCHECKED_CAST")
        return result as? T
    }

    private fun getHeaders(headers: Map<String, String>): Map<String, String> {
        return emptyMap<String, String>().plus(headers)
    }

    private fun getPayload(payload: Map<String, Any?>?): Map<String, Any?>? {
        return payload
    }

    private fun getEndpoint(endpoint: String): String {
        return endpoint
    }
}