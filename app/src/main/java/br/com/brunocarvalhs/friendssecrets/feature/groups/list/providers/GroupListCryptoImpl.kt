package br.com.brunocarvalhs.friendssecrets.feature.groups.list.providers

import br.com.brunocarvalhs.friendssecrets.commons.security.CryptoManager
import br.com.brunocarvalhs.group.list.commons.providers.GroupListCrypto
import javax.inject.Inject

class GroupListCryptoImpl @Inject constructor(
    private val crypto: CryptoManager
) : GroupListCrypto {
    override fun decryptMap(
        encryptedData: MutableMap<String, Any>,
        of: Any
    ): Map<String, Any> {
        val excludedKeys = setOf("id")
        return crypto.decryptMap(encryptedData, excludedKeys)
    }
}