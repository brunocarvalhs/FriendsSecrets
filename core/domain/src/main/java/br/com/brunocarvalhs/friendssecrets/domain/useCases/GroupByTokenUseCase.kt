package br.com.brunocarvalhs.friendssecrets.domain.useCases

internal class GroupByTokenUseCase(
    private val repository: GroupRepository,
    private val storage: StorageService
) {
    suspend fun invoke(token: String): Result<GroupEntities> {
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
        val groupList =
            storage.load(GroupEntities.Companion.COLLECTION_NAME, Array<String>::class.java)?.toList()
                .orEmpty()
        if (groupList.contains(token)) throw GroupAlreadyExistException()
        return groupList
    }

    private suspend fun fetchGroupByToken(token: String): GroupEntities {
        return repository.searchByToken(token) ?: throw GroupNotFoundException()
    }

    private suspend fun storeToken(token: String, groupList: List<String>) {
        storage.save(
            GroupEntities.Companion.COLLECTION_NAME,
            groupList.toMutableList().apply { add(token) }
        )
    }
}