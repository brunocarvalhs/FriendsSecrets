package br.com.brunocarvalhs.group.create.app.domain.useCases

import br.com.brunocarvalhs.group.create.app.domain.entities.GroupModel
import br.com.brunocarvalhs.group.create.app.domain.repositories.GroupCreateRepository
import br.com.brunocarvalhs.group.create.app.domain.services.StorageService
import javax.inject.Inject

class GroupCreateUseCase @Inject constructor(
    private val repository: GroupCreateRepository,
    private val storage: StorageService
) {

    suspend fun invoke(group: GroupModel): Result<Unit> = runCatching {
        repository.create(group)
        persistGroupToken(group.token)
        persistAdminToken(group.token)
    }

    private suspend fun persistGroupToken(token: String) {
        val groupList = storage.load(
            key = COLLECTION_NAME,
            value = Array<String>::class
        )?.toList().orEmpty()

        storage.save(
            COLLECTION_NAME,
            groupList.toMutableList().apply { add(token) }
        )
    }

    private suspend fun persistAdminToken(token: String) {
        val adminList = storage.load(
            key = COLLECTION_NAME_ADMINS,
            value = Array<String>::class
        )?.toList().orEmpty()

        storage.save(
            key = COLLECTION_NAME_ADMINS,
            value = adminList.toMutableList().apply { add(token) }
        )
    }

    companion object {
        const val COLLECTION_NAME = "group_tokens"
        const val COLLECTION_NAME_ADMINS = "group_admins"
    }
}