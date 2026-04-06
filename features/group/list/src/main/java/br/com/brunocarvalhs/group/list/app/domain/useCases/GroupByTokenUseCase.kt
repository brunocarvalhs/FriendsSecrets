package br.com.brunocarvalhs.group.list.app.domain.useCases

import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel
import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel.Companion.COLLECTION_NAME
import br.com.brunocarvalhs.friendssecrets.domain.services.StorageService
import br.com.brunocarvalhs.group.list.app.data.exceptions.GroupAlreadyExistException
import br.com.brunocarvalhs.group.list.app.data.exceptions.GroupNotFoundException
import br.com.brunocarvalhs.group.list.app.domain.repository.GroupListRepository
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
        val groupList = storage.load(COLLECTION_NAME, Array<String>::class)?.toList().orEmpty()
        if (groupList.contains(token)) throw GroupAlreadyExistException()
        return groupList
    }

    private suspend fun fetchGroupByToken(token: String): GroupModel {
        val data =  repository.searchByToken(token) ?: throw GroupNotFoundException()
        return data.toDomain()
    }

    private suspend fun storeToken(token: String, groupList: List<String>) {
        storage.save(
            COLLECTION_NAME,
            groupList.toMutableList().apply { add(token) }
        )
    }
}
