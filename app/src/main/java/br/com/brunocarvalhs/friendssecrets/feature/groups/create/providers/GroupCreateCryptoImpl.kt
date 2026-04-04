package br.com.brunocarvalhs.friendssecrets.feature.groups.create.providers

import br.com.brunocarvalhs.friendssecrets.commons.security.CryptoManager
import br.com.brunocarvalhs.group.create.commons.providers.GroupCreateCrypto
import javax.inject.Inject

class GroupCreateCryptoImpl @Inject constructor(
    private val crypto: CryptoManager
) : GroupCreateCrypto {

    override fun encrypt(input: Map<String, Any?>): Map<String, Any?> {
        val excludedKeys = setOf("id", "token")
        return crypto.encryptMap(input, excludedKeys)
    }
}
