package br.com.brunocarvalhs.group.list.app.domain.useCases

import br.com.brunocarvalhs.group.list.app.data.repository.GroupListRepositoryImpl.Companion.COLLECTION_NAME
import br.com.brunocarvalhs.group.list.app.domain.entities.GroupModel
import br.com.brunocarvalhs.group.list.app.domain.repository.GroupListRepository
import br.com.brunocarvalhs.group.list.app.domain.services.StorageService
import javax.inject.Inject


class GroupListUseCase @Inject constructor(
    private val repository: GroupListRepository,
    private val storage: StorageService
) {
    suspend fun invoke(): Result<List<GroupModel>> {
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
        storage.load(COLLECTION_NAME, Array<String>::class)?.toList().orEmpty()
}

