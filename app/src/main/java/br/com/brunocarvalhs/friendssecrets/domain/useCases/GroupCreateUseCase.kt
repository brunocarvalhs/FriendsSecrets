package br.com.brunocarvalhs.friendssecrets.domain.useCases

import android.content.Context
import br.com.brunocarvalhs.friendssecrets.CustomApplication
import br.com.brunocarvalhs.friendssecrets.R
import br.com.brunocarvalhs.friendssecrets.commons.extensions.token
import br.com.brunocarvalhs.friendssecrets.commons.performance.PerformanceManager
import br.com.brunocarvalhs.friendssecrets.data.model.GroupModel
import br.com.brunocarvalhs.friendssecrets.data.service.StorageService
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.friendssecrets.domain.repository.GroupRepository
import com.google.firebase.perf.metrics.AddTrace
import kotlin.random.Random

class GroupCreateUseCase(
    private val context: Context = CustomApplication.getInstance(),
    private val groupRepository: GroupRepository,
    private val storage: StorageService,
    private val performance: PerformanceManager,
) {

    /**
     * This function creates a new group with the specified name, description, and members.
     * It validates the name and members, generates a unique token for the group,
     * and saves the group to the repository and storage.
     *
     * @param name The name of the group.
     * @param description The description of the group.
     * @param members A map of members in the group.
     * @return A Result containing Unit if successful, or an exception if an error occurs.
     */
    @Throws(Exception::class)
    @AddTrace(name = "GroupCreateUseCase.invoke")
    suspend fun invoke(
        name: String,
        description: String,
        members: Map<String, String>,
    ): Result<Unit> =
        runCatching {
            performance.start(GroupCreateUseCase::class.java.simpleName)
            validationName(name)
            validationMembers(members)
            var group: GroupEntities =
                GroupModel(name = name, description = description, members = members)
            do {
                group = group.toCopy(token = Random.token(size = 8))
            } while (!validationTokenIsUnique(group.token))
            groupRepository.create(group)
            defineAdmin(group)
            includeGroup(group)
        }.also {
            performance.stop(GroupCreateUseCase::class.java.simpleName)
        }

    /**
     * This function validates the name of the group.
     * It checks if the name is not blank and if it is not longer than 255 characters.
     *
     * @param name The name of the group.
     */
    @Throws(Exception::class)
    @AddTrace(name = "GroupCreateUseCase.validationName")
    private fun validationName(name: String) {
        require(value = name.isNotBlank()) { context.getString(R.string.require_name_cannot_be_blank) }
        require(value = name.length <= 255) { context.getString(R.string.require_name_cannot_be_longer_than_255_characters) }
    }

    /**
     * This function validates the members of the group.
     * It checks if the members map is not empty and if it contains at least 3 members.
     *
     * @param members A map of members in the group.
     */
    @Throws(Exception::class)
    @AddTrace(name = "GroupCreateUseCase.validationMembers")
    private fun validationMembers(members: Map<String, String>) {
        require(value = members.isNotEmpty()) { context.getString(R.string.require_group_must_have_at_least_one_member) }
        require(value = members.size >= 3) { context.getString(R.string.require_group_cannot_have_more_than_2_members) }
    }

    /**
     * This function validates if the token is unique.
     * It checks if the token is not blank and if it is not already used by another group.
     *
     * @param token The token of the group.
     */
    @Throws(Exception::class)
    @AddTrace(name = "GroupCreateUseCase.validationTokenIsUnique")
    private suspend fun validationTokenIsUnique(token: String): Boolean {
        val group = groupRepository.searchByToken(token)
        return group == null
    }

    /**
     * This function includes the group in the storage.
     * It adds the token of the group to the list of groups in the storage.
     *
     * @param group The group to be included.
     */
    @Throws(Exception::class)
    @AddTrace(name = "GroupCreateUseCase.includeGroup")
    private fun includeGroup(group: GroupEntities) {
        val groupList = storage.load<List<String>>(GroupEntities.COLLECTION_NAME)
            ?: emptyList()

        storage.save(
            key = GroupEntities.COLLECTION_NAME,
            value = groupList.toMutableList().apply { add(group.token) }
        )
    }

    /**
     * This function defines the admin of the group.
     * It adds the token of the group to the list of admins in the storage.
     *
     * @param group The group to be defined as admin.
     */
    @Throws(Exception::class)
    @AddTrace(name = "GroupCreateUseCase.defineAdmin")
    private fun defineAdmin(group: GroupEntities) {
        val admins = storage.load<List<String>>(GroupEntities.COLLECTION_NAME_ADMINS)
            ?: emptyList()

        storage.save(
            key = GroupEntities.COLLECTION_NAME_ADMINS,
            value = admins.toMutableList().apply { add(group.token) }
        )
    }
}