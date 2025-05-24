package br.com.brunocarvalhs.friendssecrets.commons.security

import android.util.Base64
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

interface Base64Encoder {
    fun encodeToString(input: ByteArray, flags: Int): String
    fun decode(input: String, flags: Int): ByteArray
}

class CryptoService(
    private val base64Encoder: Base64Encoder = object : Base64Encoder {
        override fun encodeToString(input: ByteArray, flags: Int): String =
            Base64.encodeToString(input, flags)

        override fun decode(input: String, flags: Int): ByteArray = Base64.decode(input, flags)
    }
) {
    val gson = Gson()

    fun encryptMap(
        inputMap: Map<String, Any>,
        excludedKeys: Set<String> = emptySet()
    ): Map<String, Any> {
        return inputMap.mapValues { (key, value) ->
            if (key in excludedKeys) {
                value
            } else {
                if (key in excludedKeys) {
                    value
                } else {
                    val stringToEncrypt = when (value) {
                        is String,
                        is Number,
                        is Boolean -> value.toString()

                        is List<*>,
                        is Map<*, *> -> gson.toJson(value)

                        else -> gson.toJson(value)
                    }
                    encrypt(stringToEncrypt)
                }
            }
        }
    }

    fun decryptMap(
        encodedMap: Map<String, Any>,
        excludedKeys: Set<String> = emptySet()
    ): Map<String, Any> {
        return encodedMap.mapValues { (key, value) ->
            if (key in excludedKeys || value !is String) return@mapValues value

            val decrypted = try {
                decrypt(value)
            } catch (e: Exception) {
                return@mapValues value
            }

            try {
                when {
                    decrypted.equals("true", ignoreCase = true) -> true
                    decrypted.equals("false", ignoreCase = true) -> false
                    decrypted.toIntOrNull() != null -> decrypted.toInt()
                    decrypted.toDoubleOrNull() != null -> decrypted.toDouble()
                    decrypted.startsWith("{") -> {
                        val mapType = object : TypeToken<Map<String, Any>>() {}.type
                        val rawMap: Map<String, Any> = gson.fromJson(decrypted, mapType)
                        convertMap(rawMap)
                    }
                    decrypted.startsWith("[") -> {
                        val listType = object : TypeToken<List<Any>>() {}.type
                        val rawList: List<Any> = gson.fromJson(decrypted, listType)
                        convertList(rawList)
                    }
                    else -> decrypted
                }
            } catch (e: Exception) {
                decrypted
            }
        }
    }

    private fun convertMap(input: Map<String, Any?>): Map<String, Any?> {
        val result = HashMap<String, Any?>()
        input.forEach { (key, value) ->
            result[key] = when (value) {
                is Map<*, *> -> convertMap(value as Map<String, Any?>)
                is List<*> -> convertList(value)
                else -> value
            }
        }
        return result
    }

    private fun convertList(input: List<Any?>): List<Any?> {
        val result = ArrayList<Any?>()
        input.forEach { item ->
            result.add(
                when (item) {
                    is Map<*, *> -> convertMap(item as Map<String, Any?>)
                    is List<*> -> convertList(item)
                    else -> item
                }
            )
        }
        return result
    }

    fun encrypt(input: String): String {
        return base64Encoder.encodeToString(
            input = input.toByteArray(),
            flags = Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP
        )
    }

    fun decrypt(encoded: String): String {
        val decodedBytes =
            base64Encoder.decode(
                input = encoded,
                flags = Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP
            )
        return String(decodedBytes)
    }
}

