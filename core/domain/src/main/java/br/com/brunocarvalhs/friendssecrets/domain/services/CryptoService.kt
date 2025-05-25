package br.com.brunocarvalhs.friendssecrets.domain.services

interface CryptoService {
    fun encryptMap(
        inputMap: Map<String, Any>, excludedKeys: Set<String> = emptySet()
    ): Map<String, Any>

    fun decryptMap(
        encodedMap: Map<String, Any>, excludedKeys: Set<String> = emptySet()
    ): Map<String, Any>

    fun encrypt(input: String): String

    fun decrypt(encoded: String): String
}