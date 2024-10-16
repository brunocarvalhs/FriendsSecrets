package br.com.brunocarvalhs.friendssecrets.commons.security

import android.util.Base64

class CryptoService {

    fun encryptMap(inputMap: Map<String, Any>, excludedKeys: Set<String> = emptySet()): Map<String, Any> {
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

    fun decryptMap(encodedMap: Map<String, Any>, excludedKeys: Set<String> = emptySet()): Map<String, Any> {
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
        return Base64.encodeToString(input.toByteArray(), Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP)
    }

    fun decrypt(encoded: String): String {
        val decodedBytes = Base64.decode(encoded, Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP)
        return String(decodedBytes)
    }
}

