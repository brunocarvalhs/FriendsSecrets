package br.com.brunocarvalhs.group.create.commons.providers

interface GroupCreateCrypto {
    fun encrypt(input: Map<String, Any?>): Map<String, Any?>
}