package br.com.brunocarvalhs.group.app.domain.useCases

import br.com.brunocarvalhs.friendssecrets.domain.services.StorageService
import br.com.brunocarvalhs.group.app.domain.entities.GroupEntities
import br.com.brunocarvalhs.group.app.domain.exceptions.GroupAlreadyExistException
import br.com.brunocarvalhs.group.app.domain.exceptions.GroupNotFoundException
import br.com.brunocarvalhs.group.app.domain.repositories.GroupRepository
import br.com.brunocarvalhs.group.commons.providers.performance.PerformanceService

internal class GroupByTokenUseCase(
    private val repository: GroupRepository,
    private val storage: StorageService,
    private val performance: PerformanceService,
) {
    suspend fun invoke(token: String): Result<GroupEntities> {
        performance.start(GroupByTokenUseCase::class.java.simpleName)
        return try {
            runCatching {
                validateToken(token)
                val groupList = ensureTokenNotExists(token)
                val group = fetchGroupByToken(token)
                storeToken(token, groupList)
                group
            }
        } finally {
            performance.stop(GroupByTokenUseCase::class.java.simpleName)
        }
    }

    private fun validateToken(token: String) {
        if (token.isBlank()) throw IllegalArgumentException("Token cannot be blank")
    }

    private suspend fun ensureTokenNotExists(token: String): List<String> {
        val groupList =
            storage.load(GroupEntities.COLLECTION_NAME, Array<String>::class.java)?.toList()
                .orEmpty()
        if (groupList.contains(token)) throw GroupAlreadyExistException()
        return groupList
    }

    private suspend fun fetchGroupByToken(token: String): GroupEntities {
        return repository.searchByToken(token) ?: throw GroupNotFoundException()
    }

    private suspend fun storeToken(token: String, groupList: List<String>) {
        storage.save(
            GroupEntities.COLLECTION_NAME,
            groupList.toMutableList().apply { add(token) }
        )
    }
}
