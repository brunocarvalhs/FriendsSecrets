package br.com.brunocarvalhs.friendssecrets.commons.security

import android.util.Base64
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

class CryptoManager(
    private val base64Encoder: Base64Encoder = DefaultBase64Encoder,
    private val json: Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
    }
) {

     fun encryptMap(
        inputMap: Map<String, Any>,
        excludedKeys: Set<String>
    ): Map<String, Any> = inputMap.mapValues { (key, value) ->
        if (key in excludedKeys) value
        else encrypt(json.encodeToString(value.toJsonElement()))
    }

     fun decryptMap(
        encodedMap: Map<String, Any>,
        excludedKeys: Set<String>
    ): Map<String, Any> = encodedMap.mapValues { (key, value) ->
        if (key in excludedKeys || value !is String) return@mapValues value

        val decrypted = decrypt(value)
        if (decrypted == value) return@mapValues value

        runCatching {
            json.parseToJsonElement(decrypted).toAny()
        }.getOrNull() ?: decrypted
    }

     fun encrypt(input: String): String = base64Encoder.encodeToString(
        input.toByteArray(),
        BASE64_FLAGS
    )

     fun decrypt(encoded: String): String = runCatching {
        val decodedBytes = base64Encoder.decode(encoded, BASE64_FLAGS)
        String(decodedBytes)
    }.getOrDefault(encoded)

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
        private const val BASE64_FLAGS = Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP

        private object DefaultBase64Encoder : Base64Encoder {
             override fun encodeToString(input: ByteArray, flags: Int): String =
                Base64.encodeToString(input, flags)

             override fun decode(input: String, flags: Int): ByteArray =
                Base64.decode(input, flags)
        }
    }
}
