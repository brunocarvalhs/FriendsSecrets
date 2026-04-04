package br.com.brunocarvalhs.group.list.domain.useCases

import br.com.brunocarvalhs.friendssecrets.domain.services.StorageService
import br.com.brunocarvalhs.group.core.domain.entities.GroupEntities
import br.com.brunocarvalhs.group.core.domain.repositories.GroupRepository

internal class GroupListUseCase(
    private val repository: GroupRepository,
    private val storage: StorageService
) {
    suspend fun invoke(): Result<List<GroupEntities>> {
        return try {
            runCatching {
                val groupTokens = loadGroupTokens()
                if (groupTokens.isEmpty()) emptyList() else repository.list(groupTokens)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun loadGroupTokens(): List<String> =
        storage.load(GroupEntities.COLLECTION_NAME, Array<String>::class.java)?.toList().orEmpty()
}

