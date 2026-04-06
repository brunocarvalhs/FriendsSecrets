package br.com.brunocarvalhs.friendssecrets.commons.network

import br.com.brunocarvalhs.friendssecrets.domain.services.NetworkService
import timber.log.Timber
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

        Timber.tag(TAG).d("--> %s [%s]", method, finalEndpoint)
        if (finalPayload != null) Timber.tag(TAG).d("Payload: %s", finalPayload)
        if (!headers.isNullOrEmpty()) Timber.tag(TAG).d("Headers: %s", headers)
        if (!query.isNullOrEmpty()) Timber.tag(TAG).d("Query: %s", query)
        Timber.tag(TAG).d("Class: %s", clazz.simpleName)

        return runCatching {
            firebaseFirestoreManager.execute(
                endpoint = finalEndpoint,
                method = method,
                data = finalPayload,
                query = query,
                clazz = clazz
            )
        }.onSuccess { result ->
            Timber.tag(TAG).d("<-- SUCCESS %s [%s]", method, finalEndpoint)
            Timber.tag(TAG).d("Result: %s", result)
        }.onFailure {
            Timber.tag(TAG).e(it, "<-- FAILURE %s [%s] | Error: %s", method, finalEndpoint, it.message)
        }.getOrNull() as? T
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

    companion object {
        private const val TAG = "NetworkManager"
    }
}
