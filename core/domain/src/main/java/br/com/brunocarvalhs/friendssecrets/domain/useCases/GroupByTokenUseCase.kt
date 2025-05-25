package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.domain.exceptions.GroupNotFoundException
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.exceptions.GroupAlreadyExistException
import br.com.brunocarvalhs.friendssecrets.domain.repositories.GroupRepository
import br.com.brunocarvalhs.friendssecrets.domain.services.PerformanceService
import br.com.brunocarvalhs.friendssecrets.domain.services.StorageService

class GroupByTokenUseCase(
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

    private fun ensureTokenNotExists(token: String): List<String> {
        val groupList = storage.load<List<String>>(GroupEntities.COLLECTION_NAME) ?: emptyList()
        if (groupList.contains(token)) throw GroupAlreadyExistException()
        return groupList
    }

    private suspend fun fetchGroupByToken(token: String): GroupEntities {
        return repository.searchByToken(token) ?: throw GroupNotFoundException()
    }

    private fun storeToken(token: String, groupList: List<String>) {
        storage.save(
            GroupEntities.COLLECTION_NAME,
            groupList.toMutableList().apply { add(token) }
        )
    }
}
