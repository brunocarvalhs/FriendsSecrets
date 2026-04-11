package br.com.brunocarvalhs.friendssecrets.commons.security

import android.util.Base64
import br.com.brunocarvalhs.friendssecrets.domain.services.CryptoService
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.longOrNull
import timber.log.Timber

class CryptoManager(
    private val base64Encoder: Base64Encoder = DefaultBase64Encoder,
    private val json: Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
    }
): CryptoService {
    override fun encryptMap(
        inputMap: Map<String, Any?>,
        excludedKeys: Set<String>
    ): Map<String, Any?> {
        Timber.tag(TAG).d("--> ENCRYPT MAP | Excluded: %s", excludedKeys)
        return inputMap.filterValues { it != null }.mapValues { (key, value) ->
            if (key in excludedKeys) value
            else encryptValue(value)
        }.also {
            Timber.tag(TAG).d("<-- SUCCESS ENCRYPT MAP")
        }
    }

    private fun encryptValue(value: Any?): Any? {
        return when (value) {
            null -> null
            is Map<*, *> -> (value as Map<String, Any?>).mapValues { encryptValue(it.value) }
            is Iterable<*> -> value.map { encryptValue(it) }
            else -> encrypt(json.encodeToString(value.toJsonElement()))
        }
    }

    override fun decryptMap(
        encodedMap: Map<String, Any>,
        excludedKeys: Set<String>
    ): Map<String, Any> {
        Timber.tag(TAG).d("--> DECRYPT MAP | Excluded: %s", excludedKeys)
        return encodedMap.mapValues { (key, value) ->
            if (key in excludedKeys) return@mapValues value
            decryptValue(value)
        }.also {
            Timber.tag(TAG).d("<-- SUCCESS DECRYPT MAP")
        }
    }

    private fun decryptValue(value: Any?): Any {
        return when (value) {
            is String -> {
                val decrypted = decrypt(value)
                if (decrypted == value) return value

                runCatching {
                    json.parseToJsonElement(decrypted).toAny() ?: decrypted
                }.getOrElse { decrypted }
            }
            is Map<*, *> -> {
                (value as Map<String, Any>).mapValues { decryptValue(it.value) }
            }
            is Iterable<*> -> {
                value.map { decryptValue(it) }
            }
            else -> value ?: ""
        }
    }

    override fun encrypt(input: String): String {
        Timber.tag(TAG).v("--> ENCRYPT | Input: %s", input)
        return base64Encoder.encodeToString(
            input.toByteArray(),
            BASE64_FLAGS
        ).also {
            Timber.tag(TAG).v("<-- RESULT ENCRYPT: %s", it)
        }
    }

    override fun decrypt(encoded: String): String {
        Timber.tag(TAG).v("--> DECRYPT | Input: %s", encoded)
        return runCatching {
            val decodedBytes = base64Encoder.decode(encoded, BASE64_FLAGS)
            String(decodedBytes).also {
                Timber.tag(TAG).v("<-- RESULT DECRYPT: %s", it)
            }
        }.getOrElse {
            Timber.tag(TAG).e(it, "<-- FAILURE DECRYPT | Error: %s", it.message)
            encoded
        }
    }

    private fun Any?.toJsonElement(): JsonElement = when (this) {
        null -> JsonNull
        is JsonElement -> this
        is Boolean -> JsonPrimitive(this)
        is Number -> JsonPrimitive(this)
        is String -> JsonPrimitive(this)
        is Iterable<*> -> JsonArray(map { it.toJsonElement() })
        is Map<*, *> -> JsonObject(entries.associate { it.key.toString() to it.value.toJsonElement() })
        else -> JsonPrimitive(toString())
    }

    private fun JsonElement.toAny(): Any? = when (this) {
        is JsonNull -> null
        is JsonPrimitive -> {
            if (isString) content
            else booleanOrNull ?: intOrNull ?: longOrNull ?: doubleOrNull ?: content
        }

        is JsonArray -> map { it.toAny() }
        is JsonObject -> mapValues { it.value.toAny() }
    }

    interface Base64Encoder {
        fun encodeToString(input: ByteArray, flags: Int): String
        fun decode(input: String, flags: Int): ByteArray
    }

    private companion object {
        private const val TAG = "CryptoManager"
        private const val BASE64_FLAGS = Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP

        private object DefaultBase64Encoder : Base64Encoder {
            override fun encodeToString(input: ByteArray, flags: Int): String =
                Base64.encodeToString(input, flags)

            override fun decode(input: String, flags: Int): ByteArray =
                Base64.decode(input, flags)
        }
    }
}
