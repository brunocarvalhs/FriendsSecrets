package br.com.brunocarvalhs.friendssecrets.commons.security

import android.util.Base64
import com.google.firebase.perf.metrics.AddTrace

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

    /**
     * Encrypts a map of strings, excluding the specified keys.
     *
     * @param inputMap The map to encrypt.
     * @param excludedKeys The keys to exclude from encryption.
     * @return A new map with the encrypted values.
     */
    @JvmName("encryptMap")
    @AddTrace(name = "CryptoService.encryptMap")
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

    /**
     * Decrypts a map of strings, excluding the specified keys.
     *
     * @param encodedMap The map to decrypt.
     * @param excludedKeys The keys to exclude from decryption.
     * @return A new map with the decrypted values.
     */
    @JvmName("decryptMap")
    @AddTrace(name = "CryptoService.decryptMap")
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

    /**
     * Encrypts a string using Base64 encoding.
     *
     * @param input The string to encrypt.
     * @return The encrypted string.
     */
    @JvmName("encryptString")
    @AddTrace(name = "CryptoService.encryptString")
    fun encrypt(input: String): String {
        return base64Encoder.encodeToString(
            input = input.toByteArray(),
            flags = Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP
        )
    }

    /**
     * Decrypts a Base64 encoded string.
     *
     * @param encoded The Base64 encoded string to decrypt.
     * @return The decrypted string.
     */
    @JvmName("decryptString")
    @AddTrace(name = "CryptoService.decryptString")
    fun decrypt(encoded: String): String {
        val decodedBytes =
            base64Encoder.decode(
                input = encoded,
                flags = Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP
            )
        return String(decodedBytes)
    }
}

