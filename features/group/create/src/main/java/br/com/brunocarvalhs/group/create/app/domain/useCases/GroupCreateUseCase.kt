package br.com.brunocarvalhs.group.create.app.domain.useCases

import br.com.brunocarvalhs.group.create.app.domain.entities.GroupModel
import br.com.brunocarvalhs.group.create.app.domain.repositories.GroupCreateRepository
import br.com.brunocarvalhs.group.create.app.domain.services.StorageService
import javax.inject.Inject

class GroupCreateUseCase @Inject constructor(
    private val repository: GroupCreateRepository,
    private val storage: StorageService
) {

    suspend operator fun invoke(group: GroupModel): Result<Unit> = runCatching {
        repository.create(group)
        persistGroupToken(group.token)
        persistAdminToken(group.token)
    }

    private suspend fun persistGroupToken(token: String) {
        val groupList = storage.load(
            key = GroupModel.COLLECTION_NAME,
            value = Array<String>::class
        ) ?: emptyArray()

        storage.save(
            GroupModel.COLLECTION_NAME,
            groupList + token
        )
    }

    private suspend fun persistAdminToken(token: String) {
        val adminList = storage.load(
            key = GroupModel.COLLECTION_NAME_ADMINS,
            value = Array<String>::class
        ) ?: emptyArray()

        storage.save(
            key = GroupModel.COLLECTION_NAME_ADMINS,
            value = adminList + token
        )
    }
}
