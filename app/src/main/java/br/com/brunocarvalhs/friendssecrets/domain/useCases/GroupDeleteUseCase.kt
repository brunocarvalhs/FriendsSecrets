package br.com.brunocarvalhs.friendssecrets.domain.useCases

import android.content.Context
import br.com.brunocarvalhs.friendssecrets.CustomApplication
import br.com.brunocarvalhs.friendssecrets.R
import br.com.brunocarvalhs.friendssecrets.commons.performance.PerformanceManager
import br.com.brunocarvalhs.friendssecrets.data.service.StorageService
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.repository.GroupRepository
import com.google.firebase.perf.metrics.AddTrace

class GroupDeleteUseCase(
    private val context: Context = CustomApplication.getInstance(),
    private val groupRepository: GroupRepository,
    private val storage: StorageService,
    private val performance: PerformanceManager
) {

    /**
     * This function deletes a group by its ID. It validates the group ID, retrieves the group from the repository,
     * clears the group and admin from storage, and then deletes the group from the repository.
     *
     * @param groupId The ID of the group to be deleted.
     * @return A Result containing Unit if successful, or an exception if an error occurs.
     */
    @Throws(Exception::class)
    @AddTrace(name = "GroupDeleteUseCase.invoke")
    suspend fun invoke(groupId: String): Result<Unit> = runCatching {
        performance.start(GroupDeleteUseCase::class.java.simpleName)
        validationGroupId(groupId)
        val group = groupRepository.read(groupId)
        clearGroup(group)
        clearAdmin(group)
        groupRepository.delete(groupId)
    }.also {
        performance.stop(GroupDeleteUseCase::class.java.simpleName)
    }

    /**
     * This function validates the group ID. It checks if the group ID is not blank.
     *
     * @param groupId The ID of the group to be validated.
     */
    @Throws(Exception::class)
    @AddTrace(name = "GroupDeleteUseCase.validationGroupId")
    private fun validationGroupId(groupId: String) {
        require(groupId.isNotBlank()) { context.getString(R.string.require_group_id_cannot_be_blank) }
    }

    /**
     * This function clears the group from storage. It retrieves the list of groups from storage,
     * checks if the group exists, and removes it from the list.
     *
     * @param group The group to be cleared.
     */
    @Throws(Exception::class)
    @AddTrace(name = "GroupDeleteUseCase.clearGroup")
    fun clearGroup(group: GroupEntities) {
        val list = storage.load<List<String>>(key = GroupEntities.COLLECTION_NAME)
            ?: emptyList()

        if (list.contains(group.token)) {
            storage.save(
                GroupEntities.COLLECTION_NAME,
                list.toMutableList().apply { remove(group.token) })
        }
    }

    /**
     * This function clears the admin from storage. It retrieves the list of admins from storage,
     * checks if the group exists, and removes it from the list.
     *
     * @param group The group to be cleared.
     */
    @Throws(Exception::class)
    @AddTrace(name = "GroupDeleteUseCase.clearAdmin")
    fun clearAdmin(group: GroupEntities) {
        val adminList = storage.load<List<String>>(key = GroupEntities.COLLECTION_NAME_ADMINS)
            ?: emptyList()
        if (adminList.contains(group.token)) {
            storage.save(
                GroupEntities.COLLECTION_NAME_ADMINS,
                adminList.toMutableList().apply { remove(group.token) })
        }
    }
}