package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.commons.performance.PerformanceManager
import br.com.brunocarvalhs.friendssecrets.commons.security.CryptoService
import br.com.brunocarvalhs.friendssecrets.data.service.StorageService
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.repository.GroupRepository

class DrawRevelationUseCase(
    private val repository: GroupRepository,
    private val storage: StorageService,
    private val cryptoService: CryptoService,
    private val performance: PerformanceManager,
) {
    suspend operator fun invoke(
        id: String,
        code: String? = null,
    ): Result<Pair<GroupEntities, Map<String, String>>?> =
        runCatching {
            performance.start(DrawRevelationUseCase::class.java.simpleName)
            val secretKey = SECRET_KEY + id
            val secret: String? = if (code != null) {
                storage.save(secretKey, code)
                code
            } else {
                storage.load<String>(secretKey)
            }

            if (secret != null) {
                val group = repository.read(id)
                Pair(
                    group,
                    mapOf(
                        cryptoService.decrypt(secret) to
                                group.members[cryptoService.decrypt(secret)].orEmpty()
                    )
                )
            } else {
                null
            }
        }.also {
            performance.stop(DrawRevelationUseCase::class.java.simpleName)
        }

    companion object {
        private const val SECRET_KEY = "secret_key"
    }
}
