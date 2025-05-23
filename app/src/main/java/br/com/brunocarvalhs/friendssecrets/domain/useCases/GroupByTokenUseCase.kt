package br.com.brunocarvalhs.friendssecrets.domain.useCases

import br.com.brunocarvalhs.friendssecrets.commons.performance.PerformanceManager
import br.com.brunocarvalhs.friendssecrets.data.exceptions.GroupAlreadyExistException
import br.com.brunocarvalhs.friendssecrets.data.exceptions.GroupNotFoundException
import br.com.brunocarvalhs.friendssecrets.data.service.StorageService
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.repository.GroupRepository
import com.google.firebase.perf.metrics.AddTrace

class GroupByTokenUseCase(
    private val groupRepository: GroupRepository,
    private val storage: StorageService,
    private val performance: PerformanceManager,
) {

    /**
     * This function retrieves a group by its token. It validates the token, checks if the group already exists,
     * and saves the group to the storage if it doesn't exist. If the group is not found, it throws a GroupNotFoundException.
     *
     * @param token The token of the group to be retrieved.
     * @return A Result containing the GroupEntities object.
     */
    @Throws(Exception::class)
    @AddTrace(name = "GroupByTokenUseCase.invoke")
    suspend fun invoke(token: String): Result<GroupEntities> = runCatching {
        performance.start(GroupByTokenUseCase::class.java.simpleName)
        validationToken(token = token)
        val groupList = searchByToken(token)
        val group = groupRepository.searchByToken(token)
        saveGroup(token, groupList)
        group ?: throw GroupNotFoundException()
    }.also {
        performance.stop(GroupByTokenUseCase::class.java.simpleName)
    }

    /**
     * This function retrieves a group by its token. It validates the token, checks if the group already exists,
     * and saves the group to the storage if it doesn't exist. If the group is not found, it throws a GroupNotFoundException.
     *
     * @param token The token of the group to be retrieved.
     * @return A GroupEntities object.
     */
    @Throws(Exception::class)
    @AddTrace(name = "GroupByTokenUseCase.validationToken")
    private fun validationToken(token: String) {
        if (token.isBlank()) throw IllegalArgumentException("Token cannot be blank")
    }

    /**
     * This function searches for a group by its token. It checks if the group already exists in the storage.
     * If it does, it throws a GroupAlreadyExistException.
     *
     * @param token The token of the group to be searched.
     * @return A list of strings representing the group.
     */
    @Throws(Exception::class)
    @AddTrace(name = "GroupByTokenUseCase.searchByToken")
    private fun searchByToken(token: String): List<String> {
        val groupList = storage.load<List<String>>(GroupEntities.COLLECTION_NAME) ?: emptyList()
        if (groupList.contains(token)) throw GroupAlreadyExistException()
        return groupList
    }

    /**
     * This function saves a group to the storage. It adds the token to the list of groups in the storage.
     *
     * @param token The token of the group to be saved.
     * @param groupList The list of groups to be saved.
     */
    @Throws(Exception::class)
    @AddTrace(name = "GroupByTokenUseCase.saveGroup")
    private fun saveGroup(token: String, groupList: List<String>) {
        storage.save(GroupEntities.COLLECTION_NAME, groupList.toMutableList().apply { add(token) })
    }
}