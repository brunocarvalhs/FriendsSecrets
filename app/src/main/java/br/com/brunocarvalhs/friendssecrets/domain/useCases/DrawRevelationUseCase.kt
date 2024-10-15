package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.commons.security.CryptoService
import br.com.brunocarvalhs.friendssecrets.data.service.StorageService

class DrawRevelationUseCase(
    private val storage: StorageService,
    private val cryptoService: CryptoService
) {
    operator fun invoke(id: String, code: String? = null): Result<String?> = runCatching {
        val secretKey = SECRET_KEY + id
        val secret: String? = if (code != null) {
            storage.save(secretKey, code)
            code
        } else {
            storage.load<String>(secretKey)
        }

        if (secret != null) {
            cryptoService.decrypt(secret)
        } else {
            null
        }
    }

    companion object {
        private const val SECRET_KEY = "secret_key"
    }
}
