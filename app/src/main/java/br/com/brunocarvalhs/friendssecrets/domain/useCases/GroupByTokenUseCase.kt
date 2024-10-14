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
        validationToken(token = token)
        val groupList = searchByToken(token)
        val group = groupRepository.searchByToken(token)
        saveGroup(token, groupList)
        group ?: throw GroupNotFoundException()
    }

    private fun validationToken(token: String) {
        if (token.isBlank()) throw IllegalArgumentException("Token cannot be blank")
    }

    private fun searchByToken(token: String): List<String> {
        val groupList = storage.load<List<String>>(GroupEntities.COLLECTION_NAME) ?: emptyList()
        if (groupList.contains(token)) throw GroupAlreadyExistException()
        return groupList
    }

    private fun saveGroup(token: String, groupList: List<String>) {
        storage.save(GroupEntities.COLLECTION_NAME, groupList.toMutableList().apply { add(token) })
    }
}