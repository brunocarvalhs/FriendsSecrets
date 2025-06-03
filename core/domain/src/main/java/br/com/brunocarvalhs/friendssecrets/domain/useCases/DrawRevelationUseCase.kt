package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.repositories.GroupRepository
import br.com.brunocarvalhs.friendssecrets.domain.services.CryptoService
import br.com.brunocarvalhs.friendssecrets.domain.services.PerformanceService
import br.com.brunocarvalhs.friendssecrets.domain.services.StorageService

class DrawRevelationUseCase(
    private val repository: GroupRepository,
    private val storage: StorageService,
    private val crypto: CryptoService,
    private val performance: PerformanceService,
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
            storage.load(secretKey, String::class.java)
        }
    }

    private suspend fun loadGroup(id: String): GroupEntities {
        return repository.read(id)
    }

    private fun revealDraw(secret: String, group: GroupEntities): Map<String, String> {
        val memberKey = crypto.decrypt(secret)
        val member = group.members.find { it.name == memberKey }

        return mapOf(
            memberKey to member?.likes.orEmpty().toString()
        )
    }

    companion object {
        private const val SECRET_KEY = "secret_key"
    }
}
