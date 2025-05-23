package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.commons.performance.PerformanceManager
import br.com.brunocarvalhs.friendssecrets.data.service.StorageService
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.repository.GroupRepository

class GroupListUseCase(
    private val groupRepository: GroupRepository,
    private val storage: StorageService,
    private val performance: PerformanceManager
) {
    suspend fun invoke(): Result<List<GroupEntities>> = runCatching {
        performance.start(GroupListUseCase::class.java.simpleName)
        val groupList = storage.load<List<String>>(GroupEntities.COLLECTION_NAME) ?: emptyList()
        if (groupList.isEmpty()) return Result.success(emptyList())
        groupRepository.list(groupList)
    }.also {
        performance.stop(GroupListUseCase::class.java.simpleName)
    }
}