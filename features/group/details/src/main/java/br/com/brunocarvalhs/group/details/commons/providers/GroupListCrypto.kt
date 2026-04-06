package br.com.brunocarvalhs.group.list.commons.providers

interface GroupListCrypto {
    fun decryptMap(encryptedData: MutableMap<String, Any>, of: Any): Map<String, Any>
}