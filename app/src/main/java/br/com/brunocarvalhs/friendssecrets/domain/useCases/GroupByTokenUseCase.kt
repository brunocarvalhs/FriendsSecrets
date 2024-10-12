package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.data.exceptions.GroupAlreadyExistException
import br.com.brunocarvalhs.friendssecrets.data.exceptions.GroupNotFoundException
import br.com.brunocarvalhs.friendssecrets.data.service.StorageService
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.repository.GroupRepository

class GroupByTokenUseCase(
    private val groupRepository: GroupRepository,
    private val storage: StorageService,
) {
    suspend fun invoke(token: String): Result<GroupEntities> = runCatching {
        val groupList = storage.load<List<String>>(GroupEntities.COLLECTION_NAME) ?: emptyList()
        if (groupList.contains(token)) throw GroupAlreadyExistException()
        val group = groupRepository.searchByToken(token)
        storage.save(GroupEntities.COLLECTION_NAME, groupList.toMutableList().apply { add(token) })
        group ?: throw GroupNotFoundException()
    }
}