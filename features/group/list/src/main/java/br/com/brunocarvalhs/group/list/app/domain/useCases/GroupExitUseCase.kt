package br.com.brunocarvalhs.group.list.app.domain.useCases

import br.com.brunocarvalhs.group.list.app.domain.repository.GroupListRepository
import br.com.brunocarvalhs.group.list.app.domain.services.StorageService
import javax.inject.Inject

internal class GroupExitUseCase @Inject constructor(
    private val repository: GroupListRepository,
    private val storage: StorageService
) {
    suspend fun invoke(groupId: String): Result<Unit> {
        return try {
            runCatching {
                validationGroupId(groupId)
                val group = repository.read(groupId)
                clearGroupToken(group.token)
                clearAdminToken(group.token)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun validationGroupId(groupId: String) {
        require(groupId.isNotBlank())
    }

    private suspend fun clearGroupToken(token: String) {
        val groupList =
            storage.load(COLLECTION_NAME, Array<String>::class)?.toList()
                .orEmpty()
        if (groupList.contains(token)) {
            storage.save(
                COLLECTION_NAME,
                groupList.toMutableList().apply { remove(token) }
            )
        }
    }

    private suspend fun clearAdminToken(token: String) {
        val adminList =
            storage.load(COLLECTION_NAME_ADMINS, Array<String>::class)?.toList()
                .orEmpty()
        if (adminList.contains(token)) {
            storage.save(
                COLLECTION_NAME_ADMINS,
                adminList.toMutableList().apply { remove(token) }
            )
        }
    }

    companion object {
        const val COLLECTION_NAME = "group_tokens"
        const val COLLECTION_NAME_ADMINS = "group_admins"
    }
}

