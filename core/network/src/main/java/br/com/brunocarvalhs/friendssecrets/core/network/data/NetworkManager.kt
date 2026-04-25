package br.com.brunocarvalhs.friendssecrets.core.network.data

import br.com.brunocarvalhs.friendssecrets.core.network.domain.NetworkRequest
import br.com.brunocarvalhs.friendssecrets.core.network.domain.NetworkService
import br.com.brunocarvalhs.friendssecrets.core.security.domain.CryptoService
import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerializationException
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

    override suspend fun <T : Any> make(request: NetworkRequest, clazz: KClass<T>): T? {
        val finalEndpoint = getEndpoint(request.endpoint)
        val finalPayload = getPayload(request.payload)

        Timber.tag(TAG).d("--> %s [%s]", request.method, finalEndpoint)

        return runCatching {
            val response = firebaseFirestoreManager.execute(
                endpoint = finalEndpoint,
                method = request.method,
                data = finalPayload,
                query = request.query,
            )

            getResponse(response, clazz)
        }.onSuccess {
            Timber.tag(TAG).d("<-- SUCCESS %s [%s]", request.method, finalEndpoint)
        }.onFailure {
            Timber.tag(TAG)
                .e(it, "<-- FAILURE %s [%s] | Error: %s", request.method, finalEndpoint, it.message)
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
        val data = response ?: return null

        return try {
            val serializer = json.serializersModule.serializer(clazz.java)

            val decryptedData = when (data) {
                is Map<*, *> -> cryptoManager.decryptMap(data as Map<String, Any>, EXCLUDED_KEYS)
                is List<*> -> data.map { item ->
                    if (item is Map<*, *>) cryptoManager.decryptMap(
                        encodedMap = item as Map<String, Any>,
                        excludedKeys = EXCLUDED_KEYS
                    )
                    else item
                }

                else -> data
            }

            val jsonElement = compatibilityConverter.toJsonElement(decryptedData)

            Timber.tag(TAG).v("Processing JSON for %s: %s", clazz.simpleName, jsonElement)

            json.decodeFromJsonElement(serializer, jsonElement) as? T
        } catch (e: SerializationException) {
            Timber.tag(TAG).e(e, "Serialization Error on %s", clazz.simpleName)

            if (data is List<*> && clazz.java.isArray) {
                compatibilityConverter.listToTypedArray(data, clazz.java) as? T
            } else {
                null
            }
        } catch (e: IllegalArgumentException) {
            Timber.tag(TAG).e(e, "Argument Error on %s", clazz.simpleName)

            if (data is List<*> && clazz.java.isArray) {
                compatibilityConverter.listToTypedArray(data, clazz.java) as? T
            } else {
                null
            }
        }
    }

    companion object {
        private const val TAG = "NetworkManager"
        private val EXCLUDED_KEYS = setOf(GroupModel.ID, GroupModel.TOKEN, GroupModel.CREATED_AT)
    }
}
