package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.commons.performance.PerformanceManager
import br.com.brunocarvalhs.friendssecrets.data.service.StorageService
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.repository.GroupRepository
import com.google.firebase.perf.metrics.AddTrace

class GroupListUseCase(
    private val groupRepository: GroupRepository,
    private val storage: StorageService,
    private val performance: PerformanceManager
) {

    /**
     * This function retrieves a list of groups from the storage and then fetches the group details from the repository.
     * If the group list is empty, it returns an empty list.
     *
     * @return A Result containing a list of GroupEntities if successful, or an exception if an error occurs.
     */
    @Throws(Exception::class)
    @AddTrace(name = "GroupListUseCase.invoke")
    suspend fun invoke(): Result<List<GroupEntities>> = runCatching {
        performance.start(GroupListUseCase::class.java.simpleName)
        val groupList = storage.load<List<String>>(GroupEntities.COLLECTION_NAME) ?: emptyList()
        if (groupList.isEmpty()) return Result.success(emptyList())
        groupRepository.list(groupList)
    }.also {
        performance.stop(GroupListUseCase::class.java.simpleName)
    }
}