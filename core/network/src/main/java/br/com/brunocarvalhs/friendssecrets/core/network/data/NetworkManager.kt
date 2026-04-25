package br.com.brunocarvalhs.friendssecrets.core.network.data

import br.com.brunocarvalhs.friendssecrets.core.network.domain.NetworkService
import br.com.brunocarvalhs.friendssecrets.core.security.domain.CryptoService
import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import timber.log.Timber
import javax.inject.Inject
import kotlin.reflect.KClass

class NetworkManager @Inject constructor(
    private val firebaseFirestoreManager: FirebaseFirestoreManager,
    private val cryptoManager: CryptoService,
    private val compatibilityConverter: FirebaseCompatibilityConverter
) : NetworkService {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
    }

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

        return runCatching {
            val response = firebaseFirestoreManager.execute(
                endpoint = finalEndpoint,
                method = method,
                data = finalPayload,
                query = query,
            )

            getResponse(response, clazz)
        }.onSuccess {
            Timber.tag(TAG).d("<-- SUCCESS %s [%s]", method, finalEndpoint)
        }.onFailure {
            Timber.tag(TAG).e(it, "<-- FAILURE %s [%s] | Error: %s", method, finalEndpoint, it.message)
        }.getOrNull()
    }

    private fun getPayload(payload: Map<String, Any?>?): Map<String, Any?>? {
        return payload?.let {
            cryptoManager.encryptMap(it, EXCLUDED_KEYS)
        }
    }

    private fun getEndpoint(endpoint: String): String = endpoint

    @OptIn(InternalSerializationApi::class)
    @Suppress("UNCHECKED_CAST")
    private fun <T : Any> getResponse(response: Any?, clazz: KClass<T>): T? {
        if (response == null) return null

        return try {
            val serializer = json.serializersModule.serializer(clazz.java)

            val decryptedData = when (response) {
                is Map<*, *> -> cryptoManager.decryptMap(response as Map<String, Any>, EXCLUDED_KEYS)
                is List<*> -> response.map { item ->
                    if (item is Map<*, *>) cryptoManager.decryptMap(item as Map<String, Any>, EXCLUDED_KEYS)
                    else item
                }
                else -> response
            }

            val jsonElement = compatibilityConverter.toJsonElement(decryptedData)

            Timber.tag(TAG).v("Processing JSON for %s: %s", clazz.simpleName, jsonElement)

            json.decodeFromJsonElement(serializer, jsonElement) as? T
        } catch (e: Exception) {
            Timber.tag(TAG).e(e, "Compatibility Error on %s", clazz.simpleName)
            
            if (response is List<*> && clazz.java.isArray) {
                return compatibilityConverter.listToTypedArray(response, clazz.java) as? T
            }
            null
        }
    }

    companion object {
        private const val TAG = "NetworkManager"
        private val EXCLUDED_KEYS = setOf(GroupModel.ID, GroupModel.TOKEN, GroupModel.CREATED_AT)
    }
}
