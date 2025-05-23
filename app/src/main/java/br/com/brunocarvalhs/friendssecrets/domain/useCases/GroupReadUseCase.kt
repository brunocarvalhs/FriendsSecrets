package br.com.brunocarvalhs.friendssecrets.domain.useCases

import android.content.Context
import br.com.brunocarvalhs.friendssecrets.CustomApplication
import br.com.brunocarvalhs.friendssecrets.R
import br.com.brunocarvalhs.friendssecrets.commons.performance.PerformanceManager
import br.com.brunocarvalhs.friendssecrets.data.service.StorageService
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.repository.GroupRepository
import com.google.firebase.perf.metrics.AddTrace

class GroupReadUseCase(
    private val context: Context = CustomApplication.getInstance(),
    private val groupRepository: GroupRepository,
    private val storage: StorageService,
    private val performance: PerformanceManager
) {

    /**
     * This function retrieves a group by its ID.
     * It validates the group ID, fetches the group from the repository,
     * and checks if the user is an admin of the group.
     *
     * @param groupId The ID of the group to be retrieved.
     * @return A Result containing the GroupEntities if successful, or an exception if an error occurs.
     */
    @Throws(Exception::class)
    @AddTrace(name = "GroupReadUseCase.invoke")
    suspend fun invoke(groupId: String): Result<GroupEntities> = runCatching {
        performance.start(GroupReadUseCase::class.java.simpleName)
        validationGroupId(groupId)
        val group = groupRepository.read(groupId)
        defineAdmin(group)
    }.also {
        performance.stop(GroupReadUseCase::class.java.simpleName)
    }

    /**
     * Validates the group ID.
     * Throws an exception if the group ID is blank.
     *
     * @param groupId The ID of the group to be validated.
     */
    @Throws(IllegalArgumentException::class)
    @AddTrace(name = "GroupReadUseCase.validationGroupId")
    private fun validationGroupId(groupId: String) {
        require(groupId.isNotBlank()) { context.getString(R.string.require_group_id_cannot_be_blank) }
    }

    /**
     * Checks if the user is an admin of the group.
     * If the user is an admin, it returns a copy of the group with isOwner set to true.
     *
     * @param group The group to be checked.
     * @return A GroupEntities object with isOwner set to true if the user is an admin, otherwise returns the original group.
     */
    @Throws(Exception::class)
    @AddTrace(name = "GroupReadUseCase.defineAdmin")
    private fun defineAdmin(group: GroupEntities): GroupEntities {
        val adminList = storage.load<List<String>>(key = GroupEntities.COLLECTION_NAME_ADMINS)
            ?: emptyList()
        return if (adminList.contains(group.token)) group.toCopy(isOwner = true) else group
    }
}