package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.data.service.StorageService
import br.com.brunocarvalhs.friendssecrets.domain.base.BaseUseCaseNoParams
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.repository.GroupRepository
import javax.inject.Inject

class GroupListUseCase @Inject constructor(
    private val groupRepository: GroupRepository,
    private val storage: StorageService
) : BaseUseCaseNoParams<List<GroupEntities>>() {

    override val traceName: String = "GroupListUseCase"

    override suspend fun execute(): Result<List<GroupEntities>> = runCatching {
        val groupList = storage.load<List<String>>(GroupEntities.COLLECTION_NAME) ?: emptyList()
        if (groupList.isEmpty()) return Result.success(emptyList())
        groupRepository.list(groupList)
    }
}