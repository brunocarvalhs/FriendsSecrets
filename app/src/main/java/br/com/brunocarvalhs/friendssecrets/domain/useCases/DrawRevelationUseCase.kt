package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.commons.performance.PerformanceManager
import br.com.brunocarvalhs.friendssecrets.commons.security.CryptoService
import br.com.brunocarvalhs.friendssecrets.data.service.StorageService
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.repository.GroupRepository
import br.com.brunocarvalhs.friendssecrets.domain.repository.response.toModel

class DrawRevelationUseCase(
    private val repository: GroupRepository,
    private val storage: StorageService,
    private val cryptoService: CryptoService,
    private val performance: PerformanceManager,
) {
    suspend operator fun invoke(
        id: String,
        code: String? = null,
    ): Result<Pair<GroupEntities, Map<String, String>>?> {
        performance.start(DrawRevelationUseCase::class.java.simpleName)
        return try {
            runCatching {
                val secret = resolveSecret(id, code) ?: return@runCatching null
                val group = loadGroup(id)
                val memberData = revealDraw(secret, group)

                Pair(group, memberData)
            }
        } finally {
            performance.stop(DrawRevelationUseCase::class.java.simpleName)
        }
    }

    private fun resolveSecret(id: String, code: String?): String? {
        val secretKey = SECRET_KEY + id
        return if (code != null) {
            storage.save(secretKey, code)
            code
        } else {
            storage.load<String>(secretKey)
        }
    }

    private suspend fun loadGroup(id: String): GroupEntities {
        return repository.read(id).toModel()
    }

    private fun revealDraw(secret: String, group: GroupEntities): Map<String, String> {
        val memberKey = cryptoService.decrypt(secret)
        val member = group.members.find { it.name == memberKey }

        return mapOf(
            memberKey to member?.likes.orEmpty().toString()
        )
    }

    companion object {
        private const val SECRET_KEY = "secret_key"
    }
}
