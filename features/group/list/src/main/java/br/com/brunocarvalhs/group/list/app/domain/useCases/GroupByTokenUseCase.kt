package br.com.brunocarvalhs.group.list.app.domain.useCases

import br.com.brunocarvalhs.group.list.app.data.exceptions.GroupAlreadyExistException
import br.com.brunocarvalhs.group.list.app.data.exceptions.GroupNotFoundException
import br.com.brunocarvalhs.group.list.app.domain.entities.GroupModel
import br.com.brunocarvalhs.group.list.app.domain.entities.GroupModel.Companion.STORAGE_KEY
import br.com.brunocarvalhs.group.list.app.domain.repository.GroupListRepository
import br.com.brunocarvalhs.group.list.app.domain.services.StorageService
import javax.inject.Inject

class GroupByTokenUseCase @Inject constructor(
    private val repository: GroupListRepository,
    private val storage: StorageService
) {
    suspend fun invoke(token: String): Result<GroupModel> {
        return try {
            runCatching {
                validateToken(token)
                val groupList = ensureTokenNotExists(token)
                val group = fetchGroupByToken(token)
                storeToken(token, groupList)
                group
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun validateToken(token: String) {
        if (token.isBlank()) throw IllegalArgumentException("Token cannot be blank")
    }
    private suspend fun ensureTokenNotExists(token: String): List<String> {
        val groupList = storage.load(STORAGE_KEY, Array<String>::class)?.toList().orEmpty()
        if (groupList.contains(token)) throw GroupAlreadyExistException()
        return groupList
    }

    private suspend fun fetchGroupByToken(token: String): GroupModel {
        return repository.searchByToken(token) ?: throw GroupNotFoundException()
    }

    private suspend fun storeToken(token: String, groupList: List<String>) {
        storage.save(
            STORAGE_KEY,
            groupList.toMutableList().apply { add(token) }
        )
    }
}
