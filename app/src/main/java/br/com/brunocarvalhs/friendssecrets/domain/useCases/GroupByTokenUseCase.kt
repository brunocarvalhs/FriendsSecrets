package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.data.exceptions.GroupAlreadyExistException
import br.com.brunocarvalhs.friendssecrets.data.exceptions.GroupNotFoundException
import br.com.brunocarvalhs.friendssecrets.data.service.StorageService
import br.com.brunocarvalhs.friendssecrets.domain.base.BaseUseCase
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.repository.GroupRepository
import javax.inject.Inject

class GroupByTokenUseCase @Inject constructor(
    private val groupRepository: GroupRepository,
    private val storage: StorageService
) : BaseUseCase<String, GroupEntities>() {

    override val traceName: String = "GroupByTokenUseCase"

    override suspend fun execute(parameters: String): Result<GroupEntities> = runCatching {
        val token = parameters
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