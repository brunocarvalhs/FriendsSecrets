package br.com.brunocarvalhs.group.create.app.domain.useCases

import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities.Companion.COLLECTION_NAME
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities.Companion.COLLECTION_NAME_ADMINS
import br.com.brunocarvalhs.friendssecrets.domain.services.StorageService
import br.com.brunocarvalhs.group.create.app.domain.model.GroupModel
import br.com.brunocarvalhs.group.create.app.domain.repositories.GroupCreateRepository
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
            key = COLLECTION_NAME,
            value = Array<String>::class
        ) ?: emptyArray()

        storage.save(
            COLLECTION_NAME,
            groupList + token
        )
    }

    private suspend fun persistAdminToken(token: String) {
        val adminList = storage.load(
            key = COLLECTION_NAME_ADMINS,
            value = Array<String>::class
        ) ?: emptyArray()

        storage.save(
            key = COLLECTION_NAME_ADMINS,
            value = adminList + token
        )
    }
}
