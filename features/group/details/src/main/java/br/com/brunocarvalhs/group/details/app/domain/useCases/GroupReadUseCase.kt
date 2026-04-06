package br.com.brunocarvalhs.group.list.app.domain.useCases

import br.com.brunocarvalhs.group.details.app.domain.entities.GroupModel
import br.com.brunocarvalhs.group.details.app.domain.entities.GroupModel.Companion.COLLECTION_NAME_ADMINS
import br.com.brunocarvalhs.group.list.app.domain.repository.GroupListRepository
import br.com.brunocarvalhs.group.details.app.domain.services.StorageService
import javax.inject.Inject

class GroupReadUseCase @Inject constructor(
    private val repository: GroupListRepository,
    private val storage: StorageService
) {
    suspend fun invoke(groupId: String): Result<GroupModel> {
        return try {
            runCatching {
                validationGroupId(groupId)
                val group = repository.read(groupId)
                defineAdmin(group)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun validationGroupId(groupId: String) {
        require(groupId.isNotBlank())
    }

    private suspend fun defineAdmin(group: GroupModel): GroupModel {
        val adminList = storage.load(
            key = COLLECTION_NAME_ADMINS,
            value = Array<String>::class
        )?.toList().orEmpty()

        return if (adminList.contains(group.token)) group.toCopy(isOwner = true) else group
    }
}

