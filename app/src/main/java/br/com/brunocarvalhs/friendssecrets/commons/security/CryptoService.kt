package br.com.brunocarvalhs.friendssecrets.commons.security

import android.util.Base64

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

    fun encryptMap(
        inputMap: Map<String, Any>,
        excludedKeys: Set<String> = emptySet()
    ): Map<String, Any> {
        return inputMap.mapValues { (key, value) ->
            if (key in excludedKeys) {
                value
            } else {
                if (value is String) {
                    encrypt(value)
                } else {
                    value
                }
            }
        }
    }

    fun decryptMap(
        encodedMap: Map<String, Any>,
        excludedKeys: Set<String> = emptySet()
    ): Map<String, Any> {
        return encodedMap.mapValues { (key, value) ->
            if (key in excludedKeys) {
                value
            } else {
                if (value is String) {
                    decrypt(value)
                } else {
                    value
                }
            }
        }
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

