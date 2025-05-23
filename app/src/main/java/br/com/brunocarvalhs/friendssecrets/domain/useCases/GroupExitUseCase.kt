package br.com.brunocarvalhs.friendssecrets.domain.useCases

import android.content.Context
import br.com.brunocarvalhs.friendssecrets.CustomApplication
import br.com.brunocarvalhs.friendssecrets.R
import br.com.brunocarvalhs.friendssecrets.commons.performance.PerformanceManager
import br.com.brunocarvalhs.friendssecrets.data.service.StorageService
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.repository.GroupRepository
import com.google.firebase.perf.metrics.AddTrace

class GroupExitUseCase(
    private val context: Context = CustomApplication.getInstance(),
    private val groupRepository: GroupRepository,
    private val storage: StorageService,
    private val performance: PerformanceManager
) {

    /**
     * This function allows a user to exit a group by its ID.
     * It validates the group ID, retrieves the group from the repository,
     * clears the group and admin from storage, and then deletes the group from the repository.
     *
     * @param groupId The ID of the group to be exited.
     * @return A Result containing Unit if successful, or an exception if an error occurs.
     */
    @Throws(Exception::class)
    @AddTrace(name = "GroupExitUseCase.invoke")
    suspend fun invoke(groupId: String): Result<Unit> = runCatching {
        performance.start(GroupExitUseCase::class.java.simpleName)
        validationGroupId(groupId)
        val group = groupRepository.read(groupId)
        clearGroup(group)
        clearAdmin(group)
    }.also {
        performance.stop(GroupExitUseCase::class.java.simpleName)
    }

    /**
     * Validates the group ID.
     * Throws an exception if the group ID is blank.
     *
     * @param groupId The ID of the group to be validated.
     */
    @Throws(IllegalArgumentException::class)
    @AddTrace(name = "GroupExitUseCase.validationGroupId")
    private fun validationGroupId(groupId: String) {
        require(groupId.isNotBlank()) { context.getString(R.string.require_group_id_cannot_be_blank) }
    }

    /**
     * Clears the group from storage.
     * If the group is found in the storage, it removes it from the list.
     *
     * @param group The group to be cleared.
     */
    @Throws(Exception::class)
    @AddTrace(name = "GroupExitUseCase.clearGroup")
    private fun clearGroup(group: GroupEntities) {
        val list = storage.load<List<String>>(key = GroupEntities.COLLECTION_NAME)
            ?: emptyList()

        if (list.contains(group.token)) {
            storage.save(GroupEntities.COLLECTION_NAME, list.toMutableList().apply { remove(group.token) })
        }
    }

    /**
     * Clears the admin from storage.
     * If the group is found in the admin list, it removes it from the list.
     *
     * @param group The group to be cleared from the admin list.
     */
    @Throws(Exception::class)
    @AddTrace(name = "GroupExitUseCase.clearAdmin")
    private fun clearAdmin(group: GroupEntities) {
        val adminList = storage.load<List<String>>(key = GroupEntities.COLLECTION_NAME_ADMINS)
            ?: emptyList()
        if (adminList.contains(group.token)) {
            storage.save(GroupEntities.COLLECTION_NAME_ADMINS, adminList.toMutableList().apply { remove(group.token) })
        }
    }
}